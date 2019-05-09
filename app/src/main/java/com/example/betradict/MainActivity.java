package com.example.betradict;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    String name;
    TextView tvWel;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawer=findViewById(R.id.drawer);
        mToggle=new ActionBarDrawerToggle(MainActivity.this,mDrawer,R.string.open,R.string.close);
        mDrawer.addDrawerListener(mToggle);
        tvWel=findViewById(R.id.tvWel);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        name= getIntent().getStringExtra("uname");
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        tvWel.setText("Welcome "+user.getDisplayName());
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.setting)
        {
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, login_act.class));
            finish();
        }
        return false;
    }
}
