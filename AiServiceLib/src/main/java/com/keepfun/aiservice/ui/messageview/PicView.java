package com.keepfun.aiservice.ui.messageview;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.utils.GlideRoundTransform;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.TimeUtils;
import com.keepfun.imageselector.PreviewImageActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author yang
 * @description
 * @date 2021/4/20 9:42 AM
 */
public class PicView extends BaseMsgView {

    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        long mUserId = GlobalDataHelper.getInstance().getImUserInfo().getId();
        if (mUserId == message.getFromUserId()) {
            helper.setGone(R.id.layout_other, true);
            helper.setGone(R.id.iv_content_self, false);
            ImageView iv_content_self = helper.getView(R.id.iv_content_self);
            Glide.with(helper.itemView.getContext()).load(message.getContent()).error(R.mipmap.service_ic_load_failed)
                    .apply(new RequestOptions().transform(new GlideRoundTransform(helper.itemView.getContext(), 10))).into(iv_content_self);
            iv_content_self.setOnClickListener(v -> {
                ArrayList<String> images = new ArrayList<>();
                images.add(message.getContent());
                PreviewImageActivity.openActivity(ActivityUtils.getTopActivity(), images, 0);
            });
        } else {
            helper.setGone(R.id.layout_other, false);
            helper.setGone(R.id.iv_content_self, true);
            helper.setText(R.id.tv_nickname, message.getFromUserName());
            ImageView iv_avatar = helper.getView(R.id.iv_avatar);
            Glide.with(helper.itemView.getContext()).load(message.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_avatar);
            ImageView iv_content_other = helper.getView(R.id.iv_content_other);
            Glide.with(helper.itemView.getContext()).load(message.getContent()).error(R.mipmap.service_ic_load_failed)
                    .apply(new RequestOptions().transform(new GlideRoundTransform(helper.itemView.getContext(), 10))).into(iv_content_other);
            iv_content_other.setOnClickListener(v -> {
                ArrayList<String> images = new ArrayList<>();
                images.add(message.getContent());
                PreviewImageActivity.openActivity(ActivityUtils.getTopActivity(), images, 0);
            });
        }
    }

}
