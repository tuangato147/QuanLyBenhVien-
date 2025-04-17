package com.example.ql_benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AdminQuanLyLichKhamActivity extends AppCompatActivity {
    private ListView listViewAppointments;
    private Button btnAddAppointment;
    private DatabaseHelper databaseHelper;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_quanlylichkham);

        databaseHelper = new DatabaseHelper(this);
        initViews();
        setupButtonListeners();
        loadAllAppointments();
    }

    private void initViews() {
        listViewAppointments = findViewById(R.id.lvAppointments);
        btnAddAppointment = findViewById(R.id.btnAddAppointment);
    }

    private void setupButtonListeners() {
        btnAddAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đặt lịch
                Intent intent = new Intent(AdminQuanLyLichKhamActivity.this, BenhNhanDatLichActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void loadAllAppointments() {
        List<LichKham> appointments = databaseHelper.getAllLichKham();
        adapter = new AppointmentAdapter(this, appointments);
        listViewAppointments.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Refresh danh sách lịch khám sau khi thêm mới
            loadAllAppointments();
        }
    }
}