package com.example.ql_benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private Spinner spinnerRole;
    private EditText edtName, edtPhone, edtEmail, edtPassword;
    private Button btnSignUp;
    private ImageView showHidePass;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Khởi tạo views và database helper
        initViews();
        databaseHelper = new DatabaseHelper(this);

        // Setup Spinner
        setupRoleSpinner();

        // Xử lý sự kiện đăng ký
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Xử lý sự kiện ẩn/hiện mật khẩu
        showHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    private void initViews() {
        spinnerRole = findViewById(R.id.spinnerRole);
        edtName = findViewById(R.id.editTextt);  // họ và tên
        edtPhone = findViewById(R.id.editText);   // số điện thoại
        edtEmail = findViewById(R.id.editTexttt); // email
        edtPassword = findViewById(R.id.editText3); // mật khẩu
        btnSignUp = findViewById(R.id.button3);
        showHidePass = findViewById(R.id.showHidePass);
    }

    private void setupRoleSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
    }

    private void togglePasswordVisibility() {
        if (edtPassword.getInputType() == 129) { // Password hidden
            edtPassword.setInputType(144); // Show password
            showHidePass.setImageResource(android.R.drawable.ic_lock_lock); // Change icon
        } else {
            edtPassword.setInputType(129); // Hide password
            showHidePass.setImageResource(android.R.drawable.ic_lock_idle_lock);
        }
        edtPassword.setSelection(edtPassword.getText().length());
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        // Kiểm tra các trường thông tin
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng số điện thoại
        if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm user vào database
        long result = databaseHelper.addUser(phone, password, role, name, email);

        if (result != -1) {
            // Thêm thông tin chi tiết vào bảng tương ứng theo role
            switch (role) {
                case "Bác sĩ":
                    databaseHelper.addBacSi(phone, name);
                    break;
                case "Bệnh nhân":
                    databaseHelper.addBenhNhan(phone, name);
                    break;
                case "Nhân viên":
                    databaseHelper.addNhanVien(phone, name);
                    break;
            }

            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}