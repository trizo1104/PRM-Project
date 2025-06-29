package com.example.electronic_equipment.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String productId;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imageUrl;
    private String createdAt;
    private boolean isActive;
    private String categoryId;


    public Product(String ProductId, String Name, String Description, double Price, int Quantity, String ImageUrl, String CreateAt, boolean IsActive, String CategoryId) {
        productId = ProductId;
        name = Name;
        description = Description;
        price = Price;
        quantity = Quantity;
        imageUrl = ImageUrl;
        createdAt = CreateAt;
        isActive = IsActive;
        categoryId = CategoryId;
    }



    // Getter & Setter
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCreateAt() {
        return createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setProductId(String ProductId) {
        productId = ProductId;
    }

    public void setName(String Name) {
        name = Name;
    }

    public void setDescription(String Description) {
        description = Description;
    }

    public void setPrice(double Price) {
        price = Price;
    }

    public void setQuantity(int Quantity) {
        quantity = Quantity;
    }

    public void setImageUrl(String ImageUrl) {imageUrl = ImageUrl;
    }

    public void setCreateAt(String CreatedAt) {
        createdAt = CreatedAt;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCategoryId(String CategoryId) {
        categoryId = CategoryId;
    }

}
