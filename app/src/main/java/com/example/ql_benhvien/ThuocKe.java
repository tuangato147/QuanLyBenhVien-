package com.example.ql_benhvien;

public class ThuocKe {
    private Thuoc thuoc;
    private String lieuDung;
    private int soNgay;
    private String cachDung;

    // Constructor, getters, setters

    public Thuoc getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public String getLieuDung() {
        return lieuDung;
    }

    public void setLieuDung(String lieuDung) {
        this.lieuDung = lieuDung;
    }

    public int getSoNgay() {
        return soNgay;
    }

    public void setSoNgay(int soNgay) {
        this.soNgay = soNgay;
    }

    public String getCachDung() {
        return cachDung;
    }

    public void setCachDung(String cachDung) {
        this.cachDung = cachDung;
    }

    public ThuocKe(Thuoc thuoc, String lieuDung, int soNgay, String cachDung) {
        this.thuoc = thuoc;
        this.lieuDung = lieuDung;
        this.soNgay = soNgay;
        this.cachDung = cachDung;
    }
}
