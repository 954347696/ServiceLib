package com.keepfun.aiservice.ui.act;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.AppAdNoticeBean;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.RoomMediaBean;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.entity.event.PreviewEvent;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.view.SmallWindowView;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.aiservice.utils.RingUtils;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.aiservice.utils.UIUtils;
import com.keepfun.aiservice.utils.blur.BlurTransformation;
import com.keepfun.banner.Banner;
import com.keepfun.banner.adapter.BannerImageAdapter;
import com.keepfun.banner.holder.BannerImageHolder;
import com.keepfun.banner.indicator.CircleIndicator;
import com.keepfun.base.PanActivity;
import com.keepfun.base.WebActivity;
import com.keepfun.blankj.constant.TimeConstants;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.PermissionUtils;
import com.keepfun.blankj.util.SpanUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.ThreadUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.blankj.util.Utils;
import com.keepfun.facedetectlib.utils.DensityUtils;
import com.keepfun.media.Logger;
import com.keepfun.media.lib.PeerConnectionUtils;
import com.keepfun.media.lib.RoomClient;
import com.keepfun.media.lib.RoomOptions;
import com.keepfun.media.lib.lv.RoomStore;
import com.keepfun.media.lib.model.Consumers;
import com.keepfun.media.lib.model.Notify;
import com.keepfun.media.lib.model.Producers;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yang
 * @description
 * @date 2020/9/3 9:13 AM
 */
public class ServiceMediaActivity extends PanActivity implements View.OnClickListener {


    public static void start(RoomMediaBean mediaBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Arguments.DATA, mediaBean);
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK, Manifest.permission.MODIFY_AUDIO_SETTINGS};
        if (PermissionUtils.isGranted(permissions)) {
            ActivityUtils.startActivity(bundle, ServiceMediaActivity.class);
        } else {
            PermissionUtils.permission(permissions).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(@NonNull List<String> granted) {
                    ActivityUtils.startActivity(bundle, ServiceMediaActivity.class);
                }

                @Override
                public void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied) {
                    ToastUtils.showShort("请允许获取权限");
                }
            }).request();
        }

    }

    LinearLayout layout_broadcast;
    TextView tv_broadcast;
    RelativeLayout layout_banner;
    Banner banner;
    ImageView iv_thumb;
    SurfaceViewRenderer video_renderer;
    SurfaceViewRenderer fsvr_small;
    TextView tv_title;
    TextView tv_time;
    TextView tv_loud;
    TextView tv_conversion_v2m;
    TextView tv_call_in;
    TextView tv_end_call;
    TextView tv_mute;
    ImageView iv_service_avatar;
    TextView tv_service_name;
    TextView tv_call_in_type;

    ImageView switch_small_window;


    private String mRoomId, mPeerId, mDisplayName;
    private boolean mForceH264, mForceVP9;
    private RoomOptions mOptions;
    private RoomStore mRoomStore;
    private RoomClient mRoomClient;
    private boolean bProducerHasRender = false;

    private VideoTrack videoTrack;
    private VideoTrack remoteVideoTrack;
    private List<VideoTrack> videoTrackList = new LinkedList<>();

    private RoomMediaBean mRoomMediaBean;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
    private boolean isStart = false;
    public static volatile int mediaTime = 0;
    private int mediaType;

    public static final int MEDIA_TYPE_NULL = 0;
    public static final int MEDIA_TYPE_MUTE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private AudioManager audioManager;
    private int currVolume;
    private volatile boolean isAnchorIn = false;

    private long connectDuration = 0;

    private static ServiceMediaActivity serviceMediaActivity = null;

    public static ServiceMediaActivity getServiceMediaActivity() {
        return serviceMediaActivity;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.MODIFY_AUDIO_SETTINGS};
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_media;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.e(TAG, "switchOperation 3 : " + (System.currentTimeMillis() - timeStamp));
    }

    @Override
    public void bindUI(View rootView) {
        serviceMediaActivity = this;

        layout_broadcast = findViewById(R.id.layout_broadcast);
        tv_broadcast = findViewById(R.id.tv_broadcast);
        layout_banner = findViewById(R.id.layout_banner);
        banner = findViewById(R.id.banner);
        iv_thumb = findViewById(R.id.iv_thumb);
        video_renderer = findViewById(R.id.video_renderer);
        fsvr_small = findViewById(R.id.fsvr_small);
        tv_title = findViewById(R.id.tv_title);
        tv_time = findViewById(R.id.tv_time);
        tv_loud = findViewById(R.id.tv_loud);
        tv_conversion_v2m = findViewById(R.id.tv_conversion_v2m);
        tv_call_in = findViewById(R.id.tv_call_in);
        tv_end_call = findViewById(R.id.tv_end_call);
        tv_mute = findViewById(R.id.tv_mute);
        iv_service_avatar = findViewById(R.id.iv_service_avatar);
        tv_service_name = findViewById(R.id.tv_service_name);
        tv_call_in_type = findViewById(R.id.tv_call_in_type);

        switch_small_window = findViewById(R.id.switch_small_window);

        mediaTime = 0;

        try {
            video_renderer.init(PeerConnectionUtils.getEglContext(), null);
//            video_renderer.setMirror(true);

            fsvr_small.init(PeerConnectionUtils.getEglContext(), null);
            fsvr_small.setZOrderMediaOverlay(true);
        } catch (Exception e) {
        }

        Logger.d(TAG, "bindUI: 123 1");
        int n = DensityUtils.dp2px(this, 100);
        Logger.d(TAG, "bindUI: 123 " + n);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.e(TAG, "initDataWithCheck onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e(TAG, "initDataWithCheck onResume");
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.iv_broadcast_close).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_banner_close).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_loud).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_conversion_v2m).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_call_in).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_end_call).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_mute).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.fsvr_small).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.switch_small_window).setOnClickListener(new CheckClickListener(this));
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRoomMediaBean = (RoomMediaBean) bundle.getSerializable(Arguments.DATA);
            mediaType = mRoomMediaBean.getMediaType();
        }
        Logger.startWriteLog();

        if (mRoomMediaBean == null) {
            finish();
            return;
        }

        List<AppAdNoticeBean> notice = JsonUtils.jsonToArrayList(SpManager.getConfig().getString(SpManager.KEY_BROADCAST), AppAdNoticeBean.class);
        List<AppAdNoticeBean> adPics = JsonUtils.jsonToArrayList(SpManager.getConfig().getString(SpManager.KEY_AD_PIC), AppAdNoticeBean.class);
        if (notice != null && notice.size() != 0) {
            layout_broadcast.setVisibility(View.VISIBLE);
            String spanUtils = "";
            for (AppAdNoticeBean bean : notice) {
                spanUtils += Html.fromHtml(bean.getContent()).toString() + "   ";
            }
            spanUtils = spanUtils.replace("\n", "");
            tv_broadcast.setSelected(true);
            tv_broadcast.setText(spanUtils);
        } else {
            layout_broadcast.setVisibility(View.GONE);
        }
        if (adPics != null && adPics.size() != 0) {
            layout_banner.setVisibility(View.VISIBLE);
            List<String> banners = new ArrayList<>();
            for (AppAdNoticeBean bean : adPics) {
                banners.add(bean.getPicUrl());
            }
            banner.setAdapter(new BannerImageAdapter<AppAdNoticeBean>(adPics) {
                @Override
                public void onBindView(BannerImageHolder holder, AppAdNoticeBean data, int position, int size) {
                    //图片加载自己实现
                    Glide.with(holder.itemView)
                            .load(data.getPicUrl()).placeholder(R.mipmap.service_ic_banner_default)
//                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                            .into(holder.imageView);
                    holder.imageView.setOnClickListener(new CheckClickListener(v -> {
                        if (!StringUtils.isEmpty(data.getLinkUrl())) {
                            WebActivity.start(data.getLinkUrl());
                            findViewById(R.id.switch_small_window).callOnClick();
                        }
                    }));
                }
            })
                    //添加生命周期观察者
                    .addBannerLifecycleObserver(this)
                    .setIndicator(new CircleIndicator(this));
        } else {
            layout_banner.setVisibility(View.GONE);
        }
        tv_title.setText(mediaType == MEDIA_TYPE_MUTE ? "语音通话" : "视频通话");
        RequestOptions options1 = new RequestOptions();
        //必须加到List里，否则冲突
        List<Transformation> list = new ArrayList<>();
        list.add(new BlurTransformation(25, 3));
        list.add(new CenterCrop());
        MultiTransformation multiTransformation = new MultiTransformation(list);
        options1.transform(multiTransformation);
        Glide.with(this).load(mRoomMediaBean.getOtherAvatar()).placeholder(R.mipmap.service_bg_chat_default).apply(options1).into(iv_thumb);
        Glide.with(this).load(mRoomMediaBean.getOtherAvatar()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_service_avatar);
        tv_service_name.setText(mRoomMediaBean.getOtherName());
        tv_call_in_type.setText("邀请你" + (mediaType == MEDIA_TYPE_MUTE ? "语音" : "视频") + "通话…");

        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        startHeartbeat();
        openSpeaker();
        createRoom();

        if (mRoomClient != null) {
            mRoomClient.join();
            connectDuration = System.currentTimeMillis();
        }
        if (!mRoomMediaBean.isFrom()) {
            setStatus(2);
        } else {
            setStatus(mediaType == MEDIA_TYPE_VIDEO ? 4 : 3);
        }
    }

    private void createRoom() {
//        Intent intent = new Intent(ServiceMediaActivity.this, MediaService.class);
//        intent.putExtra(MediaService.KEY_MDDEL, mRoomMediaBean);
//        intent.putExtra(MediaService.KEY_DATA, mediaType);
//       Logger.d(TAG, "MediaService initData: 3 " + mediaType);
//        bindService(intent, mServiceConnectin, Context.BIND_AUTO_CREATE);
//       Logger.d(TAG, "createRoom: MediaService");

        mOptions = new RoomOptions();
        loadRoomConfig();

        getViewModelStore().clear();
        initViewModel();
    }

    private void loadRoomConfig() {
        mRoomStore = new RoomStore();
        mRoomId = String.valueOf(mRoomMediaBean.getRoomId());
//        mRoomId = "e2mlh5q8";
        mDisplayName = mRoomMediaBean.getOtherName();
        mForceH264 = SpManager.getConfig().getBoolean("forceH264", true);
        mForceVP9 = SpManager.getConfig().getBoolean("forceVP9", false);

        mPeerId = String.valueOf(mRoomMediaBean.getSelfId());
        mOptions.setmAnchorId(String.valueOf(mRoomMediaBean.getOtherId()));
        mOptions.setProduce(SpManager.getConfig().getBoolean("produce", true));
        mOptions.setConsume(SpManager.getConfig().getBoolean("consume", true));

        mOptions.setbProductVideo(mediaType == MEDIA_TYPE_VIDEO);
        mOptions.setbProductAudio(true);
        mOptions.setForceTcp(SpManager.getConfig().getBoolean("forceTcp", true));

        mOptions.setMediasoupUrl(YLConstant.MEDIA_HOST);

        String camera = "front";
        PeerConnectionUtils.setPreferCameraFace(camera);
        mRoomClient = new RoomClient(this.getApplicationContext(), mRoomStore, mRoomId,
                mPeerId, mDisplayName, mForceH264, mForceVP9, mOptions);
        mRoomClient.setBeautyEnable(false);
        mRoomClient.setTraceFaceEnable(false);
        mRoomClient.setmMainUIHandler(msgHandler);
    }

    private void initViewModel() {

        mRoomStore.getProducers().observe(this, producers -> {
            if (mediaType == MEDIA_TYPE_VIDEO) {
                if (producers != null) {
                    Producers.ProducersWrapper mVideoPW = producers.filter("video");
                    if (mVideoPW != null) {
                        Logger.d(TAG, "initViewModel producers : " + mVideoPW.getProducer().getId());
                        videoTrack = ((VideoTrack) mVideoPW.getProducer().getTrack());
                        if (videoTrack != null && !bProducerHasRender) {
                            bProducerHasRender = true;
                            video_renderer.setTag(videoTrack);
                            Logger.e(TAG, "duration render duration  : " + (System.currentTimeMillis() - connectDuration));
                            if (ismIsOpen()) {
                                addData();
                            } else {
                                videoTrack.addSink(video_renderer);
                            }
                        }
                    }
                }
            }
        });

        // Notify
        final Observer<Notify> notifyObserver =
                notify -> {
                    if (notify == null) {
                        return;
                    }
                    if (!"activeSpeaker".equals(notify.getType())) {
                        Logger.e(TAG, "notify : " + notify.getType() + "   text : " + notify.getText());
                    }
                    if ("error".equals(notify.getType())) {
                        Logger.e(TAG, notify.getText());
                    } else if ("info".equals(notify.getType())) {

                    } else if ("message".equals(notify.getType())) {
                        setStatus(mediaType == MEDIA_TYPE_VIDEO ? 4 : 3);
                        Logger.d(TAG, "initViewModel: 11 " + mediaType);
                        if (mediaType != MEDIA_TYPE_VIDEO) {
                            return;
                        }
                        Logger.e(TAG, "duration consumer join  -----> " + (System.currentTimeMillis() - connectDuration));
                        Logger.d(TAG, "initViewModel: 12 " + mediaType);
//                        if (mRoomMediaBean.isFrom()) {
                        // 若有连麦客户存在，则需要开启列表，保证主播的VideoTrack放在me上
                        // 其他放在远端,传输VideoTrack peers
                        VideoTrack videoTrack = findVideoTrack(notify.getText());
                        if (videoTrack != null) {
                            remoteVideoTrack = videoTrack;
                            videoTrackList.add(videoTrack);
                            showSmallSurfaceRenderer(videoTrack);
                            transVideo();
                            if (ismIsOpen()) {
                                addData();
                            }

                        }
                    } else if ("status-producer".equals(notify.getType())) {
                        // 状态 connected completed closed disconnected failed
                        if ("completed".equals(notify.getText())) {
                            // 隐藏蒙层
                            switch_small_window.setVisibility(View.VISIBLE);
                        }
                    } else if ("status-consumer".equals(notify.getType())) {
                        // 状态 checked connected completed closed disconnected failed
                        Logger.d(TAG, "initViewModel: " + notify.getText());
                        if ("completed".equals(notify.getText())) {
                            // 隐藏蒙层,流展示
                        } else if ("close".equals(notify.getText())) {
                            // 显示正在来的路上
                            remoteVideoTrack = null;
                        }
                    } else if ("anchor".equals(notify.getType())) {
                        if (isAnchorIn) {
                            return;
                        }
                        isAnchorIn = true;
                        if (mediaType == MEDIA_TYPE_VIDEO) {
                            if (mRoomClient != null)
                                mRoomClient.enableMic();
                        }
                        Logger.e(TAG, "duration connect duration  -----> " + (System.currentTimeMillis() - connectDuration));
//                        if ("You are in the room!".equals(notify.getText())) {
                        if (!mRoomMediaBean.isFrom()) {
                            if (mediaType == MEDIA_TYPE_MUTE) {
                                ImClient.getInstance().sendMsg(YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_START, YLConstant.ContentType.CONTENT_TYPE_CUSTOM, "语音通话开始");
                            } else {
                                ImClient.getInstance().sendMsg(YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START, YLConstant.ContentType.CONTENT_TYPE_CUSTOM, "视频通话开始");
                            }
                        }
//                        }
                    }
                };
        mRoomStore.getNotify().observe(this, notifyObserver);
    }

    private VideoTrack findVideoTrack(String consumerId) {
        VideoTrack videoTrack = null;
        Consumers consumers = mRoomStore.getConsumers().getValue();
        Consumers.ConsumerWrapper mVideoCW = consumers.getConsumer(consumerId);
        if (mVideoCW != null) {
            if (mVideoCW.getConsumer().getTrack().kind().equals("video")) {
                videoTrack = ((VideoTrack) mVideoCW.getConsumer().getTrack());
            }
        }

        return videoTrack;
    }

    private boolean isSmallShow = false;

    public void showSmallSurfaceRenderer(VideoTrack videoTrack) {
        if (!isSmallShow) {
            isSmallShow = true;
            videoTrack.addSink(fsvr_small);
            fsvr_small.setTag(videoTrack);
            fsvr_small.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_broadcast_close) {
            layout_broadcast.setVisibility(View.GONE);
        } else if (id == R.id.iv_banner_close) {
            layout_banner.setVisibility(View.GONE);
        } else if (id == R.id.tv_conversion_v2m) {
            setStatus(3);
            ImClient.getInstance().sendMsg(YLConstant.MessageType.MESSAGE_TYPE_VIDEO_2_VOICE, YLConstant.ContentType.CONTENT_TYPE_CUSTOM, "视频转语音");
        } else if (id == R.id.tv_call_in) {
            if (mRoomClient != null) {
                mRoomClient.join();
                connectDuration = System.currentTimeMillis();
            }
            setStatus(mediaType == MEDIA_TYPE_VIDEO ? 4 : 3);
        } else if (id == R.id.tv_mute) {
            switchMute(!tv_mute.isSelected());
        } else if (id == R.id.tv_loud) {
            switchMic(!tv_loud.isSelected());
        } else if (id == R.id.fsvr_small) {
            transVideo();
        } else if (id == R.id.tv_end_call) {
            endCall();
        } else if (id == R.id.switch_small_window) {
            String strBrand = android.os.Build.BRAND;
            if (strBrand.equals("xiaolajiao")) {
                ToastUtils.showShort("这款手机不支持悬浮窗口哦");
                return;
            }

            if (isBuildVersionGreaterM() && !Settings.canDrawOverlays(ServiceMediaActivity.this)) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())));
            } else {
                if (!mIsOpen) {
                    moveTaskToBack(true);

                    createWindow();
                    addView(ServiceMediaActivity.this);
                    addData();
                }
            }
        }
    }

    private void endCall() {
        ImClient.getInstance().sendMsg(mediaType == MEDIA_TYPE_VIDEO ? YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END : YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END
                , YLConstant.ContentType.CONTENT_TYPE_CUSTOM, mediaType == MEDIA_TYPE_VIDEO ? "视频通话结束" : "语音通话结束", mediaTime);
        finish();
    }

    private void transVideo() {
        VideoTrack smallVideoTrack = (VideoTrack) fsvr_small.getTag();
        VideoTrack bigVideoTrack = (VideoTrack) video_renderer.getTag();
        if (smallVideoTrack == null || bigVideoTrack == null) {
            return;
        }
        smallVideoTrack.removeSink(fsvr_small);
        smallVideoTrack.addSink(video_renderer);
        video_renderer.setTag(smallVideoTrack);
        bigVideoTrack.removeSink(video_renderer);
        bigVideoTrack.addSink(fsvr_small);
        fsvr_small.setTag(bigVideoTrack);
    }

    /**
     * @param status 1 拨入 2 拨出 3 语音 4 视频
     */
    public void setStatus(int status) {
        new Handler(Looper.getMainLooper()).post(() -> {
            switch (status) {
                case 1:
                    iv_service_avatar.setVisibility(View.VISIBLE);
                    tv_service_name.setVisibility(View.VISIBLE);
                    tv_call_in_type.setVisibility(View.VISIBLE);
                    tv_time.setVisibility(View.GONE);
                    setLoudShow(false);
                    tv_conversion_v2m.setVisibility(View.GONE);
                    tv_call_in.setVisibility(View.VISIBLE);
                    tv_end_call.setVisibility(View.VISIBLE);
                    tv_mute.setVisibility(View.GONE);
                    RingUtils.startRing();
                    video_renderer.setVisibility(mediaType == MEDIA_TYPE_VIDEO ? View.VISIBLE : View.INVISIBLE);
                    fsvr_small.setVisibility(View.INVISIBLE);
                    switch_small_window.setVisibility(View.GONE);
                    break;
                case 2:
                    iv_service_avatar.setVisibility(View.VISIBLE);
                    tv_service_name.setVisibility(View.VISIBLE);
                    tv_call_in_type.setVisibility(View.GONE);
                    tv_time.setVisibility(View.VISIBLE);
                    tv_time.setText("正在接通中...");
                    setLoudShow(false);
                    tv_conversion_v2m.setVisibility(View.GONE);
                    tv_call_in.setVisibility(View.GONE);
                    tv_end_call.setVisibility(View.VISIBLE);
                    tv_mute.setVisibility(View.GONE);
                    RingUtils.startRing();
                    video_renderer.setVisibility(mediaType == MEDIA_TYPE_VIDEO ? View.VISIBLE : View.INVISIBLE);
                    fsvr_small.setVisibility(View.INVISIBLE);
                    switch_small_window.setVisibility(View.GONE);

                    tv_end_call.postDelayed(callRunnable, TimeConstants.MIN);
                    break;
                case 3:
                    mediaType = MEDIA_TYPE_MUTE;
                    iv_service_avatar.setVisibility(View.VISIBLE);
                    tv_service_name.setVisibility(View.VISIBLE);
                    tv_call_in_type.setVisibility(View.GONE);
                    tv_time.setVisibility(View.VISIBLE);
                    setLoudShow(true);
                    tv_conversion_v2m.setVisibility(View.GONE);
                    tv_call_in.setVisibility(View.GONE);
                    tv_end_call.setVisibility(View.VISIBLE);
                    tv_mute.setVisibility(View.VISIBLE);
                    video_renderer.setVisibility(View.INVISIBLE);
                    fsvr_small.setVisibility(View.INVISIBLE);
                    switch_small_window.setVisibility(View.VISIBLE);
                    RingUtils.release();
                    if (mRoomClient != null)
                        mRoomClient.disableCam();
                    startTimer();

                    // 从音视频切换回语音时，需要关闭小窗口
                    switchOperation(true, ServiceMediaActivity.this);

                    tv_end_call.removeCallbacks(callRunnable);
                    break;
                case 4:
                    mediaType = MEDIA_TYPE_VIDEO;
                    iv_service_avatar.setVisibility(View.GONE);
                    tv_service_name.setVisibility(View.GONE);
                    tv_call_in_type.setVisibility(View.GONE);
                    tv_time.setVisibility(View.VISIBLE);
                    setLoudShow(false);
                    tv_conversion_v2m.setVisibility(View.VISIBLE);
                    tv_call_in.setVisibility(View.GONE);
                    tv_end_call.setVisibility(View.VISIBLE);
                    tv_mute.setVisibility(View.VISIBLE);
                    RingUtils.release();
                    video_renderer.setVisibility(View.VISIBLE);
                    fsvr_small.setVisibility(View.VISIBLE);
                    switch_small_window.setVisibility(View.VISIBLE);
                    if (mRoomClient != null)
                        mRoomClient.enableCam();
                    startTimer();
                    tv_end_call.removeCallbacks(callRunnable);
                    break;
                default:
                    break;
            }

        });
    }

    private void setLoudShow(boolean isShow) {
        tv_loud.clearAnimation();
        tv_loud.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private synchronized void startTimer() {
        if (!isStart) {
            isStart = true;
            scheduledExecutorService.scheduleWithFixedDelay(timeRunnable, 500, 1000, TimeUnit.MILLISECONDS);
        }
    }

    private void startHeartbeat() {
        scheduledExecutorService.scheduleWithFixedDelay(heartRunnable, 500, 60 * 1000, TimeUnit.MILLISECONDS);
    }

    public void enableVideo(boolean isOn) {
        if (isOn) {
            //消费视频
            mRoomClient.disableAudioOnly();
            mRoomClient.enableCam();
        } else {
            //只消费音频
            mRoomClient.enableAudioOnly();
            mRoomClient.disableCam();
        }

    }

    public void switchMute(boolean isOn) {
        if (isOn) {
            if (mRoomClient != null)
                mRoomClient.muteMic();
        } else {
            if (mRoomClient != null)
                mRoomClient.unmuteMic();
        }
        tv_mute.setSelected(isOn);
    }

    public void switchMic(boolean isOn) {
        if (isOn) {
            openSpeaker();
        } else {
            closeSpeaker();
        }
        tv_loud.setSelected(isOn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RingUtils.release();
        bProducerHasRender = false;

        if (!scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
        if (tv_end_call.getHandler().hasCallbacks(callRunnable)) {
            tv_end_call.removeCallbacks(callRunnable);
        }
        destroyRoom();

        removeView();

        serviceMediaActivity = null;

        if (!UIUtils.isForeground(ActivityUtils.getTopActivity()) && (!(ActivityUtils.getTopActivity() instanceof ServiceMediaActivity))) {
            ActivityUtils.startActivity(ActivityUtils.getTopActivity().getIntent().getExtras(), ActivityUtils.getTopActivity().getClass());
            ActivityUtils.getTopActivity().finish();
        }
    }


    private void destroyRoom() {
//        unbindService(mServiceConnectin);
        if (mRoomClient != null) {
            mRoomClient.close();
            mRoomClient = null;
        }
        if (video_renderer != null) {
            video_renderer.release();
        }
        if (fsvr_small != null) {
            fsvr_small.release();
        }

        if (mRoomStore != null) {
            mRoomStore = null;
        }
    }

    @Override
    public void onBackPressed() {

    }

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            mediaTime++;
            new Handler(Looper.getMainLooper()).post(() -> {
                if (tv_time != null) {
                    try {
                        int hours = mediaTime / 60 / 60;
                        int minutes = (mediaTime - hours * 3600) / 60;
                        minutes = minutes > 0 ? minutes : 0;
                        int seconds = mediaTime % 60;

                        String strTemp = getTimeSpan(hours) + ":" + getTimeSpan(minutes) + ":" + getTimeSpan(seconds);
                        tv_time.setText(strTemp);
                        if (mSmallWindowView != null && mediaType == MEDIA_TYPE_MUTE) {
                            mSmallWindowView.updateTimer(strTemp);
                        }
                        Logger.d(TAG, "run: 1 " + strTemp);
                    } catch (Exception e) {
                        Logger.e(TAG, "time Exception e : " + e.getMessage());
                    }
                }
            });

        }
    };
    private Runnable heartRunnable = () -> ImClient.getInstance().sendMsg(YLConstant.MessageType.MESSAGE_TYPE_HEARTBEAT, YLConstant.ContentType.CONTENT_TYPE_TEXT, "心跳");

    public Runnable callRunnable = new Runnable() {
        @Override
        public void run() {
            ImClient.getInstance().sendMsg(mediaType == MEDIA_TYPE_VIDEO ? YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END : YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END
                    , YLConstant.ContentType.CONTENT_TYPE_CUSTOM, mediaType == MEDIA_TYPE_VIDEO ? "视频通话结束" : "语音通话结束", mediaTime);
            ToastUtils.showShort("无人接听稍后再拨");
            finish();
        }
    };

    private String getTimeSpan(int time) {
        if (time > 9) {
            return String.valueOf(time);
        } else {
            return "0" + time;
        }
    }

    private void openSpeaker() {
        try {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            currVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
            if (!audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(true);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.STREAM_VOICE_CALL);
            }
        } catch (Exception e) {
            Logger.e(TAG, "openSpeaker e : " + e.getMessage());
        }
    }

    private void closeSpeaker() {
        try {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            if (audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(false);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, currVolume, AudioManager.STREAM_VOICE_CALL);
            }
        } catch (Exception e) {
            Logger.e(TAG, "closeSpeaker e : " + e.getMessage());
        }
    }

    @Subscribe
    public void receiveMessage(Message message) {
        if (StringUtils.isEmpty(message.getGroupId()) && !message.getGroupId().equals(mRoomMediaBean.getGroupId())) {
            return;
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END
                || message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END
                || message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_END
        ) {
            finish();
        } else if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_VIDEO_2_VOICE) {
            setStatus(3);
        } else if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SERVICE_TRANS_TIP) {
            //客服转接，转接后的客服不具有音视频功能，则取消拨打
            ServiceEntity serviceEntity = getServiceEntity(message.getExt());
            if (serviceEntity == null) {
                return;
            }
            boolean showMore = serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VIP || serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VOICE || serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VIDEO;
            if (!showMore) {
                finish();
            }
        }
    }

    private ServiceEntity getServiceEntity(Object object) {
        if (object == null) {
            return null;
        }
        List<Object> list = JsonUtil.parseArrayList(JsonUtil.encode(object), Object.class);
        if (list == null || list.size() < 8) {
            return null;
        }
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setConversationType(((Double) list.get(3)).intValue());
        serviceEntity.setShopId(Long.valueOf((String) list.get(4)));
        serviceEntity.setAvatarUrl((String) list.get(5));
        serviceEntity.setName((String) list.get(6));
        serviceEntity.setNameEn((String) list.get(7));
        return serviceEntity;
    }

    WindowManager.LayoutParams mParams;
    WindowManager mWindowManager;

    int mWidth;
    int mHeight;

    SmallWindowView mSmallWindowView;

    public boolean mIsOpen;

    private float mTouchX = 0f;
    private float mTouchY = 0f;

    public boolean ismIsOpen() {
        return mIsOpen;
    }

    /**
     * 初始化windows数据
     */
    private void createWindow() {
        mParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }


        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;

        mParams.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        //设置窗口初始停靠位置.
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.x = mWidth;
        mParams.y = 400;
    }

    private void removeView() {
        if (mIsOpen) {
            mIsOpen = false;

            if (mSmallWindowView != null) {
                VideoTrack videoTrack = mSmallWindowView.getDisplayVideoTrack();
                if (videoTrack != null) {
                    mSmallWindowView.getSurfaceViewRenderer().setVisibility(View.GONE);
                    videoTrack.removeSink(mSmallWindowView.getSurfaceViewRenderer());
                    mSmallWindowView.setDisplayVideoTrack(null);
                }
            }

            if (mWindowManager != null) {
                mWindowManager.removeView(mSmallWindowView);
            }
        }
    }

    /**
     * 创建悬浮窗口View
     */
    public void addView(Context context) {
        if (mSmallWindowView == null)
            mSmallWindowView = new SmallWindowView(context);
        mSmallWindowView.setKeepScreenOn(true);
        mSmallWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mTouchX = event.getX();
                    mTouchY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    mParams.x = (int) (event.getRawX() - mTouchX);
                    mParams.y = (int) (event.getRawY() - mTouchY);
                    Logger.d(TAG, "onTouch: " + mParams.x + " " + mParams.y + " " + mTouchX + " " + mTouchY);
                    mWindowManager.updateViewLayout(mSmallWindowView, mParams);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    int px = (int) (event.getX() - mTouchX);
                    int py = (int) (event.getY() - mTouchY);
                    Logger.d(TAG, "onTouch: " + px + " " + py + " " + mTouchX + " " + mTouchY);
                    if (px == 0 && py == 0) {
                        switchOperation(false, context);
                    }
                }

                return false;
            }
        });

        if (mediaType == MEDIA_TYPE_MUTE) {
            mParams.width = DensityUtils.dp2px(this, 100);
            mParams.height = DensityUtils.dp2px(this, 100);
        } else {
            mParams.width = DensityUtils.dp2px(this, 140);
            mParams.height = DensityUtils.dp2px(this, 230);
        }

        mParams.x = (mWidth - mParams.width - 50);
        mIsOpen = true;
        mWindowManager.addView(mSmallWindowView, mParams);
    }

    public void addData() {
        VideoTrack bigVideoTrack = (VideoTrack) video_renderer.getTag();
        if (mSmallWindowView != null) {
            mSmallWindowView.updateWindow(bigVideoTrack, mediaType);
        }
    }

    private boolean isBuildVersionGreaterM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private static long timeStamp;

    protected void switchOperation(boolean switchAudio, Context context) {
        if (mIsOpen) {
            if (mSmallWindowView != null) {
                mSmallWindowView.setVisibility(View.VISIBLE);
            }

            if (context != null) {
//                Intent intent = new Intent(context, ServiceMediaActivity.class);
//                context.startActivity(intent);

                if (isAllowed() || !isBackground(this)) {
                    if (!isBackground(this)) {
                        timeStamp = System.currentTimeMillis();
                        Logger.e(TAG, "switchOperation 1 : " + (System.currentTimeMillis() - timeStamp));
                        removeView();
                        ActivityUtils.startActivity(ServiceMediaActivity.class);
                        Logger.e(TAG, "switchOperation 2 : " + (System.currentTimeMillis() - timeStamp));
                    } else {
                        ToastUtils.showShort("悬浮窗点击受限，请返回客服应用继续会话");
                    }
                } else {
                    ToastUtils.showShort("悬浮窗点击受限，请返回客服应用继续会话");
                }
            }

            if (switchAudio) {
                video_renderer.setTag(null);
            } else {
                VideoTrack bigVideoTrack = (VideoTrack) video_renderer.getTag();
                if (bigVideoTrack != null)
                    bigVideoTrack.addSink(video_renderer);
            }
        }
    }

    private boolean isAllowed() {
        AppOpsManager ops = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10021;
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]{int.class, int.class, String.class});
            Integer result = (Integer) method.invoke(ops, op, Process.myUid(), getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;

        } catch (Exception e) {
            Logger.e(TAG, "not support");
        }
        return false;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    public static void closeSmallWindow() {
        if (ServiceMediaActivity.getServiceMediaActivity() != null) {
            int mediaType = ServiceMediaActivity.getServiceMediaActivity().mediaType;
            ImClient.getInstance().sendMsg(mediaType == MEDIA_TYPE_VIDEO ? YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END : YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END
                    , YLConstant.ContentType.CONTENT_TYPE_CUSTOM, mediaType == MEDIA_TYPE_VIDEO ? "视频通话结束" : "语音通话结束", mediaTime);
            ServiceMediaActivity.getServiceMediaActivity().finish();
        }
    }

    Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            if (mIsOpen == false) {
                return;
            }

            PreviewEvent previewEvent = null;
            if (msg.what == RoomOptions.MSG_JOIN_ROOM) {
                previewEvent = new PreviewEvent("info", (String) msg.obj, "", null);
            } else if (msg.what == RoomOptions.MSG_PRODUCE_STATUS) {
                previewEvent = new PreviewEvent("status-producer", (String) msg.obj, "", null);
            } else if (msg.what == RoomOptions.MSG_CONSUMER_STATUS) {
                previewEvent = new PreviewEvent("status-consumer", (String) msg.obj, "", null);
            } else if (msg.what == RoomOptions.MSG_CONSUMER_JOIN) {
                String temp = (String) msg.obj;
                if (temp.equals("bundle_data")) {
                    Bundle data = msg.getData();
                    previewEvent = new PreviewEvent("message", (String) data.getString("consumerId"), data.getString("peerId"), null);
                } else {
                    previewEvent = new PreviewEvent("message", (String) msg.obj, "", null);
                }
            } else if (msg.what == RoomOptions.MSG_IS_ANCHOR) {
                previewEvent = new PreviewEvent("anchor", (String) msg.obj, "", null);
            } else if (msg.what == RoomOptions.MSG_SWITCH_CAMERA) {
                previewEvent = new PreviewEvent("switchcamera", (String) msg.obj, "", null);
            } else if (msg.what == RoomOptions.MSG_PRODUCTOR_VIDEO) {
                previewEvent = new PreviewEvent("productor_video", (String) msg.obj, "", null);
            }
            if (previewEvent != null) {
                EventBus.getDefault().post(previewEvent);
            }

            super.handleMessage(msg);
        }
    };

    //    MediaService mediaService = null;
//    private ServiceConnection mServiceConnectin = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mediaService = ((MediaService.MediaBinder)service).getService();
//           Logger.d(TAG, "MediaService ServiceConnection onServiceConnected: 1");
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//           Logger.d(TAG, "MediaService ServiceConnection onServiceDisconnected: 2");
//        }
//    };
//
    @Subscribe
    public void receiveMessage(PreviewEvent previewEvent) {
        Logger.d(TAG, "MediaService receiveMessage: " + previewEvent.getType());

        switch (previewEvent.type) {
            case "error":
            case "info":
            case "message":
            case "status-producer":
            case "status-consumer":
            case "anchor":
            case "switchcamera":
            case "productor":
            case "activeSpeaker":
            case "productor_video":
                handlerMediasoupEvent(previewEvent);
                break;
            default:
                break;
        }
    }

    protected void handlerMediasoupEvent(PreviewEvent previewEvent) {
        // 需要根据peerId找到相关的RoomClient
        String peerId = previewEvent.getType();

        if (!"activeSpeaker".equals(previewEvent.getType())) {
            Logger.e(TAG, "MediaService notify : " + previewEvent.getType() + "   text : " + previewEvent.getmNotifyContent1());
        }
        if ("error".equals(previewEvent.getType())) {
            Logger.e(TAG, previewEvent.getmNotifyContent1());
        } else if ("info".equals(previewEvent.getType())) {

        } else if ("message".equals(previewEvent.getType())) {
            setStatus(mediaType == MEDIA_TYPE_VIDEO ? 4 : 3);
            Logger.d(TAG, "initViewModel: 11 " + mediaType);
            if (mediaType != MEDIA_TYPE_VIDEO) {
                return;
            }
            Logger.e(TAG, "duration consumer join small  -----> " + (System.currentTimeMillis() - connectDuration));
            Logger.d(TAG, "initViewModel: 12 " + mediaType);
            // 若有连麦客户存在，则需要开启列表，保证主播的VideoTrack放在me上
            // 其他放在远端,传输VideoTrack peers
            VideoTrack videoTrack = findVideoTrack(previewEvent.getmNotifyContent1());
            if (videoTrack != null) {
                remoteVideoTrack = videoTrack;
                videoTrackList.add(videoTrack);
                showSmallSurfaceRenderer(videoTrack);
                transVideo();
                if (ismIsOpen()) {
                    addData();
                }

            }
        } else if ("status-producer".equals(previewEvent.getType())) {
            // 状态 connected completed closed disconnected failed
            if ("completed".equals(previewEvent.getmNotifyContent1())) {
                // 隐藏蒙层
                switch_small_window.setVisibility(View.VISIBLE);
            }
        } else if ("status-consumer".equals(previewEvent.getType())) {
            // 状态 checked connected completed closed disconnected failed
            Logger.d(TAG, "initViewModel: " + previewEvent.getmNotifyContent1());
            if ("completed".equals(previewEvent.getmNotifyContent1())) {

            } else if ("close".equals(previewEvent.getmNotifyContent1())) {
                // 显示正在来的路上
                remoteVideoTrack = null;
            }
        } else if ("anchor".equals(previewEvent.getType())) {
            if (isAnchorIn) {
                return;
            }
            isAnchorIn = true;
            if (mediaType == MEDIA_TYPE_VIDEO) {
                if (mRoomClient != null)
                    mRoomClient.enableMic();
            }
            Logger.e(TAG, "duration connect duration  small -----> " + (System.currentTimeMillis() - connectDuration));

            if (!mRoomMediaBean.isFrom()) {
                if (mediaType == MEDIA_TYPE_MUTE) {
                    ImClient.getInstance().sendMsg(YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_START, YLConstant.ContentType.CONTENT_TYPE_CUSTOM, "语音通话开始");
                } else {
                    ImClient.getInstance().sendMsg(YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START, YLConstant.ContentType.CONTENT_TYPE_CUSTOM, "视频通话开始");
                }
            }
        } else if ("productor".equals(previewEvent.getType())) {
            videoTrack = ((VideoTrack) previewEvent.getVideoTrack());
            Logger.d(TAG, "initViewModel producers : " + videoTrack);
            if (videoTrack != null && !bProducerHasRender) {
                bProducerHasRender = true;
                video_renderer.setTag(videoTrack);
                if (ismIsOpen()) {
                    addData();
                } else {
                    videoTrack.addSink(video_renderer);
                }
                Logger.e(TAG, "initViewModel  ---------------");

            }
        } else if ("productor_video".equals(previewEvent.getType())) {
            Producers producers = mRoomStore.getProducers().getValue();
            Logger.d(TAG, "initViewModel producers :  0 " + producers + " " + previewEvent.getmNotifyContent1());
            if (producers != null) {
                Producers.ProducersWrapper mVideoPW = producers.filter("video");
                Logger.d(TAG, "initViewModel producers :  1 " + mVideoPW);
                Logger.e(TAG, "duration render duration  small-----> " + (System.currentTimeMillis() - connectDuration));
                if (mVideoPW != null) {

                    Logger.d(TAG, "initViewModel producers : " + mVideoPW.getProducer().getId());
                    videoTrack = ((VideoTrack) mVideoPW.getProducer().getTrack());
                    if (videoTrack != null && !bProducerHasRender) {
                        bProducerHasRender = true;

                        Logger.d(TAG, "initViewModel producers : 21  ---------------");
                        video_renderer.setTag(videoTrack);
                        if (ismIsOpen()) {
                            addData();
                        } else {
                            videoTrack.addSink(video_renderer);
                        }
                    }
                }
            }
        }
    }
}
