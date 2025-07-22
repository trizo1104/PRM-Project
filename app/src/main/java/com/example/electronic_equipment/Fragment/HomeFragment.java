package com.example.electronic_equipment.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.activities.DetailActivity;
import com.example.electronic_equipment.activities.ProductByCategoryActivity;
import com.example.electronic_equipment.adapters.BannerAdapter;
import com.example.electronic_equipment.adapters.ProductAdapter;
import com.example.electronic_equipment.models.Category;
import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.models.ProductResponse;
import com.example.electronic_equipment.networks.CategoryApi;
import com.example.electronic_equipment.networks.ProductApi;
import com.example.electronic_equipment.networks.RetrofitClient;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Sửa ID từ recyclerNewArrival thành recyclerViewProducts
        recyclerNewArrival = view.findViewById(R.id.recyclerViewProducts);
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

        // Setup ViewPager for banner
        ViewPager2 viewPager = view.findViewById(R.id.viewPagerBanner);

        List<String> banners = Arrays.asList(
                "https://maytinhhaiphong.com/wp-content/uploads/2022/07/Laptop-cu-bao-hanh-12-thang.jpg",
                "https://maytinhhaiphong.com/wp-content/uploads/2025/03/ChatGPT-Image-16_05_10-27-thg-3-2025.png",
                "https://marketplace.canva.com/EAGivKbCdCA/1/0/400w/canva-b%C3%A0i-%C4%91%C4%83ng-instagram-sale-khuy%E1%BA%BFn-m%C3%A3i-laptop-m%C3%A1y-t%C3%ADnh-c%C3%B4ng-ngh%E1%BB%87-thanh-l%E1%BB%8Bch-xanh-tr%E1%BA%AFng-zmF2-Hf_MM8.jpg"
        );

        BannerAdapter adapter = new BannerAdapter(banners);
        viewPager.setAdapter(adapter);

        // Tự động chuyển banner
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == banners.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000); // 3 giây chuyển slide
            }
        };

        handler.post(runnable);

        // Setup categories
        fetchCategoriesAndDisplay(view);

        // Load initial products
        loadMoreProducts();

        return view;
    }

    private void fetchProductsFromAPI(int page) {
        isLoading = true;

        Call<ProductResponse> call = productApi.getAllProducts(page, pageSize);
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

    private void fetchCategoriesAndDisplay(View rootView) {
        LinearLayout categoryContainer = rootView.findViewById(R.id.categoryContainer);

        Retrofit retrofit = RetrofitClient.getInstance();
        CategoryApi categoryApi = retrofit.create(CategoryApi.class);

        Map<String, Integer> categoryImageMap = new HashMap<>();
        categoryImageMap.put("Chuột máy tính", R.drawable.mouse);
        categoryImageMap.put("Laptop", R.drawable.office_chair);
        categoryImageMap.put("Loa", R.drawable.speaker);
        categoryImageMap.put("Màn hình máy tính", R.drawable.monitor);
        categoryImageMap.put("Bàn phím", R.drawable.keyboard);
        categoryImageMap.put("Tai nghe chụp tai", R.drawable.headphones);
        categoryImageMap.put("Tai nghe dây", R.drawable.earphone);
        categoryImageMap.put("Tablet", R.drawable.tablet);
        categoryImageMap.put("Điện thoại", R.drawable.smartphone);

        categoryApi.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();

                    LayoutInflater inflater = LayoutInflater.from(requireContext());

                    for (Category category : categories) {
                        String name = category.getName();
                        Integer imageResId = categoryImageMap.get(name);

                        if (imageResId != null) {
                            View categoryView = inflater.inflate(R.layout.item_category, categoryContainer, false);

                            ImageView imageView = categoryView.findViewById(R.id.imageCategory);
                            TextView textView = categoryView.findViewById(R.id.textCategory);

                            imageView.setImageResource(imageResId);
                            textView.setText(name);

                            categoryView.setOnClickListener(v -> {
                                Intent intent = new Intent(requireContext(), ProductByCategoryActivity.class);
                                intent.putExtra("category_id", category.getCategoryId());
                                startActivity(intent);
                            });

                            categoryContainer.addView(categoryView);
                        }
                    }

                } else {
                    Log.e("Category_API", "Empty or failed response");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("Category_API", "Error fetching categories", t);
            }
        });
    }

    private void loadMoreProducts() {
        if (!isLoading && !isLastPage) {
            fetchProductsFromAPI(currentPage);
        }
    }
}
