package com.example.electronic_equipment.acvitities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.models.Category;
import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.networks.CategoryApi;
import com.example.electronic_equipment.networks.ProductApi;
import com.example.electronic_equipment.networks.RetrofitClient;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEditProductActivity extends AppCompatActivity {

    EditText edtName, edtPrice, edtImageUrl, edtQuantity, edtDescription;
    Spinner spinnerCategory;
    List<Category> categoryList = new ArrayList<>();
    String selectedCategoryId = null;
    Button btnSave;
    boolean isEditMode = false;
    Product editProduct = null;
    String productId = null;
    private Retrofit retrofit;
    private ProductApi productApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtImageUrl = findViewById(R.id.edtImageUrl);
        btnSave = findViewById(R.id.btnSubmit);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtDescription = findViewById(R.id.edtDescription);

        retrofit = RetrofitClient.getInstance();
        productApi = retrofit.create(ProductApi.class);

        editProduct = (Product) getIntent().getSerializableExtra("product");
        isEditMode = editProduct != null;

        loadCategories();

//        if (product != null) {
//            isEditMode = true;
//            productId = product.getProductId();
//
//            edtName.setText(product.getName());
//            edtPrice.setText(String.valueOf(product.getPrice()));
//            edtImageUrl.setText(product.getImageUrl());
//            edtQuantity.setText(String.valueOf(product.getQuantity()));
//            edtDescription.setText(product.getDescription());
//
//            selectedCategoryId = product.getCategoryId();
//        }

        if (isEditMode) {
            edtName.setText(editProduct.getName());
            edtPrice.setText(String.valueOf(editProduct.getPrice()));
            edtImageUrl.setText(editProduct.getImageUrl());
            edtQuantity.setText(String.valueOf(editProduct.getQuantity()));
            edtDescription.setText(editProduct.getDescription());
            selectedCategoryId = editProduct.getCategoryId();
        }

//        // Check nếu đang sửa sản phẩm
//        if (getIntent().hasExtra("id")) {
//            isEditMode = true;
//            productId = getIntent().getStringExtra("id");
//            edtName.setText(getIntent().getStringExtra("name"));
//            edtPrice.setText(getIntent().getStringExtra("price"));
//            edtImageUrl.setText(getIntent().getStringExtra("image"));
//        }

        btnSave.setOnClickListener(v -> {
            String productId;
            String name = edtName.getText().toString().trim();
            String price = edtPrice.getText().toString().trim();
            String image = edtImageUrl.getText().toString().trim();
            String quantity = edtQuantity.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createAt = sdf.format(now);
            Boolean isActive = false;


            try {
                double parsedPrice = Double.parseDouble(price);
                int parsedQuantity = Integer.parseInt(quantity);

                if (!isEditMode) {
                    productId = UUID.randomUUID().toString();
                } else {
                    productId = editProduct.getProductId();
                }

                Product product = new Product(
                        productId,
                        name,
                        description,
                        parsedPrice,
                        parsedQuantity,
                        image,
                        createAt,
                        isActive,
                        selectedCategoryId
                );

                Log.d("DEBUG", "Calling PUT with ID: " + productId);
                Log.d("DEBUG", "Full URL: https://67078cefa0e04071d22acf43.mockapi.io/products/" + productId);

                if (isEditMode) {
                    Toast.makeText(this, "Đang cập nhật sản phẩm...", Toast.LENGTH_SHORT).show();
                    productApi.updateProduct(productId, product).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AddEditProductActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    Log.e("API_ERROR", "Lỗi server code=" + response.code() + ", body=" + errorBody);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(AddEditProductActivity.this, "Cập nhật thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("API_ERROR", "API cập nhật lỗi", t);
                            Toast.makeText(AddEditProductActivity.this, "Gọi API cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Đang thêm sản phẩm...", Toast.LENGTH_SHORT).show();
                    productApi.addProduct(product).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AddEditProductActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    Log.e("API_ERROR", "Lỗi server code=" + response.code() + ", body=" + errorBody);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(AddEditProductActivity.this, "Thêm thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("API_ERROR", "API thêm lỗi", t);
                            Toast.makeText(AddEditProductActivity.this, "Gọi API thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá hoặc số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            }

        });

    }


    private void loadCategories() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://67078cefa0e04071d22acf43.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CategoryApi categoryApi = retrofit.create(CategoryApi.class);
        categoryApi.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AddEditProductActivity.this,
                            android.R.layout.simple_spinner_item,
                            getCategoryNames(categoryList)
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);

                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCategoryId = categoryList.get(position).getCategoryId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(AddEditProductActivity.this, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<String> getCategoryNames(List<Category> list) {
        List<String> names = new ArrayList<>();
        for (Category c : list) {
            names.add(c.getName());
        }
        return names;
    }


}

