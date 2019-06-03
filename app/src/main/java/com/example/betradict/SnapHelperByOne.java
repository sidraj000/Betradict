package com.example.betradict;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class SnapHelperByOne extends LinearSnapHelper {
     ImageView r,l;
     Integer s;
    public SnapHelperByOne(ImageView ri, ImageView lf, int size) {
        r=ri;
        l=lf;
        s=size;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {


        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return RecyclerView.NO_POSITION;
        }

        final View currentView = findSnapView(layoutManager);
        if (currentView == null) {
            return RecyclerView.NO_POSITION;
        }

        LinearLayoutManager myLayoutManager = (LinearLayoutManager) layoutManager;

        int position1 = myLayoutManager.findFirstVisibleItemPosition();
        int position2 = myLayoutManager.findLastVisibleItemPosition();
        int currentPosition = layoutManager.getPosition(currentView);

        if (velocityY> 10) {
            currentPosition = position2;



        } else if (velocityY< -10) {
            currentPosition = position1;


        }

   if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }
       if(currentPosition==0)
        {
            r.setVisibility(View.GONE);
        }
        else {
            r.setVisibility(View.VISIBLE);
        }
        if(currentPosition==s)
        {
            l.setVisibility(View.GONE);
        }
        else {
            l.setVisibility(View.VISIBLE);
        }
        return currentPosition;
    }
}