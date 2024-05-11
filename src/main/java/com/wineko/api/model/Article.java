package com.wineko.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private float price;

    @Column(name = "image")
    private String image;

    @Column(name = "active")
    private boolean active;

    public boolean isActive() {
        return this.active;
    }
}