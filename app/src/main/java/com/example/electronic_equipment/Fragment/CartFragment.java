package com.example.electronic_equipment.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.electronic_equipment.R;
import com.example.electronic_equipment.adapters.CartAdapter;
import com.example.electronic_equipment.models.Cart;
import com.example.electronic_equipment.models.CartResponse;
import com.example.electronic_equipment.networks.CartApi;
import com.example.electronic_equipment.networks.RetrofitClient;
import com.example.electronic_equipment.payment.OrderPayment;
import com.example.electronic_equipment.utils.SessionManager;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartFragment extends Fragment {

    private RecyclerView recyclerCartItems;
    private TextView txtTotalPrice;
    private Button btnProcessPayment;
    private ImageView btnBack;
    private CartApi cartApi;
    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartList;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCartsFromAPI(new SessionManager(getContext()).getUserId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerCartItems = view.findViewById(R.id.recyclerCartItems);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        btnProcessPayment = view.findViewById(R.id.btnProcessPayment);
        btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnProcessPayment.setOnClickListener(v -> {
            double total = 0;
            for (Cart item : cartList) {
                total += item.getTotalPrice();
            }
            Intent intent = new Intent(requireContext(), OrderPayment.class);
            intent.putExtra("total", total);
            startActivity(intent);
        });

        setupCartList();
        setupRecyclerView();

        Retrofit retrofit = RetrofitClient.getInstance();
        cartApi = retrofit.create(CartApi.class);

        return view;
    }

    private void setupCartList() {
        cartList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        CartAdapter.OnCartChangeListener listener = this::updateTotalPrice;
        cartAdapter = new CartAdapter(requireContext(), cartList, listener);
        recyclerCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCartItems.setAdapter(cartAdapter);
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Cart item : cartList) {
            total += item.getTotalPrice();
        }
        txtTotalPrice.setText("Total Price\n$" + String.format("%.2f", total));
    }

    private void fetchCartsFromAPI(String userId) {
        Call<CartResponse> call = cartApi.getAllCarts(userId, 0, 10);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                Log.d("API", "Fetched " + response + " products");
                if (response.isSuccessful() && response.body() != null) {
                    cartList.clear();
                    cartList.addAll(response.body().getData());
                    cartAdapter.notifyDataSetChanged();
                    updateTotalPrice();
                    Log.d("API", "Fetched " + cartList.size() + " products");
                } else {
                    Log.e("API_ERROR", "Lỗi phản hồi từ server: " + response.message());
                    Toast.makeText(getContext(), "Không thể tải giỏ hàng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.e("API_ERROR", "Không gọi được API", t);
                Toast.makeText(getContext(), "Không thể tải giỏ hàng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
