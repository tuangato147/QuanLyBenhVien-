package com.example.ql_benhvien;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BenhNhanDatLichActivity extends AppCompatActivity {
    private EditText edtName, edtPhone;
    private Spinner spinnerSpecialty, spinnerDoctor, spinnerRoom;
    private Button btnSubmit;
    private DatabaseHelper databaseHelper;
    private String currentUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn_datlich);

        currentUserPhone = getIntent().getStringExtra("phone");
        databaseHelper = new DatabaseHelper(this);

        initViews();
        loadUserInfo();
        setupSpinners();
        setupSubmitButton();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        spinnerSpecialty = findViewById(R.id.spinnerSpecialty);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        spinnerRoom = findViewById(R.id.spinnerRoom);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Không cho phép chỉnh sửa thông tin cá nhân
        edtName.setEnabled(false);
        edtPhone.setEnabled(false);
    }

    private void loadUserInfo() {
        BenhNhan patient = databaseHelper.getBenhNhanByPhone(currentUserPhone);
        if (patient != null) {
            edtName.setText(patient.getName());
            edtPhone.setText(patient.getPhone());
        }
    }

    private void setupSpinners() {
        // Setup Khoa
        ArrayAdapter<CharSequence> specialtyAdapter = ArrayAdapter.createFromResource(this,
                R.array.departments_array, android.R.layout.simple_spinner_item);
        specialtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialty.setAdapter(specialtyAdapter);

        // Xử lý sự kiện khi chọn khoa
        spinnerSpecialty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDepartment = parent.getItemAtPosition(position).toString();
                updateDoctorSpinner(selectedDepartment);
                updateRoomSpinner(selectedDepartment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateDoctorSpinner(String department) {
        List<BacSi> doctors = databaseHelper.getBacSiByKhoa(department);

        // Add logging
        Log.d("DatLich", "Department: " + department);
        Log.d("DatLich", "Number of doctors: " + doctors.size());

        ArrayAdapter<BacSi> doctorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (BacSi doctor : doctors) {
            doctorAdapter.add(doctor);
            // Add logging
            Log.d("DatLich", "Added doctor: " + doctor.getName());
        }

        spinnerDoctor.setAdapter(doctorAdapter);
        doctorAdapter.notifyDataSetChanged();
    }

    private void updateRoomSpinner(String department) {
        String prefix;
        switch (department) {
            case "Khoa nội":
                prefix = "N";
                break;
            case "Khoa ngoại":
                prefix = "P";
                break;
            case "Khoa thần kinh":
                prefix = "K";
                break;
            default:
                return;
        }

        String[] rooms = new String[10];
        for (int i = 0; i < 10; i++) {
            rooms[i] = prefix + String.format("%02d", i);
        }

        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rooms);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom.setAdapter(roomAdapter);
    }

    private void setupSubmitButton() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAppointment();
            }
        });
    }

    private void submitAppointment() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String specialty = spinnerSpecialty.getSelectedItem().toString();
        BacSi selectedDoctor = (BacSi) spinnerDoctor.getSelectedItem();
        String room = spinnerRoom.getSelectedItem().toString();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        LichKham appointment = new LichKham(
                0, // ID sẽ được tự động tạo
                name,
                phone,
                selectedDoctor.getName(),
                selectedDoctor.getPhone(),
                getCurrentDate(),
                getCurrentTime(),
                specialty,
                room
        );

        long result = databaseHelper.addLichKham(appointment);
        if (result != -1) {
            Toast.makeText(this, "Đặt lịch thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Đặt lịch thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }
}