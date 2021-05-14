package com.keepfun.aiservice.ui.adapter;

import android.app.Activity;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.FeedbackFile;
import com.keepfun.aiservice.network.BaseConnection;
import com.keepfun.aiservice.ui.act.FeedbackEditActivity;
import com.keepfun.blankj.util.ImageUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.easyphotos.models.album.entity.Photo;
import com.keepfun.imageselector.PreviewImageActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/28 2:50 PM
 */
public class FeedbackPicAdapter extends BaseQuickAdapter<FeedbackFile, BaseViewHolder> {

    public FeedbackPicAdapter(@Nullable List<FeedbackFile> data) {
        super(R.layout.service_item_feedback_pic, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, FeedbackFile file) {
        ImageView iv_pic = helper.getView(R.id.iv_pic);
        if (file == null) {
            iv_pic.setImageResource(R.mipmap.service_ic_pic_add);
        } else {
            if (FeedbackEditActivity.LOADING.equals(file.getUrl())) {
                ImageView loading = helper.getView(R.id.loading);
                Glide.with(getContext()).load(R.drawable.service_pic_uploading).into(loading);
            } else {
                Glide.with(getContext()).load(file.getUrl()).error(R.mipmap.service_ic_load_failed).into(iv_pic);
            }
        }

        helper.setVisible(R.id.layout_mask, file != null && FeedbackEditActivity.LOADING.equals(file.getUrl()));
        helper.setVisible(R.id.iv_delete, file != null &&!StringUtils.isEmpty(file.getUrl()) && !FeedbackEditActivity.LOADING.equals(file.getUrl()));
        addChildClickViewIds(R.id.iv_delete, R.id.iv_pic);
        bindViewClickListener(helper, getItemViewType(getItemPosition(file)));

    }

    @Override
    public int getItemCount() {
        if (super.getItemCount() < 6) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return getDefItemViewType(position);
    }

    @Override
    public FeedbackFile getItem(int position) {
        if (position < getData().size()) {
            return super.getItem(position);
        } else {
            return null;
        }
    }
}
