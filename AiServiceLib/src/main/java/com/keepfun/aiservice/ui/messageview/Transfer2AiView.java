package com.keepfun.aiservice.ui.messageview;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.blankj.constant.TimeConstants;
import com.keepfun.blankj.util.ToastUtils;

/**
 * @author yang
 * @description
 * @date 2021/4/20 10:08 AM
 */
public class Transfer2AiView extends BaseMsgView {

    @Override
    public void setContent(BaseViewHolder helper, Message message) {
        helper.setText(R.id.tv_nickname, message.getFromUserName());
        ImageView iv_avatar = helper.getView(R.id.iv_avatar);
        Glide.with(helper.itemView.getContext()).load(message.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_avatar);
        helper.getView(R.id.tv_ai_2_artificial).setOnClickListener(new CheckClickListener(v -> {
            if (System.currentTimeMillis() - message.getCreateTime() > 4 * TimeConstants.MIN) {
                ToastUtils.showShort("时间太久了，内容已失效");
                return;
            }
            Integer waitingNo = (Integer) GlobalDataHelper.getInstance().getData(ChatConst.CHAT_WAITING_NO);
            if (waitingNo != null && waitingNo > 0) {
                ToastUtils.showShort("您已正在排队了，请稍候！");
                return;
            }
            Long sessionId = (Long) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_ID);
            Boolean isAi = GlobalDataHelper.getInstance().getData(ChatConst.SESSION_TYPE) != null && (Boolean) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_TYPE);
            if (sessionId != null && sessionId != -1 && isAi != null && !isAi) {
                ToastUtils.showShort("您已在会话中啦~");
                return;
            }
            if (onOperationClickListener != null) {
                onOperationClickListener.onOperationClick(message);
            }
        }));
    }
}
