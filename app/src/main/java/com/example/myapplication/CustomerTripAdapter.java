package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerTripAdapter extends RecyclerView.Adapter<CustomerTripAdapter.ViewHolder> {

    // רשימת כל הנסיעות של הלקוח
    private List<Trips> tripList;

    // מאזין חיצוני לפעולות עריכה/ביטול
    public interface OnTripActionListener {
        void onEditTrip(Trips trip);
        void onCancelTrip(Trips trip);
    }

    private OnTripActionListener actionListener;

    public CustomerTripAdapter(List<Trips> tripList, OnTripActionListener actionListener) {
        this.tripList = tripList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // משייכים לפריסה item_trip_customer.xml (ללא כפתור Accept)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trips trip = tripList.get(position);

        // מציגים פרטי הנסיעה ללקוח
        holder.textViewPickup.setText("Pickup: " + trip.getPickupLocation());
        holder.textViewDropoff.setText("Dropoff: " + trip.getDropoffLocation());
        holder.textViewDateTime.setText("Date: " + trip.getDateDrive() +
                " | Start: " + trip.getHourStart());

        // כפתור עריכה -> קורא ל-onEditTrip
        holder.buttonEditTrip.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEditTrip(trip);
            }
        });

        // כפתור ביטול -> זמין רק אם בסטטוס pending
        if ("pending".equals(trip.getStatus())) {
            holder.buttonCancelTrip.setVisibility(View.VISIBLE);
            holder.buttonCancelTrip.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onCancelTrip(trip);
                }
            });
        } else {
            holder.buttonCancelTrip.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tripList != null ? tripList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPickup, textViewDropoff, textViewDateTime;
        Button buttonEditTrip, buttonCancelTrip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPickup = itemView.findViewById(R.id.textViewPickup);
            textViewDropoff = itemView.findViewById(R.id.textViewDropoff);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            buttonEditTrip = itemView.findViewById(R.id.buttonEditTrip);
            buttonCancelTrip = itemView.findViewById(R.id.buttonCancelTrip);
        }
    }

}
