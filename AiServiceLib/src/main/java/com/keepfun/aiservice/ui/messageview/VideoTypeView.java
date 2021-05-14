package com.keepfun.aiservice.ui.messageview;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.ui.act.VideoPreviewActivity;
import com.keepfun.aiservice.utils.GlideRoundTransform;
import com.keepfun.blankj.util.TimeUtils;

/**
 * @author yang
 * @description
 * @date 2021/4/20 10:05 AM
 */
public class VideoTypeView extends BaseMsgView {
    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        if (mUserId == message.getFromUserId()) {
            helper.setGone(R.id.layout_other, true);
            helper.setGone(R.id.layout_content_self, false);
            ImageView iv_content_self = helper.getView(R.id.iv_content_self);
            helper.setText(R.id.tv_duration_self, TimeUtils.millis2FitTimeSpan(message.getTimeDuration()));
            Glide.with(helper.itemView.getContext()).load(message.getContent()).error(R.mipmap.service_ic_load_failed)
                    .apply(new RequestOptions().transform(new GlideRoundTransform(helper.itemView.getContext(), 10))).into(iv_content_self);
            iv_content_self.setOnClickListener(v -> {
                VideoPreviewActivity.start(message.getContent());
            });
        } else {
            helper.setGone(R.id.layout_other, false);
            helper.setGone(R.id.layout_content_self, true);
            helper.setText(R.id.tv_nickname, message.getFromUserName());
            helper.setText(R.id.tv_duration_other, TimeUtils.millis2FitTimeSpan(message.getTimeDuration()));
            ImageView iv_avatar = helper.getView(R.id.iv_avatar);
            Glide.with(helper.itemView.getContext()).load(message.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_avatar);
            ImageView iv_content_other = helper.getView(R.id.iv_content_other);
            Glide.with(helper.itemView.getContext()).load(message.getContent()).error(R.mipmap.service_ic_load_failed)
                    .apply(new RequestOptions().transform(new GlideRoundTransform(helper.itemView.getContext(), 10))).into(iv_content_other);
            iv_content_other.setOnClickListener(v -> {
                VideoPreviewActivity.start(message.getContent());
            });
        }
    }
}
