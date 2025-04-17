package com.example.ql_benhvien;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BacSiDonThuocActivity extends AppCompatActivity {
    private EditText edtPatientPhone, edtPatientName;
    private Button btnAddMedicine, btnSavePrescription;
    private RecyclerView listViewMedicines;
    private DatabaseHelper databaseHelper;
    private List<Thuoc> selectedMedicines;
    private PrescriptionMedicineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_donthuocdake);

        initViews();
        databaseHelper = new DatabaseHelper(this);
        selectedMedicines = new ArrayList<>();
        setupRecyclerView();
        setupButtons();
    }

    private void initViews() {
        edtPatientPhone = findViewById(R.id.edtPatientPhone);
        edtPatientName = findViewById(R.id.edtPatientName);
        btnAddMedicine = findViewById(R.id.btnAddMedicine);
        btnSavePrescription = findViewById(R.id.btnSavePrescription);
        listViewMedicines = findViewById(R.id.listViewMedicines);
    }

    private void setupRecyclerView() {
        adapter = new PrescriptionMedicineAdapter(selectedMedicines);
        listViewMedicines.setLayoutManager(new LinearLayoutManager(this));
        listViewMedicines.setAdapter(adapter);
    }

    private void setupButtons() {
        btnAddMedicine.setOnClickListener(v -> showMedicineSelectionDialog());
        btnSavePrescription.setOnClickListener(v -> savePrescription());
    }

    private void showMedicineSelectionDialog() {
        List<Thuoc> allMedicines = databaseHelper.getAllMedicines();
        String[] medicineNames = new String[allMedicines.size()];
        for (int i = 0; i < allMedicines.size(); i++) {
            medicineNames[i] = allMedicines.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thuốc")
                .setItems(medicineNames, (dialog, which) -> {
                    selectedMedicines.add(allMedicines.get(which));
                    adapter.notifyDataSetChanged();
                });
        builder.create().show();
    }

    private void savePrescription() {
        String phone = edtPatientPhone.getText().toString().trim();
        String name = edtPatientName.getText().toString().trim();

        if (phone.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bệnh nhân", Toast.LENGTH_SHORT).show();
            return;
        }

        BenhNhan patient = databaseHelper.getBenhNhanByPhone(phone);
        if (patient == null || !patient.getName().equals(name)) {
            Toast.makeText(this, "Bệnh nhân không tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedMedicines.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một loại thuốc", Toast.LENGTH_SHORT).show();
            return;
        }

        long prescriptionId = databaseHelper.savePrescription(phone, name);
        if (prescriptionId != -1) {
            for (Thuoc medicine : selectedMedicines) {
                databaseHelper.savePrescriptionDetail(prescriptionId, medicine.getId());
            }
            Toast.makeText(this, "Kê đơn thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Kê đơn thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}