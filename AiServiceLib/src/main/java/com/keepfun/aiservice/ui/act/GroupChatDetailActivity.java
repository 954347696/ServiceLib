package com.keepfun.aiservice.ui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.GroupDetail;
import com.keepfun.aiservice.entity.GroupMember;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.event.DeleteGroupEvent;
import com.keepfun.aiservice.entity.event.SetGroupTopEvent;
import com.keepfun.aiservice.ui.dialog.CommonDialog;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.GroupChatDetailPresenter;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author yang
 * @description
 * @date 2020/12/14 9:14 AM
 */
public class GroupChatDetailActivity extends PanActivity<GroupChatDetailPresenter> implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.service_activity_group_chat_detail;
    }

    @Override
    public GroupChatDetailPresenter newP() {
        return new GroupChatDetailPresenter();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    private TextView tv_member_count;
    private RecyclerView rv_member;
    private TextView tv_group_robot_name;
    private TextView tv_group_name;
    private TextView tv_group_notice;
    private Switch switch_top;
    private long groupId;
    private GroupDetail mGroupDetail;

    private CommonDialog mNoticeDialog;
    private CommonDialog mExitDialog;

    @Override
    public void bindUI(View rootView) {
        tv_member_count = findViewById(R.id.tv_member_count);
        rv_member = findViewById(R.id.rv_member);
        tv_group_robot_name = findViewById(R.id.tv_group_robot_name);
        tv_group_name = findViewById(R.id.tv_group_name);
        tv_group_notice = findViewById(R.id.tv_group_notice);
        switch_top = findViewById(R.id.switch_top);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            groupId = bundle.getLong(Arguments.DATA);
        }
        rv_member.setLayoutManager(new GridLayoutManager(this, 5));

        getP().getDetailInfo(groupId);
    }

    @Override
    public void bindEvent() {
        tv_member_count.setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_group_notice_info).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_chat_history).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_complaint).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_exit).setOnClickListener(new CheckClickListener(this));
        switch_top.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getP().setGroupTop(groupId);
        });
    }

    public void refreshUI(GroupDetail groupDetail) {
        this.mGroupDetail = groupDetail;
        tv_member_count.setText(mGroupDetail.getSize() + "名成员");
//        GroupMemberAdapter memberAdapter = new GroupMemberAdapter(mGroupDetail.getVos());
//        rv_member.setAdapter(memberAdapter);

        tv_group_robot_name.setText(mGroupDetail.getRobotName());
        tv_group_name.setText(mGroupDetail.getGroupName());
        tv_group_notice.setVisibility(StringUtils.isEmpty(mGroupDetail.getNotice()) ? View.GONE : View.VISIBLE);
        tv_group_notice.setText(mGroupDetail.getNotice());
        switch_top.setChecked(mGroupDetail.getTop());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_member_count) {
            Bundle bundle = new Bundle();
            bundle.putString(Arguments.DATA, String.valueOf(groupId));
            ActivityUtils.startActivity(bundle, GroupMemberActivity.class);
        } else if (id == R.id.tv_group_notice_info) {
            if (!StringUtils.isEmpty(mGroupDetail.getNotice())) {
                showNoticeDialog();
            }
        } else if (id == R.id.layout_chat_history) {
            Bundle bundle = new Bundle();
            bundle.putString(Arguments.DATA, String.valueOf(groupId));
            bundle.putInt(Arguments.DATA1, 2);
            ActivityUtils.startActivity(bundle, ChatSearchActivity.class);
        } else if (id == R.id.layout_complaint) {

        } else if (id == R.id.tv_exit) {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        if (mExitDialog == null) {
            mExitDialog = new CommonDialog(this);
        }
        mExitDialog.show();
        mExitDialog.setTitle("退出");
        mExitDialog.setContent("即将退群，且不会再接收此群消息，\n" +
                "是否确定？");
        mExitDialog.setButton1("确定", v -> {
            mExitDialog.dismiss();
            getP().deleteGroup(String.valueOf(groupId));
        });
        mExitDialog.setButton2("取消", v -> mExitDialog.dismiss());
    }

    private void showNoticeDialog() {
        if (mNoticeDialog == null) {
            mNoticeDialog = new CommonDialog(this);
        }
        mNoticeDialog.show();

        mNoticeDialog.setTitle("群公告");
        mNoticeDialog.setContent(mGroupDetail.getNotice());
        mNoticeDialog.setButton2Gone();
        mNoticeDialog.setButton1("关闭", v -> mNoticeDialog.dismiss());
    }

    public void setTopResult(Boolean result) {
        if (result) {
            mGroupDetail.setTop(!mGroupDetail.getTop());
        }
        EventBus.getDefault().post(new SetGroupTopEvent(String.valueOf(groupId), mGroupDetail.getTop()));
        switch_top.setChecked(mGroupDetail.getTop());
    }

    @Subscribe
    public void receiveMsg(DeleteGroupEvent event) {
        if (Long.valueOf(event.getGroupId()) == groupId) {
            finish();
        }
    }

    @Subscribe
    public void receiveMsg(Message message) {
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_CANCEL) {
            if (String.valueOf(groupId).equals(message.getGroupId())) {
                finish();
            }
        }
    }

}
