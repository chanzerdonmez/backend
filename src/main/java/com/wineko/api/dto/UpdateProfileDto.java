package com.wineko.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateProfileDto {
    private String name;
    private String firstName;
    private String phone;
    private Date birthdate;

    // Getters et Setters
}
