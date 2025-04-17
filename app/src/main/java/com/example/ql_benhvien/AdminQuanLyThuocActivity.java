package com.example.ql_benhvien;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AdminQuanLyThuocActivity extends AppCompatActivity {
    private ListView listViewThuoc;
    private Button btnAddThuoc;
    private DatabaseHelper databaseHelper;
    private MedicineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_quanlythuoc);

        databaseHelper = new DatabaseHelper(this);
        initViews();
        loadMedicines();
        setupButtonListeners();
    }

    private void initViews() {
        listViewThuoc = findViewById(R.id.listViewThuoc);
        btnAddThuoc = findViewById(R.id.btnAddThuoc);
    }

    private void loadMedicines() {
        List<Thuoc> medicines = databaseHelper.getAllThuoc();
        adapter = new MedicineAdapter(this, medicines);
        listViewThuoc.setAdapter(adapter);
    }

    private void setupButtonListeners() {
        btnAddThuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminQuanLyThuocActivity.this, AdminAddThuocActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadMedicines();
        }
    }
}

class MedicineAdapter extends BaseAdapter {
    private Context context;
    private List<Thuoc> medicines;
    private LayoutInflater inflater;

    public MedicineAdapter(Context context, List<Thuoc> medicines) {
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
        return medicines.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_medicine, parent, false);
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