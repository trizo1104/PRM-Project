package com.example.electronic_equipment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.adapters.ProductAdapter;
import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.models.ProductResponse;
import com.example.electronic_equipment.networks.ProductApi;
import com.example.electronic_equipment.networks.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductByCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private ProductApi productApi;

    private int currentPage = 0;
    private final int pageSize = 3;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private String categoryId;

    private final int visibleThreshold = 4;

    private int lastRequestedPage = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);

        categoryId = getIntent().getStringExtra("category_id");

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish(); // Goes back to the previous activity
        });


        recyclerView = findViewById(R.id.recyclerCategoryProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, productList, true, new ProductAdapter.OnItemActionListener() {
            @Override
            public void onDetail(Product product) {
                Log.d("DEBUG", "Product clicked: " + product.getName());
                Intent intent = new Intent(ProductByCategoryActivity.this, DetailActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@androidx.annotation.NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isLastPage) {
                        if ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
                            if (currentPage > lastRequestedPage) {
                                lastRequestedPage = currentPage;
                                isLoading = true;
                                loadProductsByCategory(currentPage);
                            }
                        }
                    }
                }
            }
        });

        productApi = RetrofitClient.getInstance().create(ProductApi.class);

        loadProductsByCategory(currentPage);

        // Optional: implement endless scroll listener here
    }

    private void loadProductsByCategory(int page) {
        if (isLoading || isLastPage) return;

        isLoading = true;

        Call<ProductResponse> call = productApi.getProductsByCategory(categoryId, page, pageSize);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                isLoading = false;

                if (response.isSuccessful() && response.body() != null) {
                    List<Product> newProducts = response.body().getData();

                    if (newProducts != null && !newProducts.isEmpty()) {
                        productList.addAll(newProducts);
                        adapter.notifyDataSetChanged();
                        currentPage++;

                        // Optional: check if it's the last pag
                    } else {
                        isLastPage = true;
                        Toast.makeText(ProductByCategoryActivity.this, "No more products", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Error in response");
                    Toast.makeText(ProductByCategoryActivity.this, "Error loading products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                isLoading = false;
                Log.e("API_ERROR", "API call failed", t);
                Toast.makeText(ProductByCategoryActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
