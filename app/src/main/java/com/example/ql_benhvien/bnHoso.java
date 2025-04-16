package com.example.ql_benhvien;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class bnHoso extends AppCompatActivity {

    TextView tvName, tvGender, tvBirthday, tvAddress,
            tvPhone, tvEmail, tvEmergencyContact,
            tvChronicDiseases, tvAllergies, tvSurgeries;

    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn_hoso);

        // Ánh xạ TextView
        tvName = findViewById(R.id.tvName);
        tvGender = findViewById(R.id.tvGender);
        tvBirthday = findViewById(R.id.tvBirthday);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvEmergencyContact = findViewById(R.id.tvEmergencyContact);
        tvChronicDiseases = findViewById(R.id.tvChronicDiseases);
        tvAllergies = findViewById(R.id.tvAllergies);
        tvSurgeries = findViewById(R.id.tvSurgeries);

        db = new MyDatabase(this);

        // Lấy thông tin bệnh nhân từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String phone = prefs.getString("phone", "");

        if (!phone.isEmpty()) {
            BenhNhan patient = db.getBenhNhanByPhone(phone);
            if (patient != null) {
                tvName.setText("Họ tên: " + patient.getName());
                tvGender.setText("Giới tính: " + patient.getGender());
                tvBirthday.setText("Ngày sinh: " + patient.getBirthday());
                tvAddress.setText("Địa chỉ: " + patient.getAddress());
                tvPhone.setText("SĐT: " + patient.getPhone());
                tvEmail.setText("Email: " + patient.getEmail());
                tvEmergencyContact.setText("Người thân liên hệ: " + patient.getEmergencyContact());
                tvChronicDiseases.setText("Bệnh mãn tính: " + patient.getChronicDiseases());
                tvAllergies.setText("Dị ứng: " + patient.getAllergies());
                tvSurgeries.setText("Phẫu thuật trước đây: " + patient.getSurgeries());
            } else {
                Toast.makeText(this, "Không tìm thấy hồ sơ bệnh nhân", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
