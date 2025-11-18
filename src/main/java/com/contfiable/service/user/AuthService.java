package com.contfiable.service.user;

import com.contfiable.dto.user.AuthResponse;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final JwtService jwtService;

	public AuthService(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	public AuthResponse verifyUserToken(String jwt) {
		try {
			if (!jwtService.isTokenValid(jwt)) {
				org.slf4j.LoggerFactory.getLogger(AuthService.class).warn("Token no es válido");
				return null;
			}
			String userId = jwtService.extractUserId(jwt);
			String email = jwtService.extractEmail(jwt);
			String role = jwtService.extractRole(jwt);
			
			org.slf4j.LoggerFactory.getLogger(AuthService.class).info(
				"Token verificado - UserId: {}, Email: {}, Role: {}", userId, email, role);
			
			AuthResponse resp = new AuthResponse();
			resp.setAccessToken(jwt);
			resp.setEmail(email);
			resp.setRole(role);
			resp.setUserId(userId);
			resp.setExpiresIn(jwtService.getExpirationTime());
			return resp;
		} catch (Exception e) {
			org.slf4j.LoggerFactory.getLogger(AuthService.class).error(
				"Error al verificar token: {}", e.getMessage(), e);
			return null;
		}
	}
}


