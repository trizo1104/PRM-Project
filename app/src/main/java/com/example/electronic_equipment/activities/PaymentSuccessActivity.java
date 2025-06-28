package com.example.electronic_equipment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
            startActivity(new Intent(this, HomeFragment.class));
        });

        // Optionally: update detail rows dynamically here
    }
}

