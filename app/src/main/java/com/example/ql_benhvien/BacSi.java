package com.example.ql_benhvien;

public class BacSi {
    private String phone;
    private String name;
    private String email;
    private String khoa;
    private String avatar;

    public BacSi(String phone, String name, String email, String khoa, String avatar) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.khoa = khoa;
        this.avatar = avatar;
    }

    // Getters and Setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getKhoa() { return khoa; }
    public void setKhoa(String khoa) { this.khoa = khoa; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
