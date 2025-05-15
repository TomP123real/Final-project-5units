package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class OrderFoodAdapter extends RecyclerView.Adapter<OrderFoodAdapter.OrderViewHolder> {

    private List<OrderFood> orderList;
    private OnOrderClickListener clickListener;

    public interface OnOrderClickListener {
        void onOrderClick(OrderFood order);
    }

    public OrderFoodAdapter(List<OrderFood> orderList, OnOrderClickListener clickListener) {
        this.orderList = orderList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderfood, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderFood order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvRestaurantName, tvRestaurantAddress, tvClientAddress, tvDate, tvHour, tvStatus;
        Button btnCollect;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvRestaurantAddress = itemView.findViewById(R.id.tvRestaurantAddress);
            tvClientAddress = itemView.findViewById(R.id.tvClientAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCollect = itemView.findViewById(R.id.btnCollect);
        }

        public void bind(OrderFood order) {
            tvRestaurantName.setText("Restaurant: " + order.getRestaurantName());
            tvRestaurantAddress.setText("Restaurant Address: " + order.getRestaurantAddress());
            tvClientAddress.setText("Delivery Address: " + order.getClientAddress());
            tvDate.setText("Date: " + order.getDate());
            tvHour.setText("Hour: " + (order.getHourArrived() != null ? order.getHourArrived() : "Not arrived"));
            tvStatus.setText("Status: " + order.getStatus());

            String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            boolean isDriver = currentUid.equals(order.getUidDriver());

            if (order.getStatus().equals("OPEN") && isDriver) {
                btnCollect.setVisibility(View.VISIBLE);
                btnCollect.setOnClickListener(v -> {
                    if (clickListener != null) {
                        clickListener.onOrderClick(order);
                    }
                });
            } else {
                btnCollect.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onOrderClick(order);
                }
            });
        }

    }

}
