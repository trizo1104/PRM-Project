package com.example.electronic_equipment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_equipment.Fragment.ProfileFragment;
import com.example.electronic_equipment.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.electronic_equipment.R.layout.activity_map);

        // Optional: force initialization
        com.google.android.gms.maps.MapsInitializer.initialize(getApplicationContext());

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileFragment.class);
            startActivity(intent);
        });

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e("MapActivity", "Map fragment is null!");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        LatLng mapChanh = new LatLng(10.7769, 106.7009);
        gMap.addMarker(new MarkerOptions().position(mapChanh).title("Marker in nhà Chánh"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapChanh, 15f));
    }
}
