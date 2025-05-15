package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class CustomerTripsActivity extends AppCompatActivity
        implements CustomerTripAdapter.OnTripActionListener {

    private RecyclerView recyclerView;
    private CustomerTripAdapter customerTripAdapter;
    private List<Trips> tripList;
    private DatabaseReference tripsDatabase;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_trips);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tripList = new ArrayList<>();

        // מעבירים this כ-Listener
        customerTripAdapter = new CustomerTripAdapter(tripList, this);
        recyclerView.setAdapter(customerTripAdapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserID = (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getUid() : null;

        if (currentUserID == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tripsDatabase = FirebaseDatabase.getInstance().getReference("Trips");
        loadCustomerTrips();
    }

    private void loadCustomerTrips() {
        tripsDatabase.orderByChild("uidUser").equalTo(currentUserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tripList.clear();
                        for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                            Trips trip = tripSnapshot.getValue(Trips.class);
                            if (trip != null) {
                                tripList.add(trip);
                            }
                        }
                        customerTripAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CustomerTripsActivity.this,
                                "Failed to load trips.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * מתודה שמופעלת כשהלקוח לוחץ על כפתור "Edit Trip"
     */
    @Override
    public void onEditTrip(Trips trip) {
        TripEditDialog dialog = new TripEditDialog(trip, tripsDatabase);
        dialog.show(getSupportFragmentManager(), "EditTripDialog");
    }

    /**
     * מתודה שמופעלת כשהלקוח לוחץ על "Cancel Trip"
     */
    @Override
    public void onCancelTrip(Trips trip) {
        if ("pending".equals(trip.getStatus())) {
            tripsDatabase.child(trip.getUid()).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Trip canceled successfully.", Toast.LENGTH_SHORT).show();
                            tripList.remove(trip);
                            customerTripAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Failed to cancel trip.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Trip cannot be canceled, already accepted by driver.", Toast.LENGTH_SHORT).show();
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
