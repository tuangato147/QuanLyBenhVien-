package com.example.ql_benhvien;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class PrescriptionAdapter extends BaseAdapter {
    private Context context;
    private List<DonThuoc> prescriptions;
    private LayoutInflater inflater;

    public PrescriptionAdapter(Context context, List<DonThuoc> prescriptions) {
        this.context = context;
        this.prescriptions = prescriptions;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return prescriptions.size();
    }

    @Override
    public Object getItem(int position) {
        return prescriptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return prescriptions.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_prescription, parent, false);
            holder = new ViewHolder();
            holder.txtDoctorName = convertView.findViewById(R.id.txtDoctorName);
            holder.txtDate = convertView.findViewById(R.id.txtDate);
            holder.txtStatus = convertView.findViewById(R.id.txtStatus);
            holder.txtMedicineCount = convertView.findViewById(R.id.txtMedicineCount);
            holder.btnViewDetail = convertView.findViewById(R.id.btnViewDetail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DonThuoc prescription = prescriptions.get(position);

        // Set thông tin đơn thuốc
        holder.txtDoctorName.setText("Bác sĩ: " + prescription.getDoctorName());
        holder.txtDate.setText("Ngày kê: " + prescription.getFormattedDate());
        holder.txtStatus.setText(prescription.getStatus());
        holder.txtMedicineCount.setText("Số loại thuốc: " + prescription.getTotalMedicines());

//        // Set màu cho status
//        switch (prescription.getStatus()) {
//            case "Đã kê":
//                holder.txtStatus.setBackgroundResource(R.drawable.status_pending);
//                break;
//            case "Đã phát thuốc":
//                holder.txtStatus.setBackgroundResource(R.drawable.status_completed);
//                break;
//            default:
//                holder.txtStatus.setBackgroundResource(R.drawable.status_pill_background);
//                break;
//        }

        // Xử lý sự kiện xem chi tiết
        holder.btnViewDetail.setOnClickListener(v -> showPrescriptionDetail(prescription));

        return convertView;
    }

    private void showPrescriptionDetail(DonThuoc prescription) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_prescription_detail);

        // Ánh xạ các view trong dialog
        TextView txtPatientName = dialog.findViewById(R.id.txtPatientName);
        TextView txtDoctorName = dialog.findViewById(R.id.txtDoctorName);
        TextView txtDate = dialog.findViewById(R.id.txtDate);
        ListView listViewMedicines = dialog.findViewById(R.id.listViewMedicines);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        // Set thông tin
        txtPatientName.setText("Bệnh nhân: " + prescription.getPatientName());
        txtDoctorName.setText("Bác sĩ: " + prescription.getDoctorName());
        txtDate.setText("Ngày kê: " + prescription.getFormattedDate());

        // Set adapter cho danh sách thuốc
        MedicineListAdapter medicineAdapter = new MedicineListAdapter(context, prescription.getMedicines());
        listViewMedicines.setAdapter(medicineAdapter);

        // Xử lý đóng dialog
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void updateList(List<DonThuoc> newList) {
        this.prescriptions = newList;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView txtDoctorName;
        TextView txtDate;
        TextView txtStatus;
        TextView txtMedicineCount;
        Button btnViewDetail;
    }
}

// Adapter cho danh sách thuốc trong dialog chi tiết
class MedicineListAdapter extends BaseAdapter {
    private Context context;
    private List<Thuoc> medicines;
    private LayoutInflater inflater;

    public MedicineListAdapter(Context context, List<Thuoc> medicines) {
        this.context = context;
        this.medicines = medicines;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return medicines.size();
    }

    @Override
    public Object getItem(int position) {
        return medicines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_medicine_simple, parent, false);
            holder = new ViewHolder();
            holder.txtMedicineName = convertView.findViewById(R.id.txtMedicineName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Thuoc medicine = medicines.get(position);
        holder.txtMedicineName.setText(medicine.getName());

        return convertView;
    }

    private static class ViewHolder {
        TextView txtMedicineName;
    }
}