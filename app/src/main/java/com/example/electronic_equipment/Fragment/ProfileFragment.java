package com.example.electronic_equipment.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.activities.MapActivity;
import com.example.electronic_equipment.login.LoginActivity;
import com.example.electronic_equipment.activities.OrdersActivity;
import com.example.electronic_equipment.models.User;
import com.example.electronic_equipment.networks.ProfileApi;
import com.example.electronic_equipment.networks.RetrofitClient;
import com.example.electronic_equipment.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private Button logoutButton;

    private LinearLayout  btnShowMap;
    private SessionManager sessionManager;

    ImageView profileImage;
    TextView nameTextView, emailTextView;

    User user;

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
        btnShowMap = view.findViewById(R.id.optionGgmap);

        LinearLayout optionOrders = view.findViewById(R.id.optionOrders);

        if (logoutButton == null) {
            Log.e("ProfileFragment", "❌ logoutButton not found in layout. Check fragment_profile.xml");
            return;
        }

        logoutButton.setOnClickListener(v -> showLogoutDialog());

        btnShowMap.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MapActivity.class);
            startActivity(intent);
        });

        if (optionOrders != null) {
            optionOrders.setOnClickListener(v -> {
                startActivity(new Intent(requireContext(), OrdersActivity.class));
            });
        }

        profileImage = view.findViewById(R.id.profileImage);
        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);

        // Call API to get user profile
        loadUserProfile();

        ImageButton editProfileButton = view.findViewById(R.id.btnEditProfile);

        editProfileButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null);
            builder.setView(dialogView);
            builder.setTitle("Update Profile");

            EditText editFullname = dialogView.findViewById(R.id.editFullname);
            EditText editEmail = dialogView.findViewById(R.id.editEmail);
            EditText editPhone = dialogView.findViewById(R.id.editPhone);

            // Optional: Pre-fill current user data
            editFullname.setText(user.getFullName());
            editEmail.setText(user.getEmail());
            editPhone.setText(user.getPhoneNumber());

            builder.setPositiveButton("Save", (dialog, which) -> {
                String newName = editFullname.getText().toString().trim();
                String newEmail = editEmail.getText().toString().trim();
                String newPhone = editPhone.getText().toString().trim();

                // TODO: Call API to update user profile
                User updatedUser = new User(newName, newEmail, newPhone);
                sendUpdateProfileRequest(updatedUser);
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });

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

    private void loadUserProfile() {
        String rawtoken = sessionManager.getToken();
        if (rawtoken != null) {
            ProfileApi profileApi = RetrofitClient.getInstance().create(ProfileApi.class);
            String token = "Bearer " + rawtoken;
            Call<User> call = profileApi.getUserProfile(token, "*/*");

            Log.d("adsfadf", "Bearer " + token);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d("adsfadf", String.valueOf(response.body().getFullName()));
                    if (response.isSuccessful() && response.body() != null) {
                        user = response.body();

                        Log.d("adsfasdf", String.valueOf(user.getFullName()));

                        // Set name and email
                        nameTextView.setText(user.getFullName());
                        emailTextView.setText(user.getEmail());
                        profileImage.setImageResource(R.drawable.rounded_profile);

                        // Load image (if available)
//                        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
//                            Glide.with(requireContext())
//                                    .load(user.getImageUrl())
//                                    .placeholder(R.drawable.rounded_profile)  // fallback placeholder
//                                    .into(profileImage);
//                        } else {
//                             // default
//                        }

                    } else {
                        Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(requireContext(), "Token not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendUpdateProfileRequest(User updatedUser) {
        String rawtoken = sessionManager.getToken();
        if (rawtoken != null) {
            ProfileApi profileApi = RetrofitClient.getInstance().create(ProfileApi.class);
            String token = "Bearer " + rawtoken;
            Call<User> call = profileApi.updateUserProfile(token, updatedUser);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        nameTextView.setText(user.getFullName());
                        emailTextView.setText(user.getEmail());
                        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(requireContext(), "Token missing", Toast.LENGTH_SHORT).show();
        }
    }



}

