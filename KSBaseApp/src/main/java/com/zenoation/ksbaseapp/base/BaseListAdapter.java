package com.zenoation.ksbaseapp.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;


import com.zenoation.ksbaseapp.listener.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by kisoojo on 2020.02.05
 */

public abstract class BaseListAdapter extends BaseAdapter {

    private Context mContext;
    private ListView mListView;
    private ArrayList<Object> mItems;
    private int mViewId;
    private int mSelectedPos;

    private OnItemClickListener mOnItemClickListener;

    public BaseListAdapter(Context context, ArrayList<?> items, @LayoutRes int viewId) {
        mContext = context;
        setItems(items);
        mViewId = viewId;
        mSelectedPos = -1;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void onItemClick() {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onClick(-1);
        }
    }

    public void onItemClick(int position) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onClick(position);
        }
    }

    public void setItems(ArrayList<?> items) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        } else {
            mItems.clear();
        }
        mItems.addAll(items);
    }

    public ArrayList<?> getItems() {
        return mItems;
    }

    protected BaseAdapter getAdapter() {
        return this;
    }

    protected ListView getListView() {
        return mListView;
    }

    protected Context getContext() {
        return mContext;
    }

    protected View getHolderView() {
        return LayoutInflater.from(mContext).inflate(mViewId, null);
    }

    public void setSelectedPos(int position) {
        mSelectedPos = position;
    }

    public int getSelectedPos() {
        return mSelectedPos;
    }

    @CallSuper
    @Override
    public int getCount() {
        return mItems.size();
    }

    @CallSuper
    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @CallSuper
    @Override
    public long getItemId(int position) {
        return position;
    }

    @CallSuper
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewBase(position, convertView, parent);
    }

    protected abstract View getViewBase(int position, View convertView, ViewGroup parent);

    public void setListViewHeight(ListView listView) {
        mListView = listView;
        BaseAdapter listAdapter = this;
        int totalHeight = 0;
        //int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
