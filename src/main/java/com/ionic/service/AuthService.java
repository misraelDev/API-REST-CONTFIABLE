package com.ionic.service;

import com.ionic.dto.AuthResponse;
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
				return null;
			}
			AuthResponse resp = new AuthResponse();
			resp.setAccessToken(jwt);
			resp.setEmail(jwtService.extractEmail(jwt));
			resp.setRole(jwtService.extractRole(jwt));
			resp.setUserId(jwtService.extractUserId(jwt));
			resp.setExpiresIn(jwtService.getExpirationTime());
			return resp;
		} catch (Exception e) {
			return null;
		}
	}
}


