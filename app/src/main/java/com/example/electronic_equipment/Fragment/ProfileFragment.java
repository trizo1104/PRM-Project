package com.example.electronic_equipment.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.login.LoginActivity;
import com.example.electronic_equipment.login.SessionManager;

public class ProfileFragment extends Fragment {

    Button logoutButton;

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

        setProfileItem(view, R.id.item_upi, "UPI ID", "janecooper@upi");
        setProfileItem(view, R.id.item_qr, "QR Code", "Scan to pay");
        setProfileItem(view, R.id.item_payment, "Payment Method", "Visa **** 1234");
        setProfileItem(view, R.id.item_rewards, "Rewards", "3 available");
        setProfileItem(view, R.id.item_settings, "Settings", "Notifications, Privacy");
        setProfileItem(view, R.id.item_help, "Help & Feedback", "We're here to help");

        logoutButton = view.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show();
            logoutButton.postDelayed(this::logout, 300);
        });
    }

    private void setProfileItem(View root, int includeId, String title, String subtitle) {
        View itemView = root.findViewById(includeId);
        if (itemView != null) {
            TextView titleView = itemView.findViewById(R.id.title);
            TextView subtitleView = itemView.findViewById(R.id.subtitle);

            if (titleView != null) titleView.setText(title);
            if (subtitleView != null) subtitleView.setText(subtitle);
        }
    }

    private void logout() {
        SessionManager sessionManager = new SessionManager(requireContext());
        sessionManager.logout();

        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        requireActivity().finishAffinity(); // Kill all activities cleanly
    }

}
