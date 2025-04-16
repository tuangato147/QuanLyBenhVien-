package com.example.ql_benhvien;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class sign_up extends AppCompatActivity {
    private EditText edtPhone, edtEmail, edtPassword;
    private Button btnSignUp;
    private ImageView showHidePass;
    private MyDatabase quan_ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Khởi tạo views
        initViews();

        // Xử lý sự kiện hiện/ẩn mật khẩu
        showHidePass.setOnClickListener(v -> togglePasswordVisibility());

        // Xử lý đăng ký
        btnSignUp.setOnClickListener(v -> handleSignUp());
    }

    private void initViews() {
        edtPhone = findViewById(R.id.editTextt);
        edtEmail = findViewById(R.id.editTexttt);
        edtPassword = findViewById(R.id.editText3);
        btnSignUp = findViewById(R.id.button3);
        showHidePass = findViewById(R.id.showHidePass);
        quan_ly = new MyDatabase(this);
    }

    private void togglePasswordVisibility() {
        if (edtPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
            showHidePass.setImageResource(android.R.drawable.ic_partial_secure);
            edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            showHidePass.setImageResource(android.R.drawable.ic_lock_idle_lock);
            edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        edtPassword.setSelection(edtPassword.getText().length());
    }

    private void handleSignUp() {
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra số điện thoại đã tồn tại
        if (isPhoneExists(phone)) {
            Toast.makeText(this, "Số điện thoại đã được đăng ký", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm user mới
        if (addNewUser(phone, email, password)) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            finish(); // Quay về màn hình đăng nhập
        } else {
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPhoneExists(String phone) {
        SQLiteDatabase db = quan_ly.getReadableDatabase();
        String[] columns = {"phone"};
        String selection = "phone = ?";
        String[] selectionArgs = {phone};

        Cursor cursor = db.query("users", columns, selection, selectionArgs,
                null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    private boolean addNewUser(String phone, String email, String password) {
        SQLiteDatabase db = quan_ly.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("password", password);
        values.put("role", "Bệnh nhân");

        long userId = db.insert("users", null, values);
        if (userId != -1) {
            Log.d("SIGN_UP", "Thêm user thành công với ID: " + userId);
            return true;
        } else {
            Log.d("SIGN_UP", "Lỗi khi thêm user");
            return false;
        }
    }
}