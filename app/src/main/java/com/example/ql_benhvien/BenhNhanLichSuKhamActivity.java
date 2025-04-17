package com.example.ql_benhvien;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BenhNhanLichSuKhamActivity extends AppCompatActivity {
    private RecyclerView recyclerViewHistory;
    private DatabaseHelper databaseHelper;
    private String patientPhone;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn_lichsukham);

        patientPhone = getIntent().getStringExtra("phone");
        databaseHelper = new DatabaseHelper(this);

        initViews();
        setupRecyclerView();
        loadAppointmentHistory();
    }

    private void initViews() {
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
    }

    private void setupRecyclerView() {
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void loadAppointmentHistory() {
        List<LichKham> appointmentHistory = databaseHelper.getLichKhamHistory(patientPhone);
        adapter = new AppointmentAdapter(this, appointmentHistory);
        recyclerViewHistory.setAdapter(adapter);
    }
}