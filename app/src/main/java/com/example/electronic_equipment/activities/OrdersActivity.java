package com.example.electronic_equipment.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.adapters.OrderAdapter;
import com.example.electronic_equipment.models.Order;
import com.example.electronic_equipment.networks.OrderAPI;
import com.example.electronic_equipment.networks.RetrofitClient;
import com.example.electronic_equipment.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
    private ProgressBar progressBar;
    private LinearLayout layoutEmptyState;
    private ImageView btnBack;

    private OrderAPI orderApi;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadUserOrders();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewOrders);
        progressBar = findViewById(R.id.progressBar);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        btnBack = findViewById(R.id.btnBack);

        // Initialize SessionManager and API
        sessionManager = new SessionManager(this);
        Retrofit retrofit = RetrofitClient.getInstance();
        orderApi = retrofit.create(OrderAPI.class);
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(this, orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadUserOrders() {
        String rawToken = sessionManager.getToken();
        Log.d("OrdersActivity", "Token = " + rawToken);

        if (rawToken == null || rawToken.isEmpty()) {
            Toast.makeText(this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
            showEmptyState();
            return;
        }

        String bearerToken = "Bearer " + rawToken;
        showLoading();

        orderApi.getAllOrdersOfUser(bearerToken).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                hideLoading();
                Log.d("OrdersActivity", "Status Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body();
                    Log.d("OrdersActivity", "Số lượng đơn hàng: " + orders.size());

                    // Filter only paid orders (Đã thanh toán)
                    List<Order> paidOrders = new ArrayList<>();
                    for (Order order : orders) {
                        if ("Đã thanh toán".equals(order.getStatus())) {
                            paidOrders.add(order);
                        }
                    }

                    if (paidOrders.isEmpty()) {
                        showEmptyState();
                    } else {
                        orderList.clear();
                        orderList.addAll(paidOrders);
                        orderAdapter.notifyDataSetChanged();
                        showOrdersList();
                    }
                } else {
                    Toast.makeText(OrdersActivity.this, "Không thể tải đơn hàng", Toast.LENGTH_SHORT).show();
                    showEmptyState();
                    try {
                        Log.e("OrdersActivity", "Lỗi: " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                hideLoading();
                Toast.makeText(OrdersActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.e("OrdersActivity", "Lỗi kết nối:", t);
                showEmptyState();
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showOrdersList() {
        recyclerView.setVisibility(View.VISIBLE);
        layoutEmptyState.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
    }
}