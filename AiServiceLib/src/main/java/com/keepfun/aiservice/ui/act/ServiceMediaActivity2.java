package com.keepfun.aiservice.ui.act;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
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
import android.provider.Settings;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.keepfun.aiservice.utils.MusicPlayer;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.aiservice.utils.blur.BlurTransformation;
import com.keepfun.banner.Banner;
import com.keepfun.banner.adapter.BannerImageAdapter;
import com.keepfun.banner.holder.BannerImageHolder;
import com.keepfun.banner.indicator.CircleIndicator;
import com.keepfun.base.PanActivity;
import com.keepfun.base.WebActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.facedetectlib.utils.DensityUtils;
import com.keepfun.media.lib.PeerConnectionUtils;
import com.keepfun.media.lib.RoomClient;
import com.keepfun.media.lib.RoomOptions;
import com.keepfun.media.lib.lv.RoomStore;
import com.keepfun.media.lib.model.Consumers;
import com.keepfun.media.lib.model.Notify;
import com.keepfun.media.lib.model.Producers;

import org.greenrobot.eventbus.Subscribe;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author yang
 * @description
 * @date 2020/9/3 9:13 AM
 */
public class ServiceMediaActivity2 extends PanActivity implements View.OnClickListener {

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

    private MusicPlayer musicPlayer;

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
    private int mediaTime = 0;
    private int mediaType;

    public static final int MEDIA_TYPE_NULL = 0;
    public static final int MEDIA_TYPE_MUTE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private AudioManager audioManager;
    private int currVolume;

    private static ServiceMediaActivity2 serviceMediaActivity = null;

    public static ServiceMediaActivity2 getServiceMediaActivity() {
        return serviceMediaActivity;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK, Manifest.permission.MODIFY_AUDIO_SETTINGS};
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

        try {
            video_renderer.init(PeerConnectionUtils.getEglContext(), null);
//            video_renderer.setMirror(true);

            fsvr_small.init(PeerConnectionUtils.getEglContext(), null);
            fsvr_small.setZOrderMediaOverlay(true);
        } catch (Exception e) {
        }
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
        musicPlayer = new MusicPlayer();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRoomMediaBean = (RoomMediaBean) bundle.getSerializable(Arguments.DATA);
            mediaType = mRoomMediaBean.getMediaType();
            Log.d(TAG, "MediaService initData: " + mediaType);
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

//        if (mRoomClient != null) {
//            mRoomClient.join();
//        }
        if (!mRoomMediaBean.isFrom()) {
            setStatus(2);
        } else {
            setStatus(mediaType == MEDIA_TYPE_VIDEO ? 4 : 3);
        }

        Log.d(TAG, "MediaService initData: 2 " + mediaType);
    }

    private void createRoom() {
        Intent intent = new Intent(ServiceMediaActivity2.this, MediaService.class);
        intent.putExtra(MediaService.KEY_MDDEL, mRoomMediaBean);
        intent.putExtra(MediaService.KEY_DATA, mediaType);
        Log.d(TAG, "MediaService initData: 3 " + mediaType);
        bindService(intent, mServiceConnectin, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "createRoom: MediaService");

//        mOptions = new RoomOptions();
//        loadRoomConfig();
//
//        getViewModelStore().clear();
//        initViewModel();
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

//        mOptions.setMediasoupUrl("101.133.230.207");
        //徐其文
//        mOptions.setMediasoupUrl("192.168.1.131");
        //李明
//        mOptions.setMediasoupUrl("192.168.1.47");
//        mOptions.setMediasoupUrl("mediasoup.keepfun.cn");
        String camera = "front";
        PeerConnectionUtils.setPreferCameraFace(camera);
        mRoomClient = new RoomClient(this.getApplicationContext(), mRoomStore, mRoomId,
                mPeerId, mDisplayName, mForceH264, mForceVP9, mOptions);
        mRoomClient.setBeautyEnable(false);
        mRoomClient.setTraceFaceEnable(false);

    }

    private void initViewModel() {

        mRoomStore.getProducers().observe(this, producers -> {
            if (mediaType == MEDIA_TYPE_VIDEO) {
                if (producers != null) {
                    Producers.ProducersWrapper mVideoPW = producers.filter("video");
                    if (mVideoPW != null) {
                        LogUtils.d("initViewModel producers : " + mVideoPW.getProducer().getId());
                        videoTrack = ((VideoTrack) mVideoPW.getProducer().getTrack());
                        if (videoTrack != null && !bProducerHasRender) {
                            bProducerHasRender = true;
                            videoTrack.addSink(video_renderer);
                            LogUtils.e("initViewModel  ---------------");
                            video_renderer.setTag(videoTrack);
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
                        LogUtils.e("notify : " + notify.getType() + "   text : " + notify.getText());
                    }
                    if ("error".equals(notify.getType())) {
                        LogUtils.e(notify.getText());
                    } else if ("info".equals(notify.getType())) {

                    } else if ("message".equals(notify.getType())) {
                        setStatus(mediaType == MEDIA_TYPE_VIDEO ? 4 : 3);
                        Log.d(TAG, "initViewModel: 11 " + mediaType);
                        if (mediaType != MEDIA_TYPE_VIDEO) {
                            return;
                        }

                        Log.d(TAG, "initViewModel: 12 " + mediaType);
//                        if (mRoomMediaBean.isFrom()) {
                        // 若有连麦客户存在，则需要开启列表，保证主播的VideoTrack放在me上
                        // 其他放在远端,传输VideoTrack peers
                        VideoTrack videoTrack = findVideoTrack(notify.getText());
                        if (videoTrack != null) {
                            remoteVideoTrack = videoTrack;
                            showSmallSurfaceRenderer(videoTrack);
                            videoTrackList.add(videoTrack);
                            transVideo();
                        }
                    } else if ("status-producer".equals(notify.getType())) {
                        // 状态 connected completed closed disconnected failed
                        if ("completed".equals(notify.getText())) {
                            // 隐藏蒙层
                        }
                    } else if ("status-consumer".equals(notify.getType())) {
                        // 状态 checked connected completed closed disconnected failed
                        Log.d(TAG, "initViewModel: " + notify.getText());
                        if ("completed".equals(notify.getText())) {
                            // 隐藏蒙层,流展示
//                            if (mRoomClient != null) {
//                                mRoomClient.join();
//                            }
                        } else if ("close".equals(notify.getText())) {
                            // 显示正在来的路上
                            remoteVideoTrack = null;
                        }
                    } else if ("anchor".equals(notify.getType())) {
                        if (mediaType == MEDIA_TYPE_VIDEO) {
                            if (mediaService != null)
                                mediaService.enableMic();
                        }
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

    private void startRing() {
        try {
            AssetFileDescriptor fd = getContext().getAssets().openFd("ring.mp3");
            musicPlayer.prepare(fd);
            musicPlayer.setMusicPlayerListener(new MusicPlayer.MusicPlayerListener() {
                @Override
                public void onPrepared(int duration) {

                }

                @Override
                public void onCompletion() {
                    musicPlayer.reset();
                    musicPlayer.prepare(fd);
                }

                @Override
                public void onError(int what) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            }
            setStatus(mediaType == MEDIA_TYPE_VIDEO ? 4 : 3);
        } else if (id == R.id.tv_mute) {
            switchMute(!tv_mute.isSelected());
        } else if (id == R.id.tv_loud) {
            switchMic(!tv_loud.isSelected());
        } else if (id == R.id.fsvr_small) {
            transVideo();
        } else if (id == R.id.tv_end_call) {
            ImClient.getInstance().sendMsg(mediaType == MEDIA_TYPE_VIDEO ? YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END : YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END
                    , YLConstant.ContentType.CONTENT_TYPE_CUSTOM, mediaType == MEDIA_TYPE_VIDEO ? "视频通话结束" : "语音通话结束", mediaTime);
            finish();
        }else if (id == R.id.switch_small_window){
            if (isBuildVersionGreaterM() && !Settings.canDrawOverlays(ServiceMediaActivity2.this)) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())));
            } else{
                if (mIsOpen == false){
                    moveTaskToBack(true);

                    createWindow();
                    addView(ServiceMediaActivity2.this);
                    addData();
                }
            }
        }
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
                    startRing();
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
                    startRing();
                    video_renderer.setVisibility(mediaType == MEDIA_TYPE_VIDEO ? View.VISIBLE : View.INVISIBLE);
                    fsvr_small.setVisibility(View.INVISIBLE);
                    switch_small_window.setVisibility(View.GONE);
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
                    musicPlayer.release();
                    if (mediaService != null)
                        mediaService.disableCam();
                    startTimer();

                    // 从音视频切换回语音时，需要关闭小窗口
                    switchOperation(true, ServiceMediaActivity2.this);
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
                    musicPlayer.release();
                    video_renderer.setVisibility(View.VISIBLE);
                    fsvr_small.setVisibility(View.VISIBLE);
                    switch_small_window.setVisibility(View.VISIBLE);
                    if (mediaService != null)
                        mediaService.enableCam();
                    startTimer();
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
            if (mediaService != null)
                mediaService.muteMic();
        } else {
            if (mediaService != null)
                mediaService.unmuteMic();
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
        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
        }
        bProducerHasRender = false;

        if (!scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }

        destroyRoom();

        removeView();

        serviceMediaActivity = null;
    }


    private void destroyRoom() {
        unbindService(mServiceConnectin);
//        if (mRoomClient != null) {
//            mRoomClient.close();
//            mRoomClient = null;
//        }
//        if (video_renderer != null) {
//            video_renderer.release();
//        }
//        if (fsvr_small != null) {
//            fsvr_small.release();
//        }
//
//        if (mRoomStore != null) {
//            mRoomStore = null;
//        }
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
                        if (mSmallWindowView != null && mediaType == MEDIA_TYPE_MUTE){
                            mSmallWindowView.updateTimer(strTemp);
                        }
                        Log.d(TAG, "run: 1 " + strTemp);
                    } catch (Exception e) {
                        LogUtils.e("time Exception e : " + e.getMessage());
                    }
                }
            });

        }
    };
    private Runnable heartRunnable = new Runnable() {
        @Override
        public void run() {
            ImClient.getInstance().sendMsg(YLConstant.MessageType.MESSAGE_TYPE_HEARTBEAT, YLConstant.ContentType.CONTENT_TYPE_TEXT, "心跳");
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
            LogUtils.e("openSpeaker e : " + e.getMessage());
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
            LogUtils.e("closeSpeaker e : " + e.getMessage());
        }
    }

    @Subscribe
    public void receiveMessage(Message message) {
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

    boolean mIsOpen;

    private float mTouchX = 0f;
    private float mTouchY = 0f;

    /**
     * 初始化windows数据
     */
    private void createWindow() {
        mParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager)getApplication().getSystemService(Context.WINDOW_SERVICE);


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
        if (mIsOpen){
            mIsOpen = false;

            if (mSmallWindowView != null){
                VideoTrack videoTrack = mSmallWindowView.getDisplayVideoTrack();
                if (videoTrack != null){
                    videoTrack.removeSink(mSmallWindowView.getSurfaceViewRenderer());
                    mSmallWindowView.setDisplayVideoTrack(null);
                }
            }

            if (mWindowManager != null){
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

        mSmallWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    mTouchX = event.getX();
                    mTouchY = event.getY();
                }else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    mParams.x = (int)(event.getRawX() - mTouchX);
                    mParams.y = (int)(event.getRawY() - mTouchY);
                    Log.d(TAG, "onTouch: " + mParams.x + " " + mParams.y + " " + mTouchX + " " + mTouchY);
                    mWindowManager.updateViewLayout(mSmallWindowView, mParams);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    int px = (int)(event.getX() - mTouchX);
                    int py = (int)(event.getY() - mTouchY);
                    Log.d(TAG, "onTouch: " + px + " " + py + " " + mTouchX + " " + mTouchY);
                    if (px == 0 && py == 0){
                        switchOperation(false, context);
                    }
                }

                return false;
            }
        });

        if (mediaType == MEDIA_TYPE_MUTE){
            mParams.width = DensityUtils.dp2px(this, 100);
            mParams.height = DensityUtils.dp2px(this, 100);
        }else{
            mParams.width = DensityUtils.dp2px(this, 140);
            mParams.height = DensityUtils.dp2px(this, 230);
        }

        mParams.x = (mWidth - mParams.width - 50);
        mIsOpen = true;
        mWindowManager.addView(mSmallWindowView, mParams);
    }

    public void addData() {
        VideoTrack bigVideoTrack = (VideoTrack) video_renderer.getTag();
        if(mSmallWindowView != null){
            mSmallWindowView.updateWindow(bigVideoTrack, mediaType);
        }
    }

    private boolean isBuildVersionGreaterM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    protected void switchOperation(boolean switchAudio, Context context){
        if (mIsOpen){
            if (context != null) {
//                Intent intent = new Intent(context, ServiceMediaActivity.class);
//                context.startActivity(intent);

                ActivityUtils.startActivity(ServiceMediaActivity2.class);

                removeView();
            }

            if (switchAudio){
                video_renderer.setTag(null);
            }else{
                VideoTrack bigVideoTrack = (VideoTrack) video_renderer.getTag();
                if (bigVideoTrack != null)
                    bigVideoTrack.addSink(video_renderer);
            }
        }
    }

    public void closeSmallWindow(){
        ImClient.getInstance().sendMsg(mediaType == MEDIA_TYPE_VIDEO ? YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END : YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END
                , YLConstant.ContentType.CONTENT_TYPE_CUSTOM, mediaType == MEDIA_TYPE_VIDEO ? "视频通话结束" : "语音通话结束", mediaTime);
        finish();
    }

    MediaService mediaService = null;
    private ServiceConnection mServiceConnectin = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaService = ((MediaService.MediaBinder)service).getService();
            Log.d(TAG, "MediaService ServiceConnection onServiceConnected: 1");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "MediaService ServiceConnection onServiceDisconnected: 2");
        }
    };

    @Subscribe
    public void receiveMessage(PreviewEvent previewEvent) {
        Log.d(TAG, "MediaService receiveMessage: " + previewEvent.getType());

        switch (previewEvent.type){
            case "error":
            case "info":
            case "message":
            case "status-producer":
            case "status-consumer":
            case "anchor":
            case "switchcamera":
            case "productor":
            case "activeSpeaker":
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
            LogUtils.e("MediaService notify : " + previewEvent.getType() + "   text : " + previewEvent.getmNotifyContent1());
        }
        if ("error".equals(previewEvent.getType())) {
            LogUtils.e(previewEvent.getmNotifyContent1());
        } else if ("info".equals(previewEvent.getType())) {

        } else if ("message".equals(previewEvent.getType())) {
            setStatus(mediaType == MEDIA_TYPE_VIDEO ? 4 : 3);
            Log.d(TAG, "initViewModel: 11 " + mediaType);
            if (mediaType != MEDIA_TYPE_VIDEO) {
                return;
            }

            Log.d(TAG, "initViewModel: 12 " + mediaType);
            // 若有连麦客户存在，则需要开启列表，保证主播的VideoTrack放在me上
            // 其他放在远端,传输VideoTrack peers
            VideoTrack videoTrack = mediaService.findVideoTrack(previewEvent.getmNotifyContent1());
            if (videoTrack != null) {
                remoteVideoTrack = videoTrack;
                showSmallSurfaceRenderer(videoTrack);
                videoTrackList.add(videoTrack);
                transVideo();
            }
        } else if ("status-producer".equals(previewEvent.getType())) {
            // 状态 connected completed closed disconnected failed
            if ("completed".equals(previewEvent.getmNotifyContent1())) {
                // 隐藏蒙层
                switch_small_window.setVisibility(View.VISIBLE);
            }
        } else if ("status-consumer".equals(previewEvent.getType())) {
            // 状态 checked connected completed closed disconnected failed
            Log.d(TAG, "initViewModel: " + previewEvent.getmNotifyContent1());
            if ("completed".equals(previewEvent.getmNotifyContent1())) {

            } else if ("close".equals(previewEvent.getmNotifyContent1())) {
                // 显示正在来的路上
                remoteVideoTrack = null;
            }
        } else if ("anchor".equals(previewEvent.getType())) {
            if (mediaType == MEDIA_TYPE_VIDEO) {
                if (mediaService != null)
                    mediaService.enableMic();
            }

            if (!mRoomMediaBean.isFrom()) {
                if (mediaType == MEDIA_TYPE_MUTE) {
                    ImClient.getInstance().sendMsg(YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_START, YLConstant.ContentType.CONTENT_TYPE_CUSTOM, "语音通话开始");
                } else {
                    ImClient.getInstance().sendMsg(YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START, YLConstant.ContentType.CONTENT_TYPE_CUSTOM, "视频通话开始");
                }
            }
        } else if ("productor".equals(previewEvent.getType())){
            videoTrack = ((VideoTrack) previewEvent.getVideoTrack());
            LogUtils.d("initViewModel producers : " + videoTrack);
            if (videoTrack != null && !bProducerHasRender) {
                bProducerHasRender = true;
                videoTrack.addSink(video_renderer);
                LogUtils.e("initViewModel  ---------------");
                video_renderer.setTag(videoTrack);
            }
        }
    }
}
