package com.keepfun.aiservice.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.RoomMediaBean;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.threads.YLNotifyMediaResultTask;
import com.keepfun.aiservice.threads.YLPoolExecutor;
import com.keepfun.aiservice.ui.act.ServiceMediaActivity;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.utils.RingUtils;
import com.keepfun.base.PanDialogFragment;
import com.keepfun.blankj.util.ActivityUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 选择图片弹框<p>
 *
 * @author zixuefei
 * @since 2019/8/7 17:30
 */
public class ReceiveCallDialog extends PanDialogFragment implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.service_dialog_receive_call;
    }

    private ImageView iv_avatar;
    private TextView tv_nickname;
    private TextView tv_type;

    private Message message;


    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            message = (Message) bundle.getSerializable(Arguments.DATA);
        }
        iv_avatar = rootView.findViewById(R.id.iv_avatar);
        tv_nickname = rootView.findViewById(R.id.tv_nickname);
        tv_type = rootView.findViewById(R.id.tv_type);

        Glide.with(getContext()).load(message.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).into(iv_avatar);
        tv_nickname.setText(message.getFromUserName());
        tv_type.setText("邀请你" + (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START ? "视频" : "语音") + "通话…");
        RingUtils.startRing();
    }

    @Override
    public void bindEvent() {
        rootView.findViewById(R.id.tv_refuse).setOnClickListener(new CheckClickListener(this));
        rootView.findViewById(R.id.tv_receive).setOnClickListener(new CheckClickListener(this));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_refuse) {
            ImClient.getInstance().sendMsg(message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START ? YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END : YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END
                    , YLConstant.ContentType.CONTENT_TYPE_CUSTOM, message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START ? "视频通话结束" : "语音通话结束", 0);
        } else if (id == R.id.tv_receive) {
            Bundle bundle = new Bundle();
            RoomMediaBean mediaBean = new RoomMediaBean();
            mediaBean.setFrom(true);
            if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START) {
                mediaBean.setMediaType(2);
            } else if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_START) {
                mediaBean.setMediaType(1);
            }
            mediaBean.setRoomId(Long.parseLong(message.getGroupId()));
            mediaBean.setSelfId(GlobalDataHelper.getInstance().getImUserInfo().getId());
            mediaBean.setOtherAvatar(message.getFromUserAvatar());
            mediaBean.setOtherId(message.getFromUserId());
            mediaBean.setOtherName(message.getFromUserName());
            mediaBean.setGroupId(message.getGroupId());
            bundle.putSerializable(Arguments.DATA, mediaBean);
            ServiceMediaActivity.start(mediaBean);
        }
        dismiss();
        RingUtils.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RingUtils.release();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    /**
     * @param groupId
     * @param callType    通话类型: 1-音频; 2-视频
     * @param operateType 操作类型(0-接通，1-拒绝，2-挂断，3-拨打)
     */
    private void report(String groupId, int callType, int operateType) {
        FutureTask<Boolean> futureTask = new FutureTask<>(new YLNotifyMediaResultTask(groupId, callType, operateType));
        YLPoolExecutor.getInstance().execute(futureTask);
        try {
            Boolean result = futureTask.get();
            if (result != null && result) {
                if (operateType == 1) {
                    ImClient.getInstance().sendMsg(message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START ? YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END : YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END
                            , YLConstant.ContentType.CONTENT_TYPE_CUSTOM, message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START ? "视频通话结束" : "语音通话结束", 0);
                } else {
                    Bundle bundle = new Bundle();
                    RoomMediaBean mediaBean = new RoomMediaBean();
                    mediaBean.setFrom(true);
                    if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START) {
                        mediaBean.setMediaType(2);
                    } else if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_START) {
                        mediaBean.setMediaType(1);
                    }
                    mediaBean.setRoomId(Long.parseLong(message.getGroupId()));
                    mediaBean.setSelfId(GlobalDataHelper.getInstance().getImUserInfo().getId());
                    mediaBean.setOtherAvatar(message.getFromUserAvatar());
                    mediaBean.setOtherId(message.getFromUserId());
                    mediaBean.setOtherName(message.getFromUserName());
                    mediaBean.setGroupId(message.getGroupId());
                    bundle.putSerializable(Arguments.DATA, mediaBean);
                    ServiceMediaActivity.start(mediaBean);
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
