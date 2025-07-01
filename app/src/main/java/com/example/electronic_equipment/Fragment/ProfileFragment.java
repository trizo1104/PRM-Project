package com.example.electronic_equipment.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.activities.OrdersActivity;
import com.example.electronic_equipment.login.LoginActivity;
import com.example.electronic_equipment.login.SessionManager;

public class ProfileFragment extends Fragment {

    Button logoutButton;
    SessionManager sessionManager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        logoutButton = view.findViewById(R.id.logoutButton);
        sessionManager = new SessionManager(requireContext());

        logoutButton.setOnClickListener(v -> {
            Log.d("DEBUG", "Calling check");

            sessionManager.logout();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish(); // Đóng toàn bộ stack
        });

        LinearLayout optionOrders = view.findViewById(R.id.optionOrders);
        optionOrders.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Orders clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), OrdersActivity.class));
        });


    }


}
