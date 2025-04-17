package com.example.ql_benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainBacSiActivity extends AppCompatActivity {
    private ImageButton btnTodaySchedule, btnPatientRecords, btnKethuoc, btnAccount;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_bs);

        // Lấy số điện thoại từ Intent
        phoneNumber = getIntent().getStringExtra("phone");

        initViews();
        setupButtonListeners();
    }

    private void initViews() {
        btnTodaySchedule = findViewById(R.id.btnTodaySchedule);
        btnPatientRecords = findViewById(R.id.btnPatientRecords);
        btnKethuoc = findViewById(R.id.btnKethuoc);
        btnAccount = findViewById(R.id.btnAccount);
    }

    private void setupButtonListeners() {
        // Xem lịch khám
        btnTodaySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainBacSiActivity.this, BacSiLichKhamActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
            }
        });

        // Xem hồ sơ bệnh nhân
        btnPatientRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainBacSiActivity.this, BacSiHoSoBenhNhanActivity.class);
                startActivity(intent);
            }
        });

        // Kê đơn thuốc
        btnKethuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainBacSiActivity.this, BacSiDonThuocActivity.class);
                startActivity(intent);
            }
        });

        // Quản lý tài khoản
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainBacSiActivity.this, BacSiInfoActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
            }
        });
    }
}