package com.keepfun.login.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.keepfun.login.R;
import com.keepfun.login.entity.GlCountryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2018/7/26 16:12
 *
 * @author Geminier
 */
public class MyCitySiderBar extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
//    public String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
//            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
//            "W", "X", "Y", "Z", "#"};

    public List<GlCountryEntity> siderList = new ArrayList<>();

    private int choose = -1;// 选中
    private Paint paint = new Paint();

    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public void setLetters(List<GlCountryEntity> siderList) {
        this.siderList = siderList;
        invalidate();
    }


    public MyCitySiderBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyCitySiderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCitySiderBar(Context context) {
        super(context);
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == siderList || 0 == siderList.size()) {
            return;
        }
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / siderList.size();// 获取每一个字母的高度

        for (int i = 0; i < siderList.size(); i++) {
//			paint.setColor(Color.rgb(33, 65, 98));
            paint.setColor(Color.parseColor("#ff828282"));
//			paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(30);
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#77000000"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            String name = siderList.get(i).getCnName().replace("城市", "");
            float xPos = width / 2 - paint.measureText(name) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(name, xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null == siderList || 0 == siderList.size()) {
            return true;
        }
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * siderList.size());// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                setBackgroundResource(R.drawable.sort_sidebar_background);
                if (oldChoose != c) {
                    if (c >= 0 && c < siderList.size()) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(siderList.get(c));
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(siderList.get(c).getCnName());
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(GlCountryEntity cityBean);
    }

}