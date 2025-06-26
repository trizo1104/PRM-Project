package com.example.electronic_equipment.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.electronic_equipment.R;

// HomeFragment.java
public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate your existing home layout
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
