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
import java.util.List;

public class BenhNhanLichSuKhamActivity extends AppCompatActivity {
    private ListView listViewHistory;
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
        loadAppointmentHistory();
    }

    private void initViews() {
        listViewHistory = findViewById(R.id.listViewHistory);
    }

    private void loadAppointmentHistory() {
        List<LichKham> appointmentHistory = databaseHelper.getLichKhamHistory(patientPhone);
        adapter = new AppointmentAdapter(this, appointmentHistory);
        listViewHistory.setAdapter(adapter);
    }
}

class AppointmentHistoryAdapter extends BaseAdapter {
    private Context context;
    private List<LichKham> appointmentHistory;
    private LayoutInflater inflater;

    public AppointmentHistoryAdapter(Context context, List<LichKham> appointmentHistory) {
        this.context = context;
        this.appointmentHistory = appointmentHistory;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return appointmentHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return appointmentHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_appointment_history, parent, false);
            holder = new ViewHolder();
            holder.txtDate = convertView.findViewById(R.id.txtDate);
            holder.txtDoctor = convertView.findViewById(R.id.txtDoctorName);
            holder.txtDepartment = convertView.findViewById(R.id.txtDepartment);
            holder.txtRoom = convertView.findViewById(R.id.txtRoom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LichKham appointment = appointmentHistory.get(position);
        holder.txtDate.setText(appointment.getDate() + " " + appointment.getTime());
        holder.txtDoctor.setText("Bác sĩ: " + appointment.getDoctorName());
        holder.txtDepartment.setText("Khoa: " + appointment.getDepartment());
        holder.txtRoom.setText("Phòng: " + appointment.getRoom());

        return convertView;
    }

    private class ViewHolder {
        TextView txtDate, txtDoctor, txtDepartment, txtRoom;
    }
}