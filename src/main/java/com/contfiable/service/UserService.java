package com.contfiable.service;

import com.contfiable.dto.user.*;
import com.contfiable.model.User;
import com.contfiable.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public AuthResponse register(RegisterRequest request) {
		// Check if email already exists
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new org.springframework.dao.DataIntegrityViolationException("Email already exists");
		}
		
		User user = new User();
		user.setName(request.getName());
		user.setLastName(request.getLastName());
		user.setSecondLastName(request.getSecondLastName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		if (user.getRole() == null) {
			user.setRole(User.Role.client);
		}
		userRepository.save(user);

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name().toUpperCase()))
		);

		String accessToken = jwtService.generateToken(userDetails);
		long expiresIn = jwtService.getExpirationTime();
		AuthResponse resp = new AuthResponse();
		resp.setAccessToken(accessToken);
		resp.setExpiresIn(expiresIn);
		resp.setEmail(user.getEmail());
		resp.setRole(user.getRole() != null ? user.getRole().name() : null);
		resp.setPhone(user.getPhone());
		resp.setUserId(user.getId() != null ? String.valueOf(user.getId()) : null);
		return resp;
	}

	public AuthResponse login(LoginRequest request) {
		Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
		if (userOpt.isEmpty()) {
			return null;
		}
		User user = userOpt.get();
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			return null;
		}
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name().toUpperCase()))
		);
		String accessToken = jwtService.generateToken(userDetails);
		long expiresIn = jwtService.getExpirationTime();
		AuthResponse resp = new AuthResponse();
		resp.setAccessToken(accessToken);
		resp.setExpiresIn(expiresIn);
		resp.setEmail(user.getEmail());
		resp.setRole(user.getRole() != null ? user.getRole().name() : null);
		resp.setPhone(user.getPhone());
		resp.setUserId(user.getId() != null ? String.valueOf(user.getId()) : null);
		return resp;
	}

	public AuthResponse refresh(RefreshRequest request) {
		// Simplificado: en un caso real, validarías el refreshToken y emitirías uno nuevo
		AuthResponse resp = new AuthResponse();
		resp.setAccessToken(null);
		resp.setRefreshToken(request.getRefreshToken());
		resp.setExpiresIn(jwtService.getExpirationTime());
		return resp;
	}

	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(UserResponse::from).toList();
	}
}


