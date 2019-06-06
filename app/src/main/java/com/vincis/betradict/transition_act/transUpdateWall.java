package com.vincis.betradict.transition_act;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vincis.betradict.R;
import com.vincis.betradict.admin.update_wallet;

public class transUpdateWall extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_update_wall);


        FragmentManager fm=getSupportFragmentManager();
        update_wallet fragment=new update_wallet();
        fm.beginTransaction().replace(R.id.transUpdateWall,fragment).commit();

    }
}
