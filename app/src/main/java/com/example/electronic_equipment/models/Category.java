package com.example.electronic_equipment.models;

public class Category {
    private String CategoryId;
    private String Name;

    public Category(String CategoryId, String Name) {
        this.CategoryId = CategoryId;
        this.Name = Name;
    }

    public String getId() { return CategoryId; }
    public String getName() { return Name; }

    @Override
    public String toString() {
        return Name;
    }
}

