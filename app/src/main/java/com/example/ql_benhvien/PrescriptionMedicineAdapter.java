package com.example.ql_benhvien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PrescriptionMedicineAdapter extends RecyclerView.Adapter<PrescriptionMedicineAdapter.ViewHolder> {
    private List<Thuoc> medicines;

    public PrescriptionMedicineAdapter(List<Thuoc> medicines) {
        this.medicines = medicines;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prescription_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Thuoc medicine = medicines.get(position);
        holder.txtMedicineName.setText(medicine.getName());
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMedicineName;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMedicineName = itemView.findViewById(R.id.txtMedicineName);
        }
    }
}