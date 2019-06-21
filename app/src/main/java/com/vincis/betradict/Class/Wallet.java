package com.vincis.betradict.Class;

import com.vincis.betradict.transactions;

import java.util.List;

public class Wallet {


    public float balance;
    public float lastmatch;
    public List<transactions> lastTransactions;
    public Wallet()
    {
    }

    public Wallet(float balance, List<transactions> lastTransactions) {
        this.balance = balance;
        this.lastTransactions = lastTransactions;
    }
    public Wallet(float balance, float lastmatch, List<transactions> lastTransactions) {
        this.balance = balance;
        this.lastmatch= lastmatch;
        this.lastTransactions = lastTransactions;
    }
}
