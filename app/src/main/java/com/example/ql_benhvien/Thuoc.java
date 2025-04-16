package com.example.ql_benhvien;

public class Thuoc {
    private String ten;
    private int soLuong;
    private int id;

    public Thuoc(int id , String ten, int soLuong) {
        this.id = id;             // <-- thêm dòng này
        this.ten = ten;
        this.soLuong = soLuong;
    }


    public String getTen() {
        return ten;
    }

    public int getSoLuong() {
        return soLuong;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

}
