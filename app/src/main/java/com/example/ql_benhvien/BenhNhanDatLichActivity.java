package com.example.ql_benhvien;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BenhNhanDatLichActivity extends AppCompatActivity {
    private EditText edtName, edtPhone, edtDate, edtTime;
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
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);

        // Không cho phép chỉnh sửa thông tin cá nhân
        edtName.setEnabled(false);
        edtPhone.setEnabled(false);

        // Setup date picker
        edtDate.setOnClickListener(v -> showDatePicker());
        edtTime.setOnClickListener(v -> showTimePicker());
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
        String[] departments = {"Khoa nội", "Khoa ngoại", "Khoa thần kinh"};
        ArrayAdapter<String> specialtyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, departments);
        specialtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialty.setAdapter(specialtyAdapter);

        // Khởi tạo adapter rỗng cho bác sĩ và phòng khám
        ArrayAdapter<BacSi> doctorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctor.setAdapter(doctorAdapter);

        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom.setAdapter(roomAdapter);

        // Xử lý sự kiện khi chọn khoa
        spinnerSpecialty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDepartment = departments[position];
                updateDoctorSpinner(selectedDepartment);
                updateRoomSpinner(selectedDepartment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateDoctorSpinner(String department) {
        ArrayAdapter<BacSi> doctorAdapter = (ArrayAdapter<BacSi>) spinnerDoctor.getAdapter();
        doctorAdapter.clear();

        List<BacSi> doctors = databaseHelper.getBacSiByKhoa(department);
        Log.d("DatLich", "Found " + doctors.size() + " doctors for department: " + department);

        doctorAdapter.addAll(doctors);
        doctorAdapter.notifyDataSetChanged();
    }

    private void updateRoomSpinner(String department) {
        ArrayAdapter<String> roomAdapter = (ArrayAdapter<String>) spinnerRoom.getAdapter();
        roomAdapter.clear();

        String prefix;
        switch (department.toLowerCase()) {
            case "khoa nội":
                prefix = "N";
                break;
            case "khoa ngoại":
                prefix = "P";
                break;
            case "khoa thần kinh":
                prefix = "K";
                break;
            default:
                prefix = "X";
                break;
        }

        // Thêm 10 phòng với prefix tương ứng
        for (int i = 1; i <= 10; i++) {
            roomAdapter.add(prefix + String.format("%02d", i));
        }

        roomAdapter.notifyDataSetChanged();
    }

    private void setupSubmitButton() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAppointment();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            edtDate.setText(sdf.format(calendar.getTime()));
        }, Calendar.getInstance().get(Calendar.YEAR),
           Calendar.getInstance().get(Calendar.MONTH),
           Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        // Set min date to today
        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();
    }

    private void showTimePicker() {
        Calendar current = Calendar.getInstance();
        TimePickerDialog picker = new TimePickerDialog(this, (view, hour, minute) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            edtTime.setText(sdf.format(calendar.getTime()));
        }, current.get(Calendar.HOUR_OF_DAY),
           current.get(Calendar.MINUTE), true);
        picker.show();
    }

    private void submitAppointment() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String specialty = spinnerSpecialty.getSelectedItem().toString();
        BacSi selectedDoctor = (BacSi) spinnerDoctor.getSelectedItem();
        String room = spinnerRoom.getSelectedItem().toString();
        String date = edtDate.getText().toString().trim();
        String time = edtTime.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if appointment time is valid (during work hours 8:00-17:00)
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date appointmentTime = timeFormat.parse(time);
            Date startTime = timeFormat.parse("08:00");
            Date endTime = timeFormat.parse("17:00");

            if (appointmentTime.before(startTime) || appointmentTime.after(endTime)) {
                Toast.makeText(this, "Vui lòng chọn giờ khám từ 8:00 đến 17:00", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Check for duplicate appointments
        if (isDuplicateAppointment(selectedDoctor.getPhone(), date, time)) {
            Toast.makeText(this, "Bác sĩ đã có lịch khám vào thời gian này", Toast.LENGTH_SHORT).show();
            return;
        }

        LichKham appointment = new LichKham(
                0,
                name,
                phone,
                selectedDoctor.getName(),
                selectedDoctor.getPhone(),
                date,
                time,
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

    private boolean isDuplicateAppointment(String doctorPhone, String date, String time) {
        List<LichKham> doctorAppointments = databaseHelper.getLichKhamByBacSi(doctorPhone);
        for (LichKham appointment : doctorAppointments) {
            if (appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                return true;
            }
        }
        return false;
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }
}
