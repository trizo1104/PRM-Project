//package com.example.electronic_equipment.Fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ImageButton;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.electronic_equipment.R;
//import com.example.electronic_equipment.activities.AddEditProductActivity;
//import com.example.electronic_equipment.adapters.ProductAdapter;
//import com.example.electronic_equipment.models.Category;
//import com.example.electronic_equipment.models.Product;
//import com.example.electronic_equipment.models.ProductResponse;
//import com.example.electronic_equipment.networks.CategoryApi;
//import com.example.electronic_equipment.networks.ProductApi;
//import com.example.electronic_equipment.networks.RetrofitClient;
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//
//public class StaffHomeFragment extends Fragment {
//    private RecyclerView recyclerView;
//    private ImageButton btnAdd;
//
//    private Spinner spinnerCategory;
//
//    private ProductApi productApi;
//
//    private ProductAdapter productAdapter;
//
//    private ArrayList<Product> productList;
//
//    private CategoryApi categoryApi;
//
//    private ArrayList<Category> categoryList;
//    String selectedCategoryId = null;
//    private int currentPage = 0;
//    private final int pageSize = 5;
//    private boolean isLoading = false;
//    private boolean isLastPage = false;
//    private final int visibleThreshold = 4;
//
//    private int lastRequestedPage = -1;
//
//    Retrofit retrofit;
//
//    public StaffHomeFragment() {
//
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_staff_product, container, false);
//
//        recyclerView = view.findViewById(R.id.recyclerView);
//        btnAdd = view.findViewById(R.id.btnAdd);
//        spinnerCategory = view.findViewById(R.id.spinnerCategory);
//
//        btnAdd.setOnClickListener(v -> {
//            openAddProductScreen();
//        });
//
//        //init
//        productList = new ArrayList<>();
//        categoryList = new ArrayList<>();
//        productAdapter = new ProductAdapter(requireContext()
//                , productList, false, new ProductAdapter.OnItemActionListener() {
//            @Override
//            public void onEdit(Product product) {
//                Intent intent = new Intent(requireContext(), AddEditProductActivity.class);
//                intent.putExtra("product", product);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onDelete(Product product) {
//                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
//                        .setTitle("Xác nhận xóa")
//                        .setMessage("Bạn có chắc chắn muốn xóa sản phẩm \"" + product.getName() + "\"?")
//                        .setPositiveButton("Xóa", (dialog, which) -> {
//                            deleteProduct(product.getProductId());
//                        })
//                        .setNegativeButton("Hủy", null)
//                        .show();
//            }
//        });
//        //setup RecyclerView
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        recyclerView.setAdapter(productAdapter);
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@androidx.annotation.NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//                if (layoutManager != null) {
//                    int visibleItemCount = layoutManager.getChildCount();
//                    int totalItemCount = layoutManager.getItemCount();
//                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//
//                    if (!isLoading && !isLastPage) {
//                        if ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
//                            if (currentPage > lastRequestedPage) {
//                                lastRequestedPage = currentPage;
//                                isLoading = true;
//                                fetchProductsFromAPI(currentPage);
//                            }
//                        }
//                    }
//                }
//            }
//        });
//
//        retrofit = RetrofitClient.getInstance();
//        productApi = retrofit.create(ProductApi.class);
//
//        fetchProductsFromAPI(currentPage);
//        loadCategories();
//
//        return view;
//    }
//
//    private void openAddProductScreen() {
//        Intent intent = new Intent(requireContext(), AddEditProductActivity.class);
//        startActivity(intent);
//    };
//
//    private void fetchProductsFromAPI(int page) {
//        Call<ProductResponse> call = productApi.getAllProducts( page, pageSize);
//        call.enqueue(new Callback<ProductResponse>() {
//            @Override
//            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
//                List<Product> newProducts = response.body().getData();
//
//                if (newProducts != null && !newProducts.isEmpty()) {
//                    productList.addAll(newProducts);
//                    productAdapter.notifyDataSetChanged();
//                    currentPage++;
//                }
//
//                Log.d("res", "onResponse: " + response.body().getData());
//
//                if (newProducts == null || newProducts.size() < pageSize) {
//                    isLastPage = true;
//                } else {
//                    Log.e("API_ERROR", "Lỗi phản hồi từ server");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProductResponse> call, Throwable t) {
//                Log.e("API_ERROR", "Không gọi được API", t);
//            }
//        });
//    }
//
//    private void deleteProduct(String productId) {
//        productApi.deleteProduct(productId).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(requireContext(), "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
//                    fetchProductsFromAPI(currentPage); // Reload danh sách
//                } else {
//                    Toast.makeText(requireContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(requireContext(), "Lỗi kết nối khi xóa", Toast.LENGTH_SHORT).show();
//                Log.e("DELETE_ERROR", "Lỗi:", t);
//            }
//        });
//    }
//
//    private void loadCategories() {
//        CategoryApi categoryApi = retrofit.create(CategoryApi.class);
//        categoryApi.getAllCategories().enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                if (!isAdded()) return;
//
//                if (response.isSuccessful() && response.body() != null) {
//                    categoryList.clear();
//                    categoryList.add(new Category("all", "Tất cả"));
//                    categoryList.addAll(response.body());
//
//                    ArrayAdapter<Category> adapter = new ArrayAdapter<>(
//                            requireContext(),
//                            android.R.layout.simple_spinner_item,
//                            categoryList
//                    );
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinnerCategory.setAdapter(adapter);
//
//                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            Category selected = categoryList.get(position);
//                            selectedCategoryId = selected.getCategoryId();
//
//                            if (selectedCategoryId.equals("all")) {
//                                fetchProductsFromAPI(currentPage);
//                            } else {
//                                fetchProductsByCategory(selectedCategoryId, currentPage);
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//                        }
//                    });
//                } else {
//                    Toast.makeText(requireContext(), "Không thể tải danh mục", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                if (!isAdded()) return;
//                Toast.makeText(requireContext(), "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
//                Log.e("CATEGORY_API", "Error", t);
//            }
//        });
//    }
//
//
//
//    private void fetchProductsByCategory(String categoryId, int page) {
//        Call<ProductResponse> call = productApi.getProductsByCategory( categoryId,page, pageSize);
//        call.enqueue(new Callback<ProductResponse>() {
//            @Override
//            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
//                List<Product> newProducts = response.body().getData();
//
//                if (newProducts != null && !newProducts.isEmpty()) {
//                    productList.addAll(newProducts);
//                    productAdapter.notifyDataSetChanged();
//                    currentPage++;
//                }
//
//                Log.d("res", "onResponse: " + response.body().getData());
//
//                if (newProducts == null || newProducts.size() < pageSize) {
//                    isLastPage = true;
//                } else {
//                    Log.e("API_ERROR", "Lỗi phản hồi từ server");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProductResponse> call, Throwable t) {
//                Log.e("API_ERROR", "Không gọi được API", t);
//            }
//        });
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        currentPage = 0;
//        isLastPage = false;
//        isLoading = false;
//        productList.clear();
//        productAdapter.notifyDataSetChanged();
//
//        if (selectedCategoryId != null && selectedCategoryId.equals("all")) {
//            fetchProductsFromAPI(currentPage);
//            fetchProductsFromAPI(currentPage);
//        } else {
//            fetchProductsByCategory(selectedCategoryId, currentPage);
//        }
//    }
//}


package com.example.electronic_equipment.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.activities.AddEditProductActivity;
import com.example.electronic_equipment.adapters.ProductAdapter;
import com.example.electronic_equipment.models.Category;
import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.models.ProductResponse;
import com.example.electronic_equipment.networks.CategoryApi;
import com.example.electronic_equipment.networks.ProductApi;
import com.example.electronic_equipment.networks.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StaffHomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageButton btnAdd;
    private Spinner spinnerCategory;
    private ProductApi productApi;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;
    private CategoryApi categoryApi;
    private ArrayList<Category> categoryList;
    private String selectedCategoryId = null;
    private int currentPage = 0;
    private final int pageSize = 5;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private final int visibleThreshold = 4;
    private Retrofit retrofit;

    public StaffHomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_product, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        btnAdd = view.findViewById(R.id.btnAdd);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        btnAdd.setOnClickListener(v -> openAddProductScreen());

        productList = new ArrayList<>();
        categoryList = new ArrayList<>();
        productAdapter = new ProductAdapter(requireContext(), productList, false, new ProductAdapter.OnItemActionListener() {
            @Override
            public void onEdit(Product product) {
                Intent intent = new Intent(requireContext(), AddEditProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }

            @Override
            public void onDelete(Product product) {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa sản phẩm \"" + product.getName() + "\"?")
                        .setPositiveButton("Xóa", (dialog, which) -> deleteProduct(product.getProductId()))
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(productAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@androidx.annotation.NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isLastPage) {
                        if ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
                            if (selectedCategoryId == null || selectedCategoryId.equals("all")) {
                                fetchProductsFromAPI(currentPage);
                            } else {
                                fetchProductsByCategory(selectedCategoryId, currentPage);
                            }
                        }
                    }
                }
            }
        });

        retrofit = RetrofitClient.getInstance();
        productApi = retrofit.create(ProductApi.class);
        categoryApi = retrofit.create(CategoryApi.class);

        loadCategories();

        return view;
    }

    private void openAddProductScreen() {
        Intent intent = new Intent(requireContext(), AddEditProductActivity.class);
        startActivity(intent);
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
                        productAdapter.notifyDataSetChanged();
                        currentPage++;

                        if (newProducts.size() < pageSize) {
                            isLastPage = true;
                        }
                    } else {
                        isLastPage = true;
                    }
                } else {
                    Log.e("API_ERROR", "Lỗi phản hồi từ server");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                isLoading = false;
                Log.e("API_ERROR", "Không gọi được API", t);
            }
        });
    }

    private void fetchProductsByCategory(String categoryId, int page) {
        isLoading = true;

        Call<ProductResponse> call = productApi.getProductsByCategory(categoryId, page, pageSize);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                isLoading = false;

                if (response.isSuccessful() && response.body() != null) {
                    List<Product> newProducts = response.body().getData();

                    if (newProducts != null && !newProducts.isEmpty()) {
                        productList.addAll(newProducts);
                        productAdapter.notifyDataSetChanged();
                        currentPage++;

                        if (newProducts.size() < pageSize) {
                            isLastPage = true;
                        }
                    } else {
                        isLastPage = true;
                    }
                } else {
                    Log.e("API_ERROR", "Lỗi phản hồi từ server");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                isLoading = false;
                Log.e("API_ERROR", "Không gọi được API", t);
            }
        });
    }

    private void deleteProduct(String productId) {
        productApi.deleteProduct(productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    refreshProductList();
                } else {
                    Toast.makeText(requireContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối khi xóa", Toast.LENGTH_SHORT).show();
                Log.e("DELETE_ERROR", "Lỗi:", t);
            }
        });
    }

    private void loadCategories() {
        categoryApi.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.add(new Category("all", "Tất cả"));
                    categoryList.addAll(response.body());

                    ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            categoryList
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);

                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Category selected = categoryList.get(position);
                            selectedCategoryId = selected.getCategoryId();

                            currentPage = 0;
                            isLastPage = false;
                            isLoading = false;
                            productList.clear();
                            productAdapter.notifyDataSetChanged();

                            if (selectedCategoryId.equals("all")) {
                                fetchProductsFromAPI(currentPage);
                            } else {
                                fetchProductsByCategory(selectedCategoryId, currentPage);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Không thể tải danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
                Log.e("CATEGORY_API", "Error", t);
            }
        });
    }


    private void refreshProductList() {
        currentPage = 0;
        isLastPage = false;
        isLoading = false;
        productList.clear();
        productAdapter.notifyDataSetChanged();

        if (selectedCategoryId == null || selectedCategoryId.equals("all")) {
            fetchProductsFromAPI(currentPage);
        } else {
            fetchProductsByCategory(selectedCategoryId, currentPage);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshProductList();
    }
}
