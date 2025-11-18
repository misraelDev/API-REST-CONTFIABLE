package com.contfiable.service.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

	@Value("${jwt.secret:}")
	private String jwtSecret;

	@Value("${supabase.jwt-secret:}")
	private String supabaseJwtSecret;

	@Value("${supabase.jwt.expiration:86400000}")
	private long jwtExpiration;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(stripBearer(token));
		return claimsResolver.apply(claims);
	}

	public String generateToken(org.springframework.security.core.userdetails.UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	public long getExpirationTime() {
		return jwtExpiration;
	}

	private String buildToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails,
			long expiration) {

		// Map authorities to simple String list to avoid serializer issues
		extraClaims.put(
			"authorities",
			userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toList()
		);

		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.setAllowedClockSkewSeconds(60)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignInKey() {
		String keyCandidate = (jwtSecret != null && !jwtSecret.isEmpty()) ? jwtSecret : supabaseJwtSecret;
		org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtService.class);

		if (keyCandidate == null || keyCandidate.isEmpty()) {
			logger.warn("No se encontró clave JWT configurada. Generando clave aleatoria (los tokens no serán consistentes entre reinicios)");
			byte[] keyBytes = new byte[32];
			new java.security.SecureRandom().nextBytes(keyBytes);
			return Keys.hmacShaKeyFor(keyBytes);
		}
		
		logger.debug("Usando clave JWT configurada (longitud: {})", keyCandidate.length());

		try {
			byte[] keyBytes = java.util.Base64.getDecoder().decode(keyCandidate);
			if (keyBytes.length < 16) {
				throw new IllegalArgumentException("decoded key too short");
			}
			return Keys.hmacShaKeyFor(keyBytes);
		} catch (Exception e) {
			byte[] keyBytes = keyCandidate.getBytes(java.nio.charset.StandardCharsets.UTF_8);
			if (keyBytes.length < 16) {
				try {
					java.security.MessageDigest sha = java.security.MessageDigest.getInstance("SHA-256");
					keyBytes = sha.digest(keyBytes);
				} catch (java.security.NoSuchAlgorithmException ex) {
					byte[] tmp = new byte[32];
					new java.security.SecureRandom().nextBytes(tmp);
					keyBytes = tmp;
				}
			}
			return Keys.hmacShaKeyFor(keyBytes);
		}
	}

	public String extractRole(String token) {
		try {
			Claims claims = extractAllClaims(stripBearer(token));
			@SuppressWarnings("unchecked")
			Map<String, Object> userMetadata = (Map<String, Object>) claims.get("user_metadata");
			if (userMetadata != null && userMetadata.containsKey("role")) {
				return (String) userMetadata.get("role");
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> appMetadata = (Map<String, Object>) claims.get("app_metadata");
			if (appMetadata != null && appMetadata.containsKey("role")) {
				return (String) appMetadata.get("role");
			}
			Object roleClaim = claims.get("role");
			if (roleClaim != null) {
				return roleClaim.toString();
			}
			return "client";
		} catch (Exception e) {
			return "client";
		}
	}

	public String extractUserId(String token) {
		try {
			Claims claims = extractAllClaims(stripBearer(token));
			org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtService.class);
			
			// Intentar obtener userId del claim "userId" primero
			Object userIdClaim = claims.get("userId");
			if (userIdClaim != null) {
				logger.debug("UserId encontrado en claim 'userId': {}", userIdClaim);
				return userIdClaim.toString();
			}
			// Si no existe, intentar obtener del claim "sub"
			Object subClaim = claims.get("sub");
			if (subClaim != null) {
				logger.debug("UserId encontrado en claim 'sub': {}", subClaim);
				return subClaim.toString();
			}
			// Si no existe, usar el subject (puede ser email o userId)
			String subject = claims.getSubject();
			logger.debug("UserId no encontrado en claims, usando subject: {}", subject);
			logger.warn("Token no tiene claim 'userId' o 'sub'. Subject: {}. Claims disponibles: {}", 
				subject, claims.keySet());
			return subject;
		} catch (Exception e) {
			org.slf4j.LoggerFactory.getLogger(JwtService.class).error(
				"Error al extraer userId del token: {}", e.getMessage(), e);
			return null;
		}
	}

	public String extractEmail(String token) {
		try {
			Claims claims = extractAllClaims(stripBearer(token));
			String email = claims.getSubject();
			if (email != null && email.contains("@")) {
				return email;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> userMetadata = (Map<String, Object>) claims.get("user_metadata");
			if (userMetadata != null && userMetadata.containsKey("email")) {
				return (String) userMetadata.get("email");
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> appMetadata = (Map<String, Object>) claims.get("app_metadata");
			if (appMetadata != null && appMetadata.containsKey("email")) {
				return (String) appMetadata.get("email");
			}
			return email;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean isTokenValid(String token) {
		try {
			// Intentar parsear el token para verificar que es válido
			Claims claims = extractAllClaims(stripBearer(token));
			boolean expired = isTokenExpired(token);
			org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtService.class);
			
			if (expired) {
				logger.warn("Token expirado. Expiration: {}", claims.getExpiration());
				return false;
			}
			
			logger.debug("Token válido. Subject: {}, Expiration: {}", claims.getSubject(), claims.getExpiration());
			return true;
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			org.slf4j.LoggerFactory.getLogger(JwtService.class).warn("Token expirado: {}", e.getMessage());
			return false;
		} catch (io.jsonwebtoken.security.SignatureException e) {
			org.slf4j.LoggerFactory.getLogger(JwtService.class).error("Error de firma en token: {}", e.getMessage());
			return false;
		} catch (io.jsonwebtoken.MalformedJwtException e) {
			org.slf4j.LoggerFactory.getLogger(JwtService.class).error("Token malformado: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			org.slf4j.LoggerFactory.getLogger(JwtService.class).error("Error al validar token: {}", e.getMessage(), e);
			return false;
		}
	}

	public String stripBearer(String token) {
		return token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
	}
}


