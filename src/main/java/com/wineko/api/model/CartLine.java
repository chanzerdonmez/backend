package com.wineko.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_line")
public class CartLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article; // Changed to reference Article entity

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Double price;
}
