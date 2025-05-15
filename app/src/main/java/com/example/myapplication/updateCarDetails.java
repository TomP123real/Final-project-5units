package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class updateCarDetails extends DialogFragment {

    private cars car;
    private DatabaseReference carsDatabase;
    private StorageReference storageReference;

    private EditText editTextLicense, editTextModel, editTextDescription;
    private Button buttonUpdate, buttonChooseNewLogo;
    private ImageView imageViewNewLogo;

    private Uri newImageUri;

    public updateCarDetails(cars car) {
        this.car = car;
        this.carsDatabase = FirebaseDatabase.getInstance().getReference("Cars");
        this.storageReference = FirebaseStorage.getInstance().getReference("car_logos");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_car_details, null);

        editTextLicense = view.findViewById(R.id.editTextLicenseNumber);
        editTextModel = view.findViewById(R.id.editTextModel);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        buttonUpdate = view.findViewById(R.id.buttonUpdateCar);
        buttonChooseNewLogo = view.findViewById(R.id.buttonChooseNewLogo);
        imageViewNewLogo = view.findViewById(R.id.imageViewNewLogo);

        editTextLicense.setText(car.getLicenseNumber());
        editTextModel.setText(car.getModel());
        editTextDescription.setText(car.getDescription());

        if (car.getLogoUri() != null && !car.getLogoUri().isEmpty()) {
            com.squareup.picasso.Picasso.get().load(car.getLogoUri()).into(imageViewNewLogo);
        }

        buttonChooseNewLogo.setOnClickListener(v -> openImagePicker());

        buttonUpdate.setOnClickListener(v -> updateCarData());

        builder.setView(view);
        return builder.create();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            newImageUri = data.getData();
            imageViewNewLogo.setImageURI(newImageUri);
        }
    }

    private void updateCarData() {
        String newLicense = editTextLicense.getText().toString().trim();
        String newModel = editTextModel.getText().toString().trim();
        String newDescription = editTextDescription.getText().toString().trim();

        if (newLicense.isEmpty() || newModel.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        carsDatabase.child(car.getUid()).child("licenseNumber").setValue(newLicense);
        carsDatabase.child(car.getUid()).child("model").setValue(newModel);
        carsDatabase.child(car.getUid()).child("description").setValue(newDescription);

        if (newImageUri != null) {
            StorageReference imageRef = storageReference.child(car.getUid());
            imageRef.putFile(newImageUri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String newLogoUri = uri.toString();
                    carsDatabase.child(car.getUid()).child("logoUri").setValue(newLogoUri);
                    Toast.makeText(getContext(), "Car updated successfully!", Toast.LENGTH_SHORT).show();
                    dismiss();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(getContext(), "Car updated successfully!", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

}
