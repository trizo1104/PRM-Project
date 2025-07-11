package com.example.electronic_equipment.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.electronic_equipment.Fragment.HomeFragment;
import com.example.electronic_equipment.R;

public class PaymentSuccessActivity extends AppCompatActivity {

    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(v -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.btnDone, new HomeFragment()); // fragment_container là ID của FrameLayout
            transaction.addToBackStack(null); // nếu muốn quay lại
            transaction.commit();
        });

        // Optionally: update detail rows dynamically here
    }
}

