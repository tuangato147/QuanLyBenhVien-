package com.example.ql_benhvien;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class info extends AppCompatActivity {

    private EditText txtPhone, txtFullName, txtEmail, txtBirthday, txtGender, txtAddress, txtIdCard, txtBloodType, txtAllergies;
    private ImageButton btnEdit;
    private Button btnLogout;
    private String currentPhone;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_a);

        // Nhận số điện thoại từ Intent
        currentPhone = getIntent().getStringExtra("phone");

        // Kiểm tra nếu không nhận được số điện thoại
        if (currentPhone == null || currentPhone.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không nhận được số điện thoại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d("INFO_ACTIVITY", "Số điện thoại nhận được: " + currentPhone);

        initViews();
        loadPatientInfo(); // Load thông tin mặc định
        setupListeners(); // Cấu hình các sự kiện
    }

    private void initViews() {
        txtPhone = findViewById(R.id.txtPhone);
        txtFullName = findViewById(R.id.edtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtBirthday = findViewById(R.id.txtBirthday);
        txtGender = findViewById(R.id.txtGender);
        txtAddress = findViewById(R.id.txtAddress);
        txtIdCard = findViewById(R.id.txtIdCard);
        txtBloodType = findViewById(R.id.txtBloodType);
        txtAllergies = findViewById(R.id.txtAllergies);

        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);

        setEditableState(false); // Không cho phép chỉnh sửa lúc đầu
    }

    private void setupListeners() {
        btnEdit.setOnClickListener(v -> toggleEdit()); // Toggle trạng thái chỉnh sửa

        txtBirthday.setOnClickListener(v -> {
            if (isEditing) {
                showDatePicker(); // Mở DatePicker để chọn ngày sinh
            }
        });

        btnLogout.setOnClickListener(v -> handleLogout()); // Đăng xuất
    }

    private void toggleEdit() {
        isEditing = !isEditing;
        setEditableState(isEditing);
        btnEdit.setImageResource(isEditing ? android.R.drawable.ic_menu_save : android.R.drawable.ic_menu_edit);

        if (!isEditing) {
            saveUserData(); // Lưu thông tin khi không ở trạng thái chỉnh sửa
        }
    }

    private void setEditableState(boolean editable) {
        txtFullName.setEnabled(editable);
        txtEmail.setEnabled(editable);
        txtBirthday.setEnabled(editable);
        txtGender.setEnabled(editable);
        txtAddress.setEnabled(editable);
        txtIdCard.setEnabled(editable);
        txtBloodType.setEnabled(editable);
        txtAllergies.setEnabled(editable);
    }

    private void loadPatientInfo() {
        // Hiển thị thông tin mặc định
        txtPhone.setText(currentPhone);
        txtFullName.setText("Nguyễn Văn A");
        txtEmail.setText("example@gmail.com");
        txtBirthday.setText("01/01/1990");
        txtGender.setText("Nam");
        txtAddress.setText("123 Đường ABC, TP. XYZ");
        txtIdCard.setText("123456789");
        txtBloodType.setText("O+");
        txtAllergies.setText("Không có");
    }

    private void saveUserData() {
        // Hiển thị thông báo khi nhấn lưu
        Toast.makeText(this, "Thông tin đã được lưu ", Toast.LENGTH_SHORT).show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                    txtBirthday.setText(selectedDate);
                }, year, month, day);

        pickerDialog.show();
    }

    private void handleLogout() {
        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(info.this, log_in.class);
        startActivity(intent);
        finish();
    }
}