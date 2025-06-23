package com.example.electronic_equipment.acvitities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.electronic_equipment.R;

public class AddEditProductActivity extends AppCompatActivity {

    EditText edtName, edtPrice, edtImageUrl;
    Button btnSave;
    boolean isEditMode = false;
    String productId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtImageUrl = findViewById(R.id.edtImageUrl);
        btnSave = findViewById(R.id.btnSubmit);

        // Check nếu đang sửa sản phẩm
        if (getIntent().hasExtra("id")) {
            isEditMode = true;
            productId = getIntent().getStringExtra("id");
            edtName.setText(getIntent().getStringExtra("name"));
            edtPrice.setText(getIntent().getStringExtra("price"));
            edtImageUrl.setText(getIntent().getStringExtra("image"));
        }

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString();
            String price = edtPrice.getText().toString();
            String image = edtImageUrl.getText().toString();

            // Gọi API thêm hoặc cập nhật sản phẩm tại đây
            if (isEditMode) {
                // Gọi API update
                Toast.makeText(this, "Đang cập nhật sản phẩm...", Toast.LENGTH_SHORT).show();
            } else {
                // Gọi API add
                Toast.makeText(this, "Đang thêm sản phẩm...", Toast.LENGTH_SHORT).show();
            }

            finish(); // Trở lại màn trước
        });
    }
}

