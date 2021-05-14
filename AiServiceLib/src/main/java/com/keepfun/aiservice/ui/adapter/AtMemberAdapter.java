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

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/21 5:05 PM
 */
public class AtMemberAdapter extends BaseQuickAdapter<GroupMember, BaseViewHolder> {

    public AtMemberAdapter(@Nullable List<GroupMember> data) {
        super(R.layout.service_item_at_member, data);
    }

    private OnMemberClickListener onMemberClickListener;

    public void setOnMemberClickListener(OnMemberClickListener onMemberClickListener) {
        this.onMemberClickListener = onMemberClickListener;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, GroupMember item) {
        ImageView iv_avatar = holder.findView(R.id.iv_avatar);
        Glide.with(getContext()).load(item.getImgUrl()).placeholder(R.mipmap.service_bg_chat_default).into(iv_avatar);
        holder.setText(R.id.tv_nickname, item.getName());

        holder.itemView.setOnClickListener(new CheckClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMemberClickListener != null) {
                    onMemberClickListener.onMemberClick(item);
                }
            }
        }));

    }

    public interface OnMemberClickListener {
        void onMemberClick(GroupMember groupMember);
    }
}
