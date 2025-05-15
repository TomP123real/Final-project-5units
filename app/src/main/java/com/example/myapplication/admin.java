package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admin extends AppCompatActivity {
    private ListView userListView;
    private ArrayList<String> userList;
    private ArrayAdapter<String> adapter;
    private DatabaseReference mDatabase;
    private EditText searchName;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        userListView = findViewById(R.id.userListView);
        searchName = findViewById(R.id.searchName);
        searchButton = findViewById(R.id.searchButton);
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("Client");

        loadUserData();

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedUser = userList.get(position);
                String userId = selectedUser.split(" - ")[0];
                toggleDriverStatus(userId);
            }
        });

        searchButton.setOnClickListener(v -> searchUser());
    }

    private void loadUserData() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();
                    String isDriver = snapshot.child("isDriver").getValue(String.class);
                    userList.add(userId + " - Driver: " + isDriver);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(admin.this, "Error loading user data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void toggleDriverStatus(String userId) {
        String newStatus = "yes";
        mDatabase.child(userId).child("isDriver").setValue(newStatus)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendEmailNotification(userId);
                        Toast.makeText(admin.this, "User status updated to Driver", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(admin.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmailNotification(String userId) {
        mDatabase.child(userId).child("email").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String userEmail = task.getResult().getValue(String.class);
                if (userEmail != null) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{userEmail});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Driver Status Update");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Your request to become a driver has been approved.");

                    if (emailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(emailIntent);
                    } else {
                        Toast.makeText(admin.this, "No email app found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(admin.this, "User email not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(admin.this, "Error retrieving user email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchUser() {
        String searchUid = searchName.getText().toString().trim();
        if (searchUid.isEmpty()) {
            Toast.makeText(this, "Please enter a UID to search", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.child(searchUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String isDriver = dataSnapshot.child("isDriver").getValue(String.class);
                    userList.clear();
                    userList.add(searchUid + " - " + username + " - Driver: " + isDriver);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(admin.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(admin.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
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
