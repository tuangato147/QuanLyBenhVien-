package com.example.ql_benhvien;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.TextView;
    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;
    import java.util.List;

    public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
        private Context context;
        private List<LichKham> appointments;

        public AppointmentAdapter(Context context, List<LichKham> appointments) {
            this.context = context;
            this.appointments = appointments;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            LichKham appointment = appointments.get(position);
            holder.txtDoctorName.setText("Bác sĩ: " + appointment.getDoctorName());
            holder.txtDepartment.setText("Khoa: " + appointment.getDepartment());
            holder.txtDate.setText("Ngày: " + appointment.getDate());
            holder.txtTime.setText("Giờ: " + appointment.getTime());
            holder.txtStatus.setText("Trạng thái: " + appointment.getStatus());
        }

        @Override
        public int getItemCount() {
            return appointments.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtDoctorName, txtDepartment, txtDate, txtTime, txtStatus;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtDoctorName = itemView.findViewById(R.id.txtDoctorName);
                txtDepartment = itemView.findViewById(R.id.txtDepartment);
                txtDate = itemView.findViewById(R.id.txtDate);
                txtTime = itemView.findViewById(R.id.txtTime);
                txtStatus = itemView.findViewById(R.id.txtStatus);
            }
        }

        public void updateData(List<LichKham> newAppointments) {
            this.appointments = newAppointments;
            notifyDataSetChanged();
        }
    }