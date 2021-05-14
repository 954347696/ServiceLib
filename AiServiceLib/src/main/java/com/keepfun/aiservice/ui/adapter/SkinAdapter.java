package com.keepfun.aiservice.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.downloader.DownloadService;
import com.keepfun.aiservice.downloader.callback.DownloadManager;
import com.keepfun.aiservice.downloader.domain.DownloadInfo;
import com.keepfun.aiservice.entity.SkinEntity;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.impl.MyDownloadListener;
import com.keepfun.aiservice.utils.SpManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/9/15 5:17 PM
 */
public class SkinAdapter extends BaseQuickAdapter<SkinEntity, BaseViewHolder> {

    private OnItemClickListener onItemClickListener;


    public SkinAdapter(@Nullable List<SkinEntity> data) {
        super(R.layout.service_item_skin_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, SkinEntity skinEntity) {
        helper.setText(R.id.tv_skin_name, skinEntity.getName());
        TextView tv_status = helper.getView(R.id.tv_status);
        ImageView iv_skin_radio = helper.getView(R.id.iv_skin_radio);
        long selectSkin = SpManager.getConfig().getLong(SpManager.KEY_SKIN, -1L);

        // Get download task status
        // Set a download listener
        if (skinEntity.getDownloadInfo() != null) {
            skinEntity.getDownloadInfo().setDownloadListener(new MyDownloadListener(new SoftReference(helper)) {
                //  Call interval about one second
                @Override
                public void onRefresh() {
                    if (getUserTag() != null && getUserTag().get() != null) {
                        BaseViewHolder helper = (BaseViewHolder) getUserTag().get();
                        refresh(helper, skinEntity);
                    }
                }
            });

        }
        if (skinEntity.getAcquiesce() == 1) {
            iv_skin_radio.setEnabled(true);
            iv_skin_radio.setSelected(selectSkin == -1 || selectSkin == skinEntity.getId());
            tv_status.setText("默认");
            tv_status.setSelected(false);
        } else if (skinEntity.getDownloadInfo() != null) {
            refresh(helper, skinEntity);
        }
        helper.getView(R.id.iv_status).setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDownloadStatusChanged(skinEntity);
            }
        });
        helper.getView(R.id.iv_cancel).setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onCancelClick(skinEntity);
            }
        });
        helper.getView(R.id.iv_skin_radio).setOnClickListener(new CheckClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onSkinChanged(skinEntity);
            }
        }));
    }

    private void refresh(BaseViewHolder helper, SkinEntity skinEntity) {
        View layout_download = helper.getView(R.id.layout_download);
        TextView tv_status = helper.getView(R.id.tv_status);
        ImageView iv_skin_radio = helper.getView(R.id.iv_skin_radio);
        long selectSkin = SpManager.getConfig().getLong(SpManager.KEY_SKIN, -1L);

        View.OnClickListener clickListener = null;
        iv_skin_radio.setSelected(selectSkin == skinEntity.getId());
        int state = skinEntity.getDownloadInfo().getStatus();
        if (state == DownloadInfo.STATUS_NONE || state == DownloadInfo.STATUS_REMOVED || state == DownloadInfo.STATUS_ERROR) {
            layout_download.setVisibility(View.GONE);
            iv_skin_radio.setEnabled(false);
            tv_status.setVisibility(View.VISIBLE);
            tv_status.setText("下载");
            tv_status.setSelected(true);
            clickListener = v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onStartClick(skinEntity);
                }
            };
        } else if (state == DownloadInfo.STATUS_COMPLETED) {
            layout_download.setVisibility(View.GONE);
            iv_skin_radio.setEnabled(true);
            tv_status.setVisibility(View.VISIBLE);
            tv_status.setText("已下载");
            tv_status.setSelected(false);
        } else {
            layout_download.setVisibility(View.VISIBLE);
            iv_skin_radio.setEnabled(false);
            tv_status.setVisibility(View.GONE);
            helper.setText(R.id.tv_percent, skinEntity.getDownloadInfo().getPercent() + "%");
            ProgressBar pb_percent = helper.getView(R.id.pb_percent);
            pb_percent.setProgress(skinEntity.getDownloadInfo().getPercent());
            ImageView iv_status = helper.getView(R.id.iv_status);
            if (state == DownloadInfo.STATUS_WAIT) {
                iv_status.setSelected(false);
            } else if (state == DownloadInfo.STATUS_DOWNLOADING || state == DownloadInfo.STATUS_PREPARE_DOWNLOAD) {
                iv_status.setSelected(false);
            } else if (state == DownloadInfo.STATUS_PAUSED) {
                iv_status.setSelected(true);
            }
        }
        tv_status.setOnClickListener(clickListener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onStartClick(SkinEntity skinEntity);

        void onCancelClick(SkinEntity skinEntity);

        void onSkinChanged(SkinEntity skinEntity);

        void onDownloadStatusChanged(SkinEntity skinEntity);
    }
}
