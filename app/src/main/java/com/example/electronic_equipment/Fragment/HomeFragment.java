package com.example.electronic_equipment.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.electronic_equipment.R;
import com.example.electronic_equipment.activity.DetailActivity;
import com.example.electronic_equipment.adapter.ProductAdapter;
import com.example.electronic_equipment.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerNewArrival;
    ProductAdapter productAdapter;
    List<Product> productList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerNewArrival = view.findViewById(R.id.recyclerNewArrival);
        recyclerNewArrival.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productList = new ArrayList<>();
        productList.add(new Product("Nike Air Force", "Men's Road Running Shoes", 167.76, R.drawable.af1));
        productList.add(new Product("Nike Air Force", "Men's Road Running Shoes", 158.36, R.drawable.af1));
        productList.add(new Product("Nike Air Force", "Men's Road Running Shoes", 167.76, R.drawable.af1));
        productList.add(new Product("Nike Air Force", "Men's Road Running Shoes", 158.36, R.drawable.af1));

        productAdapter = new ProductAdapter(getContext(), productList, product -> {
            Intent intent = new Intent(getActivity(), com.example.electronic_equipment.activity.DetailActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("desc", product.getDescription());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("image", product.getImageResId());
            startActivity(intent);
        });

        recyclerNewArrival.setAdapter(productAdapter);

        return view;
    }
}
