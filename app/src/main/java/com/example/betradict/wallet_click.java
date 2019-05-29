package com.example.betradict;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class wallet_click extends AppCompatActivity {
    TextView wdDet,wdQuest,wdO1,wdO2,wdO3,wdMans,wdCans,wdDate,wdMatch,wdBid,wdRate;
    String[] arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_click);
        arr=getIntent().getStringArrayExtra("det");
        wdDet=findViewById(R.id.wdDet);
        wdCans=findViewById(R.id.wdCans);
        wdMans=findViewById(R.id.wdMans);
        wdO1=findViewById(R.id.wdO1);
        wdO2=findViewById(R.id.wdO2);
        wdO3=findViewById(R.id.wdO3);
        wdDate=findViewById(R.id.wdDate);
        wdQuest=findViewById(R.id.wdQuest);
        wdMatch=findViewById(R.id.wdMatch);
        wdBid=findViewById(R.id.wdBid);
        wdRate=findViewById(R.id.wdRate);
        wdDet.setText(arr[0]);
        wdMatch.setText(arr[1]);
        wdDate.setText(arr[2]);
        wdQuest.setText(arr[3]);
        wdO1.setText("A: "+arr[4]);
        wdO2.setText("B: "+arr[5]);
        wdO3.setText("C: "+arr[6]);
        wdBid.setText("My Bid: "+arr[7]);
        wdRate.setText("My Rate :"+arr[8]);
        wdMans.setText("My Ans: "+arr[9]);
        if(arr[10].equals("U"))
        {
            wdCans.setVisibility(View.GONE);
        }
        else {
            wdCans.setText("Correct Ans: " + arr[10]);
            wdCans.setVisibility(View.VISIBLE);
        }
    }
}
