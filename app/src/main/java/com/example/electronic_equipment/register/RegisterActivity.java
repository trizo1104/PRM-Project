package com.example.electronic_equipment.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.login.LoginActivity;
import com.example.electronic_equipment.models.RegisterRequest;
import com.example.electronic_equipment.models.RegisterResponse;
import com.example.electronic_equipment.networks.AuthAPI;
import com.example.electronic_equipment.networks.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//package com.example.electronic_equipment.register;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.electronic_equipment.R;
//import com.example.electronic_equipment.login.LoginActivity;
//
//public class RegisterActivity extends AppCompatActivity {
//
//    EditText nameEditText, emailEditText, passwordEditText;
//    Button registerButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        nameEditText = findViewById(R.id.nameEditText);
//        emailEditText = findViewById(R.id.emailEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        registerButton = findViewById(R.id.registerButton);
//        TextView btnBackToLogin = findViewById(R.id.btnBackToLogin);
//
//        btnBackToLogin.setOnClickListener(v -> {
//            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish(); // optional, prevents going back to Register
//        });
//
//        registerButton.setOnClickListener(v -> {
//            // TODO: validate and send register request
//            String name = nameEditText.getText().toString();
//            String email = emailEditText.getText().toString();
//            String password = passwordEditText.getText().toString();
//            Toast.makeText(this, "Register clicked", Toast.LENGTH_SHORT).show();
//        });
//    }
//}
//
public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText;
    TextView btnBackToLogin;
    private Button registerButton;
    private AuthAPI authAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnBackToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        authAPI = RetrofitClient.getInstance().create(AuthAPI.class);

        registerButton.setOnClickListener(v -> performRegister());
    }

    private void performRegister() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        RegisterRequest request = new RegisterRequest( email, password);

        authAPI.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Log.e("Register", "❌ Register failed, code: " + response.code());
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
