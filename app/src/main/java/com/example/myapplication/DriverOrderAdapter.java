package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DriverOrderAdapter extends RecyclerView.Adapter<DriverOrderAdapter.ViewHolder> {

    private List<OrderFood> orders;

    public DriverOrderAdapter(List<OrderFood> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_driver_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderFood order = orders.get(position);
        holder.tvRestaurant.setText("מסעדה: " + order.getRestaurantName());
        holder.tvClientAddress.setText("כתובת לקוח: " + order.getClientAddress());
        holder.tvDate.setText("תאריך: " + order.getDate());
        holder.tvStatus.setText("סטטוס: " + order.getStatus());
        holder.tvHour.setText("שעת הגעה: " + (order.getHourArrived() != null ? order.getHourArrived() : "עדיין לא הוזנה"));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRestaurant, tvClientAddress, tvDate, tvStatus, tvHour;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurant = itemView.findViewById(R.id.tvRestaurant);
            tvClientAddress = itemView.findViewById(R.id.tvClientAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvHour = itemView.findViewById(R.id.tvHour);
        }
    }
}
