package com.contfiable.security;

import com.contfiable.dto.user.AuthResponse;
import com.contfiable.service.user.AuthService;
import com.contfiable.service.user.TokenCacheService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final AuthService authService;
	private final TokenCacheService tokenCacheService;

	public JwtAuthenticationFilter(AuthService authService, TokenCacheService tokenCacheService) {
		this.authService = authService;
		this.tokenCacheService = tokenCacheService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String jwt;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);

		try {
			AuthResponse authResponse = tokenCacheService.getCachedAuthResponse(jwt);

			if (authResponse == null) {
				logger.debug("Token not in cache, verifying locally via AuthService...");
				authResponse = authService.verifyUserToken(jwt);
			}

			if (authResponse != null && authResponse.getUserId() != null) {
				String userId = authResponse.getUserId();

				if (SecurityContextHolder.getContext().getAuthentication() == null) {
					String role = authResponse.getRole() != null && !authResponse.getRole().isBlank()
							? authResponse.getRole().toLowerCase()
							: "client";

					List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

					UserDetails userDetails = User.builder()
							.username(userId)
							.password("")
							.authorities(authorities)
							.accountExpired(false)
							.accountLocked(false)
							.credentialsExpired(false)
							.disabled(false)
							.build();

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
					);

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e.getMessage());
		}

		filterChain.doFilter(request, response);
	}
}


