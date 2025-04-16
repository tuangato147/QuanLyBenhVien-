package com.example.ql_benhvien;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class adAddthuoc extends AppCompatActivity {

    EditText edtTenThuoc;
    Button btnLuu;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_addthuoc);

        edtTenThuoc = findViewById(R.id.edtTenThuoc);
        btnLuu = findViewById(R.id.btnLuuThuoc);
        db = new MyDatabase(this);

        btnLuu.setOnClickListener(v -> {
            String ten = edtTenThuoc.getText().toString().trim();


        });
    }
}
