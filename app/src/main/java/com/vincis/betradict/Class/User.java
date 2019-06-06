package com.vincis.betradict.Class;


public class User {

   public per_det per;
   public Wallet wallet;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(per_det per, Wallet wallet) {
        this.per = per;
        this.wallet = wallet;
    }
}