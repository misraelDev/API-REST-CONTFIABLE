package com.contfiable.service;

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

		if (keyCandidate == null || keyCandidate.isEmpty()) {
			byte[] keyBytes = new byte[32];
			new java.security.SecureRandom().nextBytes(keyBytes);
			return Keys.hmacShaKeyFor(keyBytes);
		}

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
			return extractClaim(stripBearer(token), claims -> claims.get("sub", String.class));
		} catch (Exception e) {
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
			return !isTokenExpired(token);
		} catch (Exception e) {
			return false;
		}
	}

	public String stripBearer(String token) {
		return token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
	}
}


