package com.example.ql_benhvien;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class BacSiDonThuocActivity extends AppCompatActivity {
    private EditText edtPatientPhone, edtPatientName;
    private Button btnAddMedicine, btnSavePrescription;
    private ListView listViewMedicines;
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
        setupListView();
        setupButtons();
    }

    private void initViews() {
        edtPatientPhone = findViewById(R.id.edtPatientPhone);
        edtPatientName = findViewById(R.id.edtPatientName);
        btnAddMedicine = findViewById(R.id.btnAddMedicine);
        btnSavePrescription = findViewById(R.id.btnSavePrescription);
        listViewMedicines = findViewById(R.id.listViewMedicines);
    }

    private void setupListView() {
        adapter = new PrescriptionMedicineAdapter(this, selectedMedicines);
        listViewMedicines.setAdapter(adapter);
    }

    private void setupButtons() {
        btnAddMedicine.setOnClickListener(v -> showMedicineSelectionDialog());
        btnSavePrescription.setOnClickListener(v -> savePrescription());
    }

    private void showMedicineSelectionDialog() {
        List<Thuoc> allMedicines = databaseHelper.getAllThuoc();
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

        // Kiểm tra bệnh nhân tồn tại
        BenhNhan patient = databaseHelper.getBenhNhanByPhone(phone);
        if (patient == null || !patient.getName().equals(name)) {
            Toast.makeText(this, "Bệnh nhân không tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedMedicines.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một loại thuốc", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu đơn thuốc
        long prescriptionId = databaseHelper.savePrescription(phone, name);
        if (prescriptionId != -1) {
            // Lưu chi tiết đơn thuốc
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

class PrescriptionMedicineAdapter extends BaseAdapter {
    private Context context;
    private List<Thuoc> medicines;
    private LayoutInflater inflater;

    public PrescriptionMedicineAdapter(Context context, List<Thuoc> medicines) {
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
            convertView = inflater.inflate(R.layout.item_prescription_medicine, parent, false);
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