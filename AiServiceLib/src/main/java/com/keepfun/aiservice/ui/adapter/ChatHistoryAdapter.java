package com.keepfun.aiservice.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.module.LoadMoreModule;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.HistoryChatMessage;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.utils.MatchUtils;
import com.keepfun.blankj.util.TimeUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/15 5:21 PM
 */
public class ChatHistoryAdapter extends BaseQuickAdapter<Message, BaseViewHolder> implements LoadMoreModule {

    public ChatHistoryAdapter(@Nullable List<Message> data) {
        super(R.layout.service_item_chat_history, data);
    }

    private String keywords;

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getKeywords() {
        return keywords;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, Message item) {
        ImageView iv_avatar = helper.findView(R.id.iv_avatar);
        Glide.with(getContext()).load(item.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).into(iv_avatar);
        helper.setText(R.id.tv_nickname,item.getFromUserName());
        helper.setText(R.id.tv_time, TimeUtils.millis2String(item.getCreateTime(),"yyyy.MM.dd HH:mm:ss"));

        helper.setText(R.id.tv_content, MatchUtils.matcherSearchText(getContext().getResources().getColor(R.color.color_FF8F00), item.getShortContent(), keywords));
        TextView tv_content = helper.findView(R.id.tv_content);
        tv_content.setOnClickListener(new CheckClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_content.getMaxLines() == 1) {
                    tv_content.setMaxLines(Integer.MAX_VALUE);
                } else {
                    tv_content.setMaxLines(1);
                }
            }
        }));

    }
}
