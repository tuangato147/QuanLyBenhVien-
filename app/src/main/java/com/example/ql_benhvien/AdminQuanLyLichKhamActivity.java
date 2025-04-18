package com.example.ql_benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminQuanLyLichKhamActivity extends AppCompatActivity {
    private RecyclerView recyclerViewAppointments;
    private Button btnAddAppointment;
    private DatabaseHelper databaseHelper;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_quanlylichkham);

        databaseHelper = new DatabaseHelper(this);
        initViews();
        setupRecyclerView();
        setupButtonListeners();
        loadAllAppointments();
    }

    private void initViews() {
        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        btnAddAppointment = findViewById(R.id.btnAddAppointment);
    }

    private void setupRecyclerView() {
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAppointments.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setupButtonListeners() {
        btnAddAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(AdminQuanLyLichKhamActivity.this, BenhNhanDatLichActivity.class);
            intent.putExtra("isAdmin", true); // Add this flag
            startActivityForResult(intent, 1);
        });
    }

    private void loadAllAppointments() {
        List<LichKham> appointments = databaseHelper.getAllLichKham();
        adapter = new AppointmentAdapter(this, appointments);
        recyclerViewAppointments.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadAllAppointments();
        }
    }
}
