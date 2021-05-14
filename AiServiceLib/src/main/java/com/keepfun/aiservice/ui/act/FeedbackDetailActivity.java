package com.keepfun.aiservice.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.FeedbackDetail;
import com.keepfun.aiservice.ui.adapter.FeedbackPicAdapter;
import com.keepfun.aiservice.ui.adapter.FeedbackPicAdapter1;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.FeedbackDetailPresenter;
import com.keepfun.aiservice.utils.HtmlImageGetter;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ScreenUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.TimeUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.easyphotos.utils.bitmap.BitmapUtils;
import com.keepfun.imageselector.PreviewImageActivity;
import com.zzhoujay.okhttpimagedownloader.OkHttpImageDownloader;
import com.zzhoujay.richtext.CacheType;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.RichTextConfig;
import com.zzhoujay.richtext.RichType;
import com.zzhoujay.richtext.callback.BitmapStream;
import com.zzhoujay.richtext.callback.Callback;
import com.zzhoujay.richtext.callback.DrawableGetter;
import com.zzhoujay.richtext.callback.ImageFixCallback;
import com.zzhoujay.richtext.callback.ImageGetter;
import com.zzhoujay.richtext.callback.ImageLoadNotify;
import com.zzhoujay.richtext.callback.OnImageClickListener;
import com.zzhoujay.richtext.callback.OnImageLongClickListener;
import com.zzhoujay.richtext.ig.DefaultImageDownloader;
import com.zzhoujay.richtext.ig.DefaultImageGetter;
import com.zzhoujay.richtext.ig.ImageDownloader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cache;


/**
 * @author yang
 * @description
 * @date 2020/9/1 10:54 AM
 */
public class FeedbackDetailActivity extends PanActivity<FeedbackDetailPresenter> implements View.OnClickListener {

    View layout_feedback;
    View layout_feedback_again;
    View layout_btn;

    private long feedbackId;
    private FeedbackDetail mFeedbackDetail;

    private static final int REQUEST_FOR_EDIT = 0X100;

    @Override
    public FeedbackDetailPresenter newP() {
        return new FeedbackDetailPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_feedback_detail;
    }

    @Override
    public void bindUI(View rootView) {
        layout_feedback = findViewById(R.id.layout_feedback);
        layout_feedback_again = findViewById(R.id.layout_feedback_again);
        layout_btn = findViewById(R.id.layout_btn);
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.tv_dealed).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_undeal).setOnClickListener(new CheckClickListener(this));
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            feedbackId = bundle.getLong(Arguments.DATA);
        }
        getP().getFeedbackDetail(feedbackId);
    }

    public void getDetailSuccess(FeedbackDetail result) {
        this.mFeedbackDetail = result;
        initFeedbackInfo(layout_feedback, result);
        if (result.getChildren() == null) {
            layout_feedback_again.setVisibility(View.GONE);
        } else {
            layout_feedback_again.setVisibility(View.VISIBLE);
            initFeedbackInfo(layout_feedback_again, result.getChildren());
        }
        layout_btn.setVisibility(result.getFinish() == 1 || result.getChildren() != null ? View.GONE : View.VISIBLE);
    }

    private void initFeedbackInfo(View layout, FeedbackDetail result) {
        layout.findViewById(R.id.tv_feedback_again_tip).setVisibility(result.getType() == 2 ? View.VISIBLE : View.GONE);
        TextView tv_feedback_type = layout.findViewById(R.id.tv_feedback_type);
        tv_feedback_type.setText(result.getFeedbackTypeName());
        TextView tv_feedback_info = layout.findViewById(R.id.tv_feedback_info);
        tv_feedback_info.setText(result.getFeedback());
        RecyclerView rv_pics = layout.findViewById(R.id.rv_pics);
        rv_pics.setLayoutManager(new GridLayoutManager(getContext(), 3));
        if (result.getFiles().size() != 0) {
            FeedbackPicAdapter1 picAdapter = new FeedbackPicAdapter1(result.getFiles());
            rv_pics.setAdapter(picAdapter);
        }
        TextView tv_feedback_create_time = layout.findViewById(R.id.tv_feedback_create_time);
        tv_feedback_create_time.setText(TimeUtils.millis2String(result.getCreateTime(), "yyyy/MM/dd  HH:mm"));

        TextView tv_feedback_reply = layout.findViewById(R.id.tv_feedback_reply);
//        tv_feedback_reply.setText(Html.fromHtml(result.getAnswer(), new HtmlImageGetter(getContext(), tv_feedback_reply), null));
        LogUtils.e("imageClicked txt : " + result.getAnswer());
        RichText.initCacheDir(getContext());
        RichText.fromHtml(result.getAnswer())
//                .from(result.getAnswer()) // 数据源
//                .type(RichType.html) // 数据格式,不设置默认是Html,使用fromMarkdown的默认是Markdown格式
                .autoFix(true) // 是否自动修复，默认true
//                .autoPlay(true) // gif图片是否自动播放
//                .showBorder(true) // 是否显示图片边框
//                .borderColor(Color.RED) // 图片边框颜色
//                .borderSize(3) // 边框尺寸
//                .borderRadius(50) // 图片边框圆角弧度
                .scaleType(ImageHolder.ScaleType.center_crop) // 图片缩放方式
                .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT) // 图片占位区域的宽高
                .fix(new ImageFixCallback() {
                    @Override
                    public void onInit(ImageHolder holder) {

                    }

                    @Override
                    public void onLoading(ImageHolder holder) {

                    }

                    @Override
                    public void onSizeReady(ImageHolder holder, int imageWidth, int imageHeight, ImageHolder.SizeHolder sizeHolder) {
                        if (imageWidth > ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30)) {
                            sizeHolder.setScale(((ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30)) * 1f) / imageWidth);
                        }
                    }

                    @Override
                    public void onImageReady(ImageHolder holder, int width, int height) {
                        LogUtils.e("onImageReady holder state : " + holder.getImageState());
                    }

                    @Override
                    public void onFailure(ImageHolder holder, Exception e) {

                    }
                }) // 设置自定义修复图片宽高
//                .fixLink(linkFixCallback) // 设置链接自定义回调
                .noImage(false) // 不显示并且不加载图片
                .resetSize(false) // 默认false，是否忽略img标签中的宽高尺寸（只在img标签中存在宽高时才有效），true：忽略标签中的尺寸并触发SIZE_READY回调，false：使用img标签中的宽高尺寸，不触发SIZE_READY回调
                .clickable(true) // 是否可点击，默认只有设置了点击监听才可点击
                .imageClick((imageUrls, position) -> {
                    //
                    LogUtils.e("imageClicked position : " + position + "    imageUrls : " + imageUrls);
                    PreviewImageActivity.openActivity(FeedbackDetailActivity.this, new ArrayList<>(imageUrls), position);
                }) // 设置图片点击回调
                .imageLongClick(new OnImageLongClickListener() {
                    @Override
                    public boolean imageLongClicked(List<String> imageUrls, int position) {
                        LogUtils.e("imageClicked position : " + position + "    imageUrls : " + imageUrls);
                        PreviewImageActivity.openActivity(FeedbackDetailActivity.this, new ArrayList<>(imageUrls), position);
                        return false;
                    }
                }) // 设置图片长按回调
//                .urlClick(onURLClickListener) // 设置链接点击回调
//                .urlLongClick(onUrlLongClickListener) // 设置链接长按回调
//                .placeHolder((holder, config, textView) -> getContext().getDrawable(R.mipmap.service_ic_banner_default)) // 设置加载中显示的占位图
//                .errorImage((holder, config, textView) -> getContext().getDrawable(R.mipmap.service_ic_banner_default))
//                .error(R.mipmap.service_ic_banner_default) // 设置加载失败的错误图
                .cache(CacheType.none) // 缓存类型，默认为Cache.ALL（缓存图片和图片大小信息和文本样式信息）
//                .imageGetter(new DefaultImageGetter()) // 设置图片加载器，默认为DefaultImageGetter，使用okhttp实现
                .imageGetter(new ImageGetter() {
                    @Override
                    public void registerImageLoadNotify(ImageLoadNotify imageLoadNotify) {

                    }

                    @Override
                    public Drawable getDrawable(ImageHolder holder, RichTextConfig config, TextView textView) {
                        Drawable drawable = null;
                        URL url;
                        LogUtils.e("");
                        try {
                            url = new URL(holder.getSource());
                            drawable = Drawable.createFromStream(url.openStream(), "");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                        float scale = 1;
                        if (drawable.getIntrinsicWidth() > ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30)) {
                            scale=((ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30)) * 1f) / drawable.getIntrinsicWidth();
                        }
                        drawable.setBounds(0, 0, ((int)(drawable.getIntrinsicWidth()*scale)),
                                ((int)(drawable.getIntrinsicHeight()*scale)));

                        return drawable;
                    }

                    @Override
                    public void recycle() {

                    }
                })
                .imageDownloader(new DefaultImageDownloader()) // 设置DefaultImageGetter的图片下载器
//                .imageDownloader(new OkHttpImageDownloader()) // 设置DefaultImageGetter的图片下载器
                .bind(this) // 绑定richText对象到某个object上，方便后面的清理
                .done(new Callback() {
                    @Override
                    public void done(boolean imageLoadDone) {
                        LogUtils.e("imageClicked imageLoadDone : " + imageLoadDone);
                    }
                }) // 解析完成回调
                .into(tv_feedback_reply) // 设置目标TextView
        ;

        TextView tv_feedback_reply_time = layout.findViewById(R.id.tv_feedback_reply_time);
        tv_feedback_reply_time.setVisibility(result.hasReply() ? View.VISIBLE : View.GONE);
        tv_feedback_reply_time.setText(TimeUtils.millis2String(result.getAnswerTime(), "yyyy/MM/dd  HH:mm"));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_dealed) {
            if (mFeedbackDetail != null) {
                getP().feedbackSolved(mFeedbackDetail.getId());
            }
        } else if (v.getId() == R.id.tv_undeal) {
            if (mFeedbackDetail != null && mFeedbackDetail.getChildren() == null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Arguments.DATA, mFeedbackDetail);
                ActivityUtils.startActivityForResult(bundle, FeedbackDetailActivity.this, FeedbackEditActivity.class, REQUEST_FOR_EDIT);
            }
        }
    }

    public void feedbackSolved() {
        ToastUtils.showShort("操作成功");
        layout_btn.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_EDIT && resultCode == RESULT_OK) {
            getP().getFeedbackDetail(feedbackId);
        }
    }
}
