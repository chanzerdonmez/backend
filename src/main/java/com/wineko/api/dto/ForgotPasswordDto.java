package com.wineko.api.dto;

import lombok.Data;

@Data
public class ForgotPasswordDto {
    private String email;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}