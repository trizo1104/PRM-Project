package com.example.electronic_equipment.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class StaffManageFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();

    private OrderAPI orderApi;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_order, container, false);

        recyclerView = view.findViewById(R.id.rvOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderAdapter = new OrderAdapter(requireContext(),orderList);
        recyclerView.setAdapter(orderAdapter);

        // Khởi tạo SessionManager và lấy token
        sessionManager = new SessionManager(requireContext());
        String rawToken = sessionManager.getToken();
        Log.d("Session", "Token = " + rawToken);

        if (rawToken == null || rawToken.isEmpty()) {
            Toast.makeText(getContext(), "Token không hợp lệ", Toast.LENGTH_SHORT).show();
            return view;
        }

        String bearerToken = "Bearer " + rawToken;

        Retrofit retrofit = RetrofitClient.getInstance();
        orderApi = retrofit.create(OrderAPI.class);

        fetchOrders(bearerToken);

        return view;
    }

    private void fetchOrders(String token) {
        orderApi.getAllOrders(token).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Log.d("ORDER_API", "Status Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ORDER_API", "Số lượng đơn hàng: " + response.body().size());
                    orderList.clear();
                    orderList.addAll(response.body());
                    orderAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không thể tải đơn hàng", Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("ORDER_API", "Lỗi: " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.e("ORDER_API", "Lỗi kết nối:", t);
            }
        });
    }
}
