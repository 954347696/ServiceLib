package com.keepfun.aiservice.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.blankj.util.SizeUtils;

/**
 * 选择图片弹框<p>
 *
 * @author zixuefei
 * @since 2019/8/7 17:30
 */
public class EndSessionDialog extends AlertDialog implements View.OnClickListener {

    private OnButtonClickListener mOnButtonClickListener;

    public EndSessionDialog(@NonNull Context context) {
        super(context, R.style.UpdateDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setPadding(SizeUtils.dp2px(30), 0, SizeUtils.dp2px(30), 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
        setContentView(R.layout.service_dialog_end_session);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_end).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
        } else if (id == R.id.tv_end) {
            if (mOnButtonClickListener != null) {
                mOnButtonClickListener.onEndClick();
            }
        }
        dismiss();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {
        void onEndClick();
    }
}
