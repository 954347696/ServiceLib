package com.keepfun.aiservice.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.GroupMember;
import com.keepfun.aiservice.ui.adapter.AtMemberAdapter;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.blankj.util.SizeUtils;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/21 3:52 PM
 */
public class AtMemberDialog extends AlertDialog implements View.OnClickListener {

    public AtMemberDialog(@NonNull Context context) {
        super(context, R.style.UpdateDialog);
    }

    private RecyclerView rvMembers;

    private AtMemberAdapter.OnMemberClickListener onMemberClickListener;

    public void setOnMemberClickListener(AtMemberAdapter.OnMemberClickListener onMemberClickListener) {
        this.onMemberClickListener = onMemberClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
        setContentView(R.layout.service_dialog_at_member);
        findViewById(R.id.tv_cancel).setOnClickListener(new CheckClickListener(this));
        rvMembers = findViewById(R.id.rvMembers);

        rvMembers.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setMembers(List<GroupMember> groupMemberList) {
        AtMemberAdapter adapter = new AtMemberAdapter(groupMemberList);
        adapter.setOnMemberClickListener(groupMember -> {
            if (onMemberClickListener != null) {
                onMemberClickListener.onMemberClick(groupMember);
            }
            dismiss();
        });
        if (rvMembers.getItemDecorationCount() == 0) {
            RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(getContext())
                    .colorResId(R.color.color_EE)
                    .size(SizeUtils.dp2px(1))
                    .margin(SizeUtils.dp2px(14))
                    .showLastDivider()
                    .build();
            rvMembers.addItemDecoration(decor);
        }
        rvMembers.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
        }
    }

    public interface OnMemberClickListener {
        void onMemberClick(GroupMember groupMember);
    }
}
