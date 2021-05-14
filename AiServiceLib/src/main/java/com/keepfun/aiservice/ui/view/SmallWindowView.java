package com.keepfun.aiservice.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.keepfun.aiservice.R;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.media.lib.PeerConnectionUtils;

import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

public class SmallWindowView extends FrameLayout {
    private static final String TAG = "SmallWindowView";

    protected Context context;
    protected SurfaceViewRenderer surfaceViewRenderer;

    protected ImageView callImageView;
    protected TextView timeTextView;
    protected LinearLayout audioLinearLayout;

    protected VideoTrack displayVideoTrack = null;

    public SmallWindowView(@NonNull Context context) {
        this(context, null);
        Log.d(TAG, "VideoTypeView: 1");
    }

    public SmallWindowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        Log.d(TAG, "VideoTypeView: 2");
    }

    public SmallWindowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "VideoTypeView: 3");
        this.context = context;
        initView();
    }

    public SmallWindowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        this.context = context;
        initView();
    }

    protected void initView() {
        View parent = LayoutInflater.from(context).inflate(R.layout.small_window_view, this);

        callImageView = parent.findViewById(R.id.image_call);
        timeTextView = parent.findViewById(R.id.text_time);
        audioLinearLayout = parent.findViewById(R.id.vv_audio);
        surfaceViewRenderer = parent.findViewById(R.id.vv_video);
        surfaceViewRenderer.init(PeerConnectionUtils.getEglContext(), null);
    }

    public void updateWindow(VideoTrack videoTrack, int mediaType) {
        if (mediaType == 1) {
            timeTextView.setText("00::00:00");
            surfaceViewRenderer.setVisibility(GONE);
            audioLinearLayout.setVisibility(VISIBLE);
        } else {
            LogUtils.e("videoTrack : " + displayVideoTrack);
            if (videoTrack != null) {
                if (displayVideoTrack != null) {
                    displayVideoTrack.removeSink(surfaceViewRenderer);
                }
                displayVideoTrack = videoTrack;
                displayVideoTrack.addSink(surfaceViewRenderer);
            }

            surfaceViewRenderer.setVisibility(VISIBLE);
            audioLinearLayout.setVisibility(GONE);
        }
    }

    public VideoTrack getDisplayVideoTrack() {
        return displayVideoTrack;
    }

    public void setDisplayVideoTrack(VideoTrack displayVideoTrack) {
        this.displayVideoTrack = displayVideoTrack;
    }

    public void updateTimer(String timer) {
        if (timeTextView != null) {
            timeTextView.setText(timer);
        }
    }

    public SurfaceViewRenderer getSurfaceViewRenderer() {
        return surfaceViewRenderer;
    }
}
