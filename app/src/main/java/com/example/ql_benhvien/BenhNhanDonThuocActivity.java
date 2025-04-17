package com.example.ql_benhvien;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class BenhNhanDonThuocActivity extends AppCompatActivity {
    private ListView listViewPrescriptions;
    private EditText edtSearch;
    private TextView txtEmpty;
    private DatabaseHelper databaseHelper;
    private PrescriptionAdapter adapter;
    private List<DonThuoc> prescriptionList;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn_donthuoc);

        // Lấy số điện thoại từ Intent
        phoneNumber = getIntent().getStringExtra("phone");

        initViews();
        setupList();
        setupSearch();
    }

    private void initViews() {
        listViewPrescriptions = findViewById(R.id.listViewPrescriptions);
        edtSearch = findViewById(R.id.edtSearch);
        txtEmpty = findViewById(R.id.txtEmpty);
        databaseHelper = new DatabaseHelper(this);
    }

    private void setupList() {
        prescriptionList = databaseHelper.getDonThuocByPhone(phoneNumber);
        adapter = new PrescriptionAdapter(this, prescriptionList);
        listViewPrescriptions.setAdapter(adapter);

        // Hiển thị thông báo nếu không có đơn thuốc
        if (prescriptionList.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            listViewPrescriptions.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            listViewPrescriptions.setVisibility(View.VISIBLE);
        }
    }

    private void setupSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPrescriptions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterPrescriptions(String searchText) {
        List<DonThuoc> filteredList = new ArrayList<>();
        for (DonThuoc prescription : prescriptionList) {
            if (prescription.getDate().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(prescription);
            }
        }
        adapter.updateList(filteredList);

        // Cập nhật hiển thị khi không có kết quả tìm kiếm
        if (filteredList.isEmpty()) {
            txtEmpty.setText("Không tìm thấy đơn thuốc");
            txtEmpty.setVisibility(View.VISIBLE);
            listViewPrescriptions.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            listViewPrescriptions.setVisibility(View.VISIBLE);
        }
    }
}