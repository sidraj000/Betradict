package com.example.betradict;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class adminml extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminml);
        FragmentManager fm=getSupportFragmentManager();
        adminMList fragment=new adminMList();
        fm.beginTransaction().replace(R.id.transadminml,fragment).commit();

    }
}
