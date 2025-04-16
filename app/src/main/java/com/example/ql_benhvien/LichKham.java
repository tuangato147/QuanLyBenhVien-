package com.example.ql_benhvien;

public class LichKham {
    private int id;
    private String patientName;
    private String time;
    private String room;
    private String reason;

    public LichKham(int id, String patientName, String time, String room, String reason) {
        this.id = id;
        this.patientName = patientName;
        this.time = time;
        this.room = room;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    public String getReason() {
        return reason;
    }
}
