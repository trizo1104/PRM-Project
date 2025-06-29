package com.example.electronic_equipment.networks;


import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.models.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("Products/search")
    Call<ProductResponse> getAllProducts(@Query("name") String name);

    @POST("product")
    Call<Void> addProduct(@Body Product product);

    @PUT("product/{id}")
    Call<Void> updateProduct(@Path("id") String id, @Body Product product);

    @DELETE("product/{id}")
    Call<Void> deleteProduct(@Path("id") String id);
}

