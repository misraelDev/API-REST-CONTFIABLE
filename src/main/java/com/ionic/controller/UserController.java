package com.ionic.controller;

import com.ionic.dto.*;
import com.ionic.service.UserService;
import jakarta.validation.Valid;
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
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
		AuthResponse response = userService.login(request);
		if (response == null) {
			return ResponseEntity.status(401).build();
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
		AuthResponse response = userService.refresh(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
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


