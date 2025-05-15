package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private EditText editTextName, editTextLastName, editTextLicenseDrive, editTextLicenseType;
    private Button sendButton;
    private DatabaseReference mDatabase;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // מחליף setContentView -> inflate()
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // מאתרים רכיבי UI
        editTextName = view.findViewById(R.id.editTextText);
        editTextLastName = view.findViewById(R.id.editTextText2);
        editTextLicenseDrive = view.findViewById(R.id.editTextText3);
        editTextLicenseType = view.findViewById(R.id.editTextText4);
        sendButton = view.findViewById(R.id.Send);

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Client");

        loadUserData(); // במקום onCreate

        // מאזין לכפתור Send
        sendButton.setOnClickListener(v -> openEmailClient());

        return view;
    }

    private void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String lastname = dataSnapshot.child("lastname").getValue(String.class);
                        String licenseType = dataSnapshot.child("licenseType").getValue(String.class);
                        String licenseDrive = dataSnapshot.child("licenseDrive").getValue(String.class);

                        editTextName.setText(name);
                        editTextLastName.setText(lastname);
                        editTextLicenseType.setText(licenseType);
                        editTextLicenseDrive.setText(licenseDrive);
                    } else {
                        Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Error loading user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openEmailClient() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser != null ? currentUser.getUid() : "unknown";

        String[] email = {"1tomplot@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Driver Request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I would like to become a driver. My UID is: " + uid);

        // כדי להפעיל Intent מתוך פרגמנט:
        if (emailIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(getContext(), "No email app found", Toast.LENGTH_SHORT).show();
        }
    }

}
