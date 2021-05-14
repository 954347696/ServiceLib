package com.keepfun.aiservice.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.FeedbackFile;
import com.keepfun.aiservice.network.BaseConnection;
import com.keepfun.aiservice.ui.act.VideoPreviewActivity;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.blankj.util.ImageUtils;
import com.keepfun.imageselector.PreviewActivity;
import com.keepfun.imageselector.PreviewImageActivity;
import com.keepfun.imageselector.entry.Image;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/28 2:50 PM
 */
public class FeedbackPicAdapter1 extends BaseQuickAdapter<FeedbackFile, BaseViewHolder> {

    public FeedbackPicAdapter1(@Nullable List<FeedbackFile> data) {
        super(R.layout.service_item_feedback_pic, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, FeedbackFile file) {
        helper.setGone(R.id.layout_mask, true);
        ImageView iv_pic = helper.getView(R.id.iv_pic);
        Glide.with(getContext()).load(file.getUrl()).into(iv_pic);
        helper.setVisible(R.id.iv_delete, false);
        addChildClickViewIds(R.id.iv_delete, R.id.iv_pic);
        bindViewClickListener(helper, getItemViewType(getItemPosition(file)));

        iv_pic.setOnClickListener(new CheckClickListener(v -> {
            if (file.getType() == 0) {
                ArrayList<String> pics = new ArrayList<>();
                pics.add(file.getUrl());
                PreviewImageActivity.openActivity((Activity) getContext(), pics, getItemPosition(file));
            }else{
                VideoPreviewActivity.start(file.getUrl());
            }
        }));
    }


}
