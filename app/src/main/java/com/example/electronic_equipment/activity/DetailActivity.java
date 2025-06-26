package com.example.electronic_equipment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.adapter.CartManager;
import com.example.electronic_equipment.model.Product;

public class DetailActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView txtName, txtDesc, txtPrice;

    Button btnAddToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.electronic_equipment.R.layout.activity_detail);

        imgProduct = findViewById(R.id.imgProduct);
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDesc);
        txtPrice = findViewById(R.id.txtPrice);

        // Get product from intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String desc = intent.getStringExtra("desc");
        double price = intent.getDoubleExtra("price", 0);
        int image = intent.getIntExtra("image", R.drawable.af1);
        btnAddToCart = findViewById(R.id.btnAddToCart);



        txtName.setText(name);
        txtDesc.setText(desc);
        txtPrice.setText("$" + price);
        imgProduct.setImageResource(image);

        Product product = new Product(name, desc, price, image);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Goes back to the previous screen
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        });

    }
}

