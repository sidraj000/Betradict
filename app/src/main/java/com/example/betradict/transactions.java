package com.example.betradict;

import com.google.firebase.database.DatabaseReference;

import java.security.PublicKey;
import java.sql.Time;
import java.util.Date;

public class transactions {
    public float amt;
    public String qid;
    public String Mid;
    public Date date;
    public String type;

    public transactions()
    {

    }

    public transactions(float amt, String qid, String mid, Date date, String type) {
        this.amt = amt;
        this.qid = qid;
        Mid = mid;
        this.date = date;
        this.type = type;
    }
}
