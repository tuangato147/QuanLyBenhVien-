package com.example.ql_benhvien;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class bnLichsukham extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryAdapter adapter;
    MyDatabase db;
    List<LichSuKham> lichSuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn_lichsukham);

        recyclerView = findViewById(R.id.recyclerViewHistory);
        db = new MyDatabase(this);

        // Nhận patientPhone từ Intent
        String patientPhone = getIntent().getStringExtra("patient_phone");

        if (patientPhone != null && !patientPhone.isEmpty()) {
            // Lấy danh sách lịch sử khám của bệnh nhân bằng phone
            lichSuList = db.getLichSuKhamByPatient(patientPhone);

            adapter = new HistoryAdapter(lichSuList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Lỗi: Không có thông tin số điện thoại bệnh nhân", Toast.LENGTH_SHORT).show();
        }

    }}