package com.example.ql_benhvien;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.Locale;

public class BenhNhanInfoActivity extends AppCompatActivity {
    private ImageView profileImage;
    private EditText edtName, edtPhone, edtEmail, edtBirthday, edtAddress;

    private CheckBox checkBox,checkBox2;
    private Button  btnLogout;
    private ImageView btnEdit;
    private DatabaseHelper databaseHelper;
    private String phoneNumber;
    private static final int PICK_IMAGE = 1;
    private boolean isEditing = false;
    private String currentImagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn_info);

        phoneNumber = getIntent().getStringExtra("phone");
        databaseHelper = new DatabaseHelper(this);

        initViews();
        loadPatientInfo();
        setupClickListeners();
    }

    private void initViews() {
        profileImage = findViewById(R.id.profileImage);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.txtEmail);
        edtBirthday = findViewById(R.id.txtBirthday);
        edtAddress = findViewById(R.id.txtAddress);
        checkBox = findViewById(R.id.checkBox);
        checkBox2 = findViewById(R.id.checkBox2);
        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);

        // Thiết lập trạng thái ban đầu là không thể chỉnh sửa
        setEditableState(false);
    }

    private void loadPatientInfo() {
        BenhNhan patient = databaseHelper.getBenhNhanInfo(phoneNumber);
        if (patient != null) {
            edtName.setText(patient.getName());
            edtPhone.setText(patient.getPhone());
            edtEmail.setText(patient.getEmail());
            edtBirthday.setText(patient.getBirthday());
            edtAddress.setText(patient.getAddress());

            if ("Nam".equals(patient.getGender())) {
                checkBox.setChecked(true);
            } else {
                checkBox2.setChecked(true);
            }

            if (patient.getAvatar() != null && !patient.getAvatar().isEmpty()) {
                currentImagePath = patient.getAvatar();
                Glide.with(this)
                        .load(patient.getAvatar())
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
                btnEdit.setImageDrawable(getResources().getDrawable(R.drawable.hoso));
            } else {
                // Lưu thông tin
                saveInfo();
            }
        });

        btnLogout.setOnClickListener(v -> showLogoutConfirmation());

        // Xử lý chọn ngày sinh
        edtBirthday.setOnClickListener(v -> {
            if (isEditing) {
                showDatePickerDialog();
            }
        });
    }

    private void setEditableState(boolean editable) {
        edtName.setEnabled(editable);
        edtEmail.setEnabled(editable);
        edtBirthday.setEnabled(editable);
        edtAddress.setEnabled(editable);
        checkBox.setEnabled(editable);
        checkBox2.setEnabled(editable);
        // Số điện thoại không được phép chỉnh sửa
        edtPhone.setEnabled(false);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d",
                            year, month + 1, dayOfMonth);
                    edtBirthday.setText(selectedDate);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveInfo() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String birthday = edtBirthday.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String gender = checkBox.isChecked() ? "Nam" : "Nữ";

        if (name.isEmpty() || email.isEmpty() || birthday.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        BenhNhan patient = new BenhNhan(phoneNumber, name, birthday, gender, address, currentImagePath);
        patient.setEmail(email);

        boolean success = databaseHelper.updateBenhNhanInfo(patient);

        if (success) {
            Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
            isEditing = false;
            setEditableState(false);
            btnEdit.setImageDrawable(getResources().getDrawable(R.drawable.his));
        } else {
            Toast.makeText(this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    // Xóa thông tin đăng nhập đã lưu
                    getSharedPreferences("loginPrefs", MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply();

                    // Chuyển về màn hình đăng nhập
                    Intent intent = new Intent(BenhNhanInfoActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
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