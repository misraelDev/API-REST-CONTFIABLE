package com.contfiable.controller.user;

import com.contfiable.dto.user.*;
import com.contfiable.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		if (response == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
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

	@GetMapping("/{userId}")
	public ResponseEntity<UserDetailResponse> getUser(@PathVariable Long userId) {
		return ResponseEntity.ok(userService.getUser(userId));
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@PutMapping("/{userId}")
	public ResponseEntity<UserDetailResponse> updateUser(
			@PathVariable Long userId,
			@Valid @RequestBody UserUpdateRequest request
	) {
		UserDetailResponse response = userService.updateUser(userId, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/test-validation")
	public ResponseEntity<Map<String, Object>> testValidation(@Valid @RequestBody RegisterRequest request) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Validation passed!");
		response.put("data", request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/test-error")
	public ResponseEntity<String> testError() {
		throw new RuntimeException("Test error for debugging");
	}
}
