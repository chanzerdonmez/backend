package com.wineko.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Integer id;

//    @Column(name ="number_order")
//    private String numberOrder;

    @Column(name ="city")
    private String city;

    @Column(name ="zip_code")
    private String zipCode;

    @Column(name ="street_name")
    private String streetName;

    @Column(name ="street_number")
    private String streetNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)  // colonne de jointure
    private Users user;

    @Transient // This field is not persisted in the database
    private Integer userId;

}
