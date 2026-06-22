package com.example.inquiry.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    public Category() {}

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public Long getId()     { return id; }
    public String getName() { return name; }

    // Setters
    public void setId(Long id)         { this.id = id; }
    public void setName(String name)   { this.name = name; }
}
