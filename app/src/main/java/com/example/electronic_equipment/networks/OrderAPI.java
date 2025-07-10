package com.example.electronic_equipment.networks;

import com.example.electronic_equipment.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface OrderAPI {
    @GET("Orders/GetAllOrder")
    Call<List<Order>> getAllOrders(@Header("Authorization") String token);
}
