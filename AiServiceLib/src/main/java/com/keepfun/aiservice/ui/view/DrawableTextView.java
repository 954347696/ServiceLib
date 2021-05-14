package com.keepfun.aiservice.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;

import com.keepfun.aiservice.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import skin.support.content.res.SkinCompatVectorResources;
import skin.support.widget.SkinCompatTextView;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;
import static skin.support.widget.SkinCompatHelper.checkResourceId;

/**
 * @author yang
 * @description
 * @date 2020/8/31 2:36 PM
 */
public class DrawableTextView extends SkinCompatTextView {

    private Drawable[] drawables;
    private int[] drawableRes;
    private int[] widths;
    private int[] heights;

    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;


    @IntDef({
            LEFT,
            TOP,
            RIGHT,
            BOTTOM
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawGravity {
    }

    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawables = new Drawable[4];
        drawableRes = new int[4];
        widths = new int[4];
        heights = new int[4];
        setGravity(Gravity.CENTER);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.IcsDrawableTextView);
        drawableRes[0] = array.getResourceId(R.styleable.IcsDrawableTextView_icsLeftDrawable, INVALID_ID);
        drawableRes[1] = array.getResourceId(R.styleable.IcsDrawableTextView_icsTopDrawable, INVALID_ID);
        drawableRes[2] = array.getResourceId(R.styleable.IcsDrawableTextView_icsRightDrawable, INVALID_ID);
        drawableRes[3] = array.getResourceId(R.styleable.IcsDrawableTextView_icsBottomDrawable, INVALID_ID);

        widths[0] = array.getDimensionPixelSize(R.styleable.IcsDrawableTextView_icsLeftDrawableWidth, 0);
        widths[1] = array.getDimensionPixelSize(R.styleable.IcsDrawableTextView_icsTopDrawableWidth, 0);
        widths[2] = array.getDimensionPixelSize(R.styleable.IcsDrawableTextView_icsRightDrawableWidth, 0);
        widths[3] = array.getDimensionPixelSize(R.styleable.IcsDrawableTextView_icsBottomDrawableWidth, 0);

        heights[0] = array.getDimensionPixelSize(R.styleable.IcsDrawableTextView_icsLeftDrawableHeight, 0);
        heights[1] = array.getDimensionPixelSize(R.styleable.IcsDrawableTextView_icsTopDrawableHeight, 0);
        heights[2] = array.getDimensionPixelSize(R.styleable.IcsDrawableTextView_icsRightDrawableHeight, 0);
        heights[3] = array.getDimensionPixelSize(R.styleable.IcsDrawableTextView_icsBottomDrawableHeight, 0);

        array.recycle();

        setDrawable(LEFT);
        setDrawable(RIGHT);
        setDrawable(TOP);
        setDrawable(BOTTOM);
    }

    private void setDrawable(@DrawGravity int gravity) {
        drawableRes[gravity] = checkResourceId(drawableRes[gravity]);
        if (drawableRes[gravity] != INVALID_ID) {
            drawables[gravity] = SkinCompatVectorResources.getDrawableCompat(getContext(), drawableRes[gravity]);
        }
    }

    public void setDrawable(@DrawGravity int gravity, Drawable drawable, int width, int height) {
        drawables[gravity] = drawable;
        widths[gravity] = width;
        heights[gravity] = height;
        postInvalidate();
    }

    public void setDrawable(@DrawGravity int gravity, @DrawableRes int drawableRes, int width, int height) {
        this.drawableRes[gravity] = drawableRes;
        widths[gravity] = width;
        heights[gravity] = height;
        setDrawable(gravity);
        postInvalidate();
    }

    public void setDrawables(Drawable[] drawables, int[] widths, int[] heights) {
        if (drawables != null && drawables.length >= 4
                && widths != null && widths.length >= 4
                && heights != null && heights.length >= 4) {
            this.drawables = drawables;
            this.widths = widths;
            this.heights = heights;
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int drawablePadding = getCompoundDrawablePadding();
        translateText(canvas, drawablePadding);
        super.onDraw(canvas);

        float centerX = (getWidth() + getPaddingLeft() - getPaddingRight()) / 2;
        float centerY = (getHeight() + getPaddingTop() - getPaddingBottom()) / 2;

        float halfTextWidth = getPaint().measureText(getText().toString().isEmpty() ? getHint().toString() : getText().toString()) / 2;
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        float halfTextHeight = (fontMetrics.descent - fontMetrics.ascent) / 2;

        if (drawables[0] != null) {
            int left = (int) (centerX - drawablePadding - halfTextWidth - widths[0]);
            int top = (int) (centerY - heights[0] / 2);
            drawables[0].setBounds(
                    left,
                    top,
                    left + widths[0],
                    top + heights[0]);
            canvas.save();
            drawables[0].draw(canvas);
            canvas.restore();
        }


        if (drawables[2] != null) {
            int left = (int) (centerX + halfTextWidth + drawablePadding);
            int top = (int) (centerY - heights[2] / 2);
            drawables[2].setBounds(
                    left,
                    top,
                    left + widths[2],
                    top + heights[2]);
            canvas.save();
            drawables[2].draw(canvas);
            canvas.restore();
        }

        if (drawables[1] != null) {
            int left = (int) (centerX - widths[1] / 2);
            int bottom = (int) (centerY - halfTextHeight - drawablePadding);
            drawables[1].setBounds(
                    left,
                    bottom - heights[1],
                    left + widths[1],
                    bottom);
            canvas.save();
            drawables[1].draw(canvas);
            canvas.restore();
        }


        if (drawables[3] != null) {
            int left = (int) (centerX - widths[3] / 2);
            int top = (int) (centerY + halfTextHeight + drawablePadding);
            drawables[3].setBounds(
                    left,
                    top,
                    left + widths[3],
                    top + heights[3]);
            canvas.save();
            drawables[3].draw(canvas);
            canvas.restore();
        }
    }

    private void translateText(Canvas canvas, int drawablePadding) {

        int translateWidth = 0;
        if (drawables[0] != null && drawables[2] != null) {
            translateWidth = (widths[0] - widths[2]) / 2;
        } else if (drawables[0] != null) {
            translateWidth = (widths[0] + drawablePadding) / 2;
        } else if (drawables[2] != null) {
            translateWidth = -(widths[2] + drawablePadding) / 2;
        }

        int translateHeight = 0;
        if (drawables[1] != null && drawables[3] != null) {
            translateHeight = (heights[1] - heights[3]) / 2;
        } else if (drawables[1] != null) {
            translateHeight = (heights[1] + drawablePadding) / 2;
        } else if (drawables[3] != null) {
            translateHeight = -(heights[3] - drawablePadding) / 2;
        }

        canvas.translate(translateWidth, translateHeight);
    }

}