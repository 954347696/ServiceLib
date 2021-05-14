package com.keepfun.aiservice.ui.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.FeedbackDetail;
import com.keepfun.aiservice.entity.FeedbackFile;
import com.keepfun.aiservice.entity.FeedbackLabelBean;
import com.keepfun.aiservice.ui.adapter.FeedbackLabelAdapter;
import com.keepfun.aiservice.ui.adapter.FeedbackPicAdapter;
import com.keepfun.aiservice.ui.dialog.ChoosePhotoDialog;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.FeedbackEditPresenter;
import com.keepfun.aiservice.ui.view.ServiceTitleView;
import com.keepfun.aiservice.utils.GlideEngine;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.CollectionUtils;
import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.ThreadUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.blankj.util.Utils;
import com.keepfun.easyphotos.EasyPhotos;
import com.keepfun.easyphotos.models.album.entity.Photo;
import com.keepfun.imageselector.PreviewImageActivity;
import com.keepfun.imageselector.utils.ImageSelector;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yang
 * @description
 * @date 2020/8/31 7:12 PM
 */
public class FeedbackEditActivity extends PanActivity<FeedbackEditPresenter> implements View.OnClickListener {

    ServiceTitleView titleView;
    LinearLayout layout_feedback_type;
    RecyclerView rv_labels;
    EditText et_feedback_info;
    TextView tv_feedback_info_count;
    RecyclerView rv_photos;

    private List<FeedbackLabelBean> labels;
    private FeedbackLabelAdapter mFeedbackLabelAdapter;
    private List<FeedbackFile> photos;
    private FeedbackPicAdapter mFeedbackPicAdapter;

    private ChoosePhotoDialog choosePhotoDialog;
    private final int REQUEST_SELECTED_IMAGE = 11;
    public static final String LOADING = "loading";

    private FeedbackDetail mFeedbackDetail;

    @Override
    public FeedbackEditPresenter newP() {
        return new FeedbackEditPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_feedback_edit;
    }

    @Override
    public void bindUI(View rootView) {
        titleView = findViewById(R.id.titleView);
        layout_feedback_type = findViewById(R.id.layout_feedback_type);
        rv_labels = findViewById(R.id.rv_labels);
        et_feedback_info = findViewById(R.id.et_feedback_info);
        tv_feedback_info_count = findViewById(R.id.tv_feedback_info_count);
        rv_photos = findViewById(R.id.rv_photos);
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.tv_commit).setOnClickListener(new CheckClickListener(this));
        et_feedback_info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_feedback_info_count.setText(s.length() + " / 300");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mFeedbackDetail = (FeedbackDetail) bundle.getSerializable(Arguments.DATA);
        }
        titleView.setTitle(mFeedbackDetail == null ? "我要反馈" : "再次反馈");
        initLabels();
//        et_feedback_info.setText("不知道怎么充值，说右上角点击“网站”－“选择网站方式及金额”－“支付”即可。但是充值不了");
        initPhotos();
        if (mFeedbackDetail == null) {
            layout_feedback_type.setVisibility(View.VISIBLE);
            getP().getLabels();
        } else {
            layout_feedback_type.setVisibility(View.GONE);
        }
    }

    private void initPhotos() {
        rv_photos.setLayoutManager(new GridLayoutManager(this, 3));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.transport)
                .size(SizeUtils.dp2px(10f))
                .build();
        rv_photos.addItemDecoration(decor);
        if (photos == null) {
            photos = new ArrayList<>();
        }
        mFeedbackPicAdapter = new FeedbackPicAdapter(photos);
        mFeedbackPicAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_pic) {
                if (mFeedbackPicAdapter.getData().size() > position) {
                    FeedbackFile file = mFeedbackPicAdapter.getData().get(position);
                    if (file.getType() == 0) {
                        ArrayList<String> pics = new ArrayList<>();
                        pics.add(file.getUrl());
                        PreviewImageActivity.openActivity((Activity) getContext(), pics, position);
                    } else {
                        VideoPreviewActivity.start(file.getUrl());
                    }
                } else {
                    showChoosePhotoDialog();
                }
            } else if (view.getId() == R.id.iv_delete) {
                mFeedbackPicAdapter.removeAt(position);
            }
        });
        rv_photos.setAdapter(mFeedbackPicAdapter);
    }

    private void initLabels() {
        rv_labels.setLayoutManager(new GridLayoutManager(this, 4));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.transport)
                .size(SizeUtils.dp2px(10f))
                .build();
        rv_labels.addItemDecoration(decor);
        if (labels == null) {
            labels = new ArrayList<>();
        }
        mFeedbackLabelAdapter = new FeedbackLabelAdapter(labels);
        rv_labels.setAdapter(mFeedbackLabelAdapter);
    }

    public void getLabelsSuccess(List<FeedbackLabelBean> labels) {
//        Collections.sort(labels, (o1, o2) -> o1.getSort() - o2.getSort());
        this.labels.clear();
        this.labels.addAll(labels);
        mFeedbackLabelAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_commit) {
            commit();
        }
    }

    private void commit() {
        long type;
        if (mFeedbackDetail == null) {
            FeedbackLabelBean label = mFeedbackLabelAdapter.getSelectLabel();
            if (label == null) {
                showToast("请选择您的反馈类型");
                return;
            }
            type = label.getId();
        } else {
            type = mFeedbackDetail.getFeedbackTypeId();
        }
        String info = et_feedback_info.getText().toString().trim();
        if (StringUtils.isEmpty(info)) {
            showToast("请输入您的反馈信息");
            return;
        }
        for (FeedbackFile feedbackFile : photos) {
            if (LOADING.equals(feedbackFile.getUrl())) {
                showToast("图片上传中，请稍候");
                return;
            }
        }

        getP().commitFeedback(type, info, mFeedbackDetail == null ? -1 : mFeedbackDetail.getId(), mFeedbackPicAdapter.getData());
    }

    public void feedbackSuccess() {
        showToast("您的反馈已提交，请等待客服人员的回复");
        setResult(RESULT_OK);
        finish();
    }

    private void showChoosePhotoDialog() {
        if (choosePhotoDialog == null) {
            choosePhotoDialog = new ChoosePhotoDialog(getContext());
        }
        if (!choosePhotoDialog.isShowing()) {
            choosePhotoDialog.show();
            choosePhotoDialog.setOnSelectedDoneListener((String type) -> {
                selectedImage();
            });
        }
    }


    // 打开图库
    private void selectedImage() {
        EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                .setPuzzleMenu(false)
                .setCleanMenu(false)
                .setFileProviderAuthority(Utils.getApp().getPackageName() + ".utilcode.provider")
                .setCount(1)
                .setVideo(true)
                .setGif(true)
                .start(REQUEST_SELECTED_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                //选择头像返回
                case REQUEST_SELECTED_IMAGE:
                    ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                    LogUtils.e("-----select image result ----- " + resultPhotos);
                    if (!CollectionUtils.isEmpty(resultPhotos)) {
                        Photo photo = resultPhotos.get(0);
                        if (photo.type.contains("video")) {
                            if (photo.size > 20 * 1024 * 1024) {
                                ToastUtils.showShort("视频限制每个20M以内，请处理后重新上传");
                                return;
                            }
                        }else{
                            if (photo.size > 5 * 1024 * 1024) {
                                ToastUtils.showShort("图片每张限制5M以内，请处理后重新上传");
                                return;
                            }
                        }
                        getP().uploadImage(photo);
                    } else {
                        showToast("图像选取失败");
                        LogUtils.e("-----change bg failed-----");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void showUploadDialog() {
        ThreadUtils.runOnUiThread(() -> {
            photos.add(new FeedbackFile(0, LOADING, 0));
            mFeedbackPicAdapter.notifyDataSetChanged();
        });

    }

    public void uploadSuccess(FeedbackFile photo) {
        for (FeedbackFile feedbackFile : photos) {
            if (LOADING.equals(feedbackFile.getUrl())) {
                photos.remove(feedbackFile);
                break;
            }
        }
        photos.add(photo);
        mFeedbackPicAdapter.notifyDataSetChanged();
    }

    public void uploadFailed(String message) {
        for (FeedbackFile feedbackFile : photos) {
            if (LOADING.equals(feedbackFile.getUrl())) {
                photos.remove(feedbackFile);
                break;
            }
        }
        mFeedbackPicAdapter.notifyDataSetChanged();
        showToast(message);
    }
}
