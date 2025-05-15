package com.example.myapplication;

public class cars {
    private String uid;
    private String licenseNumber;
    private String model;
    private String description;
    private String dateCreated;
    private String logoUri;
    private String uidDriver;

    public cars() {
    }

    public cars(String uid, String licenseNumber, String model, String description,
               String dateCreated, String logoUri, String uidDriver) {
        this.uid = uid;
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.description = description;
        this.dateCreated = dateCreated;
        this.logoUri = logoUri;
        this.uidDriver = uidDriver;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getUidDriver() {
        return uidDriver;
    }

    public void setUidDriver(String uidDriver) {
        this.uidDriver = uidDriver;
    }
}
