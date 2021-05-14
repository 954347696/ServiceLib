package com.keepfun.aiservice.ui.act;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;

/**
 * @author yang
 * @description
 * @date 2020/12/10 8:04 PM
 */
public class VideoPreviewActivity extends PanActivity {
    @Override
    public int getLayoutId() {
        return R.layout.service_activity_video_preview;
    }

    public static void start(String videoUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(Arguments.DATA, videoUrl);
        ActivityUtils.startActivity(bundle, VideoPreviewActivity.class);
    }

    private VideoView mVideoView;
    private String videoUrl;

    @Override
    public void bindUI(View rootView) {
        mVideoView = findViewById(R.id.video_view);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            videoUrl = bundle.getString(Arguments.DATA);
        }
        mVideoView.setVideoPath(videoUrl);

        mVideoView.setMediaController(new MediaController(this));

    }

    @Override
    public void bindEvent() {
        findViewById(R.id.iv_back).setOnClickListener(new CheckClickListener(v -> finish()));
        mVideoView.setOnPreparedListener(mp -> mVideoView.start());
        mVideoView.setOnCompletionListener(mp -> mVideoView.start());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
