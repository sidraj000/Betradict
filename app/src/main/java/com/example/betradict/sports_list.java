package com.example.betradict;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.betradict.admin.addEvent;
import com.example.betradict.admin.addQuest;
import com.example.betradict.transition_act.transSupport;
import com.example.betradict.transition_act.trans_activity;
import com.example.betradict.transition_act.trans_adminMList;
import com.example.betradict.transition_act.trans_leader;
import com.google.firebase.auth.FirebaseAuth;

public class sports_list extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    ImageView ivCric,ivFoot,ivBask;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_list);
        ivCric=findViewById(R.id.ivCric);
        ivFoot=findViewById(R.id.ivFoot);
        ivBask=findViewById(R.id.ivBask);

        mDrawer = findViewById(R.id.sports_list_background);
        mToggle = new ActionBarDrawerToggle(sports_list.this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToggle);
        NavigationView navigationView = findViewById(R.id.nav_view3);
        navigationView.setNavigationItemSelectedListener(sports_list.this);
        navigationView.setItemIconTintList(null);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivCric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sports_list.this, trans_activity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.home)
        {
            startActivity(new Intent(this,trans_activity.class));
            finish();
        }

        if(id==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, login_act.class));
            finish();
        }
        if(id==R.id.wallet)
        {
            startActivity(new Intent(this,wallet_trans.class));
            finish();
        }
        if(id==R.id.leader_board)
        {
            startActivity(new Intent(this, trans_leader.class));
            finish();
        }
        if(id==R.id.prevml)
        {
            startActivity(new Intent(this, trans_prevmatchList.class));
            finish();
        }
        if(id==R.id.support)
        {
            startActivity(new Intent(this, transSupport.class));
            finish();
        }
  /*      if(id==R.id.suppA)
        {
            startActivity(new Intent(this, trans_adminMList.class));
            finish();
        }

        if(id==R.id.write_quest)
        {
            startActivity(new Intent(this, addQuest.class));
            finish();
        }
        if(id==R.id.addEvent)
        {
            startActivity(new Intent(this, addEvent.class));
            finish();
        }

        if(id==R.id.addDynamic)
        {
            startActivity(new Intent(this, addDynamic.class));
            finish();
        }
*/

        return false;
    }
}
