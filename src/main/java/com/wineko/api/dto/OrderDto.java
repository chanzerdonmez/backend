package com.wineko.api.dto;

import java.util.Date;
import java.util.List;

import com.wineko.api.model.Address;
import com.wineko.api.model.Orders;
import com.wineko.api.model.Users;
import lombok.Data;

@Data
public class OrderDto {
    private Integer id;
    private String numberOrder;
    private Date dateCreation;
    private String userEmail;
    private AddressDto billingAddress;
    private AddressDto shippingAddress;
    private List<OrderLineDto> orderLines;
    private Double totalPrice; // Ajouter ici le prix total

}
