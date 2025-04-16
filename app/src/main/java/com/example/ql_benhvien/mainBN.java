package com.example.ql_benhvien;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class mainBN extends AppCompatActivity {

    ImageButton btnSchedule, btnHistory, btnMedicalRecords, btnPrescriptions, btnAccount;
    String currentPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_bn);

        btnSchedule = findViewById(R.id.btnSchedule);
        btnHistory = findViewById(R.id.btnHistory);
        btnMedicalRecords = findViewById(R.id.btnMedicalRecords);
        btnPrescriptions = findViewById(R.id.btnPrescriptions);
        btnAccount = findViewById(R.id.btnAccount);
        currentPhone = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("phone", null);
        if (currentPhone == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish(); // hoặc chuyển về màn hình login
            return;


                }
        btnSchedule.setOnClickListener(view -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String patientPhone = prefs.getString("phone", ""); // hoặc "patient_id" nếu có

            Intent intent = new Intent(this, bnDatlich.class);
            intent.putExtra("patient_phone", patientPhone);
            startActivity(intent);
        });


        btnHistory.setOnClickListener(view -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String patientPhone = prefs.getString("phone", "");  // Lấy thông tin số điện thoại bệnh nhân

            if (!patientPhone.isEmpty()) {
                Intent intent = new Intent(this, bnLichsukham.class);
                intent.putExtra("patient_phone", patientPhone); // Truyền số điện thoại vào Intent
                startActivity(intent);
            } else {
                Toast.makeText(this, "Không có thông tin số điện thoại bệnh nhân", Toast.LENGTH_SHORT).show();
            }
        });


        btnMedicalRecords.setOnClickListener(view -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String phone = prefs.getString("phone", "");

            if (!phone.isEmpty()) {
                Intent intent = new Intent(this, bnHoso.class);
                intent.putExtra("patient_phone", phone); // truyền phone qua bnHoso
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Không tìm thấy thông tin bệnh nhân", Toast.LENGTH_SHORT).show();
            }
        });


        btnAccount.setOnClickListener(v -> {
            // Kiểm tra giá trị phone trước khi truyền
            if (currentPhone == null || currentPhone.isEmpty()) {
                Toast.makeText(mainBN.this, "Không tìm thấy số điện thoại!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Truyền số điện thoại qua Intent
            Intent intent = new Intent(mainBN.this, info.class);
            intent.putExtra("phone", currentPhone); // Truyền số điện thoại
            startActivity(intent);
        });


        btnPrescriptions.setOnClickListener(view -> {
            // Tạo intent để chuyển sang màn hình bnDonthuoc
            Intent intent = new Intent(this, com.example.yourapp.bnDonthuoc.class);

            // Thêm thông tin bệnh nhân và thuốc vào Intent nếu cần (ví dụ: tên bệnh nhân, ngày kê, chẩn đoán, danh sách thuốc...)
            intent.putExtra("patient", "Nguyễn Văn A"); // Tên bệnh nhân
            intent.putExtra("date", "11/04/2025"); // Ngày kê đơn
            intent.putExtra("diagnosis", "Sốt cao, viêm họng"); // Chẩn đoán bệnh

            // Tạo danh sách thuốc (dữ liệu giả lập trong trường hợp này)
            ArrayList<String> medicines = new ArrayList<>();
            medicines.add("Paracetamol 500mg - 7 ngày");
            medicines.add("Amoxicillin 500mg - 5 ngày");
            intent.putStringArrayListExtra("medicines", medicines);

            // Chuyển sang màn hình bnDonthuoc
            startActivity(intent);
        });



    }
}
