package com.wineko.api.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {
    private String oldPassword;
    private String newPassword;

    // Getters et Setters
}
