package com.vincis.betradict.transition_act;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vincis.betradict.R;
import com.vincis.betradict.frags.frag5;

public class transfrag5 extends AppCompatActivity {
ImageView btnTogg;
String arr[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfrag5);
        btnTogg=findViewById(R.id.btnTogg);
        arr=getIntent().getStringArrayExtra("det");
        if(arr[1].equals("normal"))
        {
            btnTogg.setImageResource(R.drawable.in);
        }
        else {
            btnTogg.setImageResource(R.drawable.back);
        }
        btnTogg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arr[1].equals("normal"))
                {
                    //  Toast.makeText(MainActivity.this, "hii", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(transfrag5.this,transfrag5.class);
                    arr[1]="live";
                    intent.putExtra("det",arr);
                    startActivity(intent);
                    finish();
                }
                else if(arr[1].equals("live"))
                {  Intent intent=new Intent(transfrag5.this,transfrag5.class);
                    arr[1]="normal";
                    intent.putExtra("det",arr);
                    startActivity(intent);
                    finish();
                }

            }
        });
        FragmentManager fm=getSupportFragmentManager();
        frag5 fragment=new frag5();
        Bundle b=new Bundle();
        b.putStringArray("det",arr);
        fragment.setArguments(b);
        fm.beginTransaction().replace(R.id.transfrag5,fragment).commit();


    }
}
