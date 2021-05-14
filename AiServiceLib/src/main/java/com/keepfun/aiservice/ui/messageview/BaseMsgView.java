package com.keepfun.aiservice.ui.messageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.impl.OnOperationClickListener;
import com.keepfun.aiservice.ui.adapter.ServiceChatAdapter;
import com.keepfun.blankj.util.TimeUtils;

import org.jetbrains.annotations.NotNull;

import static androidx.core.util.Preconditions.checkNotNull;


public abstract class BaseMsgView {

    protected OnOperationClickListener onOperationClickListener;
    protected long mUserId;

    public BaseMsgView() {
        mUserId = GlobalDataHelper.getInstance().getImUserInfo().getId();
    }

    public void setOnOperationClickListener(OnOperationClickListener onOperationClickListener) {
        this.onOperationClickListener = onOperationClickListener;
    }

    public abstract void setContent(BaseViewHolder helper, Message message);

    public void checkTime(@NotNull BaseViewHolder helper, Message message, Message lastMessage) {
        if (message.hideTime()) {
            return;
        }
        TextView tv_time = helper.getViewOrNull(R.id.tv_time);
        if (tv_time != null) {
            if (lastMessage == null) {
                tv_time.setVisibility(View.VISIBLE);
            } else {
                if (lastMessage.nextShowTime() || message.getCreateTime() - lastMessage.getCreateTime() > 1 * 60 * 1000) {
                    tv_time.setVisibility(View.VISIBLE);
                } else {
                    tv_time.setVisibility(View.GONE);
                }
            }
            tv_time.setText(TimeUtils.millis2String(message.getCreateTime(), "MM-dd HH:mm"));
        }
    }

    public String formatTime(long time) {
        long minutes = time / 60;
        long seconds = time % 60;
        return (minutes > 9 ? String.valueOf(minutes) : "0" + minutes) + ":" + (seconds > 9 ? String.valueOf(seconds) : "0" + seconds);
    }

}
