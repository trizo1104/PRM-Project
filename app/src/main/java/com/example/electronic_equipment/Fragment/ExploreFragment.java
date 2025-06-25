package com.example.electronic_equipment.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.adapter.ProductAdapter;
import com.example.electronic_equipment.model.Product;

import java.util.ArrayList;

// HomeFragment.java
public class ExploreFragment extends Fragment {

    private EditText editSearch;
    private RecyclerView recyclerNewArrival;
    private ProductAdapter adapter;
    private ArrayList<Product> productList;

    public ExploreFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        editSearch = view.findViewById(R.id.editSearch);
        recyclerNewArrival = view.findViewById(R.id.recyclerNewArrival);

        setupRecyclerView();
        setupSearchFilter();

        return view;
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        // Dummy data
        productList.add(new Product("Nike Air Max", "Running shoes", 150, R.drawable.af1));
        productList.add(new Product("Adidas UltraBoost", "High performance", 180, R.drawable.af1));
        // ... add more

        adapter = new ProductAdapter(getContext(), productList, product -> {
            Intent intent = new Intent(getActivity(), com.example.electronic_equipment.activity.DetailActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("desc", product.getDescription());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("image", product.getImageResId());
            startActivity(intent);
        });
        recyclerNewArrival.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerNewArrival.setAdapter(adapter);
    }

    private void setupSearchFilter() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//                adapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }
}

