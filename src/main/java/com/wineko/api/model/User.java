package com.wineko.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Integer id;

    @Column(name ="name")
    private String name;

    @Column(name ="first_name")
    private String firstName;

    @Column(name ="email")
    private String email;

    @Column(name ="password")
    private String password;

    @Column(name ="adress")
    private String adress;

}
