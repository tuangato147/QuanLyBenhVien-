package com.example.yourapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ql_benhvien.DonThuocItem;
import com.example.ql_benhvien.MyDatabase;
import com.example.ql_benhvien.R;

import java.util.*;

public class bsDonthuocdake extends AppCompatActivity {

    private EditText edtPhone, edtName, edtDate, edtDiagnosis;
    private LinearLayout layoutMedicineList;
    private Button btnAddMedicine, btnSavePrescription;

    private MyDatabase db;
    private List<DonThuocItem> dsThuoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_donthuocdake);

        db = new MyDatabase(this);

        edtPhone = findViewById(R.id.tvPhoneNumber).getRootView().findViewById(R.id.tvPhoneNumber).findViewById(R.id.edtPhone);
        edtName = findViewById(R.id.tvPatientName).getRootView().findViewById(R.id.tvPatientName).findViewById(R.id.edtName);
        edtDate = findViewById(R.id.edtDate);
        edtDiagnosis = findViewById(R.id.edtDiagnosis);
        layoutMedicineList = findViewById(R.id.layoutMedicineList);
        btnAddMedicine = findViewById(R.id.btnAddMedicine);
        btnSavePrescription = findViewById(R.id.btnSavePrescription);

        // Chọn ngày
        edtDate.setOnClickListener(v -> showDatePicker());

        // Thêm thuốc
        btnAddMedicine.setOnClickListener(v -> showThuocPicker());

        // Lưu đơn thuốc
        btnSavePrescription.setOnClickListener(v -> savePrescription());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    edtDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showThuocPicker() {
        // Lấy danh sách thuốc đã lưu
        List<String> danhSachThuoc = db.getAllThuocNames();

        // Hiển thị dialog chọn thuốc
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thuốc");

        final String[] thuocArray = danhSachThuoc.toArray(new String[0]);

        builder.setItems(thuocArray, (dialog, which) -> {
            // Hiển thị giao diện nhập liều dùng, số ngày, cách dùng
            showThuocInputDialog(thuocArray[which]);
        });

        builder.show();
    }

    private void showThuocInputDialog(String tenThuoc) {
        View view = getLayoutInflater().inflate(R.layout.item_thuoc_kedon, null);
        EditText edtLieuDung = view.findViewById(R.id.edtLieuDung);
        EditText edtSoNgay = view.findViewById(R.id.edtSoNgay);
        EditText edtCachDung = view.findViewById(R.id.edtCachDung);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập thông tin thuốc: " + tenThuoc);
        builder.setView(view);
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String lieuDung = edtLieuDung.getText().toString();
            String soNgay = edtSoNgay.getText().toString();
            String cachDung = edtCachDung.getText().toString();

            if (lieuDung.isEmpty() || soNgay.isEmpty() || cachDung.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin thuốc", Toast.LENGTH_SHORT).show();
                return;
            }

            DonThuocItem item = new DonThuocItem(tenThuoc, lieuDung, soNgay, cachDung);
            dsThuoc.add(item);

            // Thêm vào giao diện hiển thị
            TextView tv = new TextView(this);
            tv.setText(tenThuoc + " - " + lieuDung + " - " + soNgay + " ngày - " + cachDung);
            layoutMedicineList.addView(tv);
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void savePrescription() {
        String ten = edtName.getText().toString().trim();
        String sdt = edtPhone.getText().toString().trim();
        String ngayKe = edtDate.getText().toString().trim();
        String chanDoan = edtDiagnosis.getText().toString().trim();

        if (ten.isEmpty() || sdt.isEmpty() || ngayKe.isEmpty() || chanDoan.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!db.checkBenhNhanTonTai(ten, sdt)) {
            Toast.makeText(this, "Tên hoặc SĐT bệnh nhân không tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dsThuoc.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm ít nhất 1 thuốc", Toast.LENGTH_SHORT).show();
            return;
        }

        long idDon = db.insertDonThuoc(ten, sdt, ngayKe, chanDoan);
        db.insertChiTietDonThuoc(idDon, dsThuoc);

        Toast.makeText(this, "Đơn thuốc đã lưu!", Toast.LENGTH_SHORT).show();

        // Gửi đơn thuốc cho bệnh nhân (mở bnDonthuoc)
        Intent intent = new Intent(this, com.example.yourapp.bnDonthuoc.class);
        intent.putExtra("sdt", sdt);
        startActivity(intent);
        finish();
    }
}
