package com.keepfun.aiservice.ui.act;

import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.downloader.DownloadService;
import com.keepfun.aiservice.downloader.callback.DownloadManager;
import com.keepfun.aiservice.downloader.domain.DownloadInfo;
import com.keepfun.aiservice.entity.SkinEntity;
import com.keepfun.aiservice.skin.loader.CustomSDCardLoader;
import com.keepfun.aiservice.ui.adapter.SkinAdapter;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.presenter.SkinPresenter;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import skin.support.SkinCompatManager;

/**
 * @author yang
 * @description
 * @date 2020/9/15 4:51 PM
 */
public class SkinListActivity extends PanActivity<SkinPresenter> {

    private RecyclerView rv_list;

    private List<SkinEntity> data;
    private SkinAdapter mAdapter;
    private DownloadManager downloadManager;


    @Override
    public SkinPresenter newP() {
        return new SkinPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_change_skin;
    }

    @Override
    public void bindUI(View rootView) {
        rv_list = findViewById(R.id.rv_list);
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void initData() {
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.color_EE)
                .size(SizeUtils.dp2px(0.5f))
                .margin(SizeUtils.dp2px(15))
                .showLastDivider()
                .build();
        rv_list.addItemDecoration(decor);

        downloadManager = DownloadService.getDownloadManager(getApplicationContext());

        if (data == null) {
            data = new ArrayList<>();
        }
        mAdapter = new SkinAdapter(data);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.service_view_empty, null, false);
        mAdapter.setEmptyView(emptyView);
        mAdapter.setOnItemClickListener(new SkinAdapter.OnItemClickListener() {
            @Override
            public void onStartClick(SkinEntity skinEntity) {
                downloadManager.download(skinEntity.getDownloadInfo());
            }

            @Override
            public void onCancelClick(SkinEntity skinEntity) {
                downloadManager.remove(skinEntity.getDownloadInfo());
            }

            @Override
            public void onSkinChanged(SkinEntity skinEntity) {
                LogUtils.e("skin onSkinChanged ----> ");
                if (skinEntity.getAcquiesce() == 1) {
                    SpManager.getConfig().put(SpManager.KEY_SKIN, -1L);
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                } else {
                    SpManager.getConfig().put(SpManager.KEY_SKIN, skinEntity.getId());
                    long skin = SpManager.getConfig().getLong(SpManager.KEY_SKIN, -1L);
                    File skinFile = new File(YLConstant.SKIN_PATH, skin + ".skin");
                    if (skinFile.exists()) {
                        LogUtils.e("skin path ----> " + skinFile.getAbsolutePath());
                        try {
                            SkinCompatManager.getInstance().loadSkin(skin + ".skin", CustomSDCardLoader.SKIN_LOADER_STRATEGY_SDCARD);
                        } catch (Exception e) {
                            LogUtils.e("loadSkin error : " + e.getMessage());
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDownloadStatusChanged(SkinEntity skinEntity) {
                int curState = skinEntity.getDownloadInfo().getStatus();
                if (curState == DownloadInfo.STATUS_DOWNLOADING || curState == DownloadInfo.STATUS_PREPARE_DOWNLOAD) {
                    downloadManager.pause(skinEntity.getDownloadInfo());
                } else if (curState == DownloadInfo.STATUS_PAUSED) {
                    downloadManager.resume(skinEntity.getDownloadInfo());
                }

            }
        });
        rv_list.setAdapter(mAdapter);

        getP().getSkinList();
    }

    public void getSkinFailed(String message) {
    }

    public void getSkinSuccess(List<SkinEntity> result) {
        data.clear();
        SkinEntity defaultSkin = new SkinEntity();
        defaultSkin.setAcquiesce(1);
        defaultSkin.setName("柠檬黄");
        defaultSkin.setId(-1);
        data.add(defaultSkin);
        for (SkinEntity skinEntity : result) {
            skinEntity.setAcquiesce(0);
            if (skinEntity.getType() == 1) {
                if (!StringUtils.isEmpty(skinEntity.getUrl())) {
                    String path = YLConstant.SKIN_PATH + "/" + skinEntity.getId() + ".skin";
                    DownloadInfo downloadInfo = downloadManager.getDownloadById(skinEntity.getUrl());
                    File file = new File(path);
                    if (file.exists() && file.length() == skinEntity.getSize()) {
                        downloadInfo.setStatus(DownloadInfo.STATUS_COMPLETED);
                    }
                    if (downloadInfo == null || !file.exists()) {
                        downloadInfo = new DownloadInfo.Builder().setUrl(skinEntity.getUrl())
                                .setPath(path)
                                .build();
                    }

                    skinEntity.setDownloadInfo(downloadInfo);
                }
                data.add(skinEntity);
            }
        }
        LogUtils.e("skinList data : " + data);
        mAdapter.notifyDataSetChanged();
    }
}
