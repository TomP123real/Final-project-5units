package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class customerSearchDriverCars extends AppCompatActivity {

    private EditText editTextSearchName;
    private Button buttonSearch;
    private LinearLayout linearLayoutDrivers;
    private List<customer> driverList;
    private DatabaseReference customerDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_search_driver_cars);

        // אתחול רכיבים
        editTextSearchName = findViewById(R.id.editTextSearchName);
        buttonSearch = findViewById(R.id.buttonSearch);
        linearLayoutDrivers = findViewById(R.id.linearLayoutResults);

        driverList = new ArrayList<>();
        customerDatabase = FirebaseDatabase.getInstance().getReference("Client");

        // כפתור חיפוש
        buttonSearch.setOnClickListener(v -> searchDriversByName());
    }

    private void searchDriversByName() {
        String query = editTextSearchName.getText().toString().trim();

        if (TextUtils.isEmpty(query)) {
            Toast.makeText(this, "Please enter part of a driver's name", Toast.LENGTH_SHORT).show();
            return;
        }

        // לוג: התחלת החיפוש
        Log.d("SearchQuery", "Starting search with query: " + query);

        customerDatabase.orderByChild("name")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("FirebaseResult", "Snapshot exists: " + snapshot.exists());
                        linearLayoutDrivers.removeAllViews(); // ניקוי תוצאות קודמות
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                customer driver = ds.getValue(customer.class);
                                if (driver != null) {
                                    driver.setUid(ds.getKey()); // ⬅ הוספת UID
                                    Log.d("DriverFound", "Driver name: " + driver.getName());
                                    addDriverToLayout(driver);
                                } else {
                                    Log.d("DriverNull", "Driver object is null for key: " + ds.getKey());
                                }
                            }
                        } else {
                            Log.d("SearchResult", "No drivers found for query: " + query);
                            Toast.makeText(customerSearchDriverCars.this, "No drivers found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "Error during search: " + error.getMessage());
                        Toast.makeText(customerSearchDriverCars.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addDriverToLayout(customer driver) {
        // יצירת TextView דינאמי עבור כל נהג
        TextView textViewDriver = new TextView(this);
        textViewDriver.setText(driver.getName() + " " + driver.getLastname());
        textViewDriver.setPadding(16, 16, 16, 16);
        textViewDriver.setTextSize(18);
        textViewDriver.setTextColor(android.graphics.Color.WHITE);
        textViewDriver.setOnClickListener(v -> openDriverDetails(driver));

        // הוספת TextView ל-LinearLayout
        linearLayoutDrivers.addView(textViewDriver);
    }

    private void openDriverDetails(customer driver) {
        // מעבר לעמוד הצגת הרכבים של הנהג
        Intent intent = new Intent(this, DriverCarListActivity.class);
        intent.putExtra("driverUid", driver.getUid()); // ⬅ שימוש ב-UID במקום email
        startActivity(intent);
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
