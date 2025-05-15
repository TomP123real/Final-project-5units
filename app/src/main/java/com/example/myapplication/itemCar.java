package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class itemCar extends RecyclerView.Adapter<itemCar.CarViewHolder> {

    private List<cars> carList;
    private OnCarClickListener listener;

    // ממשק ללחיצה על רכב
    public interface OnCarClickListener {
        void onCarClick(cars car);
    }

    public itemCar(List<cars> carList, OnCarClickListener listener) {
        this.carList = carList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        cars car = carList.get(position);

        // הצגת המודל בלבד
        holder.textViewModel.setText("Model: " + car.getModel());

        // לחיצה על הפריט פותחת את ה-EditCarDialog
        holder.itemView.setOnClickListener(v -> listener.onCarClick(car));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView textViewModel;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewModel = itemView.findViewById(R.id.textViewModel);
        }
    }
}
