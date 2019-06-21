package com.vincis.betradict.Class;

public class Quest_wall
{


    public float bids1;
    public float bids2;
    public float bids3;
    public float ctbids1;
    public float ctbids2;
    public float ctbids3;
    public float cbids1;
    public float cbids2;
    public float cbids3;
    public String ans;
    public int wStatus;
    public float profit;
    public Quest_wall()
    {

    }

    public Quest_wall(float bids1, float bids2, float bids3, float ctbids1, float ctbids2, float ctbids3, float cbids1, float cbids2, float cbids3, String ans, int wStatus, float profit) {
        this.bids1 = bids1;
        this.bids2 = bids2;
        this.bids3 = bids3;
        this.ctbids1 = ctbids1;
        this.ctbids2 = ctbids2;
        this.ctbids3 = ctbids3;
        this.cbids1 = cbids1;
        this.cbids2 = cbids2;
        this.cbids3 = cbids3;
        this.ans = ans;
        this.wStatus = wStatus;
        this.profit = profit;
    }
}
