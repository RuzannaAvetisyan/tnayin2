package com.paypal.desk;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction {
    private int id;
    private int userFrom;
    private int userTo;
    private double transactionAmount;
    private Timestamp transactionDate;

    public Transaction(int id, int userFrom, int userTo, double transactionAmount, Timestamp transactionDate) {
        this.id = id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(int userFrom) {
        this.userFrom = userFrom;
    }

    public int getUserTo() {
        return userTo;
    }

    public void setUserTo(int userTo) {
        this.userTo = userTo;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userFrom=" + userFrom +
                ", userTo=" + userTo +
                ", transactionAmount=" + transactionAmount +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
