package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DriverHistoryAdapter extends RecyclerView.Adapter<DriverHistoryAdapter.ViewHolder> {

    private List<Trips> tripList;

    public DriverHistoryAdapter(List<Trips> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // משתמשים בפריסה item_trip_driver_history.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_driver_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trips trip = tripList.get(position);

        // תאריך ושעת התחלה
        holder.textViewDateTime.setText("Date: " + trip.getDateDrive() +
                " | Start: " + trip.getHourStart());

        // כתובת איסוף והורדה
        holder.textViewPickup.setText("Pickup: " + trip.getPickupLocation());
        holder.textViewDropoff.setText("Dropoff: " + trip.getDropoffLocation());

        // שעת סיום
        holder.textViewHourEnd.setText("End: " + (trip.getHourEnd() == null ? "N/A" : trip.getHourEnd()));

        // דירוג והערות (מאת הלקוח)
        holder.textViewRating.setText("Rating: " + trip.getUserRate());
        holder.textViewComments.setText("Comments: " + (trip.getUserComments() == null ? "" : trip.getUserComments()));
    }

    @Override
    public int getItemCount() {
        return tripList == null ? 0 : tripList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDateTime, textViewPickup, textViewDropoff, textViewHourEnd, textViewRating, textViewComments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            textViewPickup = itemView.findViewById(R.id.textViewPickup);
            textViewDropoff = itemView.findViewById(R.id.textViewDropoff);
            textViewHourEnd = itemView.findViewById(R.id.textViewHourEnd);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewComments = itemView.findViewById(R.id.textViewComments);
        }
    }
}
