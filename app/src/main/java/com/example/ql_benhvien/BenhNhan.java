package com.example.ql_benhvien;

public class BenhNhan {
    private String phone;
    private String name;
    private String birthday;
    private String gender;
    private String address;
    private String avatar;
    private String email;

    public BenhNhan(String phone, String name, String birthday, String gender, String address, String avatar) {
        this.phone = phone;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.avatar = avatar;
    }

    // Getters and Setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

}