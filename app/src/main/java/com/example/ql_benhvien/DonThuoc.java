package com.example.ql_benhvien;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DonThuoc {
    private long id;
    private String patientPhone;
    private String patientName;
    private String doctorName;
    private String date;
    private List<Thuoc> medicines;
    private String status; // Đã kê/Chưa kê/Đã phát thuốc

    public DonThuoc() {
        this.medicines = new ArrayList<>();
    }

    public DonThuoc(long id, String patientPhone, String patientName, String doctorName, String date) {
        this.id = id;
        this.patientPhone = patientPhone;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.date = date;
        this.medicines = new ArrayList<>();
        this.status = "Đã kê";
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDate() {
        return date;
    }

    public List<Thuoc> getMedicines() {
        return medicines;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMedicines(List<Thuoc> medicines) {
        this.medicines = medicines;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Thêm thuốc vào đơn
    public void addMedicine(Thuoc medicine) {
        if (!medicines.contains(medicine)) {
            medicines.add(medicine);
        }
    }

    // Xóa thuốc khỏi đơn
    public void removeMedicine(Thuoc medicine) {
        medicines.remove(medicine);
    }

    // Lấy tổng số thuốc trong đơn
    public int getTotalMedicines() {
        return medicines.size();
    }

    // Format ngày tháng cho đẹp
    public String getFormattedDate() {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = inputFormat.parse(this.date);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return this.date;
        }
    }
}