package com.keepfun.aiservice.ui.act;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.GroupMember;
import com.keepfun.aiservice.entity.GroupMemberVo;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.adapter.GroupMemberAdapter2;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.view.ServiceTitleView;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.CollectionUtils;
import com.keepfun.blankj.util.SizeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author yang
 * @description
 * @date 2020/12/14 1:54 PM
 */
public class GroupMemberActivity extends PanActivity {

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_group_member;
    }

    private ServiceTitleView title;
    private RecyclerView rv_cs_users;
    private RecyclerView rv_members;
    private String groupId;

    @Override
    public void bindUI(View rootView) {
        title = findViewById(R.id.title);
        rv_cs_users = findViewById(R.id.rv_cs_users);
        rv_members = findViewById(R.id.rv_members);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            groupId = bundle.getString(Arguments.DATA);
        }

        rv_cs_users.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decoration1 = new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.color_EE)
                .size(SizeUtils.dp2px(1f))
                .margin(SizeUtils.dp2px(14))
                .build();
        rv_cs_users.addItemDecoration(decoration1);

        rv_members.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decoration = new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.color_EE)
                .size(SizeUtils.dp2px(1f))
                .margin(SizeUtils.dp2px(14))
                .build();
        rv_members.addItemDecoration(decoration);
        getMembers();
    }

    @Override
    public void bindEvent() {

    }

    private void getMembers() {
        OkHttpUtils.get(ApiDomain.GET_GROUP_MEMBER_LIST + "?" + Arguments.ID + "=" + groupId, new GsonResponseHandler<GroupMemberVo>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(GroupMemberVo groupMembers) {
                if (groupMembers != null) {
                    setMembers(groupMembers);
                }
            }
        });
    }

    private void setMembers(GroupMemberVo groupMembers) {
        title.setTitle("群成员（" + (groupMembers.getCsUsers().size() + groupMembers.getAppVos().size()) + "）");
        GroupMemberAdapter2 csAdapter = new GroupMemberAdapter2(groupMembers.getCsUsers());
        rv_cs_users.setAdapter(csAdapter);
        GroupMemberAdapter2 memberAdapter = new GroupMemberAdapter2(groupMembers.getAppVos());
        rv_members.setAdapter(memberAdapter);
    }
}
