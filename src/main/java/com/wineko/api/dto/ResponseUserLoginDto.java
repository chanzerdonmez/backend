package com.wineko.api.dto;

import lombok.Data;

@Data
public class ResponseUserLoginDto {

    private Integer id;
    private String name;
    private String firstName;
    private String email;
    private String token;
    private Enum role;

}
