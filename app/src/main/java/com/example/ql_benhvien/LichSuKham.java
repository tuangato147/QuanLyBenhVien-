package com.example.ql_benhvien;

public class LichSuKham {
    private String doctorName;
    private String date;
    private String time;
    private String status;

    public LichSuKham(String doctorName, String date, String time, String status) {
        this.doctorName = doctorName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getDoctorName() { return doctorName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
}
