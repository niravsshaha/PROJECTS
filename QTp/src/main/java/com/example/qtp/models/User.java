package com.example.qtp.models;

public class User {
    String name;
    String name_lower;
    String contact;
    String email;
    String mpin;
    double balance;

    public User() {
    }

    public User(String name, String contact, String email, String mpin) {
        this.name = name;
        this.name_lower = name.toLowerCase();
        this.contact = contact;
        this.email = email;
        this.mpin = mpin;
        this.balance = 0;
    }

    public String getName() {
        return name;
    }

    public String getName_lower() {
        return name_lower;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getMpin() {
        return mpin;
    }

    public double getBalance() {
        return balance;
    }
}
