package com.example.electronic_equipment.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private String description;
    private double price;
    private int imageResId;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product(String name, String description, double price, int imageResId)  {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
}

