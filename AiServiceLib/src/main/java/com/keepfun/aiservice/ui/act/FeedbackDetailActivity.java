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
//                .from(result.getAnswer()) // ?????????
//                .type(RichType.html) // ????????????,??????????????????Html,??????fromMarkdown????????????Markdown??????
                .autoFix(true) // ???????????????????????????true
//                .autoPlay(true) // gif????????????????????????
//                .showBorder(true) // ????????????????????????
//                .borderColor(Color.RED) // ??????????????????
//                .borderSize(3) // ????????????
//                .borderRadius(50) // ????????????????????????
                .scaleType(ImageHolder.ScaleType.center_crop) // ??????????????????
                .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT) // ???????????????????????????
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
                }) // ?????????????????????????????????
//                .fixLink(linkFixCallback) // ???????????????????????????
                .noImage(false) // ??????????????????????????????
                .resetSize(false) // ??????false???????????????img?????????????????????????????????img???????????????????????????????????????true????????????????????????????????????SIZE_READY?????????false?????????img????????????????????????????????????SIZE_READY??????
                .clickable(true) // ???????????????????????????????????????????????????????????????
                .imageClick((imageUrls, position) -> {
                    //
                    LogUtils.e("imageClicked position : " + position + "    imageUrls : " + imageUrls);
                    PreviewImageActivity.openActivity(FeedbackDetailActivity.this, new ArrayList<>(imageUrls), position);
                }) // ????????????????????????
                .imageLongClick(new OnImageLongClickListener() {
                    @Override
                    public boolean imageLongClicked(List<String> imageUrls, int position) {
                        LogUtils.e("imageClicked position : " + position + "    imageUrls : " + imageUrls);
                        PreviewImageActivity.openActivity(FeedbackDetailActivity.this, new ArrayList<>(imageUrls), position);
                        return false;
                    }
                }) // ????????????????????????
//                .urlClick(onURLClickListener) // ????????????????????????
//                .urlLongClick(onUrlLongClickListener) // ????????????????????????
//                .placeHolder((holder, config, textView) -> getContext().getDrawable(R.mipmap.service_ic_banner_default)) // ?????????????????????????????????
//                .errorImage((holder, config, textView) -> getContext().getDrawable(R.mipmap.service_ic_banner_default))
//                .error(R.mipmap.service_ic_banner_default) // ??????????????????????????????
                .cache(CacheType.none) // ????????????????????????Cache.ALL????????????????????????????????????????????????????????????
//                .imageGetter(new DefaultImageGetter()) // ?????????????????????????????????DefaultImageGetter?????????okhttp??????
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
                .imageDownloader(new DefaultImageDownloader()) // ??????DefaultImageGetter??????????????????
//                .imageDownloader(new OkHttpImageDownloader()) // ??????DefaultImageGetter??????????????????
                .bind(this) // ??????richText???????????????object???????????????????????????
                .done(new Callback() {
                    @Override
                    public void done(boolean imageLoadDone) {
                        LogUtils.e("imageClicked imageLoadDone : " + imageLoadDone);
                    }
                }) // ??????????????????
                .into(tv_feedback_reply) // ????????????TextView
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
        ToastUtils.showShort("????????????");
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
