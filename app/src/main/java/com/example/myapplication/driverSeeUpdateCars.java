package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

public class driverSeeUpdateCars extends AppCompatActivity {

    private RecyclerView recyclerView;
    private itemCar carAdapter;
    private List<cars> carList, filteredList;
    private DatabaseReference carsDatabase;
    private String uidDriver;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_see_update_cars);

        // אתחול רכיבים
        recyclerView = findViewById(R.id.recyclerViewCars);
        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carList = new ArrayList<>();
        filteredList = new ArrayList<>();
        carAdapter = new itemCar(filteredList, this::openEditDialog);
        recyclerView.setAdapter(carAdapter);

        // קבלת UID של הנהג
        uidDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
        carsDatabase = FirebaseDatabase.getInstance().getReference("Cars");

        // טעינת הנתונים
        loadDriverCars();

        // הוספת מאזין לחיפוש
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCars(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadDriverCars() {
        carsDatabase.orderByChild("uidDriver").equalTo(uidDriver)
                .addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        carList.clear();
                        for (DataSnapshot carSnapshot : snapshot.getChildren()) {
                            cars car = carSnapshot.getValue(cars.class);
                            carList.add(car);
                        }
                        filterCars(editTextSearch.getText().toString()); // סינון ראשוני
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("Error loading data: " + error.getMessage());
                    }
                });
    }

    private void filterCars(String query) {
        filteredList.clear();
        for (cars car : carList) {
            if (car.getModel().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(car);
            }
        }
        carAdapter.notifyDataSetChanged();
    }

    private void openEditDialog(cars car) {
        updateCarDetails dialog = new updateCarDetails(car);
        dialog.show(getSupportFragmentManager(), "EditCarDialog");
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
