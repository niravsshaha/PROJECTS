package com.example.qtp.models;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.ServerValue;

public class Transaction {
    String otherUser;
    String otherUser_lower;
    String email;
    Double amount;
    boolean credit;
    Object timeStamp;

    public Transaction() {
    }

    public Transaction(String otherUser, String email, Double amount, boolean credit) {
        this.otherUser = otherUser;
        this.otherUser_lower = otherUser.toLowerCase();
        this.email = email;
        this.amount = amount;
        this.credit = credit;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public String getOtherUser_lower() {
        return otherUser_lower;
    }

    public String getEmail() {
        return email;
    }

    public Double getAmount() {
        return amount;
    }

    public boolean isCredit() {
        return credit;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }
}
