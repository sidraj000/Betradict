package com.example.betradict.transition_act;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.betradict.R;
import com.example.betradict.Support_admin;

public class trans_adminmsgs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String det=getIntent().getStringExtra("det");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_adminmsgs);
        FragmentManager fm=getSupportFragmentManager();
        Support_admin fragment=new Support_admin();
        Bundle b=new Bundle();
        b.putString("det",det);
        fragment.setArguments(b);
        fm.beginTransaction().replace(R.id.transamsg,fragment).commit();

    }
}
