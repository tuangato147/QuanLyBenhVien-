package com.example.ql_benhvien;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class bnDatlich extends AppCompatActivity {

    Spinner spinnerSpecialty, spinnerDoctor, spinnerTime, spinnerRoom;
    Button btnPickDate, btnSubmit;
    TextView tvSelectedDate;
    EditText edtNote;
    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn_datlich);

        // Ánh xạ view
        spinnerSpecialty = findViewById(R.id.spinnerSpecialty);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        spinnerTime = findViewById(R.id.spinnerTime);
        spinnerRoom = findViewById(R.id.spinnerRoom); // mới thêm
        btnPickDate = findViewById(R.id.btnPickDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        edtNote = findViewById(R.id.edtNote);

        // Dữ liệu mẫu
        String[] specialties = {"Nội khoa", "Ngoại khoa", "Tai mũi họng", "Da liễu"};
        String[] doctors = {"BS. Nguyễn Văn A", "BS. Trần Thị B", "BS. Lê Văn C"};
        String[] times = {"08:00", "09:00", "10:00", "13:00", "14:00", "15:00"};
        String[] rooms = {"Phòng 101", "Phòng 102", "Phòng 201", "Phòng 202"}; // danh sách phòng

        String patientPhone = getIntent().getStringExtra("patient_phone");

        // Adapter
        spinnerSpecialty.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, specialties));
        spinnerDoctor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doctors));
        spinnerTime.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, times));
        spinnerRoom.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rooms));

        // Chọn ngày
        btnPickDate.setOnClickListener(v -> showDatePicker());

        // Nút đặt lịch
        btnSubmit.setOnClickListener(v -> submitAppointment());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (DatePicker view, int y, int m, int d) -> {
            selectedDate = d + "/" + (m + 1) + "/" + y;
            tvSelectedDate.setText(selectedDate);
        }, year, month, day);

        dialog.show();
    }

    private void submitAppointment() {
        String specialty = spinnerSpecialty.getSelectedItem().toString();
        String doctor = spinnerDoctor.getSelectedItem().toString();
        String time = spinnerTime.getSelectedItem().toString();
        String room = spinnerRoom.getSelectedItem().toString(); // lấy phòng
        String note = edtNote.getText().toString();

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày khám!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu vào database
        MyDatabase db = new MyDatabase(this);
        try {
            db.getWritableDatabase().execSQL(
                    "INSERT INTO schedules (patient_name, doctor_name, date, time, room, reason, status) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{"Bệnh nhân A", doctor, selectedDate, time, room, note, "Chờ xác nhận"}
            );
            Toast.makeText(this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc activity
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi đặt lịch: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
