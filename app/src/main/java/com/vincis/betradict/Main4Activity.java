package com.vincis.betradict;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main4Activity extends AppCompatActivity {
    public Bundle arr;
    FragmentPagerAdapter mPagerAdapter;
    ViewPager mViewPager;
   /* ImageView one,two,three,four,five;
    ImageView btnToggle;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    PagerViewAdapter2 pagerViewAdapter;
   ViewPager viewPager;
    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    TextView tvU;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        arr=getIntent().getExtras();
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new playinng11details(),
                    new scorecard(),
                    new commentryfrag()


            };
            private final String[] mFragmentNames = new String[] {
                  "Squad","ScoreCard","Commentry"
            };
            @Override
            public Fragment getItem(int position)
            {
                mFragments[position].setArguments(arr);
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container2);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

      /*  one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        three=findViewById(R.id.three);
        arr=getIntent().getExtras();
        viewPager=findViewById(R.id.container2);
        pagerViewAdapter=new PagerViewAdapter2(getSupportFragmentManager(),arr);
        viewPager.setAdapter(pagerViewAdapter);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);

            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

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
*/

    }

}
