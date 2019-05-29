package com.example.betradict.transition_act;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.betradict.R;
import com.example.betradict.Support;

public class transSupport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_support);

        FragmentManager fm=getSupportFragmentManager();
        Support fragment=new Support();
        fm.beginTransaction().replace(R.id.transsupp,fragment).commit();

    }
}
