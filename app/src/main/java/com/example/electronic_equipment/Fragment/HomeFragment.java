package com.example.electronic_equipment.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class HomeFragment extends Fragment {

    RecyclerView recyclerNewArrival;
    ProductAdapter adapter;
    private Retrofit retrofit;
    private ProductApi productApi;
    List<Product> productList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerNewArrival = view.findViewById(R.id.recyclerNewArrival);
        recyclerNewArrival.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productList = new ArrayList<>();
        adapter = new ProductAdapter(getContext(), productList, true, new ProductAdapter.OnItemActionListener() {
            @Override
            public void onDetail(Product product) {
                Log.d("DEBUG", "Product clicked: " + product.getName());
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });

        recyclerNewArrival.setAdapter(adapter);

        Retrofit retrofit = RetrofitClient.getInstance();
        productApi = retrofit.create(ProductApi.class);

        fetchProductsFromAPI();

        return view;
    }

    private void fetchProductsFromAPI() {
        Call<ProductResponse> call = productApi.getAllProducts("");
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                Log.d("API", "Fetched " + response + " products");
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    Log.d("API", "Fetched " + productList.size() + " products");
                    Log.d("API", "Fetched " + productList + " products");
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

}
