package com.example.electronic_equipment.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.electronic_equipment.R;
import com.example.electronic_equipment.adapters.CartManager;
import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.networks.CartApi;
import com.example.electronic_equipment.networks.ProductApi;
import com.example.electronic_equipment.networks.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {

    ImageView imgProduct, btnBack, btnPlus, btnMinus;
    TextView txtName, txtDesc, txtPrice, DetailName, txtQuantity;

    Button btnAddToCart;

    private CartApi cartApi;

    int quantity = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.electronic_equipment.R.layout.activity_detail);


        txtQuantity = findViewById(R.id.txtQuantity);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);

        imgProduct = findViewById(R.id.imgProduct);
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDesc);
        txtPrice = findViewById(R.id.txtPrice);
        DetailName = findViewById(R.id.DetailName);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);

        // Get product from intent
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");

        if (product != null) {
            txtName.setText(product.getName());
            DetailName.setText("Detail " + product.getName());
            txtDesc.setText(product.getDescription());
            txtPrice.setText(String.format("%,.0f đ", product.getPrice()));

            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(imgProduct);
            } else {
                imgProduct.setImageResource(R.drawable.placeholder_image);
            }

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Goes back to the previous screen
                }
            });

            Retrofit retrofit = RetrofitClient.getInstance();
            cartApi = retrofit.create(CartApi.class);

            btnAddToCart.setOnClickListener(v -> {
                String productId = String.valueOf(product.getProductId());
                String userId = "123"; // Replace with your logged-in user’s ID
                int qty = quantity;

                cartApi.addToCart(productId, userId, qty).enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("API", "Fetched " + productId + " products");
                        Log.d("API", "Fetched " + userId + " products");
                        Log.d("API", "Fetched " + qty + " products");
                        if (response.isSuccessful()) {
                            Toast.makeText(DetailActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailActivity.this, "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(DetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });


            btnPlus.setOnClickListener(v -> {
                quantity++;
                txtQuantity.setText(String.valueOf(quantity));
            });

            btnMinus.setOnClickListener(v -> {
                if (quantity > 1) {
                    quantity--;
                    txtQuantity.setText(String.valueOf(quantity));
                }
            });
        }

    }
}

