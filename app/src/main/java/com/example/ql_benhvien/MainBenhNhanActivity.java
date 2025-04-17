package com.example.ql_benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainBenhNhanActivity extends AppCompatActivity {
    private ImageButton btnSchedule, btnHistory, btnMyHistory, btnDonthuocBN, btnAccount;
    private String phoneNumber;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_bn);

        phoneNumber = getIntent().getStringExtra("phone");
        databaseHelper = new DatabaseHelper(this);

        initViews();
        setupButtonListeners();
    }

    private void initViews() {
        btnSchedule = findViewById(R.id.btnSchedule);
        btnHistory = findViewById(R.id.btnHistory);
        btnMyHistory = findViewById(R.id.btnMyHistory);
        btnDonthuocBN = findViewById(R.id.btnDonthuocBN);
        btnAccount = findViewById(R.id.btnAccount);
    }

    private void setupButtonListeners() {
        // Đặt lịch khám
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainBenhNhanActivity.this, BenhNhanDatLichActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
            }
        });

        // Xem lịch sử khám
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainBenhNhanActivity.this, BenhNhanLichSuKhamActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
            }
        });

        // Xem lịch khám gần nhất
        btnMyHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainBenhNhanActivity.this, BenhNhanMyLichKhamActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
            }
        });

        // Xem đơn thuốc
        btnDonthuocBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainBenhNhanActivity.this, BenhNhanDonThuocActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
            }
        });

        // Quản lý tài khoản
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainBenhNhanActivity.this, BenhNhanInfoActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
            }
        });
    }
}