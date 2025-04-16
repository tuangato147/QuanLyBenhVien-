package com.example.ql_benhvien;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ThuocAdapter extends RecyclerView.Adapter<ThuocAdapter.ThuocViewHolder> {

    private Context context;
    private List<Thuoc> thuocList;
    private MyDatabase db;

    public ThuocAdapter(Context context, List<Thuoc> thuocList, MyDatabase db) {
        this.context = context;
        this.thuocList = thuocList;
        this.db = db;
    }

    @Override
    public ThuocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thuoc, parent, false);
        return new ThuocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThuocViewHolder holder, int position) {
        Thuoc thuoc = thuocList.get(position);
        holder.tvSoLuong.setText("SL: " + thuoc.getSoLuong());
        holder.tvTenThuoc.setText(thuoc.getTen());

        holder.btnEdit.setOnClickListener(v -> showEditDialog(thuoc));
        holder.btnDelete.setOnClickListener(v -> {

            db.deleteThuoc(thuoc.getId()); // <-- thêm dòng này
            thuocList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();

        });
    }

    private void showEditDialog(Thuoc thuoc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_thuoc, null);
        EditText edtTen = view.findViewById(R.id.edtTenThuoc);
        EditText edtSoLuong = view.findViewById(R.id.edtSoLuong);
        edtTen.setText(thuoc.getTen());
        edtSoLuong.setText(String.valueOf(thuoc.getSoLuong()));

        builder.setView(view);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String ten = edtTen.getText().toString();
            int sl = Integer.parseInt(edtSoLuong.getText().toString());
            thuoc.setTen(ten);
            thuoc.setSoLuong(sl);
            db.updateThuoc(thuoc);
            notifyDataSetChanged();
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return thuocList.size();
    }

    public static class ThuocViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenThuoc, tvSoLuong;
        ImageButton btnEdit, btnDelete;

        public ThuocViewHolder(View itemView) {
            super(itemView);
            tvTenThuoc = itemView.findViewById(R.id.tvTenThuoc);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            btnEdit = itemView.findViewById(R.id.btnEditThuoc);
            btnDelete = itemView.findViewById(R.id.btnDeleteThuoc);

        }
    }
}
