package com.example.ql_benhvien;

public class LichKham {
    private int id;
    private String patientName;
    private String patientPhone;
    private String doctorName;
    private String doctorPhone;
    private String date;
    private String time;
    private String department;
    private String room;
    private String status;
    // Constructor
    public LichKham(int id, String patientName, String patientPhone, String doctorName,
                    String doctorPhone, String date, String time, String department, String room) {
        this.id = id;
        this.patientName = patientName;
        this.patientPhone = patientPhone;
        this.doctorName = doctorName;
        this.doctorPhone = doctorPhone;
        this.date = date;
        this.time = time;
        this.department = department;
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }



    public void setStatus(byte[] status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }


    // Getters and Setters
    // ... (thêm các getter và setter cho tất cả các trường)
}
