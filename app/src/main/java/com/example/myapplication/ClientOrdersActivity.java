package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

public class ClientOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderFoodAdapter adapter;
    private List<OrderFood> myOrders;
    private DatabaseReference databaseReference;
    private String currentUid;
    private EditText editTextSearchClientOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_orders);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        editTextSearchClientOrders = findViewById(R.id.editTextSearchClientOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("OrderFood");

        myOrders = new ArrayList<>();
        adapter = new OrderFoodAdapter(myOrders, order -> showCloseDialog(order));
        recyclerView.setAdapter(adapter);

        loadMyOrders("");

        editTextSearchClientOrders.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadMyOrders(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadMyOrders(String query) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myOrders.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    OrderFood order = ds.getValue(OrderFood.class);
                    if (order != null &&
                            currentUid.equals(order.getUidClient()) &&
                            (query.isEmpty() || order.getRestaurantName().toLowerCase().contains(query.toLowerCase()))) {
                        myOrders.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClientOrdersActivity.this, "Error loading orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCloseDialog(OrderFood order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("הזן שעת הגעה (לדוג' 18:30)");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_DATETIME);
        builder.setView(input);

        builder.setPositiveButton("סגור הזמנה", (dialog, which) -> {
            String hour = input.getText().toString();
            if (!hour.isEmpty()) {
                order.setHourArrived(hour);
                order.setStatus("CLOSE");
                databaseReference.child(order.getUid()).setValue(order)
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "ההזמנה נסגרה", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "שגיאה: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        builder.setNegativeButton("ביטול", (dialog, which) -> dialog.cancel());
        builder.show();
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
