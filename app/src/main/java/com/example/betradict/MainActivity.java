package com.example.betradict;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.betradict.Class.Wallet;
import com.example.betradict.admin.addEvent;
import com.example.betradict.admin.addQuest;
import com.example.betradict.transition_act.transSupport;
import com.example.betradict.transition_act.trans_activity;
import com.example.betradict.transition_act.trans_adminMList;
import com.example.betradict.transition_act.trans_leader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView one,two,three,four,five;
    FloatingActionButton btnToggle;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    ViewPager viewPager;
    PagerViewAdapter pagerViewAdapter;
    public Bundle arr;
    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        btnToggle=findViewById(R.id.btnToggle);

        mDrawer = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       arr =getIntent().getExtras();
        final String[] data=arr.getStringArray("details");
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data[2].equals("normal"))
                {
                    Toast.makeText(MainActivity.this, "hii", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    data[2]="live";

                    intent.putExtra("details",data);
                   startActivity(intent);
                }

            }
        });




        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("normal")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                    }
                });


        one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        three=findViewById(R.id.three);
       // five=findViewById(R.id.five);
        viewPager=findViewById(R.id.container);
        pagerViewAdapter=new PagerViewAdapter(getSupportFragmentManager(),arr);
        viewPager.setAdapter(pagerViewAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                onChangeTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



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
                menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this,(int)wallet.balance,R.mipmap.trollar));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this,wallet_trans.class));
                return false;
            }
        });

        return true;
    }

    private void onChangeTab(int i) {
        if(i==0)
        {
            one.setImageResource(R.mipmap.quiz);
            two.setImageResource(R.mipmap.ans);
            three.setImageResource(R.mipmap.analytics);

            viewPager.setCurrentItem(0);

        }

        if(i==1)
        {
            one.setImageResource(R.mipmap.question);
            two.setImageResource(R.mipmap.ansc);
            three.setImageResource(R.mipmap.analytics);
            viewPager.setCurrentItem(1);
        }
        if(i==2)
        {
            one.setImageResource(R.mipmap.question);
            two.setImageResource(R.mipmap.ans);
            three.setImageResource(R.mipmap.analyticsc);

            viewPager.setCurrentItem(2);

        }


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
        if(id==R.id.suppA)
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


        return false;
    }
}
