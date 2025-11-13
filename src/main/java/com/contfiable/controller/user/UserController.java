package com.contfiable.controller.user;

import com.contfiable.dto.user.*;
import com.contfiable.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		AuthResponse response = userService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
		AuthResponse response = userService.login(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
		AuthResponse response = userService.refresh(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<UserDetailResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
		UserDetailResponse response = userService.createUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Obtiene el perfil del usuario autenticado.
	 * Requiere autenticación JWT en el header Authorization: Bearer {token}
	 * No requiere ID en la URL, se obtiene del token.
	 */
	@GetMapping("/me")
	public ResponseEntity<UserDetailResponse> getCurrentUser() {
		return ResponseEntity.ok(userService.getCurrentUser());
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	/**
	 * Actualiza el perfil del usuario autenticado.
	 * Requiere autenticación JWT en el header Authorization: Bearer {token}
	 * No requiere ID en la URL, se obtiene del token.
	 */
	@PutMapping("/me")
	public ResponseEntity<UserDetailResponse> updateCurrentUser(
			@Valid @RequestBody UserUpdateRequest request
	) {
		UserDetailResponse response = userService.updateCurrentUser(request);
		return ResponseEntity.ok(response);
	}

	/**
	 * Elimina el perfil del usuario autenticado.
	 * Requiere autenticación JWT en el header Authorization: Bearer {token}
	 * No requiere ID en la URL, se obtiene del token.
	 */
	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteCurrentUser() {
		userService.deleteCurrentUser();
		return ResponseEntity.noContent().build();
	}
}
