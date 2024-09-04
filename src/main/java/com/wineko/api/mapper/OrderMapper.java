package com.wineko.api.mapper;

import com.wineko.api.dto.AddressDto;
import com.wineko.api.dto.OrderDto;
import com.wineko.api.dto.OrderLineDto;
import com.wineko.api.model.Address;
import com.wineko.api.model.OrderLine;
import com.wineko.api.model.Orders;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto toDto(Orders order) {
        if (order == null) {
            return null;
        }

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setNumberOrder(order.getNumberOrder());
        dto.setDateCreation(order.getDateCreation());
        dto.setUserEmail(order.getUsers().getEmail());
        dto.setBillingAddress(toDto(order.getBillingAddress()));
        dto.setShippingAddress(toDto(order.getShippingAddress()));
        dto.setOrderLines(order.getOrderLines().stream().map(this::toDto).collect(Collectors.toList()));

        double totalPrice = order.getOrderLines().stream()
                .mapToDouble(line -> line.getQuantity() * line.getUnitPrice())
                .sum();
        dto.setTotalPrice(totalPrice);

        return dto;
    }

    public AddressDto toDto(Address address) {
        if (address == null) {
            return null;
        }

        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setName(address.getName());
        dto.setFirstName(address.getFirstName());
        dto.setCity(address.getCity());
        dto.setStreetName(address.getStreetName());
        dto.setStreetNumber(address.getStreetNumber());
        dto.setZipCode(address.getZipCode());

        return dto;
    }

    public OrderLineDto toDto(OrderLine orderLine) {
        if (orderLine == null) {
            return null;
        }

        OrderLineDto dto = new OrderLineDto();
        dto.setId(orderLine.getId());
        dto.setProductName(orderLine.getProductName());
        dto.setQuantity(orderLine.getQuantity());
        dto.setUnitPrice(orderLine.getUnitPrice());

        return dto;
    }
}
