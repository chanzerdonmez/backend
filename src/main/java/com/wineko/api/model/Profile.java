package com.wineko.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Integer id;

    @Column(name ="name")
    private String name;

    @Column(name ="first_name")
    private String firstName;

    @Column(name ="birthdate")
    private Date birthdate;

    @Column(name ="phone_number")
    private String phoneNumber;

}
