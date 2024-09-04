package com.wineko.api.dto;

import jakarta.annotation.sql.DataSourceDefinitions;
import lombok.Data;

@Data
public class OrderLineDto {

    private Integer id;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
}
