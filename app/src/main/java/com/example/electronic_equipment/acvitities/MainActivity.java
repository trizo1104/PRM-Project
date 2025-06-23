package com.example.electronic_equipment.acvitities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.electronic_equipment.adapters.ProductAdapter;
import com.example.electronic_equipment.R;
import com.example.electronic_equipment.models.Category;
import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.networks.CategoryApi;
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
    Spinner spinnerCategory;
    List<Category> categoryList;
    String selectedCategoryId = null;
    ImageButton btnAdd;
    private Retrofit retrofit;
    private ProductApi productApi;
    private CategoryApi categoryApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        retrofit = RetrofitClient.getInstance();
        productApi = retrofit.create(ProductApi.class);
        categoryApi = retrofit.create(CategoryApi.class);

        //init
        productList = new ArrayList<>();
        categoryList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddEditProductActivity.class));
        });

        fetchProductsFromAPI();
        loadCategories();
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

    private void loadCategories() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://67078cefa0e04071d22acf43.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CategoryApi categoryApi = retrofit.create(CategoryApi.class);
        categoryApi.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.add(new Category("all", "Tất cả")); // item đầu tiên là "Tất cả"
                    categoryList.addAll(response.body());

                    ArrayAdapter<Category> adapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_spinner_item, categoryList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);

                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Category selected = categoryList.get(position);
                            selectedCategoryId = selected.getId();

                            if (selectedCategoryId.equals("all")) {
                                fetchProductsFromAPI(); // load toàn bộ
                            } else {
                                fetchProductsByCategory(selectedCategoryId); // lọc theo category
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchProductsByCategory(String categoryId) {
        productApi.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> filtered = new ArrayList<>();
                    for (Product p : response.body()) {
                        if (categoryId != null && categoryId.equals(p.getCategoryId())) {
                            filtered.add(p);
                        }
                    }
                    productList.clear();
                    productList.addAll(filtered);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi filter sản phẩm", t);
            }
        });
    }


}
