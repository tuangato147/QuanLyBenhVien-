package com.example.ql_benhvien;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class BacSiLichKhamActivity extends AppCompatActivity {
    private ListView listViewAppointments;
    private DatabaseHelper databaseHelper;
    private String doctorPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_lichkham);

        doctorPhone = getIntent().getStringExtra("phone");
        databaseHelper = new DatabaseHelper(this);

        listViewAppointments = findViewById(R.id.listViewAppointments);
        loadAppointments();
    }

    private void loadAppointments() {
        List<LichKham> appointments = databaseHelper.getLichKhamByBacSi(doctorPhone);
        AppointmentAdapter adapter = new AppointmentAdapter(this, appointments);
        listViewAppointments.setAdapter(adapter);
    }
}