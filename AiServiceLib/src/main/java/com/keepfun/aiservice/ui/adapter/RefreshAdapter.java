package com.keepfun.aiservice.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.aiservice.R;

public abstract class RefreshAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    public static final int TYPE_FOOTER = -1;

    //初始化状态 隐藏
    public static final int INIT_NO_DATA = 0;
    //上拉加载更多
    public static final int PULL_UP_LOAD_MORE = 1;
    //正在加载中
    public static final int LOADING_MORE = 2;
    //没有加载更多
    public static final int NO_LOAD_MORE = 3;
    //空数据
    public static final int EMPTY_DATA = 4;
    //空数据无icon
    public static final int EMPTY_DATA_NO_ICON = 5;

    //初始化状态不显示-默认为0
    private int mLoadMoreStatus = INIT_NO_DATA;
    private String emptyStr;

    /**
     * 每页加载条数
     */
    int PAGE_COUNT = 20;

    public void setEmptyStr(String emptyStr) {
        this.emptyStr = emptyStr;
    }

    public String getEmptyStr() {
        if (emptyStr == null || emptyStr.equals("")) {
            return "暂无数据";
        }
        return emptyStr;
    }

    public RefreshAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_refresh_view_footer, parent, false);
            return new FooterViewHolder(itemView);
        } else {
            return onViewHolder(parent, viewType);
        }
    }

    public abstract RecyclerView.ViewHolder onViewHolder(ViewGroup parent, int viewType);


    public abstract void onBindView(RecyclerView.ViewHolder holder, int position);

    public abstract int getItemDataCount();

    public int getItemDataViewType(int position) {
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            bindFootView((FooterViewHolder) holder);
        } else {
            onBindView(holder, position);
        }
    }

    private void bindFootView(FooterViewHolder footerViewHolder) {
        switch (mLoadMoreStatus) {
            case INIT_NO_DATA:
            default:
                footerViewHolder.refreshTxtLayout.setVisibility(View.GONE);
                footerViewHolder.mLoadLayout.setVisibility(View.GONE);
                break;
            case PULL_UP_LOAD_MORE:
                if (footerViewHolder.mLoadLayout.getVisibility() == View.GONE) {
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.refreshTxtLayout.setVisibility(View.VISIBLE);
                }
                footerViewHolder.emptyIcon.setVisibility(View.GONE);
                footerViewHolder.progressBar.setVisibility(View.GONE);
                footerViewHolder.mLoadText.setText("上拉加载更多...");
                break;
            case LOADING_MORE:
                if (footerViewHolder.mLoadLayout.getVisibility() == View.GONE) {
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.refreshTxtLayout.setVisibility(View.VISIBLE);
                }
                footerViewHolder.emptyIcon.setVisibility(View.GONE);
                footerViewHolder.progressBar.setVisibility(View.VISIBLE);
                footerViewHolder.mLoadText.setText("正加载更多...");
                break;
            case NO_LOAD_MORE:
                if (footerViewHolder.mLoadLayout.getVisibility() == View.GONE) {
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.refreshTxtLayout.setVisibility(View.VISIBLE);
                }
                footerViewHolder.emptyIcon.setVisibility(View.GONE);
                footerViewHolder.progressBar.setVisibility(View.GONE);
                footerViewHolder.mLoadText.setText("没有更多数据了");
                break;
            case EMPTY_DATA:
                if (footerViewHolder.mLoadLayout.getVisibility() == View.GONE) {
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.refreshTxtLayout.setVisibility(View.VISIBLE);
                }
                footerViewHolder.emptyIcon.setVisibility(View.VISIBLE);
                footerViewHolder.progressBar.setVisibility(View.GONE);
                footerViewHolder.mLoadText.setText(getEmptyStr());
                break;
            case EMPTY_DATA_NO_ICON:
                if (footerViewHolder.mLoadLayout.getVisibility() == View.GONE) {
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.refreshTxtLayout.setVisibility(View.VISIBLE);
                }
                footerViewHolder.emptyIcon.setVisibility(View.GONE);
                footerViewHolder.progressBar.setVisibility(View.GONE);
                footerViewHolder.mLoadText.setText(getEmptyStr());
                break;
        }

        ViewGroup.LayoutParams params = footerViewHolder.mLoadLayout.getLayoutParams();
        if (params != null) {
            if (footerViewHolder.mLoadLayout.getVisibility() == View.GONE) {
                params.width = 0;
                params.height = 0;
            } else {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            footerViewHolder.mLoadLayout.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        //RecyclerView的count设置为数据总条数+ 1（footerView）
        return getItemDataCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            //最后一个item设置为footerView
            return TYPE_FOOTER;
        } else {
            return getItemDataViewType(position);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView mLoadText;
        LinearLayout mLoadLayout;
        LinearLayout refreshTxtLayout;
        ImageView emptyIcon;

        public FooterViewHolder(View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.pbLoad);
            mLoadText = itemView.findViewById(R.id.tvLoadText);
            mLoadLayout = itemView.findViewById(R.id.loadLayout);
            refreshTxtLayout = itemView.findViewById(R.id.foot_refresh_txt_layout);
            emptyIcon = itemView.findViewById(R.id.refresh_empty_icon);
        }
    }

    /**
     * 更新加载更多状态
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        if (status == PULL_UP_LOAD_MORE && getItemDataCount() < PAGE_COUNT && getItemDataCount() > 0) {
            setEmptyStr("  ");
            mLoadMoreStatus = EMPTY_DATA_NO_ICON;
        }
        notifyItemChanged(getItemCount() - 1);
    }

}
