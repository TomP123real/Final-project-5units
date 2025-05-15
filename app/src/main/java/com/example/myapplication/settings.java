package com.example.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class settings extends AppCompatActivity {
    private ImageView img_a, img_w;
    private TextView txtAir, txtWifi, txtTimer;

    private settingFlightMode showAirPlane = null;
    private settingWifi showWifi = null;

    private CountDownTimer countDownTimer;
    private long totalSeconds; // סך השניות שהאפליקציה הייתה פתוחה
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        initViews();

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        totalSeconds = sharedPreferences.getLong("TotalTime", 0);

        // הפעלת הטיימר
        startTimer();
    }

    private void initViews() {
        txtAir = findViewById(R.id.txtAir);
        img_a = findViewById(R.id.img_a);
        txtWifi = findViewById(R.id.txtWifi);
        img_w = findViewById(R.id.img_w);
        txtTimer = findViewById(R.id.txtTimer);

        // Initialize BroadcastReceiver handlers
        showAirPlane = new settingFlightMode(txtAir, img_a);
        showWifi = new settingWifi(txtWifi, img_w);
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                totalSeconds++;

                runOnUiThread(() -> updateTimerDisplay());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("TotalTime", totalSeconds);
                editor.apply();
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    private void updateTimerDisplay() {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        txtTimer.setText("Total time in app: " + timeFormatted);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (countDownTimer == null) {
            startTimer();
        }

        IntentFilter wifiFilter = new IntentFilter();
        wifiFilter.addAction("android.net.wifi.STATE_CHANGE");
        wifiFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        registerReceiver(showAirPlane, new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
        registerReceiver(showWifi, wifiFilter);
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
