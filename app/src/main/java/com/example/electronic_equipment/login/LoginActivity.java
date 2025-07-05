/// /package com.example.electronic_equipment.login;
/// /
/// /import android.content.Intent;
/// /import android.os.Bundle;
/// /import android.util.Log;
/// /import android.widget.Button;
/// /import android.widget.EditText;
/// /import android.widget.TextView;
/// /import android.widget.Toast;
/// /
/// /import androidx.appcompat.app.AppCompatActivity;
/// /
/// /import com.example.electronic_equipment.activities.MainActivity;
/// /import com.example.electronic_equipment.R;
/// /import com.example.electronic_equipment.register.RegisterActivity;
/// /
/// /public class LoginActivity extends AppCompatActivity {
/// /
/// /    EditText emailEditText, passwordEditText;
/// /    Button loginButton;
/// /    TextView registerLink;
/// /
/// /    SessionManager sessionManager;
/// /
/// /    @Override
/// /    protected void onCreate(Bundle savedInstanceState) {
/// /        super.onCreate(savedInstanceState);
/// /        Log.d("DEBUG", "LoginActivity opened");
/// /
/// /        sessionManager = new SessionManager(this);
/// /        if (sessionManager.isLoggedIn()) {
/// /            Log.d("DEBUG", "LoginActivity islogin");
/// /            startActivity(new Intent(this, MainActivity.class));
/// /            finish();
/// /            return;
/// /        }
/// /
/// /        setContentView(R.layout.activity_login);
/// /
/// /        emailEditText = findViewById(R.id.emailEditText);
/// /        passwordEditText = findViewById(R.id.passwordEditText);
/// /        loginButton = findViewById(R.id.loginButton);
/// /        registerLink = findViewById(R.id.registerLink);
/// /
/// /
/// /
/// /        loginButton.setOnClickListener(v -> {
/// /            String email = emailEditText.getText().toString();
/// /            String password = passwordEditText.getText().toString();
/// /
/// /            // TODO: validate and check credentials
/// /            if (!email.isEmpty() && !password.isEmpty()) {
/// /                // Pretend login is always successful
/// /                sessionManager.setLogin();
/// /                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
/// /                startActivity(new Intent(this, MainActivity.class));
/// /                finish(); // finish LoginActivity
/// /            } else {
/// /                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
/// /            }
/// /        });
/// /
/// /        registerLink.setOnClickListener(v -> {
/// /            startActivity(new Intent(this, RegisterActivity.class));
/// /        });
/// /    }
/// /}
//
//
//package com.example.electronic_equipment.login;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.electronic_equipment.R;
//import com.example.electronic_equipment.activities.MainActivity;
//import com.example.electronic_equipment.register.RegisterActivity;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText emailEditText, passwordEditText;
//    private Button loginButton;
//    private TextView registerLink;
//
//    private SessionManager sessionManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        sessionManager = new SessionManager(getApplicationContext());
//
//        if (sessionManager.isLoggedIn()) {
//            Log.d("LoginActivity", "Already logged in, redirecting...");
//            goToMain();
//            return;
//        }
//
//        setContentView(R.layout.activity_login);
//
//        emailEditText = findViewById(R.id.emailEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        loginButton = findViewById(R.id.loginButton);
//        registerLink = findViewById(R.id.registerLink);
//
//        loginButton.setOnClickListener(v -> {
//            String email = emailEditText.getText().toString().trim();
//            String password = passwordEditText.getText().toString().trim();
//
//            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
//            } else {
//                // Đây là login giả, bạn nên thay bằng check API sau này
//                sessionManager.setLogin();
//                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
//                goToMain();
//            }
//        });
//
//        registerLink.setOnClickListener(v ->
//                startActivity(new Intent(this, RegisterActivity.class))
//        );
//    }
//
//    private void goToMain() {
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
//}


package com.example.electronic_equipment.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.JWT;
import com.example.electronic_equipment.R;
import com.example.electronic_equipment.activities.MainActivity;
import com.example.electronic_equipment.activities.MainStaffActivity;
import com.example.electronic_equipment.models.LoginRequest;
import com.example.electronic_equipment.models.LoginResponse;
import com.example.electronic_equipment.networks.AuthAPI;
import com.example.electronic_equipment.networks.RetrofitClient;
import com.example.electronic_equipment.register.RegisterActivity;
import com.example.electronic_equipment.utils.SessionManager;

import okhttp3.ResponseBody;
import retrofit2.*;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    TextView registerLink;
    Button loginButton;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish(); // Không cho quay lại login
            return;
        }
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> performLogin());
    }

    private void performLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        LoginRequest request = new LoginRequest(email, password);
        AuthAPI api = RetrofitClient.getInstance().create(AuthAPI.class);

        api.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    String token = loginResponse.getToken();
                    Log.d("Token", "onResponse: token " + token);
                    if (token != null) {
                        JWT jwt = new JWT(token);

                        String role = jwt.getClaim("http://schemas.microsoft.com/ws/2008/06/identity/claims/role").asString();

                        Log.d("JWT", "Role from token: " + role);


                        sessionManager.saveSession(token, role);

                        if ("Staff".equalsIgnoreCase(role)) {
                            startActivity(new Intent(LoginActivity.this, MainStaffActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }

                        finish();
                    } else {
                        Log.e("Login", "❌ Token or Role is null in body");
                        Toast.makeText(LoginActivity.this, "Invalid login data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Login", "❌ Login failed, code: " + response.code());
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Login", "❌ Network error: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
