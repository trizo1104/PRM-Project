package com.example.electronic_equipment.networks;

import com.example.electronic_equipment.models.Category;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface CategoryApi {
    @GET("Categories")
    Call<List<Category>> getAllCategories();
}

