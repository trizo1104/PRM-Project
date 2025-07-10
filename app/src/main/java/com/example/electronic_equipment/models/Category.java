package com.example.electronic_equipment.models;

public class Category {
    private String categoryId;
    private String name;

    public Category(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}


