package com.example.electronic_equipment.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.activities.DetailActivity;
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
import retrofit2.Retrofit;

// HomeFragment.java
public class ExploreFragment extends Fragment {

    private EditText editSearch;
    private RecyclerView recyclerNewArrival;
    ProductAdapter adapter;
    private ArrayList<Product> productList;
    private Retrofit retrofit;
    private ProductApi productApi;

    public ExploreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        editSearch = view.findViewById(R.id.editSearch);
        retrofit = RetrofitClient.getInstance();
        productApi = retrofit.create(ProductApi.class);
        recyclerNewArrival = view.findViewById(R.id.recyclerNewArrival);
        recyclerNewArrival.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();

//        setupRecyclerView();
        fetchProductsFromAPI();
        setupSearchFilter();

        return view;
    }

    private void fetchProductsFromAPI() {
        String searchKeyword = editSearch.getText().toString();

        Call<ProductResponse> call = productApi.getAllProducts(searchKeyword);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body().getData());

                    if (adapter == null) {
                        adapter = new ProductAdapter(
                                getContext(),
                                productList,
                                true,
                                new ProductAdapter.OnItemActionListener() {
                                    @Override
                                    public void onDetail(Product product) {
                                        Log.d("DEBUG", "Product clicked: " + product.getName());
                                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                                        intent.putExtra("product", product);
                                        startActivity(intent);
                                    }}
                        );

                        recyclerNewArrival.setAdapter(adapter);


                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    Log.d("API", "Fetched " + productList.size() + " products");
                } else {
                    Log.e("API_ERROR", "Lỗi phản hồi từ server");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e("API_ERROR", "Không gọi được API", t);
            }
        });
    }


//    private void setupRecyclerView() {
//        productList = new ArrayList<>();
//        // Dummy datas
//        productList.add(new Product("Nike Air Max", "Running shoes", 150, R.drawable.af1));
//        productList.add(new Product("Adidas UltraBoost", "High performance", 180, R.drawable.af1));
//        // ... add more
//
//        adapter = new ProductAdapter(getContext(), productList, product -> {
//            Intent intent = new Intent(getActivity(), DetailActivity.class);
//            intent.putExtra("name", product.getName());
//            intent.putExtra("desc", product.getDescription());
//            intent.putExtra("price", product.getPrice());
//            intent.putExtra("image", product.getImageResId());
//            startActivity(intent);
//        });
//        recyclerNewArrival.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerNewArrival.setAdapter(adapter);
//    }

    private void setupSearchFilter() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchProductsFromAPI();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}

