package com.wineko.api.dto;

import com.wineko.api.model.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Data
public class UserInfoDto {
    private Integer id;
    private String email;
    private String firstName; // Ajout du prénom
    private String name;      // Ajout du nom
    private String phone;     // Ajout du téléphone
    private Date birthdate; // Ajout de la date de naissance
    private Enum role;

    public UserInfoDto(Integer id, String email, String firstName, String name, String phone, Date birthdate, Enum role) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.name = name;
        this.phone = phone;
        this.birthdate = birthdate;
        this.role = role;
    }
}
