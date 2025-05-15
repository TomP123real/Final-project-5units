package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;

public class TripEditDialog extends DialogFragment {
    private Trips trip;
    private DatabaseReference tripsDatabase;

    private RatingBar ratingBar;
    private EditText editTextComments, editTextHourEnd;
    private Button buttonSave;

    public TripEditDialog(Trips trip, DatabaseReference tripsDatabase) {
        this.trip = trip;
        this.tripsDatabase = tripsDatabase;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_trip_edit, null);

        ratingBar = view.findViewById(R.id.ratingBar);
        editTextComments = view.findViewById(R.id.editTextComments);
        editTextHourEnd = view.findViewById(R.id.editTextHourEnd);
        buttonSave = view.findViewById(R.id.buttonSave);

        ratingBar.setRating(trip.getUserRate());
        editTextComments.setText(trip.getUserComments());
        editTextHourEnd.setText(trip.getHourEnd());

        buttonSave.setOnClickListener(v -> saveTripUpdates());

        builder.setView(view);
        return builder.create();
    }

    private void saveTripUpdates() {
        float updatedRating = ratingBar.getRating();
        String updatedComments = editTextComments.getText().toString().trim();
        String updatedHourEnd = editTextHourEnd.getText().toString().trim();

        // עדכון ה-DB ב-Firebase
        tripsDatabase.child(trip.getUid()).child("userRate").setValue(updatedRating);
        tripsDatabase.child(trip.getUid()).child("userComments").setValue(updatedComments);
        tripsDatabase.child(trip.getUid()).child("hourEnd").setValue(updatedHourEnd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Trip updated successfully!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed to update trip.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
