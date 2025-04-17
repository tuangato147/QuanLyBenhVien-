package com.example.ql_benhvien;

public class User {
    private String phone;
    private String password;
    private String role;
    private String name;
    private String email;

    public User(String phone, String password, String role, String name, String email) {
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}