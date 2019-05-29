package com.example.betradict;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Home_Page extends AppCompatActivity {
    public static int Splash_Time_Out=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Home_Page.this,login_act.class));
                finish();
            }
        },Splash_Time_Out);
    }
}
