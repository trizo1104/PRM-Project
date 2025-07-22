package com.example.electronic_equipment.payment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.electronic_equipment.R;
import com.example.electronic_equipment.Api.CreateOrder;
import com.example.electronic_equipment.models.CreateOrderResponse;
import com.example.electronic_equipment.networks.OrderAPI;
import com.example.electronic_equipment.networks.RetrofitClient;
import com.example.electronic_equipment.utils.SessionManager;
import org.json.JSONObject;
import java.text.NumberFormat;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class OrderPayment extends AppCompatActivity {

    TextView tvTongTien;
    Button btnThanhToan;
    private OrderAPI orderApis;
    private String UserId;

    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_payment);

        tvTongTien = findViewById(R.id.tvTongTien);
        btnThanhToan = findViewById(R.id.btnThanhToan);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        Intent intent = getIntent();
//        tvSoluong.setText(intent.getStringExtra("soluong"));
        Double total = intent.getDoubleExtra("total", 0);

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String totalString = String.format("%.0f", total);
        String totalFormatted = formatter.format(total);
        tvTongTien.setText(totalFormatted);

        sessionManager = new SessionManager((OrderPayment.this));
        String rawToken = sessionManager.getToken();
        UserId = sessionManager.getUserId();
        Log.d("Session", "Token = " + rawToken);

        if (rawToken == null || rawToken.isEmpty()) {
            Toast.makeText(OrderPayment.this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
        }
        String bearerToken = "Bearer" + rawToken;

        Retrofit retrofit = RetrofitClient.getInstance();
        orderApis = retrofit.create(OrderAPI.class);

        btnThanhToan.setOnClickListener(v -> {
            CreateOrder orderApi = new CreateOrder();
            try {
                JSONObject data = orderApi.createOrder(totalString);
                String code = data.getString("return_code");

                if (code.equals("1")) {
                    String token = data.getString("zp_trans_token");
                    Log.d("ZaloPayDebug", "return_code: " + code + " | token: " + token);

                    ZaloPaySDK.getInstance().payOrder(OrderPayment.this, token, "demozpdk://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(String s, String s1, String s2) {
                            Log.d("ZaloPayDebug", "Payment succeeded: " + s);
                            //
                            Intent intent1 = new Intent(OrderPayment.this, PaymentNotification.class);

                            //Tạo OrderItems
                            clearCartThenCreateOrder(UserId);

                            intent1.putExtra("result", "Thanh toán thành công");
                            intent1.putExtra("total", "Bạn đã thanh toán " + totalFormatted);
                            startActivity(intent1);
                        }

                        @Override
                        public void onPaymentCanceled(String s, String s1) {
                            Log.d("ZaloPayDebug", "Payment canceled");
                            Intent intent2 = new Intent(OrderPayment.this, PaymentNotification.class);
                            intent2.putExtra("result",  "Thanh toán đã được hủy");
                            startActivity(intent2);
                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                            Log.e("ZaloPayDebug", "Payment error: " + zaloPayError.toString());
                            Intent intent3 = new Intent(OrderPayment.this, PaymentNotification.class);
                            intent3.putExtra("result", "Lỗi thanh toán");
                            startActivity(intent3);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    public void clearCartThenCreateOrder(String userId) {
        // ...
        orderApis.clearCartThenCreateOrder(userId).enqueue(new Callback<CreateOrderResponse>() {
            @Override
            public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                String message;
                if (response.isSuccessful() && response.body() != null) {
                    CreateOrderResponse apiResponse = response.body();

                    message = "Đã xóa giỏ hàng và tạo đơn hàng thành công. " +
                            "Trạng thái: " + apiResponse.getStatus() +
                            ", Thông báo: " + apiResponse.getMessage();

                    Log.d("OrderPayment", message);
                    Toast.makeText(OrderPayment.this, message, Toast.LENGTH_LONG).show();
                } else {
                    String errorMessage;
                    try {
                        errorMessage = "Lỗi: " + response.code() + " - " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error");
                        Log.e("OrderPayment", errorMessage);
                    } catch (Exception e) {
                        errorMessage = "Lỗi khi đọc phản hồi lỗi: " + e.getMessage();
                        Log.e("OrderPayment", errorMessage, e);
                    }
                    Toast.makeText(OrderPayment.this, "Không thể thực hiện tác vụ. " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CreateOrderResponse> call, Throwable t) {
                String message = "Lỗi kết nối. Vui lòng thử lại sau.";
                Log.e("OrderPayment", "Lỗi kết nối:", t);
                Toast.makeText(OrderPayment.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}