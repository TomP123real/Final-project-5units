package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trips> tripList;
    private OnTripAcceptListener acceptListener;

    public interface OnTripAcceptListener {
        void onTripAccepted(Trips trip);
    }

    public TripAdapter(List<Trips> tripList, OnTripAcceptListener acceptListener) {
        this.tripList = tripList;
        this.acceptListener = acceptListener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trips trip = tripList.get(position);
        holder.textViewPickup.setText("Pickup: " + trip.getPickupLocation());
        holder.textViewDropoff.setText("Dropoff: " + trip.getDropoffLocation());
        holder.textViewDateTime.setText("Date: " + trip.getDateDrive() + " Time: " + trip.getHourStart());

        holder.buttonAccept.setOnClickListener(v -> acceptListener.onTripAccepted(trip));
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPickup, textViewDropoff, textViewDateTime;
        Button buttonAccept;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPickup = itemView.findViewById(R.id.textViewPickup);
            textViewDropoff = itemView.findViewById(R.id.textViewDropoff);
            textViewDateTime = itemView.findViewById(R.id.textViewDate);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
        }
    }
}
