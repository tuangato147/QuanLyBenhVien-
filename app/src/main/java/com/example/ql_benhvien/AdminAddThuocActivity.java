package com.example.ql_benhvien;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminAddThuocActivity extends AppCompatActivity {
    private EditText edtMedicineName;
    private Button btnLuuThuoc;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_addthuoc);

        databaseHelper = new DatabaseHelper(this);
        initViews();
        setupButtonListeners();
    }

    private void initViews() {
        edtMedicineName = findViewById(R.id.edtTenThuoc);
        btnLuuThuoc = findViewById(R.id.btnLuuThuoc);
    }

    private void setupButtonListeners() {
        btnLuuThuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedicine();
            }
        });
    }

    private void saveMedicine() {
        String medicineName = edtMedicineName.getText().toString().trim();

        if (medicineName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên thuốc", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = databaseHelper.addThuoc(medicineName);
        if (result != -1) {
            Toast.makeText(this, "Thêm thuốc thành công", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Thêm thuốc thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}