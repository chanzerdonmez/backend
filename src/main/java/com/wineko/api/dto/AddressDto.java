package com.wineko.api.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AddressDto {
    private Integer id;
    private String name;
    private String firstName;
    private String city;
    private String streetName;
    private String streetNumber;
    private String zipCode;
}
