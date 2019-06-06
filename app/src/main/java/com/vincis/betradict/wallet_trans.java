package com.vincis.betradict;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vincis.betradict.Class.Wallet;
import com.vincis.betradict.transition_act.transSupport;
import com.vincis.betradict.transition_act.trans_activity;
import com.vincis.betradict.transition_act.trans_leader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class wallet_trans extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView tv;
    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    TextView tvU;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_trans);
        FragmentManager fm=getSupportFragmentManager();
        tv=findViewById(R.id.tBal);
        mDrawer = findViewById(R.id.d1);
        mToggle = new ActionBarDrawerToggle(wallet_trans.this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToggle);
        NavigationView navigationView = findViewById(R.id.nav_view11);
        navigationView.setNavigationItemSelectedListener(wallet_trans.this);
        View header=navigationView.getHeaderView(0);
        tvU=header.findViewById(R.id.tvUName);
        tvU.setText("Welcome! "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        navigationView.setItemIconTintList(null);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRef.child("users").child(uid).child("wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                tv.setText(Integer.toString((int)(wallet.balance)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        walletdetail fragment=new walletdetail();
        fm.beginTransaction().replace(R.id.wallet_t,fragment).commit();
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
            startActivity(new Intent(this, trans_activity.class));
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
        if(id==R.id.support)
        {
            startActivity(new Intent(this, transSupport.class));
            finish();
        }
       /* if(id==R.id.suppA)
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
          startActivity(new Intent(this,addDynamic.class));
          finish();
      }


*/



        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.cart_action);
        DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("wallet");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                menuItem.setIcon(Converter.convertLayoutToImage(wallet_trans.this,(int)wallet.balance,R.drawable.wallet));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }

}
