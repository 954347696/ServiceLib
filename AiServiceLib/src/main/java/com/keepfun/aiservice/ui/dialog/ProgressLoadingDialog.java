package com.keepfun.aiservice.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.keepfun.aiservice.R;

/**
 * loading加载框<p>
 *
 * @author zixuefei
 * @since 2019/6/10 10:31
 */
public class ProgressLoadingDialog extends AlertDialog {
    public ProgressLoadingDialog(Context context) {
        super(context, android.R.style.Theme_Material_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setDimAmount(0f);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
//        window.setWindowAnimations(R.style.ShareDialog);
        setContentView(R.layout.service_dialog_progress_loading);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {

    }
}
