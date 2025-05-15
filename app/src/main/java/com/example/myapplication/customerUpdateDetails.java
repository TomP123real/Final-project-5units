package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class customerUpdateDetails extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private ProgressBar progressBar;

    private EditText editTextUsername;
    private EditText editTextLastname;
    private EditText editTextPhone;
    private EditText editTextAddress;
    private EditText editTextCity;
    private Button buttonUpdate;
    private Button buttonChoosePhoto;
    private ImageView imageView;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_update_details);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Client");
        storageRef = FirebaseStorage.getInstance().getReference().child("client");
        progressBar = findViewById(R.id.progressBar);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextLastname = findViewById(R.id.name);
        editTextPhone = findViewById(R.id.phone2);
        editTextAddress = findViewById(R.id.editTextAdress);
        editTextCity = findViewById(R.id.City);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonChoosePhoto = findViewById(R.id.buttonChoosePhoto);
        imageView = findViewById(R.id.imageView);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(customerUpdateDetails.this, "You need to log in before updating.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            loadUserData(currentUser.getUid());
        }

        buttonChoosePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        buttonUpdate.setOnClickListener(v -> updateUser());
    }

    private void loadUserData(String userId) {
        DatabaseReference userRef = mDatabase.child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    customer user = dataSnapshot.getValue(customer.class);
                    if (user != null) {
                        editTextUsername.setText(user.getName());
                        editTextLastname.setText(user.getLastname());
                        editTextPhone.setText(user.getPhone());
                        editTextAddress.setText(user.getAddress());
                        editTextCity.setText(user.getCity());

                        if (user.getPicture() != null && !user.getPicture().isEmpty()) {
                            Picasso.get().load(user.getPicture()).into(imageView);
                            imageView.setVisibility(View.VISIBLE); //
                        }
                    }
                } else {
                    Toast.makeText(customerUpdateDetails.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(customerUpdateDetails.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser() {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = mDatabase.child(userId);

            String updatedUsername = editTextUsername.getText().toString();
            String updatedLastname = editTextLastname.getText().toString();
            String updatedPhone = editTextPhone.getText().toString();
            String updatedAddress = editTextAddress.getText().toString();
            String updatedCity = editTextCity.getText().toString();

            userRef.child("name").setValue(updatedUsername);
            userRef.child("lastname").setValue(updatedLastname);
            userRef.child("phone").setValue(updatedPhone);
            userRef.child("address").setValue(updatedAddress);
            userRef.child("city").setValue(updatedCity);

            if (selectedImageUri != null) {
                uploadPhotoToStorage(userId);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(customerUpdateDetails.this, "Update successful", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadPhotoToStorage(String userId) {
        StorageReference photoRef = storageRef.child(userId);

        photoRef.putFile(selectedImageUri)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String photoUrl = uri.toString();
                            mDatabase.child(userId).child("picture").setValue(photoUrl);
                            Toast.makeText(customerUpdateDetails.this, "Update successful", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Toast.makeText(customerUpdateDetails.this, "Photo upload failed", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Picasso.get().load(selectedImageUri).into(imageView);
            imageView.setVisibility(View.VISIBLE);
        }
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


