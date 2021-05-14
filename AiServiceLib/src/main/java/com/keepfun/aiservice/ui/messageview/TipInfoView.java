package com.keepfun.aiservice.ui.messageview;

import android.content.Context;

import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.Message;

/**
 * @author yang
 * @description
 * @date 2021/4/19 3:49 PM
 */
public class TipInfoView extends BaseMsgView {

    public TipInfoView() {

    }

    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        helper.setText(R.id.tv_tip, message.getContentStr());
    }

}
