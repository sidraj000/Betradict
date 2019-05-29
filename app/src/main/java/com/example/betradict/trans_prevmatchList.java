package com.example.betradict;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class trans_prevmatchList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_prevmatch_list);
        FragmentManager fm=getSupportFragmentManager();
        prevml fragment=new prevml();
        fm.beginTransaction().replace(R.id.trans_prevml,fragment).commit();

    }
}
