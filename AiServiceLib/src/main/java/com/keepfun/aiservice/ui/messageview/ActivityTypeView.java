package com.keepfun.aiservice.ui.messageview;

import android.text.Html;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.entity.ActiveBean;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.manager.RichTextHelper;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.base.WebActivity;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.SpanUtils;
import com.keepfun.blankj.util.StringUtils;

import java.util.HashMap;

/**
 * @author yang
 * @description
 * @date 2021/4/20 10:14 AM
 */
public class ActivityTypeView extends BaseMsgView{
    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        ActiveBean activeBean = JsonUtil.decode(message.getContent(), ActiveBean.class);
        helper.setText(R.id.tv_nickname, message.getFromUserName());
        helper.setText(R.id.tv_activity_title, activeBean.getTitle());
        ImageView iv_avatar = helper.getView(R.id.iv_avatar);
        Glide.with(helper.itemView.getContext()).load(message.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_avatar);
        TextView tv_activity_content = helper.getView(R.id.tv_activity_content);

        String[] str = activeBean.getContent().split("</p>");
        String actSpan = "";
        for (String s : str) {
            LogUtils.e("activity content str : " + s);
            if (s.trim().startsWith("<p>")) {
                actSpan += (s.trim() + "\n" + "</p>");
            } else {
                actSpan += (s.trim());
            }
        }
        LogUtils.e("activity content : " + actSpan);
        RichTextHelper.showRich(tv_activity_content,actSpan);
        TextView tv_activity_link = helper.getView(R.id.tv_activity_link);
        SpanUtils spanUtils = SpanUtils.with(tv_activity_link);
        if (!StringUtils.isEmpty(activeBean.getUrl())) {
            spanUtils.append("活动链接：" + activeBean.getUrl()).setClickSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    WebActivity.start(activeBean.getUrl());
                    report(message.getGroupId(), activeBean);
                }
            });
        }
        tv_activity_link.setText(spanUtils.create());
    }


    private synchronized void report(String groupId, ActiveBean activeBean) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("marketId", activeBean.getId());
        params.put("sessionId", groupId);
        params.put("type", 1);
        OkHttpUtils.postJson(ApiDomain.ACTIVITY_ACTION, params, new GsonResponseHandler<Object>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(Object response) {

            }
        });
    }

}
