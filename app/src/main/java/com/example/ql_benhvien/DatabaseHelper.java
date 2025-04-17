package com.example.ql_benhvien;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HealthcareDB";
    private static final int DATABASE_VERSION = 1;

    // Bảng Users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";

    // Bảng BacSi
    public static final String TABLE_BACSI = "bacsi";
    public static final String COLUMN_KHOA = "khoa";
    public static final String COLUMN_AVATAR = "avatar";

    // Bảng BenhNhan
    public static final String TABLE_BENHNHAN = "benhnhan";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_ADDRESS = "address";

    // Các câu lệnh tạo bảng
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_PHONE + " TEXT PRIMARY KEY,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_ROLE + " TEXT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT" + ")";

    private static final String CREATE_TABLE_BACSI = "CREATE TABLE " + TABLE_BACSI + "("
            + COLUMN_PHONE + " TEXT PRIMARY KEY,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_KHOA + " TEXT,"
            + COLUMN_AVATAR + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_PHONE + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_PHONE + "))";

    private static final String CREATE_TABLE_BENHNHAN = "CREATE TABLE " + TABLE_BENHNHAN + "("
            + COLUMN_PHONE + " TEXT PRIMARY KEY,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_BIRTHDAY + " TEXT,"
            + COLUMN_GENDER + " TEXT,"
            + COLUMN_ADDRESS + " TEXT,"
            + COLUMN_AVATAR + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_PHONE + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_PHONE + "))";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_BACSI);
        db.execSQL(CREATE_TABLE_BENHNHAN);
        // Thêm các bảng khác sau
        String createDonThuocTable = "CREATE TABLE DonThuoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patientPhone TEXT NOT NULL," +
                "patientName TEXT NOT NULL," +
                "doctorName TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "status TEXT NOT NULL DEFAULT 'Đã kê'" +
                ")";
        db.execSQL(createDonThuocTable);

        // Tạo bảng ChiTietDonThuoc
        String createChiTietDonThuocTable = "CREATE TABLE ChiTietDonThuoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "prescriptionId INTEGER NOT NULL," +
                "medicineId INTEGER NOT NULL," +
                "FOREIGN KEY (prescriptionId) REFERENCES DonThuoc(id)," +
                "FOREIGN KEY (medicineId) REFERENCES Thuoc(id)" +
                ")";
        db.execSQL(createChiTietDonThuocTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BACSI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BENHNHAN);
        onCreate(db);
    }

    // Thêm user mới
    public long addUser(String phone, String password, String role, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        return db.insert(TABLE_USERS, null, values);
    }

    // Kiểm tra đăng nhập
    public boolean checkLogin(String phone, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PHONE};
        String selection = COLUMN_PHONE + "=? AND " + COLUMN_PASSWORD + "=? AND " + COLUMN_ROLE + "=?";
        String[] selectionArgs = {phone, password, role};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    // Thêm vào class DatabaseHelper

    // Thêm bác sĩ mới
    public long addBacSi(String phone, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_NAME, name);
        return db.insert(TABLE_BACSI, null, values);
    }

    // Thêm bệnh nhân mới
    public long addBenhNhan(String phone, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_NAME, name);
        return db.insert(TABLE_BENHNHAN, null, values);
    }

    // Thêm nhân viên mới
    public long addNhanVien(String phone, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_NAME, name);
        return db.insert("nhanvien", null, values);
    }
    // Thêm vào DatabaseHelper.java

    public List<LichKham> getLichKhamByBacSi(String doctorPhone) {
        List<LichKham> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM lichkham WHERE doctor_phone = ? ORDER BY date, time";
        Cursor cursor = db.rawQuery(query, new String[]{doctorPhone});

        if (cursor.moveToFirst()) {
            do {
                LichKham appointment = new LichKham(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("patient_name")),
                        cursor.getString(cursor.getColumnIndex("patient_phone")),
                        cursor.getString(cursor.getColumnIndex("doctor_name")),
                        cursor.getString(cursor.getColumnIndex("doctor_phone")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("department")),
                        cursor.getString(cursor.getColumnIndex("room"))
                );
                appointments.add(appointment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }
    // Thêm vào DatabaseHelper.java

    public BenhNhan getBenhNhanByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BENHNHAN, null,
                COLUMN_PHONE + " = ?", new String[]{phone}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            BenhNhan patient = new BenhNhan(
                    cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR))
            );
            cursor.close();
            return patient;
        }
        return null;
    }

    public List<Thuoc> getAllThuoc() {
        List<Thuoc> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("thuoc", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                medicines.add(new Thuoc(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return medicines;
    }

    public long savePrescription(String patientPhone, String patientName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_phone", patientPhone);
        values.put("patient_name", patientName);
        values.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return db.insert("donthuoc", null, values);
    }

    public void savePrescriptionDetail(long prescriptionId, int medicineId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("prescription_id", prescriptionId);
        values.put("medicine_id", medicineId);
        db.insert("chitiet_donthuoc", null, values);
    }
    public BacSi getBacSiInfo(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.*, b.khoa, b.avatar FROM " + TABLE_USERS + " u "
                + "LEFT JOIN " + TABLE_BACSI + " b ON u.phone = b.phone "
                + "WHERE u.phone = ?";

        Cursor cursor = db.rawQuery(query, new String[]{phone});

        if (cursor != null && cursor.moveToFirst()) {
            BacSi doctor = new BacSi(
                    cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_KHOA)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR))
            );
            cursor.close();
            return doctor;
        }
        return null;
    }

    public boolean updateBacSiInfo(BacSi doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Cập nhật thông tin trong bảng users
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_NAME, doctor.getName());
            userValues.put(COLUMN_EMAIL, doctor.getEmail());

            int userResult = db.update(TABLE_USERS, userValues,
                    COLUMN_PHONE + " = ?", new String[]{doctor.getPhone()});

            // Cập nhật thông tin trong bảng bacsi
            ContentValues doctorValues = new ContentValues();
            doctorValues.put(COLUMN_KHOA, doctor.getKhoa());
            doctorValues.put(COLUMN_AVATAR, doctor.getAvatar());

            int doctorResult = db.update(TABLE_BACSI, doctorValues,
                    COLUMN_PHONE + " = ?", new String[]{doctor.getPhone()});

            if (userResult > 0 && doctorResult > 0) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            db.endTransaction();
        }
    }
    // Thêm vào DatabaseHelper.java

    public List<BacSi> getBacSiByKhoa(String khoa) {
        List<BacSi> doctors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT u.*, b.khoa FROM " + TABLE_USERS + " u "
                + "INNER JOIN " + TABLE_BACSI + " b ON u.phone = b.phone "
                + "WHERE b.khoa = ?";

        Cursor cursor = db.rawQuery(query, new String[]{khoa});

        if (cursor.moveToFirst()) {
            do {
                BacSi doctor = new BacSi(
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_KHOA)),
                        null // Không cần avatar trong trường hợp này
                );
                doctors.add(doctor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return doctors;
    }

    public long addLichKham(LichKham appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("patient_name", appointment.getPatientName());
        values.put("patient_phone", appointment.getPatientPhone());
        values.put("doctor_name", appointment.getDoctorName());
        values.put("doctor_phone", appointment.getDoctorPhone());
        values.put("date", appointment.getDate());
        values.put("time", appointment.getTime());
        values.put("department", appointment.getDepartment());
        values.put("room", appointment.getRoom());

        return db.insert("lichkham", null, values);
    }
    // Thêm vào DatabaseHelper.java

    public List<LichKham> getLichKhamHistory(String patientPhone) {
        List<LichKham> history = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM lichkham WHERE patient_phone = ? ORDER BY date DESC, time DESC";
        Cursor cursor = db.rawQuery(query, new String[]{patientPhone});

        if (cursor.moveToFirst()) {
            do {
                LichKham appointment = new LichKham(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("patient_name")),
                        cursor.getString(cursor.getColumnIndex("patient_phone")),
                        cursor.getString(cursor.getColumnIndex("doctor_name")),
                        cursor.getString(cursor.getColumnIndex("doctor_phone")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("department")),
                        cursor.getString(cursor.getColumnIndex("room"))
                );
                history.add(appointment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return history;
    }

    public LichKham getLatestLichKham(String patientPhone) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM lichkham WHERE patient_phone = ? " +
                "ORDER BY date DESC, time DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{patientPhone});

        if (cursor != null && cursor.moveToFirst()) {
            LichKham appointment = new LichKham(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("patient_name")),
                    cursor.getString(cursor.getColumnIndex("patient_phone")),
                    cursor.getString(cursor.getColumnIndex("doctor_name")),
                    cursor.getString(cursor.getColumnIndex("doctor_phone")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("department")),
                    cursor.getString(cursor.getColumnIndex("room"))
            );
            cursor.close();
            return appointment;
        }
        return null;
    }

    public List<DonThuoc> getDonThuocByPatient(String patientPhone) {
        List<DonThuoc> prescriptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT d.*, GROUP_CONCAT(t.name) as medicines " +
                "FROM donthuoc d " +
                "LEFT JOIN chitiet_donthuoc cd ON d.id = cd.prescription_id " +
                "LEFT JOIN thuoc t ON cd.medicine_id = t.id " +
                "WHERE d.patient_phone = ? " +
                "GROUP BY d.id " +
                "ORDER BY d.date DESC";

        Cursor cursor = db.rawQuery(query, new String[]{patientPhone});

        if (cursor.moveToFirst()) {
            do {
                List<Thuoc> medicines = new ArrayList<>();
                String[] medicineNames = cursor.getString(cursor.getColumnIndex("medicines")).split(",");
                for (String name : medicineNames) {
                    medicines.add(new Thuoc(0, name.trim()));
                }

                DonThuoc prescription = new DonThuoc(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("patient_name")),
                        cursor.getString(cursor.getColumnIndex("patient_phone")),
                        cursor.getString(cursor.getColumnIndex("doctor_name")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        medicines
                );
                prescriptions.add(prescription);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return prescriptions;
    }
    // Thêm vào DatabaseHelper.java

    public List<LichKham> getAllLichKham() {
        List<LichKham> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM lichkham ORDER BY date DESC, time DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                LichKham appointment = new LichKham(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("patient_name")),
                        cursor.getString(cursor.getColumnIndex("patient_phone")),
                        cursor.getString(cursor.getColumnIndex("doctor_name")),
                        cursor.getString(cursor.getColumnIndex("doctor_phone")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("department")),
                        cursor.getString(cursor.getColumnIndex("room"))
                );
                appointments.add(appointment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }

    public long addThuoc(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        return db.insert("thuoc", null, values);
    }
    // Thêm vào DatabaseHelper.java

    public boolean updateBenhNhanInfo(BenhNhan patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Cập nhật thông tin trong bảng users
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_NAME, patient.getName());
            userValues.put(COLUMN_EMAIL, patient.getEmail());

            int userResult = db.update(TABLE_USERS, userValues,
                    COLUMN_PHONE + " = ?", new String[]{patient.getPhone()});

            // Cập nhật thông tin trong bảng benhnhan
            ContentValues patientValues = new ContentValues();
            patientValues.put(COLUMN_NAME, patient.getName());
            patientValues.put(COLUMN_BIRTHDAY, patient.getBirthday());
            patientValues.put(COLUMN_GENDER, patient.getGender());
            patientValues.put(COLUMN_ADDRESS, patient.getAddress());
            patientValues.put(COLUMN_AVATAR, patient.getAvatar());

            int patientResult = db.update(TABLE_BENHNHAN, patientValues,
                    COLUMN_PHONE + " = ?", new String[]{patient.getPhone()});

            if (userResult > 0 && patientResult > 0) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public BenhNhan getBenhNhanInfo(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.*, b.birthday, b.gender, b.address, b.avatar " +
                "FROM " + TABLE_USERS + " u " +
                "LEFT JOIN " + TABLE_BENHNHAN + " b ON u.phone = b.phone " +
                "WHERE u.phone = ?";

        Cursor cursor = db.rawQuery(query, new String[]{phone});

        if (cursor != null && cursor.moveToFirst()) {
            BenhNhan patient = new BenhNhan(
                    cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR))
            );
            patient.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            cursor.close();
            return patient;
        }
        return null;
    }
    // Thêm phương thức này vào class DatabaseHelper

    public List<DonThuoc> getDonThuocByPhone(String phoneNumber) {
        List<DonThuoc> prescriptionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query để lấy thông tin đơn thuốc
        String query = "SELECT d.id, d.patientPhone, d.patientName, d.doctorName, d.date, d.status " +
                "FROM DonThuoc d " +
                "WHERE d.patientPhone = ? " +
                "ORDER BY d.date DESC";

        Cursor cursor = db.rawQuery(query, new String[]{phoneNumber});

        if (cursor.moveToFirst()) {
            do {
                DonThuoc prescription = new DonThuoc();
                prescription.setId(cursor.getLong(0));
                prescription.setPatientPhone(cursor.getString(1));
                prescription.setPatientName(cursor.getString(2));
                prescription.setDoctorName(cursor.getString(3));
                prescription.setDate(cursor.getString(4));
                prescription.setStatus(cursor.getString(5));

                // Lấy danh sách thuốc cho đơn này
                prescription.setMedicines(getMedicinesForPrescription(prescription.getId()));

                prescriptionList.add(prescription);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return prescriptionList;
    }

    // Phương thức hỗ trợ để lấy danh sách thuốc trong đơn
    private List<Thuoc> getMedicinesForPrescription(long prescriptionId) {
        List<Thuoc> medicineList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT t.id, t.name, t.description, t.price " +
                "FROM Thuoc t " +
                "INNER JOIN ChiTietDonThuoc ct ON t.id = ct.medicineId " +
                "WHERE ct.prescriptionId = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(prescriptionId)});

        if (cursor.moveToFirst()) {
            do {
                Thuoc medicine = new Thuoc();
                medicine.setId(cursor.getLong(0));
                medicine.setName(cursor.getString(1));
                medicineList.add(medicine);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return medicineList;
    }



    // Thêm phương thức để lưu đơn thuốc mới
    public long savePrescription(String patientPhone, String patientName, String doctorName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patientPhone", patientPhone);
        values.put("patientName", patientName);
        values.put("doctorName", doctorName);
        values.put("date", getCurrentDateTime());
        values.put("status", "Đã kê");

        return db.insert("DonThuoc", null, values);
    }

    // Thêm phương thức để lưu chi tiết đơn thuốc
    public void savePrescriptionDetail(long prescriptionId, long medicineId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("prescriptionId", prescriptionId);
        values.put("medicineId", medicineId);

        db.insert("ChiTietDonThuoc", null, values);
    }

    // Phương thức hỗ trợ để lấy thời gian hiện tại
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Thêm phương thức để cập nhật trạng thái đơn thuốc
    public void updatePrescriptionStatus(long prescriptionId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);

        db.update("DonThuoc", values, "id = ?", new String[]{String.valueOf(prescriptionId)});
    }
    // Thêm phương thức này vào class DatabaseHelper
    public List<LichKham> getLichKhamByPhone(String patientPhone) {
        List<LichKham> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query để lấy tất cả lịch khám của bệnh nhân, sắp xếp theo ngày giờ gần nhất
        String query = "SELECT l.id, l.patientPhone, l.patientName, l.doctorPhone, l.doctorName, " +
                "l.department, l.room, l.date, l.time, l.status " +
                "FROM LichKham l " +
                "WHERE l.patientPhone = ? " +
                "ORDER BY l.date DESC, l.time DESC";

        try {
            Cursor cursor = db.rawQuery(query, new String[]{patientPhone});

            if (cursor.moveToFirst()) {
                do {
                    LichKham appointment = new LichKham();
                    appointment.setId(cursor.getInt(0));
                    appointment.setPatientPhone(cursor.getString(1));
                    appointment.setPatientName(cursor.getString(2));
                    appointment.setDoctorPhone(cursor.getString(3));
                    appointment.setDoctorName(cursor.getString(4));
                    appointment.setDepartment(cursor.getString(5));
                    appointment.setRoom(cursor.getString(6));
                    appointment.setDate(cursor.getString(7));
                    appointment.setTime(cursor.getString(8));
                    appointment.setStatus(cursor.getString(9));

                    appointments.add(appointment);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }

    // Đảm bảo bảng LichKham đã được tạo trong phương thức onCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        // ... Các bảng khác ...

        // Tạo bảng LichKham nếu chưa có
        String createLichKhamTable = "CREATE TABLE IF NOT EXISTS LichKham (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patientPhone TEXT NOT NULL, " +
                "patientName TEXT NOT NULL, " +
                "doctorPhone TEXT NOT NULL, " +
                "doctorName TEXT NOT NULL, " +
                "department TEXT NOT NULL, " +
                "room TEXT NOT NULL, " +
                "date TEXT NOT NULL, " +
                "time TEXT NOT NULL, " +
                "status TEXT NOT NULL DEFAULT 'Chờ khám'" +
                ")";
        db.execSQL(createLichKhamTable);
    }

    // Thêm phương thức để thêm lịch khám mới
    public long addLichKham(LichKham lichKham) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("patientPhone", lichKham.getPatientPhone());
        values.put("patientName", lichKham.getPatientName());
        values.put("doctorPhone", lichKham.getDoctorPhone());
        values.put("doctorName", lichKham.getDoctorName());
        values.put("department", lichKham.getDepartment());
        values.put("room", lichKham.getRoom());
        values.put("date", lichKham.getDate());
        values.put("time", lichKham.getTime());
        values.put("status", lichKham.getStatus());

        return db.insert("LichKham", null, values);
    }

    // Thêm phương thức để cập nhật trạng thái lịch khám
    public int updateLichKhamStatus(int id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);

        return db.update("LichKham", values, "id = ?",
                new String[]{String.valueOf(id)});
    }

    // Thêm phương thức để xóa lịch khám
    public int deleteLichKham(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("LichKham", "id = ?",
                new String[]{String.valueOf(id)});
    }
}