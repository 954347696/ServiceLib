package com.keepfun.aiservice.ui.messageview;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.utils.audio.MediaManager;

/**
 * @author yang
 * @description
 * @date 2021/4/20 10:13 AM
 */
public class VoiceTypeView extends BaseMsgView {
    static volatile AnimationDrawable voiceAnim;

    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        if (mUserId == message.getFromUserId()) {
            helper.setGone(R.id.layout_other, true);
            helper.setGone(R.id.layout_content_self, false);
            helper.setText(R.id.tv_self_voice_time, message.getTimeDuration() + "s");
            ImageView iv_self_voice = helper.getView(R.id.iv_self_voice);
            voiceAnim = (AnimationDrawable) iv_self_voice.getDrawable();
        } else {
            helper.setGone(R.id.layout_other, false);
            helper.setGone(R.id.layout_content_self, true);
            helper.setText(R.id.tv_nickname, message.getFromUserName());
            ImageView iv_avatar = helper.getView(R.id.iv_avatar);
            Glide.with(helper.itemView.getContext()).load(message.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_avatar);
            if (message.getTimeDuration() != 0) {
                helper.setText(R.id.tv_other_voice_time, message.getTimeDuration() + "s");
            } else {
                helper.setText(R.id.tv_other_voice_time, "已取消");
            }
            ImageView iv_other_voice = helper.getView(R.id.iv_other_voice);
            voiceAnim = (AnimationDrawable) iv_other_voice.getDrawable();
        }


        helper.itemView.setOnClickListener(new CheckClickListener(v -> {
            if (voiceAnim.isRunning()) {
                voiceAnim.stop();
            }
            if (MediaManager.isPlaying()) {
                MediaManager.release();
            } else {
                MediaManager.release();
                if (!voiceAnim.isRunning()) {
                    voiceAnim.start();
                }
                MediaManager.playSound(message.getContent(), mp -> {
                    if (voiceAnim.isRunning()) {
                        voiceAnim.stop();
                    }
                    MediaManager.release();
                });
            }
        }));
    }
}
