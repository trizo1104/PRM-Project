package com.example.electronic_equipment.acvitities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.electronic_equipment.adapters.ProductAdapter;
import com.example.electronic_equipment.R;
import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.networks.ProductApi;
import com.example.electronic_equipment.networks.RetrofitClient;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productList;
    ImageButton btnAdd;
    Retrofit retrofit = RetrofitClient.getInstance();
    ProductApi productApi = retrofit.create(ProductApi.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);

        //init
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddEditProductActivity.class));
        });

        fetchProductsFromAPI();
    }



    private void fetchProductsFromAPI() {
        Call<List<Product>> call = productApi.getAllProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("API_ERROR", "Lỗi phản hồi từ server");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", "Không gọi được API", t);
            }
        });
    }

}
