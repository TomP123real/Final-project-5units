package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class TripsMainFragment extends Fragment {

    public TripsMainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trips_main, container, false);

        BottomNavigationView bottomNav = view.findViewById(R.id.bottomNavigationInsideFragment);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home_inside) {
                loadChildFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_add_trip_inside) {
                loadChildFragment(new AddTripFragment());
                return true;
            }

            return false;
        });


        loadChildFragment(new HomeFragment());

        return view;
    }


    private void loadChildFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutInside, fragment)
                .commit();
    }

}
