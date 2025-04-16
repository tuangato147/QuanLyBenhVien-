package com.example.ql_benhvien;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ql_benhvien.LichKham;

import java.util.ArrayList;
import java.util.List;



public class MyDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "hospital.db";
    public static final int DATABASE_VERSION = 3; // Tăng version để onUpgrade chạy lại

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng user
        String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "phone TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL)";
        db.execSQL(createUserTable);

        // Thêm tài khoản mẫu
        db.execSQL("INSERT INTO users (phone, password, role) VALUES ('0123456789', '123', 'Bác sĩ')");
        db.execSQL("INSERT INTO users (phone, password, role) VALUES ('0987654321', '456', 'Bệnh nhân')");
        db.execSQL("INSERT INTO users (phone, password, role) VALUES ('0111222333', '789', 'Nhân viên')");

        // Bảng lịch khám
        db.execSQL("CREATE TABLE schedules (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_name TEXT, " +
                "doctor_name TEXT, " +
                "room TEXT, " +              // thêm
                "reason TEXT, " +
                "date TEXT, " +
                "time TEXT, " +
                "status TEXT)");
        String createBenhNhanTable = "CREATE TABLE IF NOT EXISTS benhnhan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "gender TEXT," +
                "birthday TEXT," +
                "address TEXT," +
                "phone TEXT UNIQUE," +
                "email TEXT," +
                "emergency_contact TEXT," +
                "chronic_diseases TEXT," +
                "allergies TEXT," +
                "surgeries TEXT" +
                ");";
        db.execSQL(createBenhNhanTable);

        // Bảng đơn thuốc
        db.execSQL("CREATE TABLE prescriptions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER, " +
                "doctor_id INTEGER, " +
                "medicine_name TEXT, " +
                "dosage TEXT, " +
                "note TEXT)");

        // Bảng hồ sơ bệnh án
        db.execSQL("CREATE TABLE medical_records (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER, " +
                "symptoms TEXT, " +
                "diagnosis TEXT, " +
                "treatment TEXT, " +
                "date TEXT)");

        // Bảng thuốc trong kho
        db.execSQL("CREATE TABLE medicines (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "quantity INTEGER, " +
                "expiry_date TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS donthuoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ten TEXT," +
                "sdt TEXT," +
                "ngay TEXT," +
                "chandoan TEXT)");

        // Bảng chi tiết thuốc trong đơn
        db.execSQL("CREATE TABLE IF NOT EXISTS thuoc_don (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "don_id INTEGER," +
                "ten TEXT," +
                "lieu_dung TEXT," +
                "so_ngay TEXT," +
                "cach_dung TEXT," +
                "FOREIGN KEY(don_id) REFERENCES donthuoc(id))");


        // Bảng thuốc đơn giản (dùng cho màn thêm/xoá thuốc)
        db.execSQL("CREATE TABLE thuoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten TEXT, " +
                "soLuong INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS donthuoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tenBenhNhan TEXT," +
                "sdt TEXT," +
                "ngayKe TEXT," +
                "chanDoan TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS chitiet_donthuoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_donthuoc INTEGER," +
                "tenThuoc TEXT," +
                "lieuDung TEXT," +
                "soNgay TEXT," +
                "cachDung TEXT," +
                "FOREIGN KEY(id_donthuoc) REFERENCES donthuoc(id))");

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS schedules");
        db.execSQL("DROP TABLE IF EXISTS prescriptions");
        db.execSQL("DROP TABLE IF EXISTS medical_records");
        db.execSQL("DROP TABLE IF EXISTS medicines");
        db.execSQL("DROP TABLE IF EXISTS thuoc");
        db.execSQL("DROP TABLE IF EXISTS benhnhan");
        db.execSQL("DROP TABLE IF EXISTS donthuoc");
        db.execSQL("DROP TABLE IF EXISTS thuoc_don");
        db.execSQL("DROP TABLE IF EXISTS chitiet_donthuoc");
        onCreate(db);
    }

    // ==== Xử lý đăng nhập ====
    public boolean checkLogin(String phone, String pass, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE phone = ? AND password = ? AND role = ?",
                new String[]{phone, pass, role});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // Lấy danh sách lịch khám hôm nay của bác sĩ
    public List<LichSuKham> getLichKhamHomNayChoBacSi(String doctorName, String currentDate) {
        List<LichSuKham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM schedules WHERE doctor_name = ? AND date = ?",
                new String[]{doctorName, currentDate});

        if (cursor.moveToFirst()) {
            do {
                String patientName = cursor.getString(cursor.getColumnIndex("patient_name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                list.add(new LichSuKham(patientName, currentDate, time, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // ==== Danh sách thuốc ====

    public void insertThuoc(Thuoc thuoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ten", thuoc.getTen());
        values.put("soLuong", thuoc.getSoLuong());
        db.insert("thuoc", null, values);
        db.close();
    }



    public void updateThuoc(Thuoc thuoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ten", thuoc.getTen());
        values.put("soLuong", thuoc.getSoLuong());
        db.update("thuoc", values, "id = ?", new String[]{String.valueOf(thuoc.getId())});
        db.close();
    }

    public void deleteThuoc(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("thuoc", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Thuoc> getAllThuoc() {
        List<Thuoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM thuoc", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String ten = cursor.getString(1);
                int soLuong = cursor.getInt(2);
                list.add(new Thuoc(id, ten, soLuong));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ==== Lịch khám ====
    public List<String> getAllAppointments() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM schedules", null);
        if (cursor.moveToFirst()) {
            do {
                String info = cursor.getString(cursor.getColumnIndex("date")) + " - " +
                        cursor.getString(cursor.getColumnIndex("patient_name"));
                list.add(info);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public List<LichSuKham> getLichSuKhamByPatient(String patientName) {
        List<LichSuKham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM schedules WHERE patient_name = ?", new String[]{patientName});

        if (cursor.moveToFirst()) {
            do {
                String doctor = cursor.getString(cursor.getColumnIndex("doctor_name"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                list.add(new LichSuKham(doctor, date, time, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }



    // Thêm lịch khám
    public boolean insertSchedule(String patientName, String doctorName, String date, String time, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_name", patientName);
        values.put("doctor_name", doctorName);
        values.put("date", date);
        values.put("time", time);
        values.put("status", status);
        long result = db.insert("schedules", null, values);
        return result != -1;
    }

    // Cập nhật lịch khám
    public boolean updateScheduleStatus(int scheduleId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        int result = db.update("schedules", values, "id = ?", new String[]{String.valueOf(scheduleId)});
        return result > 0;
    }


    // Thêm đơn thuốc
    public boolean insertPrescription(int patientId, int doctorId, String medicineName, String dosage, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_id", patientId);
        values.put("doctor_id", doctorId);
        values.put("medicine_name", medicineName);
        values.put("dosage", dosage);
        values.put("note", note);
        long result = db.insert("prescriptions", null, values);
        return result != -1;
    }

    // ==== Hồ sơ bệnh án ====
// Lấy hồ sơ bệnh án của bệnh nhân

    // Thêm hồ sơ bệnh án
    public boolean insertMedicalRecord(int patientId, String symptoms, String diagnosis, String treatment, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_id", patientId);
        values.put("symptoms", symptoms);
        values.put("diagnosis", diagnosis);
        values.put("treatment", treatment);
        values.put("date", date);
        long result = db.insert("medical_records", null, values);
        return result != -1;
    }

    // ==== Thuốc trong kho ====
// Lấy danh sách thuốc trong kho


    // Thêm thuốc vào kho
    public boolean insertMedicine(String name, int quantity, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("expiry_date", expiryDate);
        long result = db.insert("medicines", null, values);
        return result != -1;
    }

    // Cập nhật số lượng thuốc
    public boolean updateMedicineQuantity(int medicineId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        int result = db.update("medicines", values, "id = ?", new String[]{String.valueOf(medicineId)});
        return result > 0;
    }
    public boolean updatePatientInfo(String phone, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Cập nhật thông tin bệnh nhân theo số điện thoại
        int rowsAffected = db.update("benhnhan", values, "phone = ?", new String[]{phone});
        return rowsAffected > 0; // Trả về true nếu có ít nhất một dòng được cập nhật
    }

    public BenhNhan getBenhNhanByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        BenhNhan benhNhan = null;

        Cursor cursor = db.query("BenhNhan", null, "phone = ?",
                new String[]{phone}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            String birthday = cursor.getString(cursor.getColumnIndexOrThrow("birthday"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String emergencyContact = cursor.getString(cursor.getColumnIndexOrThrow("emergency_contact"));
            String chronicDiseases = cursor.getString(cursor.getColumnIndexOrThrow("chronic_diseases"));
            String allergies = cursor.getString(cursor.getColumnIndexOrThrow("allergies"));
            String surgeries = cursor.getString(cursor.getColumnIndexOrThrow("surgeries"));

            benhNhan = new BenhNhan(name, gender, birthday, address, phone, email,
                    emergencyContact, chronicDiseases, allergies, surgeries);
            cursor.close();
        }

        db.close();
        return benhNhan;
    }


    public List<LichKham> getAppointmentListForDoctor() {
        List<LichKham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM schedules", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String patientName = cursor.getString(cursor.getColumnIndex("patient_name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String room = cursor.getString(cursor.getColumnIndex("room"));
                String reason = cursor.getString(cursor.getColumnIndex("reason"));
                list.add(new LichKham(id, patientName, time, room, reason));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Kiểm tra bệnh nhân tồn tại
// Kiểm tra bệnh nhân tồn tại
    public boolean checkBenhNhanTonTai(String ten, String sdt) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM benhnhan WHERE name = ? AND phone = ?", new String[]{ten, sdt});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Thêm đơn thuốc
    public long insertDonThuoc(String ten, String sdt, String ngayKe, String chandoan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ten", ten);
        values.put("sdt", sdt);
        values.put("ngay", ngayKe);
        values.put("chandoan", chandoan);
        return db.insert("donthuoc", null, values);
    }

    // Thêm chi tiết thuốc kê đơn
    public void insertChiTietDonThuoc(long donId, List<DonThuocItem> ds) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (DonThuocItem item : ds) {
            ContentValues values = new ContentValues();
            values.put("don_id", donId);
            values.put("ten", item.getTenThuoc());
            values.put("lieu_dung", item.getLieuDung());
            values.put("so_ngay", item.getSoNgay());
            values.put("cach_dung", item.getCachDung());
            db.insert("thuoc_don", null, values);
        }
    }

    // Lấy thuốc theo bệnh nhân
    public List<DonThuocItem> getThuocTheoBenhNhan(String sdt) {
        List<DonThuocItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT t.ten, t.lieu_dung, t.so_ngay, t.cach_dung FROM donthuoc d " +
                        "JOIN thuoc_don t ON d.id = t.don_id WHERE d.sdt = ?", new String[]{sdt});

        while (cursor.moveToNext()) {
            String ten = cursor.getString(0);
            String lieu = cursor.getString(1);
            String soNgay = cursor.getString(2);
            String cach = cursor.getString(3);
            list.add(new DonThuocItem(ten, lieu, soNgay, cach));
        }

        cursor.close();
        return list;
    }

    public List<String> getAllThuocNames() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ten FROM thuoc", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


}