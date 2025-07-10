package com.example.electronic_equipment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.example.electronic_equipment.Fragment.CartFragment;
import com.example.electronic_equipment.Fragment.ExploreFragment;
import com.example.electronic_equipment.Fragment.HomeFragment;
import com.example.electronic_equipment.Fragment.ProfileFragment;
import com.example.electronic_equipment.Fragment.StaffHomeFragment;
import com.example.electronic_equipment.Fragment.StaffManageFragment;
import com.example.electronic_equipment.Fragment.StaffProfileFragment;
import com.example.electronic_equipment.adapters.ProductAdapter;
import com.example.electronic_equipment.R;
import com.example.electronic_equipment.models.Category;
import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.models.ProductResponse;
import com.example.electronic_equipment.networks.CategoryApi;
import com.example.electronic_equipment.networks.ProductApi;
import com.example.electronic_equipment.networks.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainStaffActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_main_activity);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationStaffView);
        bottomNavigationView.setSelectedItemId(R.id.nav_staff_home);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentStaffContainer, new StaffHomeFragment())
                .commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_staff_home) {
                selectedFragment = new StaffHomeFragment();
            } else if (itemId == R.id.nav_staff_order) {
                selectedFragment = new StaffManageFragment();

            } else if (itemId == R.id.nav_staff_pofile) {
                selectedFragment = new StaffProfileFragment();
            } else {
                return false;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentStaffContainer, selectedFragment)
                    .commit();

            return true;
        });
    }
}
