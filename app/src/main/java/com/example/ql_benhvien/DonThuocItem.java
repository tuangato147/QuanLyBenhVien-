package com.example.ql_benhvien;

public class DonThuocItem {
    private String tenThuoc;
    private String lieuDung;
    private String soNgay;
    private String cachDung;

    public DonThuocItem(String tenThuoc, String lieuDung, String soNgay, String cachDung) {
        this.tenThuoc = tenThuoc;
        this.lieuDung = lieuDung;
        this.soNgay = soNgay;
        this.cachDung = cachDung;
    }

    // Getter và Setter
    public String getTenThuoc() { return tenThuoc; }
    public void setTenThuoc(String tenThuoc) { this.tenThuoc = tenThuoc; }

    public String getLieuDung() { return lieuDung; }
    public void setLieuDung(String lieuDung) { this.lieuDung = lieuDung; }

    public String getSoNgay() { return soNgay; }
    public void setSoNgay(String soNgay) { this.soNgay = soNgay; }

    public String getCachDung() { return cachDung; }
    public void setCachDung(String cachDung) { this.cachDung = cachDung; }
}

