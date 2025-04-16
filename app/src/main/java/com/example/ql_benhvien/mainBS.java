package com.example.ql_benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class mainBS extends AppCompatActivity {

    private LinearLayout featureGrid;
    private ImageButton btnAccount;
    private MyDatabase quan_ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_bs);

        quan_ly = new MyDatabase(this);
        featureGrid = findViewById(R.id.main_bs); // Giả sử bạn đã định nghĩa ID này trong XML
        btnAccount = findViewById(R.id.btnAccount);

        // Cài đặt sự kiện cho nút tài khoản
        btnAccount.setOnClickListener(v -> {
            // Chuyển sang màn hình thông tin tài khoản
            Intent intent = new Intent(mainBS.this, info.class);
            startActivity(intent);
        });


        // Cài đặt sự kiện cho các nút chức năng
        featureGrid.findViewById(R.id.btnTodaySchedule).setOnClickListener(v -> {
            // Chuyển sang màn hình lịch khám hôm nay
            Intent intent = new Intent(mainBS.this, bsLichkham.class);
            startActivity(intent);
        });

        featureGrid.findViewById(R.id.btnPatientRecords).setOnClickListener(v -> {
            // Chuyển sang màn hình hồ sơ bệnh nhân
            Toast.makeText(this, "Chức năng hồ sơ bệnh nhân chưa triển khai", Toast.LENGTH_SHORT).show();
        });

        featureGrid.findViewById(R.id.btnPrescriptions).setOnClickListener(v -> {
            Intent intent = new Intent(mainBS.this, com.example.yourapp.bsDonthuocdake.class);
            startActivity(intent);
        });


    }
}
