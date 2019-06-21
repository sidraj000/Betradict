package com.vincis.betradict.transition_act;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vincis.betradict.R;
import com.vincis.betradict.SupportAdminL;

public class trans_adminMList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_admin_mlist);
        FragmentManager fm=getSupportFragmentManager();
        SupportAdminL fragment=new SupportAdminL();
        fm.beginTransaction().replace(R.id.trans_ml,fragment).commit();

    }
}
