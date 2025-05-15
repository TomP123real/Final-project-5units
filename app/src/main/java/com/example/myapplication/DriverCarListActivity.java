package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DriverCarListActivity extends AppCompatActivity {

    private static final String TAG = "DriverCarListActivity";

    private RecyclerView recyclerViewCars;
    private itemCar carAdapter;
    private List<cars> carList;
    private DatabaseReference carsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_car_list);

        // אתחול רכיבים
        recyclerViewCars = findViewById(R.id.recyclerViewDriverCars);
        recyclerViewCars.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();
        carAdapter = new itemCar(carList, car -> {
            // הודעה על לחיצה על רכב
            Toast.makeText(this, "Car Model: " + car.getModel(), Toast.LENGTH_SHORT).show();
        });
        recyclerViewCars.setAdapter(carAdapter);

        // משיכת UID מה-Intent
        String driverUid = getIntent().getStringExtra("driverUid");
        Log.d(TAG, "Driver UID from Intent: " + driverUid);

        if (driverUid != null) {
            // אתחול מסד הנתונים
            carsDatabase = FirebaseDatabase.getInstance().getReference("Cars");

            // טעינת הרכבים של הנהג
            loadDriverCars(driverUid);
        } else {
            Toast.makeText(this, "Driver UID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDriverCars(String driverUid) {
        Log.d(TAG, "Loading cars for driver UID: " + driverUid);
        carsDatabase.orderByChild("uidDriver").equalTo(driverUid)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        carList.clear();
                        for (DataSnapshot carSnapshot : snapshot.getChildren()) {
                            cars car = carSnapshot.getValue(cars.class);
                            if (car != null) {
                                carList.add(car);
                            }
                        }
                        carAdapter.notifyDataSetChanged();

                        if (carList.isEmpty()) {
                            Log.d(TAG, "No cars found for driver UID: " + driverUid);
                            Toast.makeText(DriverCarListActivity.this, "No cars found for this driver", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to fetch cars", error.toException());
                        Toast.makeText(DriverCarListActivity.this, "Failed to fetch cars", Toast.LENGTH_SHORT).show();
                    }
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
