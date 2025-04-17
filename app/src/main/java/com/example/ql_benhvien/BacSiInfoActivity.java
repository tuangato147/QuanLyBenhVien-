package com.example.ql_benhvien;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
// Sửa lại import
import com.bumptech.glide.Glide; // thay vì com.bumpimage.glide.Glide

public class BacSiInfoActivity extends AppCompatActivity {
    private ImageView profileImage;
    private EditText edtName, edtPhone, edtEmail;
    private AutoCompleteTextView txtIdCard; // Chuyên khoa
    private Button btnLogout;
    private DatabaseHelper databaseHelper;
    private String phoneNumber;
    private static final int PICK_IMAGE = 1;
    private boolean isEditing = false;
    private String currentImagePath;
    ImageButton btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_info);

        databaseHelper = new DatabaseHelper(this); // Thêm dòng này
        phoneNumber = getIntent().getStringExtra("phone");
        initViews();
        setupAutoComplete();
        loadDoctorInfo();
        setupClickListeners();
    }
    //test
    public BacSi getBacSiInfo(String phone) {
        try {
            // Code truy vấn hiện tại
            BacSi doctor = databaseHelper.getBacSiInfo(phone);
            if (doctor != null) {
                return doctor;
            } else {
                Log.e("DatabaseError", "No doctor found with phone number: " + phone);
                return null;
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error getting doctor info: " + e.getMessage());
            return null;
        }
    }
    private void initViews() {
        profileImage = findViewById(R.id.profileImage);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.txtEmail);
        txtIdCard = findViewById(R.id.txtIdCard);
        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);

        // Thiết lập trạng thái ban đầu là không thể chỉnh sửa
        setEditableState(false);
    }

    private void setupAutoComplete() {
        String[] departments = {"Khoa nội", "Khoa ngoại", "Khoa thần kinh"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, departments);
        txtIdCard.setAdapter(adapter);
    }

    private void loadDoctorInfo() {
        BacSi doctor = databaseHelper.getBacSiInfo(phoneNumber);
        if (doctor != null) {
            edtName.setText(doctor.getName());
            edtPhone.setText(doctor.getPhone());
            edtEmail.setText(doctor.getEmail());
            txtIdCard.setText(doctor.getKhoa());

            if (doctor.getAvatar() != null && !doctor.getAvatar().isEmpty()) {
                currentImagePath = doctor.getAvatar();
                Glide.with(this)
                        .load(doctor.getAvatar())
                        .circleCrop()
                        .into(profileImage);
            }
        }
    }

    private void setupClickListeners() {
        profileImage.setOnClickListener(v -> {
            if (isEditing) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        btnEdit.setOnClickListener(v -> {
            if (!isEditing) {
                // Bắt đầu chỉnh sửa
                isEditing = true;
                setEditableState(true);
                //luu lai
               // btnEdit.setImageDrawable(getResources().getDrawable(R.drawable.hoso));
            } else {
                // Lưu thông tin
                saveInfo();
            }
        });

        btnLogout.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void setEditableState(boolean editable) {
        edtName.setEnabled(editable);
        edtEmail.setEnabled(editable);
        txtIdCard.setEnabled(editable);
        edtPhone.setEnabled(editable);
    }

    private void saveInfo() {
        // Get values from input fields
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String khoa = txtIdCard.getText().toString().trim();

        // Basic validation for required fields
        if (name.isEmpty() || email.isEmpty() || khoa.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Basic email format validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create doctor object with updated info
        BacSi doctor = new BacSi(phoneNumber, name, email, khoa, currentImagePath);

        // Save to database without additional checks
        try {
            databaseHelper.updateBacSiInfo(doctor);
            Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();

            // Reset edit state
            isEditing = false;
            setEditableState(false);
            btnEdit.setImageDrawable(getResources().getDrawable(R.drawable.acc));
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Xóa thông tin đăng nhập đã lưu
                    getSharedPreferences("loginPrefs", MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply();

                    // Chuyển về màn hình đăng nhập
                    Intent intent = new Intent(BacSiInfoActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Ở lại", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            currentImagePath = selectedImage.toString();
            Glide.with(this)
                    .load(selectedImage)
                    .circleCrop()
                    .into(profileImage);
        }
    }
}