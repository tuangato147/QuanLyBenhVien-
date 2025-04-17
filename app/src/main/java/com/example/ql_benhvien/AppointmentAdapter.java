package com.example.ql_benhvien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class AppointmentAdapter extends BaseAdapter {
    private Context context;
    private List<LichKham> appointments;
    private LayoutInflater inflater;

    public AppointmentAdapter(Context context, List<LichKham> appointments) {
        this.context = context;
        this.appointments = appointments;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Object getItem(int position) {
        return appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_appointment, parent, false);
            holder = new ViewHolder();
            holder.txtPatientName = convertView.findViewById(R.id.txtPatientName);
//            holder.txtPatientPhone = convertView.findViewById(R.id.txtPatientPhone);
            holder.txtDateTime = convertView.findViewById(R.id.txtDateTime);
            holder.txtRoom = convertView.findViewById(R.id.txtRoom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LichKham appointment = appointments.get(position);
        holder.txtPatientName.setText(appointment.getPatientName());
        holder.txtPatientPhone.setText(appointment.getPatientPhone());
        holder.txtDateTime.setText(appointment.getDate() + " " + appointment.getTime());
        holder.txtRoom.setText(appointment.getRoom());

        return convertView;
    }

    private class ViewHolder {
        TextView txtPatientName;
        TextView txtPatientPhone;
        TextView txtDateTime;
        TextView txtRoom;
    }
}