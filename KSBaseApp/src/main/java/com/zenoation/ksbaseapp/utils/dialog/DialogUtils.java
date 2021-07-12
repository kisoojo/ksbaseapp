package com.zenoation.ksbaseapp.utils.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zenoation.ksbaseapp.base.BaseApplication;
import com.zenoation.ksbaseapp.R;
import com.zenoation.ksbaseapp.base.BaseRecyclerAdapter;
import com.zenoation.ksbaseapp.listener.MyOnclickListener;
import com.zenoation.ksbaseapp.listener.OnCompleteParamListener;
import com.zenoation.ksbaseapp.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Stack;


/**
 * Created by kisoojo on 2020.02.03
 */

public class DialogUtils {

    //private static class LazyHolder {
    //    private static final DialogUtils INSTANCE = new DialogUtils();
    //}
    //
    //public static DialogUtils getInstance() {
    //    return LazyHolder.INSTANCE;
    //}

    private final static String TAG = "DialogUtils";

    private volatile static DialogUtils uniqueInstance;

    public static DialogUtils getInstance() {
        if (uniqueInstance == null) {
            synchronized (DialogUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new DialogUtils();
                }
            }
        }
        return uniqueInstance;
    }

    private DialogUtils() {
        mPopDialogStack = new Stack<>();
    }

    private Dialog mProgDialog;
    private AlertDialog mPopDialog;
    private int mMinWidth = -1, mMinHeight = -1;
    private int mMaxWidth = -1, mMaxHeight = -1;

    private Stack<AlertDialog> mPopDialogStack;

    public void initWidth() {
        mMinWidth = -1;
        mMinHeight = -1;
    }

    private void setMinWidth(Activity activity) {
        mMinWidth = (int) (Utils.getInstance().getDisplayWidth(activity) * 0.9f);
        mMinHeight = (int) (Utils.getInstance().getDisplayHeight(activity) * 0.5f);

        mMaxWidth = (int) (Utils.getInstance().getDisplayWidth(activity) * 0.9f);
        mMaxHeight = (int) (Utils.getInstance().getDisplayHeight(activity) * 0.8f);
    }

    public int getMinWidth() {
        return mMinWidth;
    }

    private void setWidth(boolean isResize) {
        WindowManager.LayoutParams params = mPopDialog.getWindow().getAttributes();
        if (mMinHeight == -1 && mMinWidth == -1) {
            Activity activity = ((BaseApplication) mPopDialog.getContext().getApplicationContext()).getCurrentActivity();
            setMinWidth(activity);
        }
        params.width = mMinWidth;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (isResize) {
            params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        } else {
            params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        }
        mPopDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mPopDialog.getWindow().setAttributes(params);
    }

    private void setWidthFull(boolean isResize) {
        WindowManager.LayoutParams params = mPopDialog.getWindow().getAttributes();
        if (mMinHeight == -1 && mMinWidth == -1) {
            Activity activity = ((BaseApplication) mPopDialog.getContext().getApplicationContext()).getCurrentActivity();
            setMinWidth(activity);
        }
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (isResize) {
            params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        } else {
            params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        }
        mPopDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mPopDialog.getWindow().setAttributes(params);
    }


    /**
     * 로딩팝업 실행
     */
    public void showProgressDialog(Activity activity) {
        showProgressDialog(activity, false, false);
    }

    public void showProgressTDialog(Activity activity) {
        showProgressDialog(activity, true, false);
    }

    public void showProgressDialogFinish(Activity activity) {
        showProgressDialog(activity, false, true);
    }

    public void showProgressTDialogFinish(Activity activity) {
        showProgressDialog(activity, true, true);
    }

    /**
     * 로딩팝업 실행
     *
     * @param bIsTransparent 투명배경 여부
     * @param bIsFinish      취소시 엑티비티 종료 여부
     */
    public void showProgressDialog(Activity activity, boolean bIsTransparent, boolean bIsFinish) {
        if (mProgDialog == null && activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            try {
                DialogInterface.OnCancelListener listener = dialog -> {
                    if (bIsFinish) {
                        activity.finish();
                    }
                    dismissProgressDialog();
                };

                if (bIsTransparent) {
                    mProgDialog = ProgressDialogTransparent.show(activity, "", "", true, listener);
                } else {
                    mProgDialog = ProgressDialog.show(activity, "", "", true, listener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //30초동안 dismiss 없으면 dismiss
        new Handler().postDelayed(this::dismissProgressDialog, 30000);
    }

    /**
     * 로딩팝업 닫기
     */
    public void dismissProgressDialog() {
        if (mProgDialog != null) {
            try {
                mProgDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mProgDialog = null;
            }
        }
    }

    public void dismissProgressDialogDelay() {
        dismissProgressDialogDelay(300);
    }

    public void dismissProgressDialogDelay(long delay) {
        new Handler().postDelayed(this::dismissProgressDialog, delay);
    }

    private void createPopDialog(Activity activity, boolean isCancelable) {
        if (mPopDialog != null) {
            mPopDialogStack.push(mPopDialog);
        }
        mPopDialog = new AlertDialog.Builder(activity)
                .setTitle("")
                .setCancelable(isCancelable)
                .create();

        if (isCancelable) {
            mPopDialog.setOnCancelListener(dialog -> {
                if (mPopDialogStack.size() > 0) {
                    mPopDialogStack.pop();
                }
            });
        }
    }

    /**
     * 팝업 실행
     */
    private void showPopDialog(Activity activity, View view) {
        if (mPopDialog != null && activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            mPopDialog.show();
            //mPopDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopDialog.getWindow().setBackgroundDrawableResource(R.drawable.box_round_white);
            mPopDialog.setContentView(view);
            setWidth(false);
        }
    }

    /**
     * 바텀팝업 실행
     */
    private void showPopDialogBottom(Activity activity, View view) {
        if (mPopDialog != null && activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            mPopDialog.show();
            //mPopDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopDialog.getWindow().setBackgroundDrawableResource(R.drawable.box_round_white);
            //mPopDialog.getWindow().getAttributes().dimAmount = 0f;
            mPopDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
            mPopDialog.getWindow().setWindowAnimations(R.style.dialogAnimation);
            mPopDialog.setContentView(view);
            setWidthFull(false);
        }
    }

    /**
     * 팝업 닫기
     */
    public void dismissPopDialog() {
        if (mPopDialog != null && mPopDialog.isShowing()) {
            try {
                mPopDialog.dismiss();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            mPopDialog = null;

            if (mPopDialogStack.size() != 0) {
                mPopDialog = mPopDialogStack.pop();
            }
        }
    }

    /**
     * 모든 팝업 닫기
     */
    public void dismissPopDialogAll() {
        dismissPopDialog();
        for (AlertDialog dialog : mPopDialogStack) {
            dismissPopDialog();
        }
    }

    /**
     * 알림 팝업
     */
    public void showAlertDialog(Activity activity, String text) {
        showAlertDialog(activity, text, null, true);
    }

    public void showAlertDialog(Activity activity, String text, boolean isCancelable) {
        showAlertDialog(activity, text, null, isCancelable);
    }


    public void showAlertDialog(Activity activity, String text, View.OnClickListener confirmListener) {
        showAlertDialog(activity, text, confirmListener, true);
    }

    public void showAlertDialog(Activity activity, String text, View.OnClickListener confirmListener, boolean isCancelable) {
        showAlertDialog(activity, null, text, null, confirmListener, isCancelable);
    }

    public void showAlertDialog(Activity activity, String title, String text, View.OnClickListener confirmListener, boolean isCancelable) {
        showAlertDialog(activity, title, text, null, confirmListener, isCancelable);
    }

    public void showAlertDialog(Activity activity, String title, String text, String btnText, View.OnClickListener confirmListener, boolean isCancelable) {
        createPopDialog(activity, isCancelable);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_alert, null);
        view.setMinimumWidth(mMinWidth);

        FrameLayout flTitle = view.findViewById(R.id.fl_title);
        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        Button btnConfirm = view.findViewById(R.id.btn_confirm);

        if (!TextUtils.isEmpty(title)) {
            flTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }

        tvMsg.setText(text);

        if (!TextUtils.isEmpty(btnText)) {
            btnConfirm.setText(btnText);
        }

        btnConfirm.setOnClickListener(new MyOnclickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                dismissPopDialog();
                if (confirmListener != null) {
                    confirmListener.onClick(v);
                }
            }
        });

        showPopDialog(activity, view);
    }

    /**
     * 확인/취소 팝업
     */
    public void showConfirmDialog(Activity activity, String text, @NonNull View.OnClickListener confirmListener) {
        showConfirmDialog(activity, text, confirmListener, null, true);
    }

    public void showConfirmDialog(Activity activity, String text, @NonNull View.OnClickListener confirmListener, boolean isCancelable) {
        showConfirmDialog(activity, text, confirmListener, null, isCancelable);
    }

    public void showConfirmDialog(Activity activity, String text, @NonNull View.OnClickListener confirmListener, View.OnClickListener cancelListener) {
        showConfirmDialog(activity, text, confirmListener, cancelListener, true);
    }

    public void showConfirmDialog(Activity activity, String text, @NonNull View.OnClickListener confirmListener, View.OnClickListener cancelListener, boolean isCancelable) {
        showConfirmDialog(activity, null, text, null, null, confirmListener, cancelListener, isCancelable);
    }

    public void showConfirmDialog(Activity activity, String title, String text, String textConfirm, String textCancel, @NonNull View.OnClickListener confirmListener, View.OnClickListener cancelListener, boolean isCancelable) {
        createPopDialog(activity, isCancelable);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        view.setMinimumWidth(mMinWidth);

        FrameLayout flTitle = view.findViewById(R.id.fl_title);
        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        Button btnConfirm = view.findViewById(R.id.btn_confirm);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        if (!TextUtils.isEmpty(title)) {
            flTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }

        text = text.replaceAll("\n", "<br>");
        tvMsg.setText(Html.fromHtml(text));

        if (!TextUtils.isEmpty(textConfirm)) {
            btnConfirm.setText(textConfirm);
        }
        if (!TextUtils.isEmpty(textCancel)) {
            btnCancel.setText(textCancel);
        }

        btnConfirm.setOnClickListener(new MyOnclickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                dismissPopDialog();
                confirmListener.onClick(v);
            }
        });
        btnCancel.setOnClickListener(new MyOnclickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                dismissPopDialog();
                if (cancelListener != null) {
                    cancelListener.onClick(v);
                }
            }
        });

        showPopDialog(activity, view);
    }

    public void showLoginDialog(Activity activity, View view, boolean isCancelable) {
        createPopDialog(activity, isCancelable);
        showPopDialogBottom(activity, view);
        setWidthFull(true);
        mPopDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    public void showImageFileDialog(Activity activity, View.OnClickListener cameraListener, View.OnClickListener galleryListener) {
        showImageFileDialog(activity, cameraListener, galleryListener, null);
    }

    public void showImageFileDialog(Activity activity, View.OnClickListener cameraListener, View.OnClickListener galleryListener, View.OnClickListener fileListener) {
        createPopDialog(activity, true);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_image_file_selector, null);
        view.setMinimumWidth(mMinWidth);

        FrameLayout flTitle = view.findViewById(R.id.fl_title);
        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        Button btnGallery = view.findViewById(R.id.btn_gallery);
        Button btnCamera = view.findViewById(R.id.btn_camera);
        Button btnFile = view.findViewById(R.id.btn_file);

        flTitle.setVisibility(View.VISIBLE);
        if (fileListener == null) {
            tvTitle.setText("사진 업로드");
            btnFile.setVisibility(View.GONE);
        } else {
            tvTitle.setText("사진/파일 업로드");
            btnFile.setVisibility(View.VISIBLE);
        }

        tvMsg.setText("업로드 방법을 선택해 주세요.");

        btnGallery.setOnClickListener(new MyOnclickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                dismissPopDialog();
                if (galleryListener != null) {
                    galleryListener.onClick(v);
                }
            }
        });
        btnCamera.setOnClickListener(new MyOnclickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                dismissPopDialog();
                if (cameraListener != null) {
                    cameraListener.onClick(v);
                }
            }
        });
        btnFile.setOnClickListener(new MyOnclickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                dismissPopDialog();
                if (fileListener != null) {
                    fileListener.onClick(v);
                }
            }
        });

        showPopDialog(activity, view);
    }

    public void showCalendarDialog(Activity activity, boolean bIsShowDay, View target, OnCompleteParamListener completeListener) {
        createPopDialog(activity, true);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_calendar_month, null);
        view.setMinimumWidth(mMinWidth);

        NumberPicker monthPicker = view.findViewById(R.id.np_month);
        NumberPicker yearPicker = view.findViewById(R.id.np_year);
        NumberPicker dayPicker = view.findViewById(R.id.np_day);

        Button btnSelect = view.findViewById(R.id.btn_select);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        dayPicker.setVisibility(bIsShowDay ? View.VISIBLE : View.GONE);

        String yearMonthDay = ((EditText) target).getText().toString();
        int year, month, day = 1;
        int maxDay;
        if (!TextUtils.isEmpty(yearMonthDay)) {
            String[] ymd = yearMonthDay.split("-");
            year = Integer.parseInt(ymd[0]);
            month = Integer.parseInt(ymd[1]);
            if (bIsShowDay) {
                day = Integer.parseInt(ymd[2]);
            }
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            maxDay = cal.getMaximum(Calendar.DAY_OF_MONTH);
        } else {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1;
            if (bIsShowDay) {
                day = cal.get(Calendar.DAY_OF_MONTH);
            }
            maxDay = cal.getMaximum(Calendar.DAY_OF_MONTH);
        }

        yearPicker.setMinValue(1980);
        yearPicker.setMaxValue(2099);
        yearPicker.setValue(year);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(month);
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(maxDay);
        dayPicker.setValue(day);

        btnSelect.setOnClickListener(new MyOnclickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                if (completeListener != null) {
                    String date = String.format(Locale.KOREA, "%04d-%02d-%02d", yearPicker.getValue(), monthPicker.getValue(), dayPicker.getValue());
                    completeListener.onComplete(date);
                }

                dismissPopDialog();
            }
        });

        btnCancel.setOnClickListener(new MyOnclickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                dismissPopDialog();
            }
        });

        yearPicker.setOnValueChangedListener(
                (picker, oldVal, newVal) -> dayPicker.setMaxValue(Utils.getInstance().getMaxDay(yearPicker.getValue(), monthPicker.getValue()))
        );

        monthPicker.setOnValueChangedListener(
                (picker, oldVal, newVal) -> dayPicker.setMaxValue(Utils.getInstance().getMaxDay(yearPicker.getValue(), monthPicker.getValue()))
        );

        showPopDialog(activity, view);
    }

    public void showListDialog(Activity activity, String title, ArrayList<ContentValues> items, View target) {
        createPopDialog(activity, true);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
        view.setMinimumWidth(mMinWidth);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        ListAdapter adapter = new ListAdapter(activity, items, R.layout.item_list);
        rvList.setLayoutManager(new LinearLayoutManager(activity));
        rvList.setAdapter(adapter);
        //if (!TextUtils.isEmpty(title)) {
        //    tvTitle.setText(title);
        //}

        adapter.setOnItemClickListener(position -> {
            ContentValues item = items.get(position);
            ((EditText) target).setText(item.getAsString("name"));
            target.setTag(item.getAsString("idx"));
            dismissPopDialog();
        });

        showPopDialog(activity, view);
    }


    private static class ListAdapter extends BaseRecyclerAdapter {

        public ListAdapter(Context context, ArrayList<?> items, int viewId) {
            super(context, items, viewId);
        }

        @Override
        protected RecyclerView.ViewHolder onCreateViewHolderBase(View view, @NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(this, view);
        }

        @Override
        protected void onBindViewHolderBase(@NonNull RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            ContentValues item = (ContentValues) getItem(position);
            viewHolder.tvText.setText(item.getAsString("name"));
            viewHolder.tvText.setTag(item.getAsString("idx"));
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ListAdapter adapter;
        View itemView;
        TextView tvText;

        public ViewHolder(@NonNull ListAdapter adapter, @NonNull View itemView) {
            super(itemView);
            this.adapter = adapter;
            this.itemView = itemView;
            tvText = itemView.findViewById(R.id.tv_text);
            itemView.setOnClickListener(new MyOnclickListener() {
                @Override
                public void onClick(View v) {
                    super.onClick(v);
                    adapter.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
