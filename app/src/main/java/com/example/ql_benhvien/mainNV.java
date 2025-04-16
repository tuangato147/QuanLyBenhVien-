package com.example.ql_benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class mainNV extends AppCompatActivity {

    ImageButton btnScheduleManage, btnMedicineManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_nv);

        btnScheduleManage = findViewById(R.id.btnScheduleManage);
        btnMedicineManage = findViewById(R.id.btnMedicineManage); // ánh xạ nút thuốc

        // Xử lý click mở giao diện lịch khám bác sĩ
        btnScheduleManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainNV.this, bsLichkham.class);
                startActivity(intent);
            }
        });

        // ✅ Xử lý click mở giao diện quản lý thuốc
        btnMedicineManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainNV.this, adQuanlythuoc.class);
                startActivity(intent);
            }
        });
    }
}
