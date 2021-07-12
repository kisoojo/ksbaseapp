package com.zenoation.ksbaseapp.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zenoation.ksbaseapp.listener.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by kisoojo on 2020.02.05
 */

public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private ArrayList<Object> mItems;
    private int mViewId;
    private int mSelectedPos;

    private OnItemClickListener mOnItemClickListener;

    public BaseRecyclerAdapter(Context context, ArrayList<?> items, @LayoutRes int viewId) {
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
    public void setViewId(int viewId) {
        mViewId = viewId;
    }

    protected RecyclerView.Adapter getAdapter() {
        return this;
    }

    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public ArrayList<?> getItems() {
        return mItems;
    }

    protected Context getContext() {
        return mContext;
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public void setSelectedPos(int position) {
        mSelectedPos = position;
    }

    public int getSelectedPos() {
        return mSelectedPos;
    }

    @CallSuper
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @CallSuper
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mViewId, parent, false);
        return onCreateViewHolderBase(view, parent, viewType);
    }

    @CallSuper
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindViewHolderBase(holder, position);
    }

    public void notifyDataSetInvalidated() {
        getAdapter().notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    public void scrollToCenter(View v) {
        int itemToScroll = mRecyclerView.getChildAdapterPosition(v);
        int centerOfScreen = mRecyclerView.getWidth() / 2 - v.getWidth() / 2;
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(itemToScroll, centerOfScreen);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    protected abstract RecyclerView.ViewHolder onCreateViewHolderBase(View view, @NonNull ViewGroup parent, int viewType);
    protected abstract void onBindViewHolderBase(@NonNull RecyclerView.ViewHolder holder, int position);
}
