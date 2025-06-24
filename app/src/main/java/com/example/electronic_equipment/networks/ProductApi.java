package com.example.electronic_equipment.networks;


import com.example.electronic_equipment.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductApi {
    @GET("product")
    Call<List<Product>> getAllProducts();

    @POST("product")
    Call<Void> addProduct(@Body Product product);

    @PUT("product/{id}")
    Call<Void> updateProduct(@Path("id") String id, @Body Product product);

    @DELETE("product/{id}")
    Call<Void> deleteProduct(@Path("id") String id);
}

