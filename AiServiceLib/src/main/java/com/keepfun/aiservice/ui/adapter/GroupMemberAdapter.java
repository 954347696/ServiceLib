package com.keepfun.aiservice.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.GroupMember;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/14 10:37 AM
 */
public class GroupMemberAdapter extends BaseQuickAdapter<GroupMember, BaseViewHolder> {

    public GroupMemberAdapter(@Nullable List<GroupMember> data) {
        super(R.layout.service_item_group_member, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, GroupMember item) {
        ImageView iv_avatar = holder.findView(R.id.iv_avatar);
        Glide.with(getContext()).load(item.getImgUrl()).placeholder(R.mipmap.service_bg_chat_default).into(iv_avatar);
        holder.setText(R.id.tv_nickname, item.getName());
    }
}
