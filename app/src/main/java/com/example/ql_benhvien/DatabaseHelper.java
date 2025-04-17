package com.example.ql_benhvien;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HealthcareDB";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_BACSI = "bacsi";
    private static final String TABLE_BENHNHAN = "benhnhan";
    private static final String TABLE_LICHKHAM = "lichkham";
    private static final String TABLE_DONTHUOC = "donthuoc";
    private static final String TABLE_THUOC = "thuoc";
    private static final String TABLE_CHITIET_DONTHUOC = "chitiet_donthuoc";

    // Common columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_KHOA = "khoa";
    private static final String COLUMN_AVATAR = "avatar";
    private static final String COLUMN_BIRTHDAY = "birthday";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";

    // Create table statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_PHONE + " TEXT PRIMARY KEY,"
            + COLUMN_PASSWORD + " TEXT NOT NULL,"
            + COLUMN_ROLE + " TEXT NOT NULL,"
            + COLUMN_NAME + " TEXT NOT NULL,"
            + COLUMN_EMAIL + " TEXT"
            + ")";

    private static final String CREATE_TABLE_BACSI = "CREATE TABLE " + TABLE_BACSI + "("
            + COLUMN_PHONE + " TEXT PRIMARY KEY,"
            + COLUMN_NAME + " TEXT NOT NULL,"
            + COLUMN_KHOA + " TEXT NOT NULL,"
            + COLUMN_AVATAR + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_PHONE + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_PHONE + ") ON DELETE CASCADE"
            + ")";

    private static final String CREATE_TABLE_BENHNHAN = "CREATE TABLE " + TABLE_BENHNHAN + "("
            + COLUMN_PHONE + " TEXT PRIMARY KEY,"
            + COLUMN_NAME + " TEXT NOT NULL,"
            + COLUMN_BIRTHDAY + " TEXT,"
            + COLUMN_GENDER + " TEXT,"
            + COLUMN_ADDRESS + " TEXT,"
            + COLUMN_AVATAR + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_PHONE + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_PHONE + ") ON DELETE CASCADE"
            + ")";

    private static final String CREATE_TABLE_LICHKHAM = "CREATE TABLE " + TABLE_LICHKHAM + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "patient_phone TEXT NOT NULL,"
            + "patient_name TEXT NOT NULL,"
            + "doctor_phone TEXT NOT NULL,"
            + "doctor_name TEXT NOT NULL,"
            + "department TEXT NOT NULL,"
            + "room TEXT NOT NULL,"
            + COLUMN_DATE + " TEXT NOT NULL,"
            + COLUMN_TIME + " TEXT NOT NULL,"
            + COLUMN_STATUS + " TEXT NOT NULL DEFAULT 'Chờ khám',"
            + "FOREIGN KEY(patient_phone) REFERENCES " + TABLE_BENHNHAN + "(" + COLUMN_PHONE + ") ON DELETE CASCADE,"
            + "FOREIGN KEY(doctor_phone) REFERENCES " + TABLE_BACSI + "(" + COLUMN_PHONE + ") ON DELETE CASCADE"
            + ")";

    private static final String CREATE_TABLE_DONTHUOC = "CREATE TABLE " + TABLE_DONTHUOC + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "patient_phone TEXT NOT NULL,"
            + "patient_name TEXT NOT NULL,"
            + "doctor_name TEXT NOT NULL,"
            + COLUMN_DATE + " TEXT NOT NULL,"
            + COLUMN_STATUS + " TEXT NOT NULL DEFAULT 'Đã kê',"
            + "FOREIGN KEY(patient_phone) REFERENCES " + TABLE_BENHNHAN + "(" + COLUMN_PHONE + ") ON DELETE CASCADE"
            + ")";

    private static final String CREATE_TABLE_THUOC = "CREATE TABLE " + TABLE_THUOC + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT NOT NULL UNIQUE"
            + ")";

    private static final String CREATE_TABLE_CHITIET_DONTHUOC = "CREATE TABLE " + TABLE_CHITIET_DONTHUOC + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "prescription_id INTEGER NOT NULL,"
            + "medicine_id INTEGER NOT NULL,"
            + "FOREIGN KEY(prescription_id) REFERENCES " + TABLE_DONTHUOC + "(" + COLUMN_ID + ") ON DELETE CASCADE,"
            + "FOREIGN KEY(medicine_id) REFERENCES " + TABLE_THUOC + "(" + COLUMN_ID + ") ON DELETE CASCADE"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_BACSI);
            db.execSQL(CREATE_TABLE_BENHNHAN);
            db.execSQL(CREATE_TABLE_LICHKHAM);
            db.execSQL(CREATE_TABLE_DONTHUOC);
            db.execSQL(CREATE_TABLE_THUOC);
            db.execSQL(CREATE_TABLE_CHITIET_DONTHUOC);
            // Tạo bảng NhanVien
            db.execSQL("CREATE TABLE IF NOT EXISTS NhanVien (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "phone TEXT NOT NULL, " +
                    "name TEXT NOT NULL)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Drop tables in reverse order of creation
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHITIET_DONTHUOC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_THUOC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONTHUOC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LICHKHAM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BENHNHAN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BACSI);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility methods
    @NonNull
    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    private int getColumnIndexOrThrow(@NonNull Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index == -1) {
            throw new IllegalArgumentException("Column '" + columnName + "' does not exist");
        }
        return index;
    }

    @Nullable
    private String getStringOrNull(@NonNull Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        return index != -1 && !cursor.isNull(index) ? cursor.getString(index) : null;
    }
    // User methods
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

    // Thêm vào class DatabaseHelper
    public List<LichKham> getAllLichKham() {
        List<LichKham> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id", "patient_name", "patient_phone", "doctor_name", "doctor_phone",
                           "date", "time", "symptoms", "status"};
        String orderBy = "date DESC, time DESC";

        try (Cursor cursor = db.query(TABLE_LICHKHAM, columns, null, null, null, null, orderBy)) {
            while (cursor.moveToNext()) {
                LichKham appointment = new LichKham(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
                );
                appointments.add(appointment);
            }
        }
        return appointments;
    }
    public boolean checkLogin(String phone, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PHONE};
        String selection = COLUMN_PHONE + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_ROLE + " = ?";
        String[] selectionArgs = {phone, password, role};

        try (Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs,
                null, null, null)) {
            return cursor.moveToFirst();
        }
    }

    public String getUserRole(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ROLE};
        String selection = COLUMN_PHONE + " = ?";
        String[] selectionArgs = {phone};
        try (Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)) {
            return cursor.moveToFirst() ? cursor.getString(0) : null;
        }
    }

    // Appointment (LichKham) methods
    public long addAppointment(LichKham lichKham) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_phone", lichKham.getPatientPhone());
        values.put("patient_name", lichKham.getPatientName());
        values.put("doctor_phone", lichKham.getDoctorPhone());
        values.put("doctor_name", lichKham.getDoctorName());
        values.put("department", lichKham.getDepartment());
        values.put("room", lichKham.getRoom());
        values.put(COLUMN_DATE, lichKham.getDate());
        values.put(COLUMN_TIME, lichKham.getTime());
        values.put(COLUMN_STATUS, "Chờ khám");
        return db.insert(TABLE_LICHKHAM, null, values);
    }

    public List<LichKham> getLichKhamByPatientPhone(String patientPhone) {
        List<LichKham> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id", "patient_phone", "patient_name", "doctor_phone", "doctor_name",
                "department", "room", "date", "time", "status"};
        String selection = "patient_phone = ?";
        String[] selectionArgs = {patientPhone};
        String orderBy = "date DESC, time DESC";

        try (Cursor cursor = db.query(TABLE_LICHKHAM, columns, selection, selectionArgs,
                null, null, orderBy)) {
            if (cursor.moveToFirst()) {
                do {
                    LichKham appointment = new LichKham(
                            cursor.getInt(getColumnIndexOrThrow(cursor, "id")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "patient_name")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "patient_phone")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "doctor_name")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "doctor_phone")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "date")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "time")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "department")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "room"))
                    );
                    appointment.setStatus(cursor.getString(getColumnIndexOrThrow(cursor, "status")));
                    appointments.add(appointment);
                } while (cursor.moveToNext());
            }
        }
        return appointments;
    }

    public List<LichKham> getLichKhamByDoctorPhone(String doctorPhone) {
        List<LichKham> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id", "patient_phone", "patient_name", "doctor_phone", "doctor_name",
                "department", "room", "date", "time", "status"};
        String selection = "doctor_phone = ?";
        String[] selectionArgs = {doctorPhone};
        String orderBy = "date ASC, time ASC";

        try (Cursor cursor = db.query(TABLE_LICHKHAM, columns, selection, selectionArgs,
                null, null, orderBy)) {
            if (cursor.moveToFirst()) {
                do {
                    LichKham appointment = new LichKham(
                            cursor.getInt(getColumnIndexOrThrow(cursor, "id")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "patient_name")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "patient_phone")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "doctor_name")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "doctor_phone")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "date")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "time")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "department")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "room"))
                    );
                    appointment.setStatus(cursor.getString(getColumnIndexOrThrow(cursor, "status")));
                    appointments.add(appointment);
                } while (cursor.moveToNext());
            }
        }
        return appointments;
    }

    public boolean updateAppointmentStatus(int appointmentId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(appointmentId)};
        return db.update(TABLE_LICHKHAM, values, whereClause, whereArgs) > 0;
    }

    // Prescription (DonThuoc) methods
    public long addPrescription(DonThuoc donThuoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_phone", donThuoc.getPatientPhone());
        values.put("patient_name", donThuoc.getPatientName());
        values.put("doctor_name", donThuoc.getDoctorName());
        values.put(COLUMN_DATE, getCurrentDateTime());
        values.put(COLUMN_STATUS, "Đã kê");
        return db.insert(TABLE_DONTHUOC, null, values);
    }

    public List<DonThuoc> getDonThuocByPatientPhone(String patientPhone) {
        List<DonThuoc> prescriptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id", "patient_phone", "patient_name", "doctor_name", "date", "status"};
        String selection = "patient_phone = ?";
        String[] selectionArgs = {patientPhone};
        String orderBy = "date DESC";

        try (Cursor cursor = db.query(TABLE_DONTHUOC, columns, selection, selectionArgs,
                null, null, orderBy)) {
            if (cursor.moveToFirst()) {
                do {
                    DonThuoc prescription = new DonThuoc(
                            cursor.getLong(getColumnIndexOrThrow(cursor, "id")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "patient_phone")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "patient_name")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "doctor_name")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "date"))
                    );
                    prescription.setStatus(cursor.getString(getColumnIndexOrThrow(cursor, "status")));
                    prescriptions.add(prescription);
                } while (cursor.moveToNext());
            }
        }
        return prescriptions;
    }

    public boolean updatePrescriptionStatus(long prescriptionId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(prescriptionId)};
        return db.update(TABLE_DONTHUOC, values, whereClause, whereArgs) > 0;
    }

    // Medicine (Thuoc) methods
    public long addMedicine(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        return db.insert(TABLE_THUOC, null, values);
    }

    // Thêm vào class DatabaseHelper
    public BenhNhan getBenhNhanByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PHONE, COLUMN_NAME, COLUMN_BIRTHDAY, COLUMN_GENDER, COLUMN_ADDRESS};
        String selection = COLUMN_PHONE + " = ?";
        String[] selectionArgs = {phone};

        try (Cursor cursor = db.query(TABLE_BENHNHAN, columns, selection, selectionArgs,
                null, null, null)) {
            if (cursor.moveToFirst()) {
                return new BenhNhan(
                    cursor.getString(getColumnIndexOrThrow(cursor, COLUMN_PHONE)),
                    cursor.getString(getColumnIndexOrThrow(cursor, COLUMN_NAME)),
                    cursor.getString(getColumnIndexOrThrow(cursor, COLUMN_BIRTHDAY)),
                    cursor.getString(getColumnIndexOrThrow(cursor, COLUMN_GENDER)),
                    cursor.getString(getColumnIndexOrThrow(cursor, COLUMN_ADDRESS)),
                    getStringOrNull(cursor, COLUMN_AVATAR)
                );
            }
        }
        return null;
    }

    public BacSi getBacSiInfo(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn join 2 bảng users và bacsi
        String query = "SELECT u." + COLUMN_NAME + ", u." + COLUMN_PHONE + ", u." + COLUMN_EMAIL +
                       ", b." + COLUMN_KHOA + ", b." + COLUMN_AVATAR +
                       " FROM " + TABLE_USERS + " u" +
                       " INNER JOIN " + TABLE_BACSI + " b ON u." + COLUMN_PHONE + " = b." + COLUMN_PHONE +
                       " WHERE u." + COLUMN_PHONE + " = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{phone})) {
            if (cursor.moveToFirst()) {
                return new BacSi(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOA)),
                    getStringOrNull(cursor, COLUMN_AVATAR)
                );
            }
        }
        return null;
    }

    public boolean updateBacSiInfo(BacSi bacSi) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Cập nhật bảng users
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_NAME, bacSi.getName());
            userValues.put(COLUMN_EMAIL, bacSi.getEmail());

            int userResult = db.update(TABLE_USERS, userValues,
                COLUMN_PHONE + " = ?",
                new String[]{bacSi.getPhone()});

            // Cập nhật bảng bacsi
            ContentValues doctorValues = new ContentValues();
            doctorValues.put(COLUMN_KHOA, bacSi.getKhoa());
            doctorValues.put(COLUMN_AVATAR, bacSi.getAvatar());

            int doctorResult = db.update(TABLE_BACSI, doctorValues,
                COLUMN_PHONE + " = ?",
                new String[]{bacSi.getPhone()});

            // Kiểm tra kết quả và commit transaction
            if (userResult > 0 && doctorResult > 0) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public List<LichKham> getLichKhamByBacSi(String doctorPhone) {
        List<LichKham> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id", "patient_phone", "patient_name", "doctor_phone", "doctor_name",
                "department", "room", "date", "time", "status"};
        String selection = "doctor_phone = ?";
        String[] selectionArgs = {doctorPhone};
        String orderBy = "date ASC, time ASC";

        try (Cursor cursor = db.query(TABLE_LICHKHAM, columns, selection, selectionArgs,
                null, null, orderBy)) {
            if (cursor.moveToFirst()) {
                do {
                    LichKham appointment = new LichKham(
                            cursor.getInt(getColumnIndexOrThrow(cursor, "id")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "patient_name")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "patient_phone")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "doctor_name")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "doctor_phone")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "date")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "time")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "department")),
                            cursor.getString(getColumnIndexOrThrow(cursor, "room"))
                    );
                    appointment.setStatus(cursor.getString(getColumnIndexOrThrow(cursor, "status")));
                    appointments.add(appointment);
                } while (cursor.moveToNext());
            }
        }
        return appointments;
    }

    public List<BacSi> getBacSiByKhoa(String khoa) {
        List<BacSi> doctors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Add logging
        Log.d("Database", "Querying doctors for department: " + khoa);

        String query = "SELECT u." + COLUMN_PHONE + ", u." + COLUMN_NAME + ", u." + COLUMN_EMAIL +
                       ", b." + COLUMN_KHOA + ", b." + COLUMN_AVATAR +
                       " FROM " + TABLE_USERS + " u" +
                       " INNER JOIN " + TABLE_BACSI + " b ON u." + COLUMN_PHONE + " = b." + COLUMN_PHONE +
                       " WHERE b." + COLUMN_KHOA + " = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{khoa})) {
            // Add logging
            Log.d("Database", "Found " + cursor.getCount() + " doctors");

            if (cursor.moveToFirst()) {
                do {
                    BacSi doctor = new BacSi(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOA)),
                        getStringOrNull(cursor, COLUMN_AVATAR)
                    );
                    doctors.add(doctor);

                    // Add logging
                    Log.d("Database", "Added doctor: " + doctor.getName());
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Database", "Error getting doctors: " + e.getMessage());
        }
        return doctors;
    }

    public List<DonThuoc> getDonThuocByPhone(String phone) {
        List<DonThuoc> prescriptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id", "patient_phone", "patient_name", "doctor_name", "date", "status"};
        String selection = "patient_phone = ?";
        String[] selectionArgs = {phone};
        String orderBy = "date DESC";

        try (Cursor cursor = db.query(TABLE_DONTHUOC, columns, selection, selectionArgs,
                null, null, orderBy)) {
            if (cursor.moveToFirst()) {
                do {
                    DonThuoc prescription = new DonThuoc(
                        cursor.getLong(getColumnIndexOrThrow(cursor, "id")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "patient_phone")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "patient_name")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "doctor_name")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "date"))
                    );
                    prescription.setStatus(cursor.getString(getColumnIndexOrThrow(cursor, "status")));
                    prescriptions.add(prescription);
                } while (cursor.moveToNext());
            }
        }
        return prescriptions;
    }

    public BenhNhan getBenhNhanInfo(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn join 2 bảng users và benhnhan
        String query = "SELECT u." + COLUMN_NAME + ", u." + COLUMN_PHONE + ", u." + COLUMN_EMAIL +
                       ", b." + COLUMN_BIRTHDAY + ", b." + COLUMN_GENDER + ", b." + COLUMN_ADDRESS +
                       ", b." + COLUMN_AVATAR +
                       " FROM " + TABLE_USERS + " u" +
                       " INNER JOIN " + TABLE_BENHNHAN + " b ON u." + COLUMN_PHONE + " = b." + COLUMN_PHONE +
                       " WHERE u." + COLUMN_PHONE + " = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{phone})) {
            if (cursor.moveToFirst()) {
                BenhNhan patient = new BenhNhan(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTHDAY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    getStringOrNull(cursor, COLUMN_AVATAR)
                );
                patient.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                return patient;
            }
        }
        return null;
    }

    public List<LichKham> getLichKhamHistory(String patientPhone) {
        List<LichKham> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id", "patient_phone", "patient_name", "doctor_phone", "doctor_name",
                "department", "room", "date", "time", "status"};
        String selection = "patient_phone = ?";
        String[] selectionArgs = {patientPhone};
        String orderBy = "date DESC, time DESC";  // Newest appointments first

        try (Cursor cursor = db.query(TABLE_LICHKHAM, columns, selection, selectionArgs,
                null, null, orderBy)) {
            if (cursor.moveToFirst()) {
                do {
                    LichKham appointment = new LichKham(
                        cursor.getInt(getColumnIndexOrThrow(cursor, "id")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "patient_name")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "patient_phone")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "doctor_name")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "doctor_phone")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "date")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "time")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "department")),
                        cursor.getString(getColumnIndexOrThrow(cursor, "room"))
                    );
                    appointment.setStatus(cursor.getString(getColumnIndexOrThrow(cursor, "status")));
                    appointments.add(appointment);
                } while (cursor.moveToNext());
            }
        }
        return appointments;
    }
    public boolean updateBenhNhanInfo(BenhNhan patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Cập nhật bảng users
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_NAME, patient.getName());
            userValues.put(COLUMN_EMAIL, patient.getEmail());

            int userResult = db.update(TABLE_USERS, userValues,
                COLUMN_PHONE + " = ?",
                new String[]{patient.getPhone()});

            // Cập nhật bảng benhnhan
            ContentValues patientValues = new ContentValues();
            patientValues.put(COLUMN_BIRTHDAY, patient.getBirthday());
            patientValues.put(COLUMN_GENDER, patient.getGender());
            patientValues.put(COLUMN_ADDRESS, patient.getAddress());
            patientValues.put(COLUMN_AVATAR, patient.getAvatar());

            int patientResult = db.update(TABLE_BENHNHAN, patientValues,
                COLUMN_PHONE + " = ?",
                new String[]{patient.getPhone()});

            // Kiểm tra kết quả và commit transaction
            if (userResult > 0 && patientResult > 0) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            db.endTransaction();
        }
    }
    public long addLichKham(LichKham lichKham) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_phone", lichKham.getPatientPhone());
        values.put("patient_name", lichKham.getPatientName());
        values.put("doctor_phone", lichKham.getDoctorPhone());
        values.put("doctor_name", lichKham.getDoctorName());
        values.put("department", lichKham.getDepartment());
        values.put("room", lichKham.getRoom());
        values.put(COLUMN_DATE, lichKham.getDate());
        values.put(COLUMN_TIME, lichKham.getTime());
        values.put(COLUMN_STATUS, "Chờ khám");

        return db.insert(TABLE_LICHKHAM, null, values);
    }
    public LichKham getLatestLichKham(String patientPhone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id", "patient_name", "patient_phone", "doctor_name", "doctor_phone",
                           "department", "room", "date", "time", "status"};
        String selection = "patient_phone = ?";
        String[] selectionArgs = {patientPhone};
        String orderBy = "date DESC, time DESC LIMIT 1";

        try (Cursor cursor = db.query(TABLE_LICHKHAM, columns, selection, selectionArgs,
                null, null, orderBy)) {
            if (cursor.moveToFirst()) {
                return new LichKham(
                    cursor.getInt(getColumnIndexOrThrow(cursor, "id")),
                    cursor.getString(getColumnIndexOrThrow(cursor, "patient_name")),
                    cursor.getString(getColumnIndexOrThrow(cursor, "patient_phone")),
                    cursor.getString(getColumnIndexOrThrow(cursor, "doctor_name")),
                    cursor.getString(getColumnIndexOrThrow(cursor, "doctor_phone")),
                    cursor.getString(getColumnIndexOrThrow(cursor, "date")),
                    cursor.getString(getColumnIndexOrThrow(cursor, "time")),
                    cursor.getString(getColumnIndexOrThrow(cursor, "department")),
                    cursor.getString(getColumnIndexOrThrow(cursor, "room"))
                );
            }
        }
        return null;
    }
    public long savePrescription(String patientPhone, String patientName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_phone", patientPhone);
        values.put("patient_name", patientName);
        values.put("doctor_name", "Bác sĩ"); // Có thể thay đổi theo context
        values.put(COLUMN_DATE, getCurrentDateTime());
        values.put(COLUMN_STATUS, "Đã kê");

        return db.insert(TABLE_DONTHUOC, null, values);
    }

    public boolean savePrescriptionDetail(long prescriptionId, int medicineId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("prescription_id", prescriptionId);
        values.put("medicine_id", medicineId);

        return db.insert(TABLE_CHITIET_DONTHUOC, null, values) != -1;
    }
    // Trong DatabaseHelper.java
    public List<Thuoc> getAllMedicines() {
        List<Thuoc> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME};
        String orderBy = COLUMN_NAME + " ASC";

        try (Cursor cursor = db.query(TABLE_THUOC, columns, null, null,
                null, null, orderBy)) {
            while (cursor.moveToNext()) {
                // Sử dụng getColumnIndexOrThrow thay vì getColumnIndex
                int id = cursor.getInt(getColumnIndexOrThrow(cursor, COLUMN_ID));
                String name = cursor.getString(getColumnIndexOrThrow(cursor, COLUMN_NAME));
                medicines.add(new Thuoc(id, name));
            }
        }
        return medicines;
    }

    // Thêm vào class DatabaseHelper
    public long addBacSi(String phone, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("name", name);
        return db.insert("BacSi", null, values);
    }

    public long addBenhNhan(String phone, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("name", name);
        return db.insert("BenhNhan", null, values);
    }

    public long addNhanVien(String phone, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("name", name);
        return db.insert("NhanVien", null, values);
    }
    // PrescriptionDetail (ChiTietDonThuoc) methods
    public long addPrescriptionDetail(long prescriptionId, long medicineId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("prescription_id", prescriptionId);
        values.put("medicine_id", medicineId);
        return db.insert(TABLE_CHITIET_DONTHUOC, null, values);
    }

    public List<String> getMedicinesByPrescriptionId(long prescriptionId) {
        List<String> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT t." + COLUMN_NAME +
                " FROM " + TABLE_THUOC + " t" +
                " INNER JOIN " + TABLE_CHITIET_DONTHUOC + " cd" +
                " ON t." + COLUMN_ID + " = cd.medicine_id" +
                " WHERE cd.prescription_id = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(prescriptionId)})) {
            if (cursor.moveToFirst()) {
                do {
                    medicines.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }
        return medicines;
    }
}