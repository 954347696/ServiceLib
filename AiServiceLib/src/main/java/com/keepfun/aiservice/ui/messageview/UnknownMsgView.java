package com.keepfun.aiservice.ui.messageview;

import android.content.Context;
import android.graphics.Color;

import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.Message;

public class UnknownMsgView extends BaseMsgView {

    public UnknownMsgView() {
    }

    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        // tvInfo.setText("未知消息");
        helper.setText(R.id.tv_tip, "未知消息");
    }
}
