package com.example.electronic_equipment.networks;


import com.example.electronic_equipment.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductApi {
    @GET("product")
    Call<List<Product>> getAllProducts();
}

