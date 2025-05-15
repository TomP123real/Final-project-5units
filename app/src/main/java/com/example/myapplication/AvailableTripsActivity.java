package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AvailableTripsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private List<Trips> tripList;
    private DatabaseReference tripsDatabase;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_driver_trips);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tripList = new ArrayList<>();
        tripAdapter = new TripAdapter(tripList, this::acceptTrip);
        recyclerView.setAdapter(tripAdapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserID = (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getUid() : null;

        if (currentUserID == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tripsDatabase = FirebaseDatabase.getInstance().getReference("Trips");
        loadAvailableTrips();
    }

    private void loadAvailableTrips() {
        tripsDatabase.orderByChild("status").equalTo("pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tripList.clear();
                for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                    Trips trip = tripSnapshot.getValue(Trips.class);
                    if (trip != null) {
                        tripList.add(trip);
                    }
                }
                tripAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AvailableTripsActivity.this, "Failed to load trips.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void acceptTrip(Trips trip) {
        if (trip.getUid() == null) {
            Toast.makeText(this, "Invalid trip data.", Toast.LENGTH_SHORT).show();
            return;
        }
        tripsDatabase.child(trip.getUid()).child("uidDriver").setValue(currentUserID);
        tripsDatabase.child(trip.getUid()).child("status").setValue("accepted")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Trip accepted!", Toast.LENGTH_SHORT).show();
                        tripList.remove(trip);
                        tripAdapter.notifyDataSetChanged();

                        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("Client").child(trip.getUidUser());
                        userDatabase.child("phone").get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful() && task1.getResult() != null) {
                                String phoneNumber = task1.getResult().getValue(String.class);
                                if (phoneNumber != null) {
                                    sendSMS(phoneNumber, "Your trip has been accepted by a driver!");
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, "Failed to accept trip.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendSMS(String phoneNumber, String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS sent successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
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
