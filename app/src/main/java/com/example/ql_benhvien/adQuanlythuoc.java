package com.example.ql_benhvien;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adQuanlythuoc extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ThuocAdapter adapter;
    private MyDatabase db;
    private List<Thuoc> thuocList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_quanlythuoc);

        recyclerView = findViewById(R.id.recyclerThuoc);
        db = new MyDatabase(this);
        thuocList = db.getAllThuoc();

        adapter = new ThuocAdapter(this, thuocList, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnAddThuoc).setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_thuoc, null);
        EditText edtTen = view.findViewById(R.id.edtTenThuoc);
        EditText edtSoLuong = view.findViewById(R.id.edtSoLuong);

        builder.setView(view);
        builder.setTitle("Thêm thuốc mới");

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String ten = edtTen.getText().toString().trim();
            String soLuongStr = edtSoLuong.getText().toString().trim();

            if (ten.isEmpty() || soLuongStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int soLuong = Integer.parseInt(soLuongStr);
            Thuoc thuoc = new Thuoc(0, ten, soLuong);
            db.insertThuoc(thuoc);
            thuocList.clear();
            thuocList.addAll(db.getAllThuoc());
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã thêm", Toast.LENGTH_SHORT).show();
        });


        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

}
