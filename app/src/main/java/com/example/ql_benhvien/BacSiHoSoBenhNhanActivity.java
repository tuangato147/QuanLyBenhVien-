package com.example.ql_benhvien;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class BacSiHoSoBenhNhanActivity extends AppCompatActivity {
    private EditText edtSearchPhone;
    private Button btnSearchPatient;
    private DatabaseHelper databaseHelper;
    ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_hoso_bn);

        initViews();
        databaseHelper = new DatabaseHelper(this);

        btnSearchPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPatient();
            }
        });
    }

    private void initViews() {
        edtSearchPhone = findViewById(R.id.edtPhoneSearch);
        btnSearchPatient = findViewById(R.id.btnSearchPatient);
    }

    private void searchPatient() {
        String phoneNumber = edtSearchPhone.getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        BenhNhan patient = databaseHelper.getBenhNhanByPhone(phoneNumber);

        if (patient != null) {
            showPatientInfoDialog(patient);
        } else {
            Toast.makeText(this, "Không tìm thấy bệnh nhân", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPatientInfoDialog(BenhNhan patient) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_patient_info);

        // Thiết lập kích thước dialog
        android.view.Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT,
                            android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }

        // Ánh xạ các view
        ImageView imgAvatar = dialog.findViewById(R.id.imgAvatar);
        TextView txtName = dialog.findViewById(R.id.txtName);
        TextView txtPhone = dialog.findViewById(R.id.txtPhone);
        TextView txtEmail = dialog.findViewById(R.id.txtEmail);
        TextView txtBirthday = dialog.findViewById(R.id.txtBirthday);
        TextView txtGender = dialog.findViewById(R.id.txtGender);
        TextView txtAddress = dialog.findViewById(R.id.txtAddress);
        TextView txtLastVisit = dialog.findViewById(R.id.txtLastVisit);

        // Set dữ liệu
        if (patient.getAvatar() != null && !patient.getAvatar().isEmpty()) {
            Glide.with(this)
                    .load(patient.getAvatar())
                    .circleCrop()
                    .into(imgAvatar);
        }

        txtName.setText("Họ tên: " + patient.getName());
        txtPhone.setText("Số điện thoại: " + patient.getPhone());
        txtEmail.setText("Email: " + patient.getEmail());
        txtBirthday.setText("Ngày sinh: " + patient.getBirthday());
        txtGender.setText("Giới tính: " + patient.getGender());
        txtAddress.setText("Địa chỉ: " + patient.getAddress());

        // Lấy lịch sử khám gần nhất
        LichKham lastVisit = databaseHelper.getLatestLichKham(patient.getPhone());
        if (lastVisit != null) {
            txtLastVisit.setText("Lần khám gần nhất: " + lastVisit.getDate() +
                    "\nKhoa: " + lastVisit.getDepartment() +
                    "\nBác sĩ: " + lastVisit.getDoctorName());
        } else {
            txtLastVisit.setText("Chưa có lịch sử khám");
        }

        dialog.show();
    }

}