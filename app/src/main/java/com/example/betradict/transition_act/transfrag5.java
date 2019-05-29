package com.example.betradict.transition_act;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.betradict.R;
import com.example.betradict.frags.frag5;

public class transfrag5 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfrag5);
        String s=getIntent().getStringExtra("det");
        FragmentManager fm=getSupportFragmentManager();
        frag5 fragment=new frag5();
        Bundle b=new Bundle();
        b.putString("det",s);
        fragment.setArguments(b);
        fm.beginTransaction().replace(R.id.transfrag5,fragment).commit();


    }
}
