package com.example.myapplication;

public class Trips {
    private String status;
    private String uid;
    private String uidDriver;
    private String uidUser;
    private String pickupLocation; // מקום התחלה
    private String dropoffLocation; // מקום סיום
    private String dateDrive;
    private String hourStart;
    private String hourEnd;
    private float userRate;
    private String userComments;

    public Trips() {}

    public Trips(String status, String uid, String uidDriver, String uidUser,
                 String pickupLocation, String dropoffLocation,
                 String dateDrive, String hourStart, String hourEnd,
                 float userRate, String userComments) {
        this.status = status;
        this.uid = uid;
        this.uidDriver = uidDriver;
        this.uidUser = uidUser;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.dateDrive = dateDrive;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        this.userRate = userRate;
        this.userComments = userComments;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidDriver() {
        return uidDriver;
    }

    public void setUidDriver(String uidDriver) {
        this.uidDriver = uidDriver;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public String getDateDrive() {
        return dateDrive;
    }

    public void setDateDrive(String dateDrive) {
        this.dateDrive = dateDrive;
    }

    public String getHourStart() {
        return hourStart;
    }

    public void setHourStart(String hourStart) {
        this.hourStart = hourStart;
    }

    public String getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(String hourEnd) {
        this.hourEnd = hourEnd;
    }

    public float getUserRate() {
        return userRate;
    }

    public void setUserRate(float userRate) {
        this.userRate = userRate;
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }
}
