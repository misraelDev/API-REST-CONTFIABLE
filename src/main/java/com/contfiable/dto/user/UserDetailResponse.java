package com.contfiable.dto.user;

import com.contfiable.model.User;

import java.time.OffsetDateTime;

public class UserDetailResponse {

    private Long id;
    private String name;
    private String lastName;
    private String secondLastName;
    private String email;
    private String codePhone;
    private String phone;
    private String role;
    private String companyName;
    private String rfc;
    private String legalName;
    private String taxAddress;
    private String cfdiUse;
    private String taxRegime;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static UserDetailResponse fromEntity(User user) {
        UserDetailResponse response = new UserDetailResponse();
        response.id = user.getId();
        response.name = user.getName();
        response.lastName = user.getLastName();
        response.secondLastName = user.getSecondLastName();
        response.email = user.getEmail();
        response.codePhone = user.getCodePhone();
        response.phone = user.getPhone();
        response.role = user.getRole() != null ? user.getRole().name() : null;
        response.companyName = user.getCompanyName();
        response.rfc = user.getRfc();
        response.legalName = user.getLegalName();
        response.taxAddress = user.getTaxAddress();
        response.cfdiUse = user.getCfdiUse();
        response.taxRegime = user.getTaxRegime();
        response.createdAt = user.getCreatedAt();
        response.updatedAt = user.getUpdatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCodePhone() {
        return codePhone;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRfc() {
        return rfc;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getTaxAddress() {
        return taxAddress;
    }

    public String getCfdiUse() {
        return cfdiUse;
    }

    public String getTaxRegime() {
        return taxRegime;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}

