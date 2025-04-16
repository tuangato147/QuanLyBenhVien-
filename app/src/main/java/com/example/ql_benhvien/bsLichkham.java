package com.example.ql_benhvien;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.List;

public class bsLichkham extends AppCompatActivity {

    private LinearLayout layoutAppointmentList;
    private MyDatabase quan_ly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_lichkham);

        // Khởi tạo database và layout
        quan_ly = new MyDatabase(this);
        layoutAppointmentList = findViewById(R.id.layoutAppointmentList);

        // Load lịch khám của bác sĩ
        loadAppointmentList();
    }

    // Hàm để load danh sách lịch khám từ cơ sở dữ liệu
    private void loadAppointmentList() {
        // Lấy danh sách lịch khám của bác sĩ từ cơ sở dữ liệu
        List<LichKham> appointmentList = quan_ly.getAppointmentListForDoctor();

        if (appointmentList != null && !appointmentList.isEmpty()) {
            // Duyệt qua từng lịch khám và tạo UI cho từng item
            for (LichKham lichKham : appointmentList) {
                createAppointmentCard(lichKham);
            }
        }
    }

    // Hàm tạo CardView cho mỗi lịch khám
    private void createAppointmentCard(LichKham lichKham) {
        // Tạo một CardView động
        CardView cardView = new CardView(this);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        cardView.setCardElevation(4);
        cardView.setRadius(12);
        cardView.setPadding(12, 12, 12, 12);

        // Tạo một LinearLayout để chứa thông tin lịch khám
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        cardView.addView(layout);

        // Thêm TextView hiển thị tên bệnh nhân
        TextView patientNameText = new TextView(this);
        patientNameText.setText("Bệnh nhân: " + lichKham.getPatientName());
        patientNameText.setTextSize(16);
        patientNameText.setTextColor(getResources().getColor(R.color.black));
        layout.addView(patientNameText);

        // Thêm TextView hiển thị thời gian khám
        TextView appointmentTimeText = new TextView(this);
        appointmentTimeText.setText("Thời gian: " + lichKham.getTime());
        layout.addView(appointmentTimeText);

        // Thêm TextView hiển thị phòng khám
        TextView roomText = new TextView(this);
        roomText.setText("Phòng khám: " + lichKham.getRoom());
        layout.addView(roomText);

        // Thêm TextView hiển thị lý do khám
        TextView reasonText = new TextView(this);
        reasonText.setText("Lý do: " + lichKham.getReason());
        layout.addView(reasonText);

        // Tạo Button "Xem chi tiết"
        Button detailButton = new Button(this);
        detailButton.setText("Xem chi tiết");
        detailButton.setTextSize(14);
        detailButton.setBackgroundColor(getResources().getColor(R.color.purple_200));
        detailButton.setTextColor(getResources().getColor(R.color.white));
        layout.addView(detailButton);

        // Thêm cardView vào layout danh sách lịch khám
        layoutAppointmentList.addView(cardView);

        // Set hành động cho nút "Xem chi tiết"
        detailButton.setOnClickListener(v -> {
            // Mở chi tiết lịch khám (ví dụ có thể mở một màn hình chi tiết lịch khám)
            // Intent intent = new Intent(bsLichkham.this, LichKhamDetailActivity.class);
            // intent.putExtra("appointmentId", lichKham.getId());
            // startActivity(intent);
        });
    }
}
