package com.example.electronic_equipment.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electronic_equipment.R;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
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

}
