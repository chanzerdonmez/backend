package com.wineko.api.dto;

import com.wineko.api.model.Users;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {

    private Integer id;
    private String name;
    private String firstName;
    private String email;
    private String password;

    /**
     * Convert
     * @return
     */
    public Users getUser() {
        Users users = new Users();
        users.setId(this.id); // Ajouter l'ID ici
        users.setName(this.name);
        users.setFirstName(this.firstName);
        users.setEmail(this.email);
        users.setPassword(this.password);
        users.setDateCreation(new Date());
        return users;
    }

}