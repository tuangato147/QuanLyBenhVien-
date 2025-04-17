package com.example.ql_benhvien;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BenhNhanMyLichKhamActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSchedule;
    private DatabaseHelper databaseHelper;
    private String patientPhone;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn_mylichkham);

        patientPhone = getIntent().getStringExtra("phone");
        databaseHelper = new DatabaseHelper(this);

        initViews();
        loadAppointments();
    }

    private void initViews() {
        recyclerViewSchedule = findViewById(R.id.recyclerViewSchedule);
        recyclerViewSchedule.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSchedule.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void loadAppointments() {
        List<LichKham> appointments = databaseHelper.getLichKhamByPhone(patientPhone);
        adapter = new AppointmentAdapter(appointments);
        recyclerViewSchedule.setAdapter(adapter);
    }
}

class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private List<LichKham> appointments;

    public AppointmentAdapter(List<LichKham> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        LichKham appointment = appointments.get(position);
        holder.txtAppointmentDate.setText("Ngày khám: " + appointment.getDate() +
                "\nGiờ khám: " + appointment.getTime());
        holder.txtDoctorName.setText("Bác sĩ: " + appointment.getDoctorName());
        holder.txtDepartment.setText("Khoa: " + appointment.getDepartment());
        holder.txtRoom.setText("Phòng: " + appointment.getRoom());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView txtAppointmentDate, txtDoctorName, txtDepartment, txtRoom;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAppointmentDate = itemView.findViewById(R.id.txtAppointmentDate);
            txtDoctorName = itemView.findViewById(R.id.txtDoctorName);
            txtDepartment = itemView.findViewById(R.id.txtDepartment);
            txtRoom = itemView.findViewById(R.id.txtRoom);
        }
    }
}