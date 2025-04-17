package com.example.ql_benhvien;

        import android.os.Bundle;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.DividerItemDecoration;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import java.util.List;

        public class BenhNhanMyLichKhamActivity extends AppCompatActivity {
            private RecyclerView recyclerViewSchedule;
            private DatabaseHelper databaseHelper;
            private String patientPhone;
            private AppointmentAdapter adapter;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.bn_mylichkham);

                patientPhone = getIntent().getStringExtra("phone");
                databaseHelper = new DatabaseHelper(this);

                initViews();
                loadAppointments();
            }

            private void initViews() {
                recyclerViewSchedule = findViewById(R.id.recyclerViewSchedule);
                recyclerViewSchedule.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewSchedule.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            }


            private void loadAppointments() {
                List<LichKham> appointments = databaseHelper.getLichKhamByPatientPhone(patientPhone);
                adapter = new AppointmentAdapter(this, appointments);
                recyclerViewSchedule.setAdapter(adapter);
            }
        }