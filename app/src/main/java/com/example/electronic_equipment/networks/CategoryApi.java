package com.example.electronic_equipment.networks;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;
import com.example.electronic_equipment.models.Category;

public interface CategoryApi {
    @GET("Categories")
    Call<List<Category>> getAllCategories();
}

