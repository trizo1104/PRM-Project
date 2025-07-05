package com.example.electronic_equipment.models;

public class RegisterRequest {
//    private String fullName;
    private String email;
    private String password;

    public RegisterRequest( String email, String password) {
//        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}

