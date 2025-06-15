package com.example.electronic_equipment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.adapter.ProductAdapter;
import com.example.electronic_equipment.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerNewArrival;
    ProductAdapter productAdapter;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerNewArrival = findViewById(R.id.recyclerNewArrival);
        recyclerNewArrival.setLayoutManager(new GridLayoutManager(this, 2));

        // Sample data
        productList = new ArrayList<>();
        productList.add(new Product("Nike Air Force", "Men's Road Running Shoes", 167.76, R.drawable.af1));
        productList.add(new Product("Nike Air Force", "Men's Road Running Shoes", 158.36, R.drawable.af1));
        // Add more...

        productAdapter = new ProductAdapter(this, productList);
        recyclerNewArrival.setAdapter(productAdapter);
    }
}

