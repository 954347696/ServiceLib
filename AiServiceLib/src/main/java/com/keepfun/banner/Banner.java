package com.keepfun.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.keepfun.aiservice.R;
import com.keepfun.banner.adapter.BannerAdapter;
import com.keepfun.banner.config.BannerConfig;
import com.keepfun.banner.config.IndicatorConfig;
import com.keepfun.banner.indicator.Indicator;
import com.keepfun.banner.listener.OnBannerListener;
import com.keepfun.banner.listener.OnPageChangeListener;
import com.keepfun.banner.transformer.MZScaleInTransformer;
import com.keepfun.banner.transformer.ScaleInTransformer;
import com.keepfun.banner.util.BannerLifecycleObserverAdapter;
import com.keepfun.banner.util.BannerUtils;
import com.keepfun.banner.util.BannerLifecycleObserver;
import com.keepfun.banner.util.LogUtils;
import com.keepfun.banner.util.ScrollSpeedManger;

import java.lang.annotation.Retention;
import java.lang.ref.WeakReference;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;


public class Banner<T, BA extends BannerAdapter> extends FrameLayout implements BannerLifecycleObserver {
    public static final int INVALID_VALUE = -1;
    private ViewPager2 mViewPager2;
    private AutoLoopTask mLoopTask;
    private OnPageChangeListener mOnPageChangeListener;
    private BA mAdapter;
    private Indicator mIndicator;
    private CompositePageTransformer mCompositePageTransformer;
    private BannerOnPageChangeCallback mPageChangeCallback;

    // ???????????????????????????????????????????????????
    private boolean mIsInfiniteLoop = BannerConfig.IS_INFINITE_LOOP;
    // ??????????????????
    private boolean mIsAutoLoop = BannerConfig.IS_AUTO_LOOP;
    // ????????????????????????
    private long mLoopTime = BannerConfig.LOOP_TIME;
    // ??????????????????
    private int mScrollTime = BannerConfig.SCROLL_TIME;
    // ??????????????????
    private int mStartPosition = 1;
    // banner????????????
    private float mBannerRadius = 0;

    // ?????????????????????
    private int normalWidth = BannerConfig.INDICATOR_NORMAL_WIDTH;
    private int selectedWidth = BannerConfig.INDICATOR_SELECTED_WIDTH;
    private int normalColor = BannerConfig.INDICATOR_NORMAL_COLOR;
    private int selectedColor = BannerConfig.INDICATOR_SELECTED_COLOR;
    private int indicatorGravity = IndicatorConfig.Direction.CENTER;
    private int indicatorSpace;
    private int indicatorMargin;
    private int indicatorMarginLeft;
    private int indicatorMarginTop;
    private int indicatorMarginRight;
    private int indicatorMarginBottom;
    private int indicatorHeight = BannerConfig.INDICATOR_HEIGHT;
    private int indicatorRadius = BannerConfig.INDICATOR_RADIUS;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    // ??????????????????
    private int mTouchSlop;
    // ???????????????????????????????????????????????????????????????
    private float mStartX, mStartY;
    // ??????viewpager2???????????????
    private boolean mIsViewPager2Drag;
    // ?????????????????????
    private boolean isIntercept = true;

    //??????????????????
    private Paint mRoundPaint;
    private Paint mImagePaint;

    @Retention(SOURCE)
    @IntDef( {HORIZONTAL, VERTICAL})
    public @interface Orientation {
    }

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initTypedArray(context, attrs);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() / 2;
        mCompositePageTransformer = new CompositePageTransformer();
        mPageChangeCallback = new BannerOnPageChangeCallback();
        mLoopTask = new AutoLoopTask(this);
        mViewPager2 = new ViewPager2(context);
        mViewPager2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mViewPager2.setOffscreenPageLimit(1);
        mViewPager2.registerOnPageChangeCallback(mPageChangeCallback);
        mViewPager2.setPageTransformer(mCompositePageTransformer);
        ScrollSpeedManger.reflectLayoutManager(this);
        addView(mViewPager2);

        mRoundPaint = new Paint();
        mRoundPaint.setColor(Color.WHITE);
        mRoundPaint.setAntiAlias(true);
        mRoundPaint.setStyle(Paint.Style.FILL);
        mRoundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mImagePaint = new Paint();
        mImagePaint.setXfermode(null);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IcsBanner);
        mBannerRadius = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_radius, 0);
        mLoopTime = a.getInt(R.styleable.IcsBanner_icsBanner_loop_time, BannerConfig.LOOP_TIME);
        mIsAutoLoop = a.getBoolean(R.styleable.IcsBanner_icsBanner_auto_loop, BannerConfig.IS_AUTO_LOOP);
        mIsInfiniteLoop = a.getBoolean(R.styleable.IcsBanner_icsBanner_infinite_loop, BannerConfig.IS_INFINITE_LOOP);
        normalWidth = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_indicator_normal_width, BannerConfig.INDICATOR_NORMAL_WIDTH);
        selectedWidth = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_indicator_selected_width, BannerConfig.INDICATOR_SELECTED_WIDTH);
        normalColor = a.getColor(R.styleable.IcsBanner_icsBanner_indicator_normal_color, BannerConfig.INDICATOR_NORMAL_COLOR);
        selectedColor = a.getColor(R.styleable.IcsBanner_icsBanner_indicator_selected_color, BannerConfig.INDICATOR_SELECTED_COLOR);
        indicatorGravity = a.getInt(R.styleable.IcsBanner_icsBanner_indicator_gravity, IndicatorConfig.Direction.CENTER);
        indicatorSpace = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_indicator_space, 0);
        indicatorMargin = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_indicator_margin, 0);
        indicatorMarginLeft = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_indicator_marginLeft, 0);
        indicatorMarginTop = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_indicator_marginTop, 0);
        indicatorMarginRight = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_indicator_marginRight, 0);
        indicatorMarginBottom = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_indicator_marginBottom, 0);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_indicator_height, BannerConfig.INDICATOR_HEIGHT);
        indicatorRadius = a.getDimensionPixelSize(R.styleable.IcsBanner_icsBanner_indicator_radius, BannerConfig.INDICATOR_RADIUS);
        int orientation = a.getInt(R.styleable.IcsBanner_icsBanner_orientation, HORIZONTAL);
        setOrientation(orientation);
        setInfiniteLoop();
        a.recycle();
    }

    private void initIndicatorAttr() {
        if (indicatorMargin != 0) {
            setIndicatorMargins(new IndicatorConfig.Margins(indicatorMargin));
        } else if (indicatorMarginLeft != 0
                || indicatorMarginTop != 0
                || indicatorMarginRight != 0
                || indicatorMarginBottom != 0) {
            setIndicatorMargins(new IndicatorConfig.Margins(
                    indicatorMarginLeft,
                    indicatorMarginTop,
                    indicatorMarginRight,
                    indicatorMarginBottom));
        }
        if (indicatorSpace > 0) {
            setIndicatorSpace(indicatorSpace);
        }
        if (indicatorGravity != IndicatorConfig.Direction.CENTER) {
            setIndicatorGravity(indicatorGravity);
        }
        if (normalWidth > 0) {
            setIndicatorNormalWidth(normalWidth);
        }
        if (selectedWidth > 0) {
            setIndicatorSelectedWidth(selectedWidth);
        }

        if (indicatorHeight > 0) {
            setIndicatorHeight(indicatorHeight);
        }
        if (indicatorRadius > 0) {
            setIndicatorRadius(indicatorRadius);
        }
        setIndicatorNormalColor(normalColor);
        setIndicatorSelectedColor(selectedColor);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!getViewPager2().isUserInputEnabled()) {
            return super.dispatchTouchEvent(ev);
        }

        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_OUTSIDE) {
            start();
        } else if (action == MotionEvent.ACTION_DOWN) {
            stop();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!getViewPager2().isUserInputEnabled() || !isIntercept) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float distanceX = Math.abs(endX - mStartX);
                float distanceY = Math.abs(endY - mStartY);
                if (getViewPager2().getOrientation() == HORIZONTAL) {
                    mIsViewPager2Drag = distanceX > mTouchSlop && distanceX > distanceY;
                } else {
                    mIsViewPager2Drag = distanceY > mTouchSlop && distanceY > distanceX;
                }
                getParent().requestDisallowInterceptTouchEvent(mIsViewPager2Drag);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mBannerRadius > 0) {
            canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), mImagePaint, Canvas.ALL_SAVE_FLAG);
            super.dispatchDraw(canvas);
            //???????????????????????????
            drawTopLeft(canvas);
            drawTopRight(canvas);
            drawBottomLeft(canvas);
            drawBottomRight(canvas);
            canvas.restore();
        } else {
            super.dispatchDraw(canvas);
        }
    }

    private void drawTopLeft(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, mBannerRadius);
        path.lineTo(0, 0);
        path.lineTo(mBannerRadius, 0);
        path.arcTo(new RectF(0, 0, mBannerRadius * 2, mBannerRadius * 2),
                -90, -90);
        path.close();
        canvas.drawPath(path, mRoundPaint);
    }

    private void drawTopRight(Canvas canvas) {
        int width = getWidth();
        Path path = new Path();
        path.moveTo(width - mBannerRadius, 0);
        path.lineTo(width, 0);
        path.lineTo(width, mBannerRadius);
        path.arcTo(new RectF(width - 2 * mBannerRadius, 0, width,
                mBannerRadius * 2), 0, -90);
        path.close();
        canvas.drawPath(path, mRoundPaint);
    }

    private void drawBottomLeft(Canvas canvas) {
        int height = getHeight();
        Path path = new Path();
        path.moveTo(0, height - mBannerRadius);
        path.lineTo(0, height);
        path.lineTo(mBannerRadius, height);
        path.arcTo(new RectF(0, height - 2 * mBannerRadius,
                mBannerRadius * 2, height), 90, 90);
        path.close();
        canvas.drawPath(path, mRoundPaint);
    }

    private void drawBottomRight(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        Path path = new Path();
        path.moveTo(width - mBannerRadius, height);
        path.lineTo(width, height);
        path.lineTo(width, height - mBannerRadius);
        path.arcTo(new RectF(width - 2 * mBannerRadius, height - 2
                * mBannerRadius, width, height), 0, 90);
        path.close();
        canvas.drawPath(path, mRoundPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    class BannerOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        private int mTempPosition = INVALID_VALUE;
        private boolean isScrolled;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = BannerUtils.getRealPosition(isInfiniteLoop(), position, getRealCount());
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            }
            if (mIndicator != null) {
                mIndicator.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (isScrolled) {
                mTempPosition = position;
                int realPosition = BannerUtils.getRealPosition(isInfiniteLoop(), position, getRealCount());
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(realPosition);
                }
                if (mIndicator != null) {
                    mIndicator.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //???????????????,?????????????????????
            if (state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING) {
                isScrolled = true;
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                //???????????????????????????
                isScrolled = false;
                if (mTempPosition != INVALID_VALUE && mIsInfiniteLoop) {
                    if (mTempPosition == 0) {
                        setCurrentItem(getRealCount(), false);
                    } else if (mTempPosition == getItemCount() - 1) {
                        setCurrentItem(1, false);
                    }
                }
            }
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
            if (mIndicator != null) {
                mIndicator.onPageScrollStateChanged(state);
            }
        }

    }

    static class AutoLoopTask implements Runnable {
        private final WeakReference<Banner> reference;

        AutoLoopTask(Banner banner) {
            this.reference = new WeakReference<>(banner);
        }

        @Override
        public void run() {
            Banner banner = reference.get();
            if (banner != null && banner.mIsAutoLoop) {
                int count = banner.getItemCount();
                if (count == 0) {
                    return;
                }
                int next = (banner.getCurrentItem() + 1) % count;
                banner.setCurrentItem(next);
                banner.postDelayed(banner.mLoopTask, banner.mLoopTime);
            }
        }
    }

    private RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (getItemCount() <= 1) {
                stop();
            } else {
                start();
            }
            setIndicatorPageChange();
        }
    };

    private void initIndicator() {
        if (mIndicator == null || getAdapter() == null) {
            return;
        }
        if (mIndicator.getIndicatorConfig().isAttachToBanner()) {
            removeIndicator();
            addView(mIndicator.getIndicatorView());
        }
        initIndicatorAttr();
        setIndicatorPageChange();
    }

    private void setInfiniteLoop() {
        // ???????????????????????????????????????????????????
        if (!isInfiniteLoop()) {
            isAutoLoop(false);
        }
        setStartPosition(isInfiniteLoop() ? 1 : 0);
    }

    private void setRecyclerViewPadding(int itemPadding) {
        setRecyclerViewPadding(itemPadding, itemPadding);
    }

    private void setRecyclerViewPadding(int leftItemPadding, int rightItemPadding) {
        RecyclerView recyclerView = (RecyclerView) getViewPager2().getChildAt(0);
        if (getViewPager2().getOrientation() == ViewPager2.ORIENTATION_VERTICAL) {
            recyclerView.setPadding(0, leftItemPadding, 0, rightItemPadding);
        } else {
            recyclerView.setPadding(leftItemPadding, 0, rightItemPadding, 0);
        }
        recyclerView.setClipToPadding(false);
    }

    /**
     * **********************************************************************
     * ------------------------ ????????????API ---------------------------------*
     * **********************************************************************
     */

    public int getCurrentItem() {
        return getViewPager2().getCurrentItem();
    }

    public int getItemCount() {
        if (getAdapter() == null) {
            return 0;
        }
        return getAdapter().getItemCount();
    }

    public int getScrollTime() {
        return mScrollTime;
    }

    public boolean isInfiniteLoop() {
        return mIsInfiniteLoop;
    }

    public BA getAdapter() {
        if (mAdapter == null) {
            LogUtils.e(getContext().getString(R.string.banner_adapter_use_error));
        }
        return mAdapter;
    }

    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    public Indicator getIndicator() {
        if (mIndicator == null) {
            LogUtils.e(getContext().getString(R.string.indicator_null_error));
        }
        return mIndicator;
    }

    public IndicatorConfig getIndicatorConfig() {
        if (getIndicator() != null) {
            return getIndicator().getIndicatorConfig();
        }
        return null;
    }

    /**
     * ??????banner????????????
     */
    public int getRealCount() {
        return getAdapter().getRealCount();
    }

    //-----------------------------------------------------------------------------------------

    /**
     * ?????????????????????
     * @param intercept
     * @return
     */
    public Banner setIntercept(boolean intercept) {
        isIntercept = intercept;
        return this;
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     * @param position
     * @return
     */
    public Banner setCurrentItem(int position) {
        return setCurrentItem(position, true);
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     * @param position
     * @param smoothScroll
     * @return
     */
    public Banner setCurrentItem(int position, boolean smoothScroll) {
        getViewPager2().setCurrentItem(position, smoothScroll);
        return this;
    }

    public Banner setIndicatorPageChange() {
        if (mIndicator != null) {
            int realPosition = BannerUtils.getRealPosition(isInfiniteLoop(), getCurrentItem(), getRealCount());
            mIndicator.onPageChanged(getRealCount(), realPosition);
        }
        return this;
    }

    public Banner removeIndicator() {
        if (mIndicator != null) {
            removeView(mIndicator.getIndicatorView());
        }
        return this;
    }


    /**
     * ????????????????????? (?????????setAdapter??????setDatas????????????????????????)
     */
    public Banner setStartPosition(int mStartPosition) {
        this.mStartPosition = mStartPosition;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param enabled true ?????????false ??????
     */
    public Banner setUserInputEnabled(boolean enabled) {
        getViewPager2().setUserInputEnabled(enabled);
        return this;
    }

    /**
     * ??????PageTransformer?????????????????????
     * {@link ViewPager2.PageTransformer}
     * ????????????????????????implementation "androidx.viewpager2:viewpager2:1.0.0"
     */
    public Banner addPageTransformer(@Nullable ViewPager2.PageTransformer transformer) {
        mCompositePageTransformer.addTransformer(transformer);
        return this;
    }

    /**
     * ??????PageTransformer??????addPageTransformer??????????????????????????????transformer
     */
    public Banner setPageTransformer(@Nullable ViewPager2.PageTransformer transformer) {
        getViewPager2().setPageTransformer(transformer);
        return this;
    }

    public Banner removeTransformer(ViewPager2.PageTransformer transformer) {
        mCompositePageTransformer.removeTransformer(transformer);
        return this;
    }

    /**
     * ?????? ItemDecoration
     */
    public Banner addItemDecoration(RecyclerView.ItemDecoration decor) {
        getViewPager2().addItemDecoration(decor);
        return this;
    }

    public Banner addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        getViewPager2().addItemDecoration(decor, index);
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param isAutoLoop ture ?????????false ?????????
     */
    public Banner isAutoLoop(boolean isAutoLoop) {
        this.mIsAutoLoop = isAutoLoop;
        return this;
    }


    /**
     * ????????????????????????
     *
     * @param loopTime ??????????????????
     */
    public Banner setLoopTime(long loopTime) {
        this.mLoopTime = loopTime;
        return this;
    }

    /**
     * ?????????????????????????????????
     */
    public Banner setScrollTime(int scrollTime) {
        this.mScrollTime = scrollTime;
        return this;
    }

    /**
     * ????????????
     */
    public Banner start() {
        if (mIsAutoLoop) {
            stop();
            postDelayed(mLoopTask, mLoopTime);
        }
        return this;
    }

    /**
     * ????????????
     */
    public Banner stop() {
        if (mIsAutoLoop) {
            removeCallbacks(mLoopTask);
        }
        return this;
    }

    /**
     * ??????????????????
     */
    public void destroy() {
        if (getViewPager2() != null && mPageChangeCallback != null) {
            getViewPager2().unregisterOnPageChangeCallback(mPageChangeCallback);
            mPageChangeCallback = null;
        }
        stop();
    }

    /**
     * ??????banner????????????
     */
    public Banner setAdapter(BA adapter) {
        if (adapter == null) {
            throw new NullPointerException(getContext().getString(R.string.banner_adapter_null_error));
        }
        this.mAdapter = adapter;
        if (!isInfiniteLoop()) {
            mAdapter.setIncreaseCount(0);
        }
        mAdapter.registerAdapterDataObserver(mAdapterDataObserver);
        mViewPager2.setAdapter(adapter);
        setCurrentItem(mStartPosition, false);
        initIndicator();
        return this;
    }

    /**
     * ??????banner????????????
     * @param adapter
     * @param isInfiniteLoop ????????????????????????
     * @return
     */
    public Banner setAdapter(BA adapter,boolean isInfiniteLoop) {
        mIsInfiniteLoop=isInfiniteLoop;
        setInfiniteLoop();
        setAdapter(adapter);
        return this;
    }


    /**
     * ????????????banner?????????????????????????????????adapter?????????????????????,???????????????????????????????????????????????????
     *
     * @param datas ?????????????????????null??????datas??????????????????banner????????????????????????????????????UI??????
     */
    public Banner setDatas(List<T> datas) {
        if (getAdapter() != null) {
            getAdapter().setDatas(datas);
            getAdapter().notifyDataSetChanged();
            setCurrentItem(mStartPosition, false);
            setIndicatorPageChange();
            start();
        }
        return this;
    }

    /**
     * ??????banner????????????
     *
     * @param orientation {@link Orientation}
     */
    public Banner setOrientation(@Orientation int orientation) {
        getViewPager2().setOrientation(orientation);
        return this;
    }

    /**
     * ????????????????????????
     */
    public Banner setTouchSlop(int mTouchSlop) {
        this.mTouchSlop = mTouchSlop;
        return this;
    }

    /**
     * ??????????????????
     */
    public Banner setOnBannerListener(OnBannerListener listener) {
        if (getAdapter() != null) {
            getAdapter().setOnBannerListener(listener);
        }
        return this;
    }

    /**
     * ??????viewpager????????????
     * <p>
     * ???viewpager2???????????????{@link ViewPager2.OnPageChangeCallback}?????????????????????
     * ??????????????????????????????????????????viewpager?????????{@link ViewPager.OnPageChangeListener}??????
     * </p>
     */
    public Banner addOnPageChangeListener(OnPageChangeListener pageListener) {
        this.mOnPageChangeListener = pageListener;
        return this;
    }

    /**
     * ??????banner??????
     * <p>
     * ?????????????????????????????????????????????????????????0??????
     *
     * @param radius ????????????
     */
    public Banner setBannerRound(float radius) {
        mBannerRadius = radius;
        return this;
    }

    /**
     * ??????banner??????(??????????????????????????????????????????????????????)????????????5.0??????
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Banner setBannerRound2(float radius) {
        BannerUtils.setBannerRound(this, radius);
        return this;
    }

    /**
     * ???banner??????????????????
     *
     * @param itemWidth  item?????????????????????,??????dp
     * @param pageMargin ????????????,??????dp
     */
    public Banner setBannerGalleryEffect(int itemWidth, int pageMargin) {
        return setBannerGalleryEffect(itemWidth, pageMargin, .85f);
    }

    /**
     * ???banner??????????????????
     *
     * @param leftItemWidth  item??????????????????,??????dp
     * @param rightItemWidth  item??????????????????,??????dp
     * @param pageMargin ????????????,??????dp
     */
    public Banner setBannerGalleryEffect(int leftItemWidth, int rightItemWidth, int pageMargin) {
        return setBannerGalleryEffect(leftItemWidth,rightItemWidth, pageMargin, .85f);
    }

    /**
     * ???banner??????????????????
     *
     * @param itemWidth  item?????????????????????,??????dp
     * @param pageMargin ????????????,??????dp
     * @param scale      ??????[0-1],1???????????????
     */
    public Banner setBannerGalleryEffect(int itemWidth, int pageMargin, float scale) {
        return setBannerGalleryEffect(itemWidth, itemWidth, pageMargin, scale);
    }

    /**
     * ???banner??????????????????
     *
     * @param leftItemWidth  item??????????????????,??????dp
     * @param rightItemWidth  item??????????????????,??????dp
     * @param pageMargin ????????????,??????dp
     * @param scale      ??????[0-1],1???????????????
     */
    public Banner setBannerGalleryEffect(int leftItemWidth, int rightItemWidth, int pageMargin, float scale) {
        if (pageMargin > 0) {
            addPageTransformer(new MarginPageTransformer((int) BannerUtils.dp2px(pageMargin)));
        }
        if (scale < 1 && scale > 0) {
            addPageTransformer(new ScaleInTransformer(scale));
        }
        setRecyclerViewPadding(leftItemWidth > 0 ? (int) BannerUtils.dp2px(leftItemWidth + pageMargin) : 0,
                rightItemWidth > 0 ? (int) BannerUtils.dp2px(rightItemWidth + pageMargin) : 0);
        return this;
    }

    /**
     * ???banner??????????????????
     *
     * @param itemWidth item?????????????????????,??????dp
     */
    public Banner setBannerGalleryMZ(int itemWidth) {
        return setBannerGalleryMZ(itemWidth, .88f);
    }

    /**
     * ???banner??????????????????
     *
     * @param itemWidth item?????????????????????,??????dp
     * @param scale     ??????[0-1],1???????????????
     */
    public Banner setBannerGalleryMZ(int itemWidth, float scale) {
        if (scale < 1 && scale > 0) {
            addPageTransformer(new MZScaleInTransformer(scale));
        }
        setRecyclerViewPadding((int) BannerUtils.dp2px(itemWidth));
        return this;
    }

    /**
     * **********************************************************************
     * ------------------------ ????????????????????? --------------------------------*
     * **********************************************************************
     */

    /**
     * ?????????????????????(?????????banner???)
     */
    public Banner setIndicator(Indicator indicator) {
        return setIndicator(indicator, true);
    }

    /**
     * ?????????????????????(?????????????????????????????????????????????attachToBanner???false)
     *
     * @param attachToBanner ???????????????????????????banner??????false ?????????????????????????????????????????????????????????
     *                       ??????????????????false??????????????? setIndicatorGravity()???setIndicatorMargins() ??????????????????
     *                       ???????????????????????????????????????????????????????????????????????????????????????????????????demo
     */
    public Banner setIndicator(Indicator indicator, boolean attachToBanner) {
        removeIndicator();
        indicator.getIndicatorConfig().setAttachToBanner(attachToBanner);
        this.mIndicator = indicator;
        initIndicator();
        return this;
    }


    public Banner setIndicatorSelectedColor(@ColorInt int color) {
        if (mIndicator != null) {
            mIndicator.getIndicatorConfig().setSelectedColor(color);
        }
        return this;
    }

    public Banner setIndicatorSelectedColorRes(@ColorRes int color) {
        setIndicatorSelectedColor(ContextCompat.getColor(getContext(), color));
        return this;
    }

    public Banner setIndicatorNormalColor(@ColorInt int color) {
        if (mIndicator != null) {
            mIndicator.getIndicatorConfig().setNormalColor(color);
        }
        return this;
    }

    public Banner setIndicatorNormalColorRes(@ColorRes int color) {
        setIndicatorNormalColor(ContextCompat.getColor(getContext(), color));
        return this;
    }

    public Banner setIndicatorGravity(@IndicatorConfig.Direction int gravity) {
        if (mIndicator != null && mIndicator.getIndicatorConfig().isAttachToBanner()) {
            mIndicator.getIndicatorConfig().setGravity(gravity);
            mIndicator.getIndicatorView().postInvalidate();
        }
        return this;
    }

    public Banner setIndicatorSpace(int indicatorSpace) {
        if (mIndicator != null) {
            mIndicator.getIndicatorConfig().setIndicatorSpace(indicatorSpace);
        }
        return this;
    }

    public Banner setIndicatorMargins(IndicatorConfig.Margins margins) {
        if (mIndicator != null && mIndicator.getIndicatorConfig().isAttachToBanner()) {
            mIndicator.getIndicatorConfig().setMargins(margins);
            mIndicator.getIndicatorView().requestLayout();
        }
        return this;
    }

    public Banner setIndicatorWidth(int normalWidth, int selectedWidth) {
        if (mIndicator != null) {
            mIndicator.getIndicatorConfig().setNormalWidth(normalWidth);
            mIndicator.getIndicatorConfig().setSelectedWidth(selectedWidth);
        }
        return this;
    }

    public Banner setIndicatorNormalWidth(int normalWidth) {
        if (mIndicator != null) {
            mIndicator.getIndicatorConfig().setNormalWidth(normalWidth);
        }
        return this;
    }

    public Banner setIndicatorSelectedWidth(int selectedWidth) {
        if (mIndicator != null) {
            mIndicator.getIndicatorConfig().setSelectedWidth(selectedWidth);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorRadius(int indicatorRadius) {
        if (mIndicator != null) {
            mIndicator.getIndicatorConfig().setRadius(indicatorRadius);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorHeight(int indicatorHeight) {
        if (mIndicator != null) {
            mIndicator.getIndicatorConfig().setHeight(indicatorHeight);
        }
        return this;
    }

    /**
     * **********************************************************************
     * ------------------------ ?????????????????? --------------------------------*
     * **********************************************************************
     */

    public Banner addBannerLifecycleObserver(LifecycleOwner owner) {
        if (owner != null) {
            owner.getLifecycle().addObserver(new BannerLifecycleObserverAdapter(owner, this));
        }
        return this;
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        start();
    }

    @Override
    public void onStop(LifecycleOwner owner) {
        stop();
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        destroy();
    }

}
