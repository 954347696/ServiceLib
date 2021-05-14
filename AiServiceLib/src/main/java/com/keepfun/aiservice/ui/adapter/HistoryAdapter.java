package com.keepfun.aiservice.ui.adapter;

import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.blankj.util.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/28 11:31 AM
 */
public class HistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public HistoryAdapter(@Nullable List<String> data) {
        super(R.layout.service_item_history_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, String s) {
        if (!StringUtils.isEmpty(s)) {
            helper.setText(R.id.tv_history, s);
        }
    }
}
