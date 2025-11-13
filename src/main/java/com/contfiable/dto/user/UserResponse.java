package com.contfiable.dto.user;

import com.contfiable.model.User;

public class UserResponse {

	private Long id;
	private String name;
	private String lastName;
	private String secondLastName;
	private String email;
	private String phone;
	private String codePhone;
	private String role;

	public static UserResponse from(User user) {
		UserResponse r = new UserResponse();
		r.id = user.getId();
		r.name = user.getName();
		r.lastName = user.getLastName();
		r.secondLastName = user.getSecondLastName();
		r.email = user.getEmail();
		r.phone = user.getPhone();
		r.codePhone = user.getCodePhone();
		r.role = user.getRole() != null ? user.getRole().name() : null;
		return r;
	}

	public Long getId() { return id; }
	public String getName() { return name; }
	public String getLastName() { return lastName; }
	public String getSecondLastName() { return secondLastName; }
	public String getEmail() { return email; }
	public String getPhone() { return phone; }
	public String getCodePhone() { return codePhone; }
	public String getRole() { return role; }
}


