//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.blankj.util;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.SystemClock;
import android.util.Log;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.keepfun.blankj.util.ShadowUtils.DrawableWrapper;

public class ClickUtils {
    private static final int PRESSED_VIEW_SCALE_TAG = -1;
    private static final float PRESSED_VIEW_SCALE_DEFAULT_VALUE = -0.06F;
    private static final int PRESSED_VIEW_ALPHA_TAG = -2;
    private static final int PRESSED_VIEW_ALPHA_SRC_TAG = -3;
    private static final float PRESSED_VIEW_ALPHA_DEFAULT_VALUE = 0.8F;
    private static final int PRESSED_BG_ALPHA_STYLE = 4;
    private static final float PRESSED_BG_ALPHA_DEFAULT_VALUE = 0.9F;
    private static final int PRESSED_BG_DARK_STYLE = 5;
    private static final float PRESSED_BG_DARK_DEFAULT_VALUE = 0.9F;
    private static final int DEBOUNCING_TAG = -7;
    private static final long DEBOUNCING_DEFAULT_VALUE = 200L;
    private static final long TIP_DURATION = 2000L;
    private static long sLastClickMillis;
    private static int sClickCount;

    private ClickUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static long lastClickTime;
    @IdRes
    private static int lastClickView = 0;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        //500毫秒内按钮无效，这样可以控制快速点击，自己调整频率
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 检测点击是否无效
     *
     * @param v
     * @return
     */
    public static boolean checkInvalidClick(View v, long clickTime) {
        if (v == null) {
            return false;
        }
        boolean isDurationInvalid = lastClickTime >= clickTime - 800;

        //如果当前点击间隔小于500毫秒
        if (v.getId() == lastClickView && isDurationInvalid) {
            return true;
        }
        //记录上次点击时间
        lastClickTime = clickTime;
        //记录上次有效点击view
        lastClickView = v.getId();
        return false;

    }

    public static void applyPressedViewScale(View... views) {
        applyPressedViewScale(views, (float[]) null);
    }

    public static void applyPressedViewScale(View[] views, float[] scaleFactors) {
        if (views != null && views.length != 0) {
            for (int i = 0; i < views.length; ++i) {
                if (scaleFactors != null && i < scaleFactors.length) {
                    applyPressedViewScale(views[i], scaleFactors[i]);
                } else {
                    applyPressedViewScale(views[i], -0.06F);
                }
            }

        }
    }

    public static void applyPressedViewScale(View view, float scaleFactor) {
        if (view != null) {
            view.setTag(-1, scaleFactor);
            view.setClickable(true);
            view.setOnTouchListener(ClickUtils.OnUtilsTouchListener.getInstance());
        }
    }

    public static void applyPressedViewAlpha(View... views) {
        applyPressedViewAlpha(views, (float[]) null);
    }

    public static void applyPressedViewAlpha(View[] views, float[] alphas) {
        if (views != null && views.length != 0) {
            for (int i = 0; i < views.length; ++i) {
                if (alphas != null && i < alphas.length) {
                    applyPressedViewAlpha(views[i], alphas[i]);
                } else {
                    applyPressedViewAlpha(views[i], 0.8F);
                }
            }

        }
    }

    public static void applyPressedViewAlpha(View view, float alpha) {
        if (view != null) {
            view.setTag(-2, alpha);
            view.setTag(-3, view.getAlpha());
            view.setClickable(true);
            view.setOnTouchListener(ClickUtils.OnUtilsTouchListener.getInstance());
        }
    }

    public static void applyPressedBgAlpha(View view) {
        applyPressedBgAlpha(view, 0.9F);
    }

    public static void applyPressedBgAlpha(View view, float alpha) {
        applyPressedBgStyle(view, 4, alpha);
    }

    public static void applyPressedBgDark(View view) {
        applyPressedBgDark(view, 0.9F);
    }

    public static void applyPressedBgDark(View view, float darkAlpha) {
        applyPressedBgStyle(view, 5, darkAlpha);
    }

    private static void applyPressedBgStyle(View view, int style, float value) {
        if (view != null) {
            Drawable background = view.getBackground();
            Object tag = view.getTag(-style);
            if (tag instanceof Drawable) {
                ViewCompat.setBackground(view, (Drawable) tag);
            } else {
                background = createStyleDrawable(background, style, value);
                ViewCompat.setBackground(view, background);
                view.setTag(-style, background);
            }

        }
    }

    private static Drawable createStyleDrawable(Drawable src, int style, float value) {
        if (src == null) {
            src = new ColorDrawable(0);
        }

        if (((Drawable) src).getConstantState() == null) {
            return (Drawable) src;
        } else {
            Drawable pressed = ((Drawable) src).getConstantState().newDrawable().mutate();
            if (style == 4) {
                pressed = createAlphaDrawable(pressed, value);
            } else if (style == 5) {
                pressed = createDarkDrawable(pressed, value);
            }

            Drawable disable = ((Drawable) src).getConstantState().newDrawable().mutate();
            disable = createAlphaDrawable(disable, 0.5F);
            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[]{16842919}, pressed);
            drawable.addState(new int[]{-16842910}, disable);
            drawable.addState(StateSet.WILD_CARD, (Drawable) src);
            return drawable;
        }
    }

    private static Drawable createAlphaDrawable(Drawable drawable, float alpha) {
        ClickUtils.DrawableWrapperBefore21 drawableWrapper = new ClickUtils.DrawableWrapperBefore21(drawable);
        drawableWrapper.setAlphaFix((int) (alpha * 255.0F));
        return drawableWrapper;
    }

    private static Drawable createDarkDrawable(Drawable drawable, float alpha) {
        ClickUtils.DrawableWrapperBefore21 drawableWrapper = new ClickUtils.DrawableWrapperBefore21(drawable);
        drawableWrapper.setColorFilterFix(getDarkColorFilter(alpha));
        return drawableWrapper;
    }

    private static ColorMatrixColorFilter getDarkColorFilter(float darkAlpha) {
        return new ColorMatrixColorFilter(new ColorMatrix(new float[]{darkAlpha, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, darkAlpha, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, darkAlpha, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 0.0F}));
    }

    public static void applySingleDebouncing(View view, OnClickListener listener) {
        applySingleDebouncing(new View[]{view}, listener);
    }

    public static void applySingleDebouncing(View view, @IntRange(from = 0L) long duration, OnClickListener listener) {
        applySingleDebouncing(new View[]{view}, duration, listener);
    }

    public static void applySingleDebouncing(View[] views, OnClickListener listener) {
        applySingleDebouncing(views, 200L, listener);
    }

    public static void applySingleDebouncing(View[] views, @IntRange(from = 0L) long duration, OnClickListener listener) {
        applyDebouncing(views, false, duration, listener);
    }

    public static void applyGlobalDebouncing(View view, OnClickListener listener) {
        applyGlobalDebouncing(new View[]{view}, listener);
    }

    public static void applyGlobalDebouncing(View view, @IntRange(from = 0L) long duration, OnClickListener listener) {
        applyGlobalDebouncing(new View[]{view}, duration, listener);
    }

    public static void applyGlobalDebouncing(View[] views, OnClickListener listener) {
        applyGlobalDebouncing(views, 200L, listener);
    }

    public static void applyGlobalDebouncing(View[] views, @IntRange(from = 0L) long duration, OnClickListener listener) {
        applyDebouncing(views, true, duration, listener);
    }

    private static void applyDebouncing(View[] views, boolean isGlobal, @IntRange(from = 0L) long duration, final OnClickListener listener) {
        if (views != null && views.length != 0 && listener != null) {
            View[] var5 = views;
            int var6 = views.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                View view = var5[var7];
                if (view != null) {
                    view.setOnClickListener(new ClickUtils.OnDebouncingClickListener(isGlobal, duration) {
                        public void onDebouncingClick(View v) {
                            listener.onClick(v);
                        }
                    });
                }
            }

        }
    }

    public static void expandClickArea(@NonNull View view, int expandSize) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            expandClickArea(view, expandSize, expandSize, expandSize, expandSize);
        }
    }

    public static void expandClickArea(@NonNull final View view, final int expandSizeTop, final int expandSizeLeft, final int expandSizeRight, final int expandSizeBottom) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            final View parentView = (View) view.getParent();
            if (parentView == null) {
                Log.e("ClickUtils", "expandClickArea must have parent view.");
            } else {
                parentView.post(new Runnable() {
                    public void run() {
                        Rect rect = new Rect();
                        view.getHitRect(rect);
                        rect.top -= expandSizeTop;
                        rect.bottom += expandSizeBottom;
                        rect.left -= expandSizeLeft;
                        rect.right += expandSizeRight;
                        parentView.setTouchDelegate(new TouchDelegate(rect, view));
                    }
                });
            }
        }
    }

    public static void back2HomeFriendly(CharSequence tip) {
        back2HomeFriendly(tip, 2000L, ClickUtils.Back2HomeFriendlyListener.DEFAULT);
    }

    public static void back2HomeFriendly(@NonNull CharSequence tip, long duration, @NonNull ClickUtils.Back2HomeFriendlyListener listener) {
        if (tip == null) {
            throw new NullPointerException("Argument 'tip' of type CharSequence (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (listener == null) {
            throw new NullPointerException("Argument 'listener' of type Back2HomeFriendlyListener (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            long nowMillis = System.currentTimeMillis();
            if (nowMillis - sLastClickMillis < duration) {
                ++sClickCount;
                if (sClickCount == 2) {
                    UtilsBridge.startHomeActivity();
                    listener.dismiss();
                    sLastClickMillis = 0L;
                }
            } else {
                sClickCount = 1;
                listener.show(tip, duration);
                sLastClickMillis = nowMillis;
            }

        }
    }

    static class DrawableWrapperBefore21 extends DrawableWrapper {
        private BitmapDrawable mBitmapDrawable = null;
        private Paint mColorPaint = null;

        public DrawableWrapperBefore21(Drawable drawable) {
            super(drawable);
            if (drawable instanceof ColorDrawable) {
                this.mColorPaint = new Paint(5);
                this.mColorPaint.setColor(((ColorDrawable) drawable).getColor());
            }

        }

        public void setColorFilter(ColorFilter cf) {
        }

        public void setColorFilterFix(ColorFilter cf) {
            super.setColorFilter(cf);
            if (this.mColorPaint != null) {
                this.mColorPaint.setColorFilter(cf);
            }

        }

        public void setAlpha(int alpha) {
        }

        public void setAlphaFix(int alpha) {
            super.setAlpha(alpha);
            if (this.mColorPaint != null) {
                this.mColorPaint.setColor(((ColorDrawable) this.getWrappedDrawable()).getColor());
            }

        }

        public void draw(Canvas canvas) {
            if (this.mBitmapDrawable == null) {
                Bitmap bitmap = Bitmap.createBitmap(this.getBounds().width(), this.getBounds().height(), Config.ARGB_8888);
                Canvas myCanvas = new Canvas(bitmap);
                if (this.mColorPaint != null) {
                    myCanvas.drawRect(this.getBounds(), this.mColorPaint);
                } else {
                    super.draw(myCanvas);
                }

                this.mBitmapDrawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                this.mBitmapDrawable.setBounds(this.getBounds());
            }

            this.mBitmapDrawable.draw(canvas);
        }
    }

    private static class OnUtilsTouchListener implements OnTouchListener {
        public static ClickUtils.OnUtilsTouchListener getInstance() {
            return ClickUtils.OnUtilsTouchListener.LazyHolder.INSTANCE;
        }

        private OnUtilsTouchListener() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == 0) {
                this.processScale(v, true);
                this.processAlpha(v, true);
            } else if (action == 1 || action == 3) {
                this.processScale(v, false);
                this.processAlpha(v, false);
            }

            return false;
        }

        private void processScale(View view, boolean isDown) {
            Object tag = view.getTag(-1);
            if (tag instanceof Float) {
                float value = isDown ? 1.0F + (Float) tag : 1.0F;
                view.animate().scaleX(value).scaleY(value).setDuration(200L).start();
            }
        }

        private void processAlpha(View view, boolean isDown) {
            Object tag = view.getTag(isDown ? -2 : -3);
            if (tag instanceof Float) {
                view.setAlpha((Float) tag);
            }
        }

        private static class LazyHolder {
            private static final ClickUtils.OnUtilsTouchListener INSTANCE = new ClickUtils.OnUtilsTouchListener();

            private LazyHolder() {
            }
        }
    }

    public abstract static class OnMultiClickListener implements OnClickListener {
        private static final long INTERVAL_DEFAULT_VALUE = 666L;
        private final int mTriggerClickCount;
        private final long mClickInterval;
        private long mLastClickTime;
        private int mClickCount;

        public OnMultiClickListener(int triggerClickCount) {
            this(triggerClickCount, 666L);
        }

        public OnMultiClickListener(int triggerClickCount, long clickInterval) {
            this.mTriggerClickCount = triggerClickCount;
            this.mClickInterval = clickInterval;
        }

        public abstract void onTriggerClick(View var1);

        public abstract void onBeforeTriggerClick(View var1, int var2);

        public void onClick(View v) {
            if (this.mTriggerClickCount <= 1) {
                this.onTriggerClick(v);
            } else {
                long curTime = System.currentTimeMillis();
                if (curTime - this.mLastClickTime < this.mClickInterval) {
                    ++this.mClickCount;
                    if (this.mClickCount == this.mTriggerClickCount) {
                        this.onTriggerClick(v);
                    } else if (this.mClickCount < this.mTriggerClickCount) {
                        this.onBeforeTriggerClick(v, this.mClickCount);
                    } else {
                        this.mClickCount = 1;
                        this.onBeforeTriggerClick(v, this.mClickCount);
                    }
                } else {
                    this.mClickCount = 1;
                    this.onBeforeTriggerClick(v, this.mClickCount);
                }

                this.mLastClickTime = curTime;
            }
        }
    }

    public abstract static class OnDebouncingClickListener implements OnClickListener {
        private static boolean mEnabled = true;
        private static final Runnable ENABLE_AGAIN = new Runnable() {
            public void run() {
                ClickUtils.OnDebouncingClickListener.mEnabled = true;
            }
        };
        private long mDuration;
        private boolean mIsGlobal;

        private static boolean isValid(@NonNull View view, long duration) {
            if (view == null) {
                throw new NullPointerException("Argument 'view' of type View (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            } else {
                long curTime = System.currentTimeMillis();
                Object tag = view.getTag(-7);
                if (!(tag instanceof Long)) {
                    view.setTag(-7, curTime);
                    return true;
                } else {
                    long preTime = (Long) tag;
                    if (curTime - preTime < 0L) {
                        view.setTag(-7, curTime);
                        return false;
                    } else if (curTime - preTime <= duration) {
                        return false;
                    } else {
                        view.setTag(-7, curTime);
                        return true;
                    }
                }
            }
        }

        public OnDebouncingClickListener() {
            this(true, 200L);
        }

        public OnDebouncingClickListener(boolean isGlobal) {
            this(isGlobal, 200L);
        }

        public OnDebouncingClickListener(long duration) {
            this(true, duration);
        }

        public OnDebouncingClickListener(boolean isGlobal, long duration) {
            this.mIsGlobal = isGlobal;
            this.mDuration = duration;
        }

        public abstract void onDebouncingClick(View var1);

        public final void onClick(View v) {
            if (this.mIsGlobal) {
                if (mEnabled) {
                    mEnabled = false;
                    v.postDelayed(ENABLE_AGAIN, this.mDuration);
                    this.onDebouncingClick(v);
                }
            } else if (isValid(v, this.mDuration)) {
                this.onDebouncingClick(v);
            }

        }
    }

    public interface Back2HomeFriendlyListener {
        ClickUtils.Back2HomeFriendlyListener DEFAULT = new ClickUtils.Back2HomeFriendlyListener() {
            public void show(CharSequence text, long duration) {
                UtilsBridge.toastShowShort(text);
            }

            public void dismiss() {
                UtilsBridge.toastCancel();
            }
        };

        void show(CharSequence var1, long var2);

        void dismiss();
    }
}
