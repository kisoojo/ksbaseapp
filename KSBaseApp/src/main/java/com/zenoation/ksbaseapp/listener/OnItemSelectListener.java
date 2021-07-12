package com.zenoation.ksbaseapp.listener;

import android.content.ContentValues;

/**
 * Created by kisoojo on 2020.04.06
 */
public interface OnItemSelectListener {
    void onSelect();
    void onSelect(ContentValues item);
}
