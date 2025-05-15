package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class driverDetails extends AppCompatActivity {

    private TextView textViewDriverName, textViewDriverEmail, textViewAdditionalInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_details);

        // אתחול רכיבי התצוגה
        textViewDriverName = findViewById(R.id.textViewDriverName);
        textViewDriverEmail = findViewById(R.id.textViewDriverEmail);
        textViewAdditionalInfo = findViewById(R.id.textViewAdditionalInfo);

        // קבלת הנתונים מה-Intent
        String driverName = getIntent().getStringExtra("driverName");
        String driverEmail = getIntent().getStringExtra("driverEmail");
        String additionalInfo = getIntent().getStringExtra("additionalInfo");

        // הצגת הנתונים
        textViewDriverName.setText("Name: " + driverName);
        textViewDriverEmail.setText("Email: " + driverEmail);
        textViewAdditionalInfo.setText("Additional Info: " + additionalInfo);
    }
}
