package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class AddTripFragment extends Fragment {

    private EditText editTextPickup, editTextDropoff;
    private TextView textViewDate, textViewStartTime;
    private Button buttonPickDate, buttonPickStartTime, buttonSubmitTrip;
    private DatabaseReference tripsDatabase;
    private FirebaseAuth mAuth;
    private String currentUserID;

    public AddTripFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);

        mAuth = FirebaseAuth.getInstance();
        tripsDatabase = FirebaseDatabase.getInstance().getReference("Trips");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
        } else {
            Toast.makeText(getContext(), "User not logged in!", Toast.LENGTH_SHORT).show();
        }

        editTextPickup = view.findViewById(R.id.editTextPickup);
        editTextDropoff = view.findViewById(R.id.editTextDropoff);
        textViewDate = view.findViewById(R.id.textViewDate);
        textViewStartTime = view.findViewById(R.id.textViewStartTime);
        buttonPickDate = view.findViewById(R.id.buttonSelectDate);
        buttonPickStartTime = view.findViewById(R.id.buttonSelectStartTime);
        buttonSubmitTrip = view.findViewById(R.id.buttonSubmitTrip);

        buttonPickDate.setOnClickListener(v -> openDatePicker());
        buttonPickStartTime.setOnClickListener(v -> openTimePicker(textViewStartTime));
        buttonSubmitTrip.setOnClickListener(v -> submitTripRequest());

        return view;
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            textViewDate.setText(selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openTimePicker(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            textView.setText(selectedTime);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void submitTripRequest() {
        String pickup = editTextPickup.getText().toString().trim();
        String dropoff = editTextDropoff.getText().toString().trim();
        String date = textViewDate.getText().toString().trim();
        String startTime = textViewStartTime.getText().toString().trim();

        if (TextUtils.isEmpty(pickup) || TextUtils.isEmpty(dropoff)
                || TextUtils.isEmpty(date) || TextUtils.isEmpty(startTime)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String tripId = tripsDatabase.push().getKey();
        if (tripId == null) {
            Toast.makeText(getContext(), "Failed to create trip ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        Trips trip = new Trips("pending", tripId, "", currentUserID,
                pickup, dropoff, date, startTime, "", 0, "");

        tripsDatabase.child(tripId).setValue(trip)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Trip request submitted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(requireContext(), CustomerTripsActivity.class));
                    } else {
                        Toast.makeText(getContext(), "Failed to submit request.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
