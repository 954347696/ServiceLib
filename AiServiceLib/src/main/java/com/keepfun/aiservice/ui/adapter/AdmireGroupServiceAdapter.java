package com.keepfun.aiservice.ui.adapter;


import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.GroupMember;
import com.keepfun.aiservice.ui.impl.CheckClickListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/28 2:50 PM
 */
public class AdmireGroupServiceAdapter extends BaseQuickAdapter<GroupMember, BaseViewHolder> {

    public AdmireGroupServiceAdapter(@Nullable List<GroupMember> data) {
        super(R.layout.service_item_group_admire_service, data);
        ids = new ArrayList<>();
    }

    private OnMemberSelectedListener memberSelectedListener;

    public void setOnMemberSelectedListener(OnMemberSelectedListener memberSelectedListener) {
        this.memberSelectedListener = memberSelectedListener;
    }

    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GroupMember member) {

        ImageView iv_avatar = helper.findView(R.id.iv_avatar);
        Glide.with(getContext()).load(member.getImgUrl()).placeholder(R.mipmap.service_bg_chat_default).into(iv_avatar);
        helper.setText(R.id.tv_nickname, member.getName());
        View layout_avatar = helper.findView(R.id.layout_avatar);
        layout_avatar.setSelected(ids.contains(member.getId()));
        ImageView iv_select_tip=helper.findView(R.id.iv_select_tip);
        iv_select_tip.setSelected(ids.contains(member.getId()));

        helper.itemView.setOnClickListener(new CheckClickListener(v -> {
            if (ids.contains(member.getId())) {
                ids.remove(member.getId());
            } else {
                ids.add(member.getId());
            }
            layout_avatar.setSelected(ids.contains(member.getId()));
            iv_select_tip.setSelected(ids.contains(member.getId()));
            if (memberSelectedListener != null) {
                memberSelectedListener.onMemberSelected(member);
            }
        }));
    }

    public interface OnMemberSelectedListener {
        void onMemberSelected(GroupMember groupMember);
    }
}
