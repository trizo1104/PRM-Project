package com.example.electronic_equipment.models;

import com.google.gson.annotations.SerializedName;

public class CreateOrderResponse {
    @SerializedName("message") // Ánh xạ trường "message" từ JSON
    private String message;

    @SerializedName("status") // Ánh xạ trường "status" từ JSON
    private String status;

    // Constructor (tùy chọn)
    public CreateOrderResponse(String message,String status) {
        this.message = message;
        this.status = status;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    // Setters (tùy chọn, nếu bạn cần)
    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
