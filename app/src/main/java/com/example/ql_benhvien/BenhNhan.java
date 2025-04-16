package com.example.ql_benhvien;

public class BenhNhan {
    private String name;
    private String gender;
    private String birthday;
    private String address;
    private String phone;
    private String email;
    private String emergencyContact;
    private String chronicDiseases;
    private String allergies;
    private String surgeries;

    // Constructor có tham số
    public BenhNhan(String name, String gender, String birthday, String address,
                    String phone, String email, String emergencyContact,
                    String chronicDiseases, String allergies, String surgeries) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.chronicDiseases = chronicDiseases;
        this.allergies = allergies;
        this.surgeries = surgeries;
    }

    // Getters và setters cho các trường
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(String chronicDiseases) {
        this.chronicDiseases = chronicDiseases;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getSurgeries() {
        return surgeries;
    }

    public void setSurgeries(String surgeries) {
        this.surgeries = surgeries;
    }

}
