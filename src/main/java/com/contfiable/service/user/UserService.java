package com.contfiable.service.user;

import com.contfiable.dto.user.*;
import com.contfiable.exception.BadRequestException;
import com.contfiable.exception.ResourceNotFoundException;
import com.contfiable.model.User;
import com.contfiable.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new org.springframework.dao.DataIntegrityViolationException("Email already exists");
		}

		User user = new User();
		user.setName(request.getName());
		user.setLastName(request.getLastName());
		user.setSecondLastName(request.getSecondLastName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setCodePhone(request.getCodePhone());
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
		resp.setCodePhone(user.getCodePhone());
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
		resp.setCodePhone(user.getCodePhone());
		resp.setUserId(user.getId() != null ? String.valueOf(user.getId()) : null);
		return resp;
	}

	public AuthResponse refresh(RefreshRequest request) {
		AuthResponse resp = new AuthResponse();
		resp.setAccessToken(null);
		resp.setRefreshToken(request.getRefreshToken());
		resp.setExpiresIn(jwtService.getExpirationTime());
		return resp;
	}

	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(UserResponse::from).toList();
	}

	public UserDetailResponse createUser(UserCreateRequest request) {
		userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
			throw new BadRequestException("El correo electrónico ya está registrado");
		});

		User user = new User();
		user.setName(request.getName());
		user.setLastName(request.getLastName());
		user.setSecondLastName(request.getSecondLastName());
		user.setEmail(request.getEmail());
		user.setCodePhone(request.getCodePhone());
		user.setPhone(request.getPhone());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole() != null ? request.getRole() : User.Role.client);
		user.setCompanyName(request.getCompanyName());
		user.setRfc(request.getRfc());
		user.setLegalName(request.getLegalName());
		user.setTaxAddress(request.getTaxAddress());
		user.setCfdiUse(request.getCfdiUse());
		user.setTaxRegime(request.getTaxRegime());

		User saved = userRepository.save(user);
		return UserDetailResponse.fromEntity(saved);
	}

	public UserDetailResponse getUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id %d".formatted(id)));
		return UserDetailResponse.fromEntity(user);
	}

	public UserDetailResponse updateUser(Long id, UserUpdateRequest request) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id %d".formatted(id)));

		if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
			userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
				if (!existing.getId().equals(user.getId())) {
					throw new BadRequestException("El correo electrónico ya está registrado");
				}
			});
			user.setEmail(request.getEmail());
		}

		if (StringUtils.hasText(request.getName())) {
			user.setName(request.getName());
		}
		if (StringUtils.hasText(request.getLastName())) {
			user.setLastName(request.getLastName());
		}
		if (request.getSecondLastName() != null) {
			user.setSecondLastName(request.getSecondLastName());
		}
		if (StringUtils.hasText(request.getCodePhone())) {
			user.setCodePhone(request.getCodePhone());
		}
		if (StringUtils.hasText(request.getPhone())) {
			user.setPhone(request.getPhone());
		}
		if (StringUtils.hasText(request.getPassword())) {
			user.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		if (request.getRole() != null) {
			user.setRole(request.getRole());
		}
		if (request.getCompanyName() != null) {
			user.setCompanyName(request.getCompanyName());
		}
		if (request.getRfc() != null) {
			user.setRfc(request.getRfc());
		}
		if (request.getLegalName() != null) {
			user.setLegalName(request.getLegalName());
		}
		if (request.getTaxAddress() != null) {
			user.setTaxAddress(request.getTaxAddress());
		}
		if (request.getCfdiUse() != null) {
			user.setCfdiUse(request.getCfdiUse());
		}
		if (request.getTaxRegime() != null) {
			user.setTaxRegime(request.getTaxRegime());
		}

		User saved = userRepository.save(user);
		return UserDetailResponse.fromEntity(saved);
	}

	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id %d".formatted(id)));
		userRepository.delete(user);
	}
}
