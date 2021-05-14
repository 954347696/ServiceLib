package com.keepfun.aiservice.ui.messageview;


import android.app.Activity;
import android.os.Bundle;

import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.ui.act.ServiceEvaluationActivity;
import com.keepfun.aiservice.ui.act.ServiceOnlineActivity;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.ToastUtils;

/**
 * @author yang
 * @description
 * @date 2021/4/19 5:24 PM
 */
public class SessionEndView extends BaseMsgView {

    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        helper.itemView.setOnClickListener(new CheckClickListener(v -> {
            if (StringUtils.isEmpty(message.getGroupId())) {
                ToastUtils.showShort("该会话评价失效");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(Arguments.DATA, message);
            bundle.putInt(Arguments.POSITION, helper.getAdapterPosition());
            ActivityUtils.startActivityForResult(bundle, ActivityUtils.getTopActivity(), ServiceEvaluationActivity.class, ServiceOnlineActivity.REQUEST_FOR_EVALUATION);
        }));
    }
}
