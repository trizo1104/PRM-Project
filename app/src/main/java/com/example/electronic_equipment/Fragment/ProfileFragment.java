package com.example.electronic_equipment.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.login.LoginActivity;
import com.example.electronic_equipment.activities.OrdersActivity;
import com.example.electronic_equipment.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private Button logoutButton;
    private SessionManager sessionManager;

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

        sessionManager = new SessionManager(requireContext());

        logoutButton = view.findViewById(R.id.logoutButton);
        LinearLayout optionOrders = view.findViewById(R.id.optionOrders);

        if (logoutButton == null) {
            Log.e("ProfileFragment", "❌ logoutButton not found in layout. Check fragment_profile.xml");
            return;
        }

        logoutButton.setOnClickListener(v -> showLogoutDialog());

        if (optionOrders != null) {
            optionOrders.setOnClickListener(v -> {
                startActivity(new Intent(requireContext(), OrdersActivity.class));
            });
        }
    }

    private void showLogoutDialog() {
        Log.d("logout", "showLogoutDialog: checked logout");
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    sessionManager.logout();  // Xoá token và role

                    // Quay về LoginActivity và clear hết stack
                    Intent intent = new Intent(requireActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();  // Đóng toàn bộ app về lại login
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

