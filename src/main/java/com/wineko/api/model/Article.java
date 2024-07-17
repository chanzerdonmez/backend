package com.wineko.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "article")
public class Article {

    public Article(){
        this.active = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Integer id;

    @Column(name ="brand")
    private String brand;

    @Column(name ="title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name ="date_creation")
    private Date dateCreation;

    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)  // colonne de jointure
    private Category category;

    public boolean isActive() {
        return this.active;
    }
}