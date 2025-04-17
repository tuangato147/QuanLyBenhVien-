package com.example.ql_benhvien;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BacSiLichKhamActivity extends AppCompatActivity {
    private RecyclerView listViewAppointments;
    private DatabaseHelper databaseHelper;
    private String doctorPhone;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_lichkham);

        doctorPhone = getIntent().getStringExtra("phone");
        databaseHelper = new DatabaseHelper(this);

        listViewAppointments = findViewById(R.id.listViewAppointments);
        listViewAppointments.setLayoutManager(new LinearLayoutManager(this));
        listViewAppointments.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        loadAppointments();
    }

    private void loadAppointments() {
        List<LichKham> appointments = databaseHelper.getLichKhamByBacSi(doctorPhone);
        adapter = new AppointmentAdapter(this, appointments);
        listViewAppointments.setAdapter(adapter);
    }
}