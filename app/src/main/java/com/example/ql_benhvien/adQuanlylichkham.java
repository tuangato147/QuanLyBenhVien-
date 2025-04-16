package com.example.ql_benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ql_benhvien.R;
import com.example.ql_benhvien.MyDatabase;

import java.util.List;

public class adQuanlylichkham extends AppCompatActivity {

    ListView lvAppointments;
    Button btnAddAppointment;
    MyDatabase db;
    ArrayAdapter<String> adapter;
    List<String> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_quanlylichkham);

        lvAppointments = findViewById(R.id.lvAppointments);
        btnAddAppointment = findViewById(R.id.btnAddAppointment);
        db = new MyDatabase(this);

        loadAppointments();

        btnAddAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // chuyển sang màn thêm lịch khám
                startActivity(new Intent(adQuanlylichkham.this, adAddthuoc.class));
            }
        });
    }

    private void loadAppointments() {


}}
