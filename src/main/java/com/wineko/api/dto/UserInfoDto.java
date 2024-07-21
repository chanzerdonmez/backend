package com.wineko.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Data
public class UserInfoDto {
    private Integer id;
    private String email;

    public UserInfoDto(Integer id, String email) {
        this.id = id;
        this.email = email;
    }
}

