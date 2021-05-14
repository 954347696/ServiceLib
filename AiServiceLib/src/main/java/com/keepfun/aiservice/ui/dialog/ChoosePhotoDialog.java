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

/**
 * 选择图片弹框<p>
 *
 * @author zixuefei
 * @since 2019/8/7 17:30
 */
public class ChoosePhotoDialog extends AlertDialog implements View.OnClickListener {
    TextView female;
    TextView male;
    TextView cancel;
    private onSelectedDoneListener onSelectedDoneListener;

    public ChoosePhotoDialog(@NonNull Context context) {
        super(context, R.style.UpdateDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
        setContentView(R.layout.service_dialog_choose_photo);
        female = findViewById(R.id.choose_photo_gallery);
        male = findViewById(R.id.choose_photo_look_large);
        cancel = findViewById(R.id.choose_photo_cancel);
        female.setOnClickListener(new CheckClickListener(this));
        male.setOnClickListener(new CheckClickListener(this));
        cancel.setOnClickListener(new CheckClickListener(this));

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.choose_photo_gallery) {
            if (onSelectedDoneListener != null) {
                onSelectedDoneListener.onSelectedDone("gallery");
            }
        } else if (id == R.id.choose_photo_look_large) {
            if (onSelectedDoneListener != null) {
                onSelectedDoneListener.onSelectedDone("lookLarge");
            }
        } else if (id == R.id.choose_photo_cancel) {
        }
        dismiss();
    }

    public void setOnSelectedDoneListener(onSelectedDoneListener onSelectedDoneListener) {
        this.onSelectedDoneListener = onSelectedDoneListener;
    }

    public interface onSelectedDoneListener {
        void onSelectedDone(String type);
    }
}
