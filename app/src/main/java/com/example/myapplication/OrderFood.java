package com.example.myapplication;

public class OrderFood {
    private String uid;
    private String uidDriver;
    private String uidClient;
    private String restaurantName;
    private String restaurantAddress;
    private String clientAddress;
    private String date;
    private String hourArrived;
    private String status;

    public OrderFood() {
        // נדרש על ידי Firebase
    }

    public OrderFood(String uid, String uidDriver, String uidClient, String restaurantName, String restaurantAddress, String clientAddress, String date, String hourArrived, String status) {
        this.uid = uid;
        this.uidDriver = uidDriver;
        this.uidClient = uidClient;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.clientAddress = clientAddress;
        this.date = date;
        this.hourArrived = hourArrived;
        this.status = status;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getUidDriver() { return uidDriver; }
    public void setUidDriver(String uidDriver) { this.uidDriver = uidDriver; }

    public String getUidClient() { return uidClient; }
    public void setUidClient(String uidClient) { this.uidClient = uidClient; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public String getRestaurantAddress() { return restaurantAddress; }
    public void setRestaurantAddress(String restaurantAddress) { this.restaurantAddress = restaurantAddress; }

    public String getClientAddress() { return clientAddress; }
    public void setClientAddress(String clientAddress) { this.clientAddress = clientAddress; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getHourArrived() { return hourArrived; }
    public void setHourArrived(String hourArrived) { this.hourArrived = hourArrived; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}