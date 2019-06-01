package com.example.betradict;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SnapHelperByOne extends LinearSnapHelper {

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

        if (velocityY> 400) {
            currentPosition = position2;
        } else if (velocityY < -400) {
            currentPosition = position1;
        }

        if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }

        return currentPosition;
    }
}