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
    private int currentPage = 0;
    private final int pageSize = 3;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private final int visibleThreshold = 4;

    private int lastRequestedPage = -1;

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

        recyclerNewArrival.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                fetchProductsFromAPI(currentPage);
                            }
                        }
                    }
                }
            }
        });


        Retrofit retrofit = RetrofitClient.getInstance();
        productApi = retrofit.create(ProductApi.class);

        fetchProductsFromAPI(currentPage);

        return view;
    }

    private void fetchProductsFromAPI(int page) {
        isLoading = true;

        Call<ProductResponse> call = productApi.getAllProducts("", page, pageSize);
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
                    }

                    // Check if we've loaded all pages
//                    int totalItems = response.body().getTotalItems(); // example
//                    if (productList.size() >= totalItems) {
//                        isLastPage = true;
//                    }
                } else {
                    Log.e("API_ERROR", "Server returned an error");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                isLoading = false;
                Log.e("API_ERROR", "API call failed", t);
            }
        });
    }


}
