package com.example.yourapp;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ql_benhvien.DonThuocItem;
import com.example.ql_benhvien.MyDatabase;
import com.example.ql_benhvien.R;

import java.util.List;

public class bnDonthuoc extends AppCompatActivity {

    private MyDatabase db;
    private LinearLayout layoutShowThuoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn_donthuoc);

        layoutShowThuoc = findViewById(R.id.layoutShowThuoc);
        db = new MyDatabase(this);

        String sdt = getIntent().getStringExtra("sdt");
        if (sdt != null) {
            loadThuocChoBenhNhan(sdt);
        }
    }

    private void loadThuocChoBenhNhan(String sdt) {
        List<DonThuocItem> ds = db.getThuocTheoBenhNhan(sdt);

        if (ds.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("Không có đơn thuốc nào.");
            layoutShowThuoc.addView(tv);
            return;
        }

        for (DonThuocItem item : ds) {
            TextView tv = new TextView(this);
            tv.setText("- " + item.getTenThuoc() + ": " + item.getLieuDung() + ", " +
                    item.getSoNgay() + " ngày, " + item.getCachDung());
            layoutShowThuoc.addView(tv);
        }
    }
}
