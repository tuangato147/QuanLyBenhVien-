package com.example.ql_benhvien;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

        // Khởi tạo databaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Lấy số điện thoại từ intent
        phoneNumber = getIntent().getStringExtra("phone");

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin số điện thoại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
        String[] departments = {"Khoa Nội", "Khoa Ngoại", "Khoa Thần Kinh"}; // Viết hoa chữ cái đầu mỗi từ
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, departments);
        txtIdCard.setAdapter(adapter);
    }

    private void loadDoctorInfo() {
        try {
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
            } else {
                Toast.makeText(this, "Không tìm thấy thông tin bác sĩ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("BacSiInfoActivity", "Error loading doctor info: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi tải thông tin bác sĩ", Toast.LENGTH_SHORT).show();
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
                btnEdit.setImageDrawable(getResources().getDrawable(R.drawable.save));
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
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String khoa = txtIdCard.getText().toString().trim();

        // Detailed logging for debugging
        Log.d("BacSiInfoActivity", "Starting save operation:");
        Log.d("BacSiInfoActivity", "Phone: " + phoneNumber);
        Log.d("BacSiInfoActivity", "Name: " + name);
        Log.d("BacSiInfoActivity", "Email: " + email);
        Log.d("BacSiInfoActivity", "Khoa: " + khoa);
        Log.d("BacSiInfoActivity", "Avatar path: " + currentImagePath);

        if (name.isEmpty() || email.isEmpty() || khoa.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BacSi doctor = new BacSi(phoneNumber, name, email, khoa, currentImagePath);
            boolean isUpdated = databaseHelper.updateBacSiInfo(doctor);

            if (isUpdated) {
                Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                isEditing = false;
                setEditableState(false);
                btnEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));
                loadDoctorInfo();
            } else {
                Log.e("BacSiInfoActivity", "Update failed but no exception thrown");
                Toast.makeText(this, "Không thể cập nhật thông tin. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("BacSiInfoActivity", "Error saving doctor info: " + e.getMessage());
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("OK", (dialog, which) -> {

                    // Chuyển về màn hình đăng nhập
                    Intent intent = new Intent(BacSiInfoActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Ở lại", null)
                .show();
    }
    }
