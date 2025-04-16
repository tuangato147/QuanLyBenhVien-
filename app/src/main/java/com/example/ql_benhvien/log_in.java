package com.example.ql_benhvien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class log_in extends AppCompatActivity {

    Spinner spinnerRole;
    EditText edtPhone, edtPassword;
    Button btnLogin;
    TextView txtSign; // Thêm TextView đăng ký
    MyDatabase db;

    String selectedRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        spinnerRole = findViewById(R.id.spinnerRole);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSign = findViewById(R.id.txtsign); // Ánh xạ TextView đăng ký

        db = new MyDatabase(this);

        // Gán dữ liệu cho Spinner
        String[] roles = {"Bác sĩ", "Bệnh nhân", "Nhân viên"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        // Lưu vai trò được chọn
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = roles[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRole = "";
            }
        });

        // Xử lý khi nhấn Login
        btnLogin.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (phone.isEmpty() || pass.isEmpty() || selectedRole.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("LOGIN", "Thông tin đăng nhập: phone=" + phone + ", role=" + selectedRole);

            if (db.checkLogin(phone, pass, selectedRole)) {
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                // Lưu thông tin người dùng vào SharedPreferences
                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        .edit()
                        .putString("phone", phone)
                        .putString("role", selectedRole)
                        .apply();

                Intent intent;
                switch (selectedRole) {
                    case "Bác sĩ":
                        intent = new Intent(this, mainBS.class);
                        break;
                    case "Bệnh nhân":
                        intent = new Intent(this, mainBN.class);
                        break;
                    case "Nhân viên":
                        intent = new Intent(this, mainNV.class);
                        break;
                    default:
                        return;
                }
                startActivity(intent);
                finish();
            } else {
                Log.d("LOGIN", "Đăng nhập thất bại");
                Toast.makeText(this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý khi nhấn vào "Đăng ký tài khoản"
        txtSign.setOnClickListener(v -> {
            if ("Bệnh nhân".equals(selectedRole)) {
                Intent intent = new Intent(this, sign_up.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Chỉ bệnh nhân có thể đăng ký tài khoản", Toast.LENGTH_SHORT).show();
            }
        });
    }
}