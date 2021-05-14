package com.keepfun.aiservice.ui.messageview;

import android.graphics.Color;
import android.text.Html;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.AiQuestion;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.ui.view.ExpandableTextView;
import com.keepfun.aiservice.utils.HtmlImageGetter;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.blankj.util.ColorUtils;
import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.blankj.util.SpanUtils;
import com.keepfun.blankj.util.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2021/4/20 10:17 AM
 */
public class DefaultTypeView extends BaseMsgView {
    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        ExpandableTextView tv_content_self = helper.getView(R.id.tv_content_self);
        if (mUserId == message.getFromUserId()) {
            helper.setGone(R.id.layout_other, true);
            tv_content_self.setVisibility(View.VISIBLE);
            tv_content_self.setText(message.getContent(), helper.getAdapterPosition());
        } else {
            helper.setGone(R.id.layout_other, false);
            tv_content_self.setVisibility(View.GONE);
            helper.setText(R.id.tv_nickname, message.getFromUserName());
            ExpandableTextView tv_content_other = helper.getView(R.id.tv_content_other);
            if (message.getType() != YLConstant.MessageType.MESSAGE_TYPE_AI_SEND) {
                tv_content_other.setText(message.getContent(), helper.getAdapterPosition());
            } else {
                if (!StringUtils.isEmpty(message.getContent()) && JsonUtils.isJsonFormat(message.getContent())) {
                    List<AiQuestion> questions = JsonUtils.jsonToArrayList(JsonUtils.encode(JsonUtils.decode(message.getContent())), AiQuestion.class);
                    SpanUtils spanUtils = new SpanUtils();
                    spanUtils.append("猜你想问").setBold().setFontSize(15, true).setForegroundColor(Color.parseColor("#444444"));
                    for (int i = 0; i < questions.size(); i++) {
                        AiQuestion question = questions.get(i);
                        spanUtils.append("\n");
                        spanUtils.append((i + 1) + "." + question.getQuestion()).setBold().setFontSize(14, true).setForegroundColor(Color.parseColor("#444444"))
                                .setClickSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View widget) {
                                        Message message1 = JsonUtil.decode(JsonUtil.encode(message), Message.class);
                                        message1.setContent(question.getContent());
                                        EventBus.getDefault().post(message1);
                                    }

                                    @Override
                                    public void updateDrawState(@NonNull TextPaint ds) {
                                        ds.setColor(ColorUtils.getColor(R.color.color_44));
                                        ds.setUnderlineText(false);
                                    }

                                });
//                            spanUtils.append(question.getContent()).setFontSize(14, true).setForegroundColor(Color.parseColor("#555555"));
                        spanUtils.append("\n");
                    }
                    tv_content_other.setText(spanUtils.create(), helper.getAdapterPosition());
                } else {
                    tv_content_other.setText(message.getContent(), helper.getAdapterPosition());
                }
            }
            ImageView iv_avatar = helper.getView(R.id.iv_avatar);
            Glide.with(helper.itemView.getContext()).load(message.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_avatar);
        }
    }
}
