package com.example.ql_benhvien;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText edtPhone, edtPassword;
    private Spinner spinnerRole;
    private Switch swRemember;
    private Button btnLogin;
    private TextView txtSignup;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Khởi tạo các view
        initViews();
        // Khởi tạo database helper
        databaseHelper = new DatabaseHelper(this);
        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        // Kiểm tra xem có thông tin đăng nhập đã lưu không
        checkSavedCredentials();

        // Setup Spinner
        setupRoleSpinner();

        // Xử lý sự kiện đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Xử lý sự kiện chuyển sang màn hình đăng ký
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void initViews() {
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        swRemember = findViewById(R.id.swRemember);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.txtsign);
    }

    private void setupRoleSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
    }

    private void checkSavedCredentials() {
        if (sharedPreferences.getBoolean("remember", false)) {
            edtPhone.setText(sharedPreferences.getString("phone", ""));
            edtPassword.setText(sharedPreferences.getString("password", ""));
            String savedRole = sharedPreferences.getString("role", "");
            // Đặt spinner về vai trò đã lưu
            ArrayAdapter adapter = (ArrayAdapter) spinnerRole.getAdapter();
            int position = adapter.getPosition(savedRole);
            spinnerRole.setSelection(position);
            swRemember.setChecked(true);
        }
    }

    private void loginUser() {
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkLogin(phone, password, role)) {
            // Lưu thông tin đăng nhập nếu người dùng chọn "Nhớ mật khẩu"
            if (swRemember.isChecked()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phone", phone);
                editor.putString("password", password);
                editor.putString("role", role);
                editor.putBoolean("remember", true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
            }

            // Chuyển đến màn hình tương ứng với vai trò
            Intent intent;
            switch (role) {
                case "Bác sĩ":
                    intent = new Intent(LoginActivity.this, MainBacSiActivity.class);
                    break;
                case "Bệnh nhân":
                    intent = new Intent(LoginActivity.this, MainBenhNhanActivity.class);
                    break;
                case "Nhân viên":
                    intent = new Intent(LoginActivity.this, MainNhanVienActivity.class);
                    break;
                default:
                    return;
            }
            intent.putExtra("phone", phone);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show();
        }
    }
}