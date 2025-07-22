package com.example.electronic_equipment.networks;

import com.example.electronic_equipment.models.CreateOrderResponse;
import com.example.electronic_equipment.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderAPI {
    @GET("Orders/GetAllOrder")
    Call<List<Order>> getAllOrders(@Header("Authorization") String token);

    @GET("Orders/GetAllOrderOfUser")
    Call<List<Order>> getAllOrdersOfUser(@Header("Authorization") String token);

    @POST("Orders/create")
    Call<Order> createOrder(@Header("Authorization") String token);

    @POST("Orders/{userId}")
    Call<CreateOrderResponse> clearCartThenCreateOrder(@Path("userId") String userId);
}
