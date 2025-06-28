package com.example.electronic_equipment.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String ProductId;
    private String Name;
    private String Description;
    private double Price;
    private int Quantity;
    private String ImageUrl;
    private String CreateAt;
    private boolean IsActive;
    private String CategoryId;


    public Product(String productId, String name, String description, double price, int quantity, String imageUrl, String createAt, boolean isActive, String categoryId) {
        ProductId = productId;
        Name = name;
        Description = description;
        Price = price;
        Quantity = quantity;
        ImageUrl = imageUrl;
        CreateAt = createAt;
        IsActive = isActive;
        CategoryId = categoryId;
    }



    // Getter & Setter
    public String getProductId() {
        return ProductId;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public double getPrice() {
        return Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public boolean isActive() {
        return IsActive;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

}
