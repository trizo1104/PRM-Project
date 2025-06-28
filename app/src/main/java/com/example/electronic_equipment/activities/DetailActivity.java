package com.example.electronic_equipment.activities;


import android.content.Intent;
import android.os.Bundle;
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

public class DetailActivity extends AppCompatActivity {

    ImageView imgProduct, btnBack;
    TextView txtName, txtDesc, txtPrice, DetailName;

    Button btnAddToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.electronic_equipment.R.layout.activity_detail);

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

            btnAddToCart.setOnClickListener(v -> {
                CartManager.getInstance().addToCart(product);
                Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            });
        }

    }
}

