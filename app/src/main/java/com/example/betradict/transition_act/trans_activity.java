package com.example.betradict.transition_act;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.betradict.Class.Wallet;
import com.example.betradict.Converter;
import com.example.betradict.R;
import com.example.betradict.cricketMList;
import com.example.betradict.login_act;
import com.example.betradict.trans_prevmatchList;
import com.example.betradict.wallet_trans;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class trans_activity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_activity);
        mDrawer = findViewById(R.id.trans_background);
        mToggle = new ActionBarDrawerToggle(trans_activity.this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToggle);
        NavigationView navigationView = findViewById(R.id.nav_view4);
        navigationView.setNavigationItemSelectedListener(trans_activity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToggle.syncState();


    FragmentManager fm=getSupportFragmentManager();
        cricketMList fragment=new cricketMList();
        fm.beginTransaction().replace(R.id.trans,fragment).commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.cart_action);
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("wallet");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                menuItem.setIcon(Converter.convertLayoutToImage(trans_activity.this,(int)wallet.balance,R.mipmap.appl));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(trans_activity.this, wallet_trans.class));
                return false;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed()
    {
        final AlertDialog.Builder builder=new AlertDialog.Builder(trans_activity.this);
        builder.setMessage("Confirm to Exit");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Close!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

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
  /*if(id==R.id.suppA)
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
      if(id==R.id.stopmatch)
      {
          startActivity(new Intent(this, adminml.class));
          finish();
      }
*/

        return false;
    }
}
