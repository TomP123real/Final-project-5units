package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddOrderFoodActivity extends AppCompatActivity {

    private EditText etRestaurantName, etRestaurantAddress, etClientAddress, etDate, etHourArrived;
    private Button btnSubmitOrder;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_orderfood);

        etRestaurantName = findViewById(R.id.etRestaurantName);
        etRestaurantAddress = findViewById(R.id.etRestaurantAddress);
        etClientAddress = findViewById(R.id.etClientAddress);
        etDate = findViewById(R.id.etDate);
        etHourArrived = findViewById(R.id.etHourArrived);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("OrderFood");

        btnSubmitOrder.setOnClickListener(v -> submitOrder());
    }

    private void submitOrder() {
        String restaurantName = etRestaurantName.getText().toString().trim();
        String restaurantAddress = etRestaurantAddress.getText().toString().trim();
        String clientAddress = etClientAddress.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String hourArrived = etHourArrived.getText().toString().trim();

        if (TextUtils.isEmpty(restaurantName) || TextUtils.isEmpty(restaurantAddress)
                || TextUtils.isEmpty(clientAddress) || TextUtils.isEmpty(date)
                || TextUtils.isEmpty(hourArrived)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = databaseReference.push().getKey();
        String uidClient = mAuth.getCurrentUser().getUid();

        OrderFood order = new OrderFood(
                uid,
                "",
                uidClient,
                restaurantName,
                restaurantAddress,
                clientAddress,
                date,
                hourArrived,
                "OPEN"
        );

        if (uid != null) {
            databaseReference.child(uid).setValue(order).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Order submitted!", Toast.LENGTH_SHORT).show();
                finish();
            }).addOnFailureListener(e ->
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
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
