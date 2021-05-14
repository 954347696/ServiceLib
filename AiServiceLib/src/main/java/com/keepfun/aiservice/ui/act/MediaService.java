package com.keepfun.aiservice.ui.act;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.RoomMediaBean;
import com.keepfun.aiservice.entity.event.PreviewEvent;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.media.lib.PeerConnectionUtils;
import com.keepfun.media.lib.RoomClient;
import com.keepfun.media.lib.RoomOptions;
import com.keepfun.media.lib.lv.RoomStore;
import com.keepfun.media.lib.model.Consumers;
import com.keepfun.media.lib.model.Notify;
import com.keepfun.media.lib.model.Producers;

import org.greenrobot.eventbus.EventBus;
import org.webrtc.VideoTrack;


/**
 * Description:
 * Author:star
 * Email:guimingxing@163.com
 * Date:2020/11/6 9:11
 **/
public class MediaService extends Service {
    private String mRoomId, mPeerId, mDisplayName;
    private boolean mForceH264, mForceVP9;
    private RoomOptions mOptions;
    private RoomStore mRoomStore;
    private RoomClient mRoomClient;
    private RoomMediaBean mRoomMediaBean;

    private int mediaType;

    private String cameraName = "1";
    public static String KEY_MDDEL = "model";
    public static String KEY_DATA = "data";

    protected ServiceMediaActivity serviceMediaActivity;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MediaService", "onBind " + intent);
        if (intent != null){
            serviceMediaActivity = ServiceMediaActivity.getServiceMediaActivity();
            mRoomMediaBean = (RoomMediaBean) intent.getSerializableExtra(KEY_MDDEL);
            mediaType = intent.getIntExtra(KEY_DATA, 2);
            starJoinRoom(mediaType);
        }

        return new MediaBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ch", "onStartCommand: MediaService " + intent);

        if (intent != null){
            serviceMediaActivity = ServiceMediaActivity.getServiceMediaActivity();
            mRoomMediaBean = (RoomMediaBean) intent.getSerializableExtra(KEY_MDDEL);
            mediaType = intent.getIntExtra(KEY_DATA, 2);
            starJoinRoom(mediaType);
        }

        return super.onStartCommand(intent, START_STICKY, startId);
    }



    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MediaService", "onUnbind");
        destroyRoom();
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyRoom();
        Log.d("MediaService", "onDestroy");
    }

    /**
     * 开始录制
     */
    public void starJoinRoom(int mediaType) {
        this.mediaType = mediaType;
        createRoom(mediaType);
    }

    private void createRoom(int mediaType) {
        mOptions = new RoomOptions();
        loadRoomConfig(mediaType);
        initViewModel();
    }

    private void loadRoomConfig(int mediaType) {
        mRoomStore = new RoomStore();
        mRoomId = String.valueOf(mRoomMediaBean.getRoomId());
        mDisplayName = mRoomMediaBean.getOtherName();
        mForceH264 = SpManager.getConfig().getBoolean("forceH264", true);
        mForceVP9 = SpManager.getConfig().getBoolean("forceVP9", false);

        mPeerId = String.valueOf(mRoomMediaBean.getSelfId());
        mOptions.setmAnchorId(String.valueOf(mRoomMediaBean.getOtherId()));
        mOptions.setProduce(SpManager.getConfig().getBoolean("produce", true));
        mOptions.setConsume(SpManager.getConfig().getBoolean("consume", true));

        mOptions.setbProductVideo(mediaType == ServiceMediaActivity.MEDIA_TYPE_VIDEO);
        mOptions.setbProductAudio(true);
        mOptions.setForceTcp(SpManager.getConfig().getBoolean("forceTcp", true));

        String camera = "front";
        PeerConnectionUtils.setPreferCameraFace(camera);
        mRoomClient = new RoomClient(this.getApplicationContext(), mRoomStore, mRoomId,
                mPeerId, mDisplayName, mForceH264, mForceVP9, mOptions);
        mRoomClient.setBeautyEnable(false);
        mRoomClient.setTraceFaceEnable(false);

        mRoomClient.setmMainUIHandler(msgHandler);
        if (mRoomClient != null){
            mRoomClient.join();
        }
    }

    /**
     * 停止录制
     */
    public void destroyRoom() {
        if (mRoomClient != null) {
            mRoomClient.close();
            mRoomClient = null;
        }
        if (mRoomStore != null) {
            mRoomStore = null;
        }
    }

    private void initViewModel() {
        mRoomStore.getProducers().observe(serviceMediaActivity, producers -> {
            LogUtils.d("MediaService initViewModel producers : "  + mediaType + " " + producers);
            if (mediaType == ServiceMediaActivity.MEDIA_TYPE_VIDEO) {
                if (producers != null) {
                    Producers.ProducersWrapper mVideoPW = producers.filter("video");
                    if (mVideoPW != null) {
                        VideoTrack videoTrack = ((VideoTrack) mVideoPW.getProducer().getTrack());
                        if (videoTrack != null) {
                            EventBus.getDefault().post(new PreviewEvent("productor", "", "", videoTrack));
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

                    if ("activeSpeaker".equals(notify.getType())) {
                        LogUtils.e("MediaService : " + notify.getType() + "   text : " + notify.getText());
                        EventBus.getDefault().post(new PreviewEvent(notify.getType(), notify.getText(), notify.getmText2(), null));
                    }

                    if ("error".equals(notify.getType())) {
                        EventBus.getDefault().post(new PreviewEvent(notify.getType(), notify.getText(), notify.getmText2(), null));
                    } else if ("info".equals(notify.getType())) {

                    } else if ("message".equals(notify.getType())) {

                    } else if ("status-producer".equals(notify.getType())) {
                        // 状态 connected completed closed disconnected failed

                    } else if ("status-consumer".equals(notify.getType())) {
                        // 状态 checked connected completed closed disconnected failed
                    } else if ("anchor".equals(notify.getType())) {
                    }
                };
        mRoomStore.getNotify().observe(serviceMediaActivity, notifyObserver);
    }

    public VideoTrack findVideoTrack(String consumerId) {
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

    public void disableCam(){
        if (mRoomClient != null){
            mRoomClient.disableCam();
        }
    }

    public void enableCam(){
        if (mRoomClient != null){
            mRoomClient.enableCam();
        }
    }

    public void enableMic(){
        if (mRoomClient != null){
            mRoomClient.enableMic();
        }
    }

    public void muteMic(){
        if (mRoomClient != null){
            mRoomClient.muteMic();
        }
    }

    public void unmuteMic(){
        if (mRoomClient != null){
            mRoomClient.unmuteMic();
        }
    }

    Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d("MediaService", "handleMessage: 11111111111111 " + msg.what + " " + (String)msg.obj);
            PreviewEvent previewEvent = null;
            if (msg.what == RoomOptions.MSG_JOIN_ROOM){
                previewEvent = new PreviewEvent("info", (String) msg.obj, "", null);
            }else if (msg.what == RoomOptions.MSG_PRODUCE_STATUS){
                previewEvent = new PreviewEvent("status-producer", (String) msg.obj, "", null);
            }else if (msg.what == RoomOptions.MSG_CONSUMER_STATUS){
                previewEvent = new PreviewEvent("status-consumer", (String) msg.obj, "", null);
            }else if (msg.what == RoomOptions.MSG_CONSUMER_JOIN){
                previewEvent = new PreviewEvent("message", (String) msg.obj, "", null);
            }else if (msg.what == RoomOptions.MSG_IS_ANCHOR){
                previewEvent = new PreviewEvent("anchor", (String) msg.obj, "", null);
            }else if (msg.what == RoomOptions.MSG_SWITCH_CAMERA){
                previewEvent = new PreviewEvent("switchcamera", (String) msg.obj, "", null);
            }

            EventBus.getDefault().post(previewEvent);

            super.handleMessage(msg);
        }
    };

    class MediaBinder extends Binder {
        public MediaService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MediaService.this;
        }

    }
}
