package com.example.electronic_equipment.networks;

import com.example.electronic_equipment.models.LoginRequest;
import com.example.electronic_equipment.models.LoginResponse;
import com.example.electronic_equipment.models.RegisterRequest;
import com.example.electronic_equipment.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("Auths/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("Auths/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
}
