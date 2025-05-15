package com.example.myapplication;

public class customer {
    private String email;
    private String name;
    private String lastname;
    private String birthday;
    private String phone;
    private String address;
    private String city;
    private String gender;
    private String picture;
    private String isDriver;
    private String uid; // הוספת UID


    public customer() {
    }

    public customer(String email, String name, String lastname, String birthday, String phone,
                    String address, String city, String gender, String picture, String isDriver) {
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.gender = gender;
        this.picture = picture;
        this.isDriver = isDriver;
    }

    public customer(String email, String name, String lastname, String birthday, String phone,
                    String address, String city, String gender, String picture, String isDriver, String uid) {
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.gender = gender;
        this.picture = picture;
        this.isDriver = isDriver;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIsDriver() {
        return isDriver;
    }

    public void setIsDriver(String isDriver) {
        this.isDriver = isDriver;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
