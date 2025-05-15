package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;

import java.util.Calendar;

public class addCar extends AppCompatActivity {

    private EditText editTextLicenseNumber, editTextModel, editTextDescription;
    private Button buttonPickDate, buttonChooseLogo, buttonAddCar;
    private TextView textViewDateCreated;
    private ImageView imageViewLogo;
    private ProgressBar progressBar;

    private Uri selectedImageUri;
    private DatabaseReference carsDatabase;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_add_car);

        carsDatabase = FirebaseDatabase.getInstance().getReference("Cars");
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        editTextLicenseNumber = findViewById(R.id.editTextLicenseNumber);
        editTextModel = findViewById(R.id.editTextModel);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonChooseLogo = findViewById(R.id.buttonChooseLogo);
        buttonAddCar = findViewById(R.id.buttonAddCar);
        textViewDateCreated = findViewById(R.id.textViewDateCreated);
        imageViewLogo = findViewById(R.id.imageViewLogo);
        progressBar = findViewById(R.id.progressBar);

        buttonPickDate.setOnClickListener(v -> openDatePicker());

        buttonChooseLogo.setOnClickListener(v -> openImagePicker());

        buttonAddCar.setOnClickListener(v -> saveCarToDatabase());
    }

    private void openDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            textViewDateCreated.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageViewLogo.setImageURI(selectedImageUri);
        }
    }

    private void saveCarToDatabase() {
        String licenseNumber = editTextLicenseNumber.getText().toString().trim();
        String model = editTextModel.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dateCreated = textViewDateCreated.getText().toString().trim();

        if (licenseNumber.isEmpty() || model.isEmpty() || description.isEmpty() || dateCreated.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select a logo", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String carId = carsDatabase.push().getKey();
        String uidDriver = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "Unknown";

        StorageReference logoRef = storageReference.child("car_logos/" + carId);
        logoRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    logoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String logoUrl = uri.toString();

                        cars newCar = new cars(carId, licenseNumber, model, description, dateCreated, logoUrl, uidDriver);

                        carsDatabase.child(carId).setValue(newCar)
                                .addOnCompleteListener(task -> {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, "Car added successfully!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(addCar.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(this, "Failed to add car", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }).addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homepage) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.update) {
            startActivity(new Intent(this, customerUpdateDetails.class));
            return true;
        } else if (item.getItemId() == R.id.searchDriverCars) {
            startActivity(new Intent(this, customerSearchDriverCars.class));
            return true;
        } else if (item.getItemId() == R.id.addCar) {
            startActivity(new Intent(this, addCar.class));
            return true;
        } else if (item.getItemId() == R.id.driverCars) {
            startActivity(new Intent(this, driverSeeUpdateCars.class));
            return true;
        } else if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, settings.class));
            return true;
        } else if (item.getItemId() == R.id.AvailableDriverTripsActivity) {
            startActivity(new Intent(this, AvailableTripsActivity.class));
            return true;
        } else if (item.getItemId() == R.id.CustomerTripsActivity) {
            startActivity(new Intent(this, CustomerTripsActivity.class));
            return true;
        } else if (item.getItemId() == R.id.addOrderFood) {
            startActivity(new Intent(this, AddOrderFoodActivity.class));
            return true;
        } else if (item.getItemId() == R.id.availableOrderFood) {
            startActivity(new Intent(this, AvailableOrderFoodActivity.class));
            return true;
        } else if (item.getItemId() == R.id.driverOrders) {
            startActivity(new Intent(this, DriverOrdersActivity.class));
            return true;
        } else if (item.getItemId() == R.id.viewClientOrders) {
            startActivity(new Intent(this, ClientOrdersActivity.class));
            return true;
        } else if (item.getItemId() == R.id.driver_history) {
            startActivity(new Intent(this, DriverHistoryActivity.class));
            return true;
        } else if (item.getItemId() == R.id.logout) {
            logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
        finish();
    }
}
