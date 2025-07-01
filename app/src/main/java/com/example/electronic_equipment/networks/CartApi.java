package com.example.electronic_equipment.networks;

import com.example.electronic_equipment.models.Cart;
import com.example.electronic_equipment.models.CartResponse;
import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.models.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartApi {
    @POST("CartItems/{productId}/add-to-cart")
    Call<Void> addToCart(@Path("productId") String productId, @Query("userId") String userId, @Query("quantity") int quantity);

    @GET("CartItems/cart/{userId}")
    Call<CartResponse> getAllCarts(@Path("userId") String userId);

    @PUT("CartItems/{productId}/decrease-to-quantity")
    Call<CartResponse> decreaseCart(@Path("productId") String productId, @Query("userId") String userId, @Query("quantity") int quantity);
}




