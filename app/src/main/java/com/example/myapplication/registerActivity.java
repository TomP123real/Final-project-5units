package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class registerActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;

    private EditText etName, etLastName, etPassword, etAddress, etPhone, etEmail;
    private Spinner spinnerCity;
    private RadioGroup rgGender;
    private DatePicker dpBirthDate;
    private ImageView imageProfile;
    private Uri selectedImageUri;
    private Button btnChooseImage, btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Client");
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_images");

        // התחברות לרכיבי ממשק
        etName = findViewById(R.id.Name);
        etLastName = findViewById(R.id.LastName);
        etPassword = findViewById(R.id.Password);
        etAddress = findViewById(R.id.Address);
        spinnerCity = findViewById(R.id.spinnerCity);
        etPhone = findViewById(R.id.Phone);
        etEmail = findViewById(R.id.Email);
        rgGender = findViewById(R.id.Gender);
        dpBirthDate = findViewById(R.id.BirthDate);
        imageProfile = findViewById(R.id.ProfileImage);
        btnChooseImage = findViewById(R.id.ChooseImage);
        btnRegister = findViewById(R.id.Register);
        tvLogin = findViewById(R.id.Login);

        // מילוי Spinner של ערים
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(registerActivity.this, loginActivity.class);
            startActivity(intent);
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Picasso.get().load(selectedImageUri).into(imageProfile);
        }
    }

    private void registerUser() {
        final String name = etName.getText().toString().trim();
        final String lastName = etLastName.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String address = etAddress.getText().toString().trim();
        final String city = spinnerCity.getSelectedItem().toString();
        final String phone = etPhone.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String isDriver = "no";

        int genderId = rgGender.getCheckedRadioButtonId();
        String gender = "";
        if (genderId != -1) {
            RadioButton selectedGender = findViewById(genderId);
            gender = selectedGender.getText().toString();
        }
        final String finalGender = gender;

        int day = dpBirthDate.getDayOfMonth();
        int month = dpBirthDate.getMonth();
        int year = dpBirthDate.getYear();
        final String birthDate = day + "/" + (month + 1) + "/" + year;

        if (name.isEmpty() || lastName.isEmpty() || password.isEmpty() || address.isEmpty()
                || city.isEmpty() || phone.isEmpty() || email.isEmpty() || finalGender.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            final String uid = user.getUid();

                            if (selectedImageUri != null) {
                                uploadImageAndSave(uid, email, name, lastName, birthDate, phone, address, city, finalGender, isDriver);
                            } else {
                                saveToDatabase(uid, email, name, lastName, birthDate, phone, address, city, finalGender, isDriver, null);
                                goToMain();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadImageAndSave(final String uid, final String email, final String name, final String lastName,
                                    final String birthDate, final String phone, final String address,
                                    final String city, final String gender, final String isDriver) {
        StorageReference ref = storageReference.child(uid + ".jpg");
        ref.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot ->
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    final String imageUrl = uri.toString();
                    saveToDatabase(uid, email, name, lastName, birthDate, phone, address, city, gender, isDriver, imageUrl);
                    goToMain();
                }).addOnFailureListener(e -> {
                    saveToDatabase(uid, email, name, lastName, birthDate, phone, address, city, gender, isDriver, null);
                    goToMain();
                })
        );
    }

    private void saveToDatabase(String uid, String email, String name, String lastName,
                                String birthDate, String phone, String address,
                                String city, String gender, String isDriver, String imageUrl) {
        customer user = new customer(email, name, lastName, birthDate, phone, address, city, gender, imageUrl, isDriver);
        mDatabase.child(uid).setValue(user);
    }

    private void goToMain() {
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
