package com.contfiable.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {

	@NotBlank(message = "El nombre es obligatorio")
	@Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
	private String name;
	
	@NotBlank(message = "El apellido paterno es obligatorio")
	@Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
	private String lastName;
	
	@Size(max = 50, message = "El apellido materno no puede exceder 50 caracteres")
	private String secondLastName;
	
	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El formato del email no es válido")
	@Size(max = 100, message = "El email no puede exceder 100 caracteres")
	private String email;
	
	@Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
	private String phone;
	
	@NotBlank(message = "La contraseña es obligatoria")
	@Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSecondLastName() {
		return secondLastName;
	}

	public void setSecondLastName(String secondLastName) {
		this.secondLastName = secondLastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}


