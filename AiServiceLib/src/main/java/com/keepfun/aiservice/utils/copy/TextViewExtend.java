package com.keepfun.aiservice.utils.copy;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

import com.keepfun.aiservice.R;

public class TextViewExtend extends AppCompatTextView {
    private SelectableTextHelper mSelectableTextHelper;

    public TextViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSelectableTextHelper();
    }

    public TextViewExtend(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSelectableTextHelper();
    }

    public void initSelectableTextHelper(){
        if (mSelectableTextHelper == null){
            mSelectableTextHelper = new SelectableTextHelper.Builder(this)
                    .setSelectedColor(getResources().getColor(R.color.selected_blue))
                    .setCursorHandleSizeInDp(20)
                    .setCursorHandleColor(getResources().getColor(R.color.cursor_handle_color))
                    .build();
        }
    }
}
