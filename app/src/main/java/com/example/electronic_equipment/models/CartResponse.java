package com.example.electronic_equipment.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CartResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<Cart> data;

    public String getStatus() {
        return status;
    }

    public List<Cart> getData() {
        return data;
    }
}

