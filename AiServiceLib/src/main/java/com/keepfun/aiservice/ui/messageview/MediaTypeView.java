package com.keepfun.aiservice.ui.messageview;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.Message;

/**
 * @author yang
 * @description
 * @date 2021/4/20 10:10 AM
 */
public class MediaTypeView extends BaseMsgView {
    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        if (mUserId == message.getFromUserId()) {
            helper.setGone(R.id.layout_other, true);
            helper.setGone(R.id.layout_content_self, false);
            helper.setText(R.id.tv_self_voice_time, message.getTimeDuration() > 0 ? "时长：" + formatTime(message.getTimeDuration()) : "已取消");
        } else {
            helper.setGone(R.id.layout_other, false);
            helper.setGone(R.id.layout_content_self, true);
            helper.setText(R.id.tv_nickname, message.getFromUserName());
            ImageView iv_avatar = helper.getView(R.id.iv_avatar);
            Glide.with(helper.itemView.getContext()).load(message.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_avatar);
            if (message.getTimeDuration() != 0) {
                helper.setText(R.id.tv_other_voice_time, "时长：" + formatTime(message.getTimeDuration()));
            } else {
                helper.setText(R.id.tv_other_voice_time, "已取消");
            }
        }
    }
}
