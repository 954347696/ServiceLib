package com.keepfun.aiservice.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.ui.impl.CheckClickListener;

/**
 * @author yang
 * @description
 * @date 2020/12/14 3:05 PM
 */
public class CommonDialog extends AlertDialog {

    public CommonDialog(Context context) {
        super(context, R.style.UpdateDialog);
    }

    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_button_1;
    private View layout_button_2;
    private TextView tv_button_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
        setContentView(R.layout.service_dialog_general);
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_button_1 = findViewById(R.id.tv_button_1);
        layout_button_2 = findViewById(R.id.layout_button_2);
        tv_button_2 = findViewById(R.id.tv_button_2);

    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setContent(String content) {
        tv_content.setText(content);
    }

    public void setButton1(String txt, View.OnClickListener listener) {
        tv_button_1.setText(txt);
        tv_button_1.setOnClickListener(new CheckClickListener(listener));
    }

    public void setButton2Gone() {
        layout_button_2.setVisibility(View.GONE);
    }

    public void setButton2(String txt, View.OnClickListener listener) {
        layout_button_2.setVisibility(View.VISIBLE);
        tv_button_2.setText(txt);
        tv_button_2.setOnClickListener(new CheckClickListener(listener));
    }


}
