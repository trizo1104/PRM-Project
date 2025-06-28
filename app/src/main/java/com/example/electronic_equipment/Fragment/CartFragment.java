package com.example.electronic_equipment.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.activities.PaymentSuccessActivity;
import com.example.electronic_equipment.adapters.CartAdapter;
import com.example.electronic_equipment.adapters.CartManager;
import com.example.electronic_equipment.models.Cart;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView recyclerCartItems;
    private TextView txtTotalPrice;
    private Button btnProcessPayment;
    private ImageView btnBack;

    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartList;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        cartList.clear();
        cartList.addAll(CartManager.getInstance().getCartItems());
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerCartItems = view.findViewById(R.id.recyclerCartItems);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        btnProcessPayment = view.findViewById(R.id.btnProcessPayment);
        btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        btnProcessPayment.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PaymentSuccessActivity.class);
            startActivity(intent);
        });

        setupCartList();
        setupRecyclerView();
//        updateTotalPrice();
        onResume();

        return view;
    }

    private void setupCartList() {
        cartList = new ArrayList<>(CartManager.getInstance().getCartItems());
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(cartList, this::updateTotalPrice);
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

}
