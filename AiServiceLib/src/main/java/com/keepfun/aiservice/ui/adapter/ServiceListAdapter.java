package com.keepfun.aiservice.ui.adapter;

import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.ServiceListItem;
import com.keepfun.blankj.util.ColorUtils;
import com.keepfun.blankj.util.SizeUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/8 2:40 PM
 */
public class ServiceListAdapter extends BaseQuickAdapter<ServiceListItem, BaseViewHolder> {


    public ServiceListAdapter(@Nullable List<ServiceListItem> data) {
        super(R.layout.service_item_service_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ServiceListItem item) {
        helper.setText(R.id.title, item.getTitle());
        helper.setImageResource(R.id.iv, R.mipmap.service_ic_arrow_up1);
        setArrowSpin(helper, item, false);

        RecyclerView rv_sub_list = helper.getView(R.id.rv_sub_list);
        rv_sub_list.setLayoutManager(new LinearLayoutManager(getContext()));
        ServiceListSubAdapter adapter = new ServiceListSubAdapter(item.getChildNode());
        adapter.setType(item.getType());
        rv_sub_list.setAdapter(adapter);
        TextView emptyView = new TextView(getContext());
        emptyView.setTextColor(ColorUtils.getColor(R.color.color_99));
        emptyView.setTextSize(13);
        emptyView.setGravity(Gravity.CENTER_HORIZONTAL);
        emptyView.setPadding(0, 0, 0, SizeUtils.dp2px(20));
        emptyView.setText(item.getEmptyStr());
        adapter.setEmptyView(emptyView);

        helper.getView(R.id.layout_title).setOnClickListener(v -> {
            item.setExpanded(!item.getExpanded());
            setArrowSpin(helper, item, true);
        });
    }

    private void setArrowSpin(BaseViewHolder helper, ServiceListItem entity, boolean isAnimate) {
        ImageView imageView = helper.getView(R.id.iv);
        RecyclerView rv_sub_list = helper.getView(R.id.rv_sub_list);
        if (entity.getExpanded()) {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .rotation(0f)
                        .start();
            } else {
                imageView.setRotation(0f);
            }
            rv_sub_list.setVisibility(View.GONE);
        } else {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .rotation(180f)
                        .start();
            } else {
                imageView.setRotation(180f);
            }
            rv_sub_list.setVisibility(View.VISIBLE);
        }
    }
}
