package com.example.electronic_equipment.networks;


import com.example.electronic_equipment.models.Category;
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
    Call<ProductResponse> getSearchProduct(@Query("name") String name, @Query("pageIndex") int page,
                                           @Query("pageSize") int limit);

    @GET("Products")
    Call<ProductResponse> getAllProducts(@Query("pageIndex") int page,
                                         @Query("pageSize") int limit);

    @GET("Products/category/{categoryId}")
    Call<ProductResponse> getProductsByCategory(@Path("categoryId") String categoryId, @Query("pageIndex") int page,
                                                @Query("pageSize") int limit);

    @POST("Products")
    Call<Void> addProduct(@Body Product product);

    @PUT("Products/{id}")
    Call<Void> updateProduct(@Path("id") String id, @Body Product product);

    @DELETE("Products/{id}")
    Call<Void> deleteProduct(@Path("id") String id);
}

