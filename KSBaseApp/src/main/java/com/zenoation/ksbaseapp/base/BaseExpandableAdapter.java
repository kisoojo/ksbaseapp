package com.zenoation.ksbaseapp.base;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BaseExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<Object> mGroupItems;
    private ArrayList<ArrayList<Object>> mChildItems;
    private int mGroupViewId;
    private int mChildViewId;
    private ExpandableListView mListView;

    public BaseExpandableAdapter(Context context, ArrayList<?> groupItems, ArrayList<? extends ArrayList<?>> childItems, @LayoutRes int groupViewId, @LayoutRes int childViewId) {
        mContext = context;
        setItems(groupItems, childItems);
        mGroupViewId = groupViewId;
        mChildViewId = childViewId;
    }

    @CallSuper
    public void setItems(ArrayList<?> groupItems, ArrayList<? extends ArrayList<?>> childItems) {
        setGroupItems(groupItems);
        setChildItems(childItems);
    }

    @CallSuper
    public void setGroupItems(ArrayList<?> items) {
        if (mGroupItems == null) {
            mGroupItems = new ArrayList<>();
        } else {
            mGroupItems.clear();
        }
        mGroupItems.addAll(items);
    }

    @CallSuper
    public void setChildItems(ArrayList<?> items) {
        if (mChildItems == null) {
            mChildItems = new ArrayList<>();
        } else {
            mChildItems.clear();
        }
        mChildItems.addAll((Collection<? extends ArrayList<Object>>) items);
    }

    protected Context getContext() {
        return mContext;
    }

    @CallSuper
    protected View getGroupHolderView() {
        return LayoutInflater.from(mContext).inflate(mGroupViewId, null);
    }

    @CallSuper
    protected View getChildHolderView() {
        return LayoutInflater.from(mContext).inflate(mChildViewId, null);
    }

    @CallSuper
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @CallSuper
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    @CallSuper
    @Override
    public int getGroupCount() {
        return mGroupItems.size();
    }

    @CallSuper
    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildItems.get(groupPosition).size();
    }

    @CallSuper
    @Override
    public Object getGroup(int groupPosition) {
        return mGroupItems.get(groupPosition);
    }

    @CallSuper
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildItems.get(groupPosition).get(childPosition);
    }

    @CallSuper
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @CallSuper
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @CallSuper
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @CallSuper
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @CallSuper
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @CallSuper
    @Override
    public boolean isEmpty() {
        return false;
    }

    @CallSuper
    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        //setExpandableListViewHeight(mListView);
    }

    @CallSuper
    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
        //setExpandableListViewHeight(mListView);
    }

    @CallSuper
    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return childId;
    }

    @CallSuper
    @Override
    public long getCombinedGroupId(long groupId) {
        return groupId;
    }

    @CallSuper
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getGroupViewBase(groupPosition, isExpanded, convertView, parent);
    }

    @CallSuper
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return getChildViewBase(groupPosition, childPosition, isLastChild, convertView, parent);
    }

    protected abstract View getGroupViewBase(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

    protected abstract View getChildViewBase(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

    public void setExpandableListViewHeight(ExpandableListView listView) {
        try {
            mListView = listView;
            ExpandableListAdapter listAdapter = this;
            int totalHeight = 0;
            //int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
            for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                View listItem = listAdapter.getGroupView(i, listView.isGroupExpanded(i), null, listView);
                listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight() + 1;

                if (listView.isGroupExpanded(i)) {
                    for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                        View childItem = listAdapter.getChildView(i, j, (j==getChildrenCount(i)-1), null, listView);
                        childItem.measure(0, View.MeasureSpec.UNSPECIFIED);
                        totalHeight += childItem.getMeasuredHeight() + 3;
                    }
                }
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
            //if (height < 10) height = 200;
            params.height = height;
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
