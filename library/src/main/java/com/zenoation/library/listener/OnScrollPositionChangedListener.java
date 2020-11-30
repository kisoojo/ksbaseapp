package com.zenoation.library.listener;

import android.view.View;
import android.widget.AbsListView;

public abstract class OnScrollPositionChangedListener implements AbsListView.OnScrollListener {
    private int pos;
    private int prevIndex;
    private int prevViewPos;
    private int prevViewHeight;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        try {
            View currView = view.getChildAt(0);
            int currViewPos = Math.round(currView.getTop());
            int diffViewPos = prevViewPos - currViewPos;
            int currViewHeight = currView.getHeight();
            pos += diffViewPos;
            if (firstVisibleItem > prevIndex) {
                pos += prevViewHeight;
            } else if (firstVisibleItem < prevIndex) {
                pos -= currViewHeight;
            }
            prevIndex = firstVisibleItem;
            prevViewPos = currViewPos;
            prevViewHeight = currViewHeight;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onScrollPositionChanged(pos);
        }
    }

    public abstract void onScrollPositionChanged(int scrollYPosition);
}
