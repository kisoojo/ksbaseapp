package com.zenoation.ksbaseapp.utils.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import com.zenoation.ksbaseapp.R;


/**
 * Created by kisoojo on 2020.02.03
 */

public class ProgressDialog extends Dialog {
    public ProgressDialog(Context context) {
        super(context, R.style.progressDialog);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message) {
        return show(context, title, message, false, null);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean cancelable) {
        return show(context, title, message, cancelable, null);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(cancelListener);
        dialog.addContentView(new ProgressBar(context), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        dialog.show();
        return dialog;
    }
}
