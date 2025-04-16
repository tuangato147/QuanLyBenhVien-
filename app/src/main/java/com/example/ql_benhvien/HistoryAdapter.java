package com.example.ql_benhvien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<LichSuKham> lichSuList;

    public HistoryAdapter(List<LichSuKham> list) {
        this.lichSuList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctor, tvDate, tvTime, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lichsukham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LichSuKham lichSu = lichSuList.get(position);
        holder.tvDoctor.setText("Bác sĩ: " + lichSu.getDoctorName());
        holder.tvDate.setText("Ngày: " + lichSu.getDate());
        holder.tvTime.setText("Giờ: " + lichSu.getTime());
        holder.tvStatus.setText("Trạng thái: " + lichSu.getStatus());
    }

    @Override
    public int getItemCount() {
        return lichSuList.size();
    }
}
