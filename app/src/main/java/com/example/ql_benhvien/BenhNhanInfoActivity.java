package com.example.ql_benhvien;

   import android.app.AlertDialog;
   import android.content.Intent;
   import android.net.Uri;
   import android.os.Bundle;
   import android.provider.MediaStore;
   import android.view.View;
   import android.widget.Button;
   import android.widget.CheckBox;
   import android.widget.EditText;
   import android.widget.ImageButton;
   import android.widget.ImageView;
   import android.widget.Toast;
   import androidx.annotation.Nullable;
   import androidx.appcompat.app.AppCompatActivity;
   import com.bumptech.glide.Glide;

   public class BenhNhanInfoActivity extends AppCompatActivity {
       private ImageView profileImage;
       private EditText edtName, edtPhone, edtEmail, edtBirthday, edtAddress;
       private CheckBox checkBox, checkBox2;
       private Button btnLogout;
       private ImageButton btnEdit;
       private DatabaseHelper databaseHelper;
       private String phoneNumber;
       private static final int PICK_IMAGE = 1;
       private boolean isEditing = false;
       private String currentImagePath;

       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.bn_info);

           databaseHelper = new DatabaseHelper(this);
           phoneNumber = getIntent().getStringExtra("phone");

           initViews();
           loadPatientInfo();
           setupClickListeners();
       }

       private void initViews() {
           profileImage = findViewById(R.id.profileImage);
           edtName = findViewById(R.id.edtName);
           edtPhone = findViewById(R.id.edtPhone);
           edtEmail = findViewById(R.id.txtEmail);
           edtBirthday = findViewById(R.id.txtBirthday);
           edtAddress = findViewById(R.id.txtAddress);
           checkBox = findViewById(R.id.checkBox);
           checkBox2 = findViewById(R.id.checkBox2);
           btnEdit = findViewById(R.id.btnEdit);
           btnLogout = findViewById(R.id.btnLogout);

           setEditableState(false);
       }

       private void loadPatientInfo() {
           BenhNhan patient = databaseHelper.getBenhNhanInfo(phoneNumber);
           if (patient != null) {
               edtName.setText(patient.getName());
               edtPhone.setText(patient.getPhone());
               edtEmail.setText(patient.getEmail());
               edtBirthday.setText(patient.getBirthday());
               edtAddress.setText(patient.getAddress());

               if ("Nam".equals(patient.getGender())) {
                   checkBox.setChecked(true);
                   checkBox2.setChecked(false);
               } else {
                   checkBox.setChecked(false);
                   checkBox2.setChecked(true);
               }

               if (patient.getAvatar() != null && !patient.getAvatar().isEmpty()) {
                   currentImagePath = patient.getAvatar();
                   Glide.with(this)
                       .load(patient.getAvatar())
                       .circleCrop()
                       .into(profileImage);
               }
           }
       }

       private void setupClickListeners() {
           profileImage.setOnClickListener(v -> {
               if (isEditing) {
                   Intent intent = new Intent(Intent.ACTION_PICK,
                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                   startActivityForResult(intent, PICK_IMAGE);
               }
           });

           btnEdit.setOnClickListener(v -> {
               if (!isEditing) {
                   isEditing = true;
                   setEditableState(true);
               } else {
                   saveInfo();
               }
           });

           btnLogout.setOnClickListener(v -> showLogoutConfirmation());

           checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
               if (isChecked) {
                   checkBox2.setChecked(false);
               }
           });

           checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
               if (isChecked) {
                   checkBox.setChecked(false);
               }
           });
       }

       private void setEditableState(boolean editable) {
           edtName.setEnabled(editable);
           edtEmail.setEnabled(editable);
           edtBirthday.setEnabled(editable);
           edtAddress.setEnabled(editable);
           checkBox.setEnabled(editable);
           checkBox2.setEnabled(editable);
           edtPhone.setEnabled(false);
       }

       private void saveInfo() {
           String name = edtName.getText().toString().trim();
           String email = edtEmail.getText().toString().trim();
           String birthday = edtBirthday.getText().toString().trim();
           String address = edtAddress.getText().toString().trim();
           String gender = checkBox.isChecked() ? "Nam" : "Nữ";


           if (name.isEmpty() || email.isEmpty() || birthday.isEmpty() || address.isEmpty()) {
               Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
               return;
           }

           if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
               Toast.makeText(this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
               return;
           }

           BenhNhan patient = new BenhNhan(phoneNumber, name, birthday, gender, address, currentImagePath);
           patient.setEmail(email);

           boolean success = databaseHelper.updateBenhNhanInfo(patient);

           if (success) {
               Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
               isEditing = false;
               setEditableState(false);
//               btnEdit.setImageDrawable(getResources().getDrawable(R.drawable.acc));
           } else {
               Toast.makeText(this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
           }
       }

       private void showLogoutConfirmation() {
           new AlertDialog.Builder(this)
               .setTitle("Xác nhận đăng xuất")
               .setMessage("Bạn có chắc chắn muốn đăng xuất?")
               .setPositiveButton("OK", (dialog, which) -> {
                   getSharedPreferences("loginPrefs", MODE_PRIVATE)
                       .edit()
                       .clear()
                       .apply();

                   Intent intent = new Intent(BenhNhanInfoActivity.this, LoginActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(intent);
                   finish();
               })
               .setNegativeButton("Ở lại", null)
               .show();
       }

       @Override
       protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
           super.onActivityResult(requestCode, resultCode, data);
           if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
               Uri selectedImage = data.getData();
               currentImagePath = selectedImage.toString();
               Glide.with(this)
                   .load(selectedImage)
                   .circleCrop()
                   .into(profileImage);
           }
       }
   }