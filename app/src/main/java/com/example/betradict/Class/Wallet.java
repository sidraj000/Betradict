package com.example.betradict.Class;

import com.example.betradict.transactions;

import java.util.List;

public class Wallet {
    public float balance;
    public List<transactions> lastTransactions;
    public Wallet()
    {
    }

    public Wallet(float balance, List<transactions> lastTransactions) {
        this.balance = balance;
        this.lastTransactions = lastTransactions;
    }
}
