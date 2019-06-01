package com.example.betradict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.betradict.admin.Add_answers;
import com.example.betradict.admin.update_wallet;
import com.example.betradict.frags.frag2;
import com.example.betradict.frags.frag3;
import com.example.betradict.frags.frag4;

class PagerViewAdapter extends FragmentPagerAdapter{
    Bundle bd;
    public PagerViewAdapter(FragmentManager fm,Bundle b) {
        super(fm);
        bd=b;
    }


    @Override
    public Fragment getItem(int i) {
        Fragment fragment=null;
        switch (i)
        {
            case 0:
                   fragment= new frag4();
                   fragment.setArguments(bd);
                   break;


                case 1:
                   fragment=new frag3();
                   fragment.setArguments(bd);
                   break;
            case 2:
                fragment=new frag2();
                fragment.setArguments(bd);
                break;
 /*       case 3:
                fragment=new update_wallet();
                fragment.setArguments(bd);
                break;
            case 4:
                fragment=new Add_answers();
                fragment.setArguments(bd);
                break;
*/
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
