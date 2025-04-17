package com.example.ql_benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainNhanVienActivity extends AppCompatActivity {
    private ImageButton btnScheduleManage, btnMedicineManage;
    private String phoneNumber;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_nv);

        phoneNumber = getIntent().getStringExtra("phone");
        databaseHelper = new DatabaseHelper(this);

        initViews();
        setupButtonListeners();
    }

    private void initViews() {
        btnScheduleManage = findViewById(R.id.btnScheduleManage);
        btnMedicineManage = findViewById(R.id.btnMedicineManage);
    }

    private void setupButtonListeners() {
        // Quản lý lịch khám
        btnScheduleManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainNhanVienActivity.this, AdminQuanLyLichKhamActivity.class);
                startActivity(intent);
            }
        });

        // Quản lý thuốc
        btnMedicineManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainNhanVienActivity.this, AdminQuanLyThuocActivity.class);
                startActivity(intent);
            }
        });
    }
}