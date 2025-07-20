package com.example.electronic_equipment.networks;

import com.example.electronic_equipment.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface ProfileApi {
    @GET("Users/GetUser")
    Call<User> getUserProfile(@Header("Authorization") String token, @Header("accept") String accept);

    @PUT("Users/SelfUpdateUser")
    Call<User> updateUserProfile(@Header("Authorization") String token, @Body() User user);
}
