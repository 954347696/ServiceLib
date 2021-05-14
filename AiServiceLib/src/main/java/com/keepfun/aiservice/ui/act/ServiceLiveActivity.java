package com.keepfun.aiservice.ui.act;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.emoji.Actions.EmojIconActions;
import com.keepfun.aiservice.emoji.Helper.EmojiconEditText;
import com.keepfun.aiservice.entity.AppLiveInfo;
import com.keepfun.aiservice.entity.ImUserInfo;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.ReceiveAmount;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.entity.WelcomeInfo;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.adapter.ServiceChatAdapter2;
import com.keepfun.aiservice.ui.dialog.AdmireDialog;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.aiservice.utils.audio.MediaManager;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.ColorUtils;
import com.keepfun.blankj.util.KeyboardUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.TimeUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.media.MediaSdk;
import com.keepfun.media.lib.PeerConnectionUtils;
import com.keepfun.media.lib.RoomClient;
import com.keepfun.media.lib.RoomOptions;
import com.keepfun.media.lib.lv.RoomStore;
import com.keepfun.media.lib.model.Consumers;
import com.keepfun.media.lib.model.Notify;
import com.keepfun.media.lib.model.Producers;

import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/9 2:15 PM
 */
public class ServiceLiveActivity extends PanActivity implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_live;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    //    private NestedScrollView scrollView;
    private View root;
    private RecyclerView rv_chat;
    private ImageView iv_emoji;
    private TextView tv_send_standby;
    private EmojiconEditText et_input;
    private EmojIconActions emojIcon;

    private ServiceChatAdapter2 mAdapter;
    private List<Message> dataList;
    private ImClient mImClient;
    private ImUserInfo mImUserInfo;

    private AdmireDialog mAdmireDialog;
    private AppLiveInfo mAppLiveInfo;

    private String mGroupId = "-1";

    private RoomOptions mOptions;
    private RoomStore mRoomStore;
    private RoomClient mRoomClient;

    SurfaceViewRenderer surfaceViewRenderer;

    ImageView bgLiveImageView;

    LinearLayout endLinearLayout;
    LinearLayout pausedLinearLayout;

    TextView endTextView;
    TextView pausedTextView;
    TextView liveTitleTextView;

    private Handler handler = new Handler();

    private long pauseTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void bindUI(View rootView) {
//        scrollView = findViewById(R.id.scrollView);
        root = findViewById(R.id.rootView);
        rv_chat = findViewById(R.id.rv_chat);
        et_input = findViewById(R.id.et_input);
        tv_send_standby = findViewById(R.id.tv_send_standby);
        iv_emoji = findViewById(R.id.iv_emoji);

        bgLiveImageView = findViewById(R.id.live_bg);

        endLinearLayout = findViewById(R.id.end_layout);

        pausedLinearLayout = findViewById(R.id.paused_layout);

        endTextView = findViewById(R.id.end_text);

        pausedTextView = findViewById(R.id.paused_text);
        liveTitleTextView = findViewById(R.id.live_title);

        surfaceViewRenderer = findViewById(R.id.video_renderer);
        surfaceViewRenderer.init(PeerConnectionUtils.getEglContext(), null);
    }

    @Override
    public void initData() {
        KeyboardUtils.fixAndroidBug5497(this);
        long id = -1;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getLong(Arguments.DATA);
        }
        mImClient = ImClient.getInstance();
        mImUserInfo = GlobalDataHelper.getInstance().getImUserInfo();

        initEmoji();
        initChatRecyclerView();

        getAnchorInfo(id);
    }

    private void getAnchorInfo(long id) {
        OkHttpUtils.get(ApiDomain.GET_LIVE_INFO + id, new GsonResponseHandler<AppLiveInfo>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(AppLiveInfo response) {
                mAppLiveInfo = response;
                if (mAppLiveInfo != null) {
                    getLiveInfoSuccess();
                }
            }
        });
    }

    private void getLiveInfoSuccess() {
        mGroupId = mAppLiveInfo.getImId();
        liveTitleTextView.setText(mAppLiveInfo.getTitle());
        createRoom();
        String welcome = SpManager.getConfig().getString(SpManager.KEY_WELCOME_CONTENT);
        if (!StringUtils.isEmpty(welcome)) {
            Message message1 = new Message();
            message1.setContent(Html.fromHtml(welcome).toString());
            message1.setType(YLConstant.MessageType.MESSAGE_TYPE_SYSTEM);
            message1.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
            message1.setGroupId(mGroupId);
            receiveMessage(message1);
        }
        reportJoin();
        startHeartbeat();

        if (mAppLiveInfo.getLiveStatus() == 25) {
            bgLiveImageView.setBackgroundResource(R.mipmap.pause_live_bg);
            bgLiveImageView.setVisibility(View.VISIBLE);
            pausedLinearLayout.setVisibility(View.VISIBLE);
            liveTitleTextView.setVisibility(View.GONE);

            pauseTime = ((System.currentTimeMillis() - mAppLiveInfo.getPauseTime()) / 1000);
            handler.removeCallbacks(pauseTimeRunnable);
            handler.postDelayed(pauseTimeRunnable, 1000);
        } else if (30 == mAppLiveInfo.getLiveStatus()) {
            // 显示正在来的路上
            bgLiveImageView.setBackgroundResource(R.drawable.bg_end_live);
            bgLiveImageView.setVisibility(View.VISIBLE);
            endLinearLayout.setVisibility(View.VISIBLE);
            liveTitleTextView.setVisibility(View.GONE);
            setEndText(mAppLiveInfo.getFinishTime());
        }
        setRoomStoreObserver();
    }

    public void startHeartbeat() {
        handler.post(heartbeatRunnable);
    }


    private void initEmoji() {
        emojIcon = new EmojIconActions(this, root, et_input, iv_emoji);
        emojIcon.setIconsIds(R.mipmap.service_ic_online_keyboard, R.mipmap.service_ic_online_smile);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                LogUtils.e("Keyboard open");
                tv_send_standby.setVisibility(View.VISIBLE);
            }

            @Override
            public void onKeyboardClose() {
                LogUtils.e("Keyboard close");
                tv_send_standby.setVisibility(View.GONE);
            }

            @Override
            public void toggleKeyboard() {
                et_input.findFocus();
                KeyboardUtils.showSoftInput(et_input);
            }
        });
    }

    private void initChatRecyclerView() {
        rv_chat.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(getContext())
                .colorResId(R.color.transport)
                .size(SizeUtils.dp2px(10f))
                .showLastDivider()
                .build();
        rv_chat.addItemDecoration(decor);
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        mAdapter = new ServiceChatAdapter2(this, dataList);
        if (GlobalDataHelper.getInstance().getImUserInfo() == null) {
            return;
        }
        mAdapter.getUpFetchModule().setUpFetchEnable(false);
        mAdapter.setUserId(GlobalDataHelper.getInstance().getImUserInfo().getId());
        rv_chat.setAdapter(mAdapter);


    }


    @Override
    public void bindEvent() {
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String message = et_input.getText().toString().trim();
                if (!StringUtils.isEmpty(message)) {
                    sendMessage(message, YLConstant.MessageType.MESSAGE_TYPE_MONITOR, YLConstant.ContentType.CONTENT_TYPE_TEXT, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String message = et_input.getText().toString().trim();
                if (!StringUtils.isEmpty(message)) {
                    sendMessage(message, YLConstant.ContentType.CONTENT_TYPE_TEXT);
                    et_input.setText("");
                }
            }
            return false;
        });
        et_input.setOnClickListener(v -> {
            if (!KeyboardUtils.isSoftInputVisible(ServiceLiveActivity.this)) {
                KeyboardUtils.showSoftInput(v);
            }
        });
        tv_send_standby.setOnClickListener(new CheckClickListener(v -> {
            String message = et_input.getText().toString().trim();
            if (!StringUtils.isEmpty(message)) {
                sendMessage(message, YLConstant.ContentType.CONTENT_TYPE_TEXT);
                et_input.setText("");
                et_input.clearFocus();
                KeyboardUtils.hideSoftInput(this);
            }
        }));

        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            scrollToBottom();
        });

        findViewById(R.id.iv_back).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_emoji).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_admire).setOnClickListener(new CheckClickListener(this));

    }

    private void setRoomStoreObserver() {
        mRoomStore.getProducers().observe(this, producers -> {
            if (producers != null) {
                Producers.ProducersWrapper mVideoPW = producers.filter("video");
                if (mVideoPW != null)
                    Log.d(TAG, "initViewModel: " + ((VideoTrack) mVideoPW.getProducer().getTrack() != null));
            }
        });

        // Notify
        final Observer<Notify> notifyObserver =
                notify -> {
                    if (notify == null) {
                        return;
                    }
                    if ("error".equals(notify.getType())) {
                        ToastUtils.showShort(notify.getText());
                    } else if ("info".equals(notify.getType())) {
                        //ToastUtils.showSmallToast(notify.getText());
                    } else if ("message".equals(notify.getType())) {
                        LogUtils.d(TAG, "initViewModel: message ");
                        VideoTrack videoTrack = findVideoTrack(notify.getText());
                        Log.d(TAG, "initViewModel: videoTrack " + notify.getText());
                        if (videoTrack != null) {
                            videoTrack.addSink(surfaceViewRenderer);
                        }
                    } else if ("status-producer".equals(notify.getType())) {
                        // 状态 connected completed closed disconnected failed
                        if ("completed".equals(notify.getText())) {
                            // 隐藏蒙层
                        }
                    } else if ("status-consumer".equals(notify.getType())) {
                        // 状态 checked connected completed closed disconnected failed
                        LogUtils.d(TAG, "initViewModel: " + notify.getText());
                        if ("completed".equals(notify.getText())) {
                            // 隐藏蒙层,流展示
                            if (mAppLiveInfo.getLiveStatus() != 25 && 30 != mAppLiveInfo.getLiveStatus()) {
                                bgLiveImageView.setVisibility(View.GONE);
                                pausedLinearLayout.setVisibility(View.GONE);
                                endLinearLayout.setVisibility(View.GONE);
                            }
                        } else if ("close".equals(notify.getText())
//                                ||"close_viewer".equals(notify.getText())
                        ) {
                            mAppLiveInfo.setLiveStatus(30);
                            // 显示正在来的路上
                            bgLiveImageView.setBackgroundResource(R.drawable.bg_end_live);
                            bgLiveImageView.setVisibility(View.VISIBLE);
                            endLinearLayout.setVisibility(View.VISIBLE);
                            pausedLinearLayout.setVisibility(View.GONE);
                            liveTitleTextView.setVisibility(View.GONE);
                            setEndText(System.currentTimeMillis() + 15 * 60 * 1000);
                        } else if ("paused".equals(notify.getText())) {
                            mAppLiveInfo.setLiveStatus(25);
                            bgLiveImageView.setBackgroundResource(R.mipmap.pause_live_bg);
                            bgLiveImageView.setVisibility(View.VISIBLE);
                            endLinearLayout.setVisibility(View.GONE);
                            pausedLinearLayout.setVisibility(View.VISIBLE);
                            liveTitleTextView.setVisibility(View.GONE);
                            handler.removeCallbacks(pauseTimeRunnable);
                            handler.postDelayed(pauseTimeRunnable, 1000);
                        } else if ("resumed".equals(notify.getText())) {
                            mAppLiveInfo.setLiveStatus(20);
                            bgLiveImageView.setVisibility(View.GONE);
                            pausedLinearLayout.setVisibility(View.GONE);
                            liveTitleTextView.setVisibility(View.VISIBLE);

                            handler.removeCallbacks(pauseTimeRunnable);
                            pauseTime = 0;
                            pausedTextView.setText("主播暂时离开了，暂停时长 " + TimeUtils.millis2FitTimeSpan(pauseTime * 1000));
                        }
                    } else if ("anchor".equals(notify.getType())) {
                        // 说明有主播正在直播
                        if (!notify.getText().isEmpty()) {
                            // 可以展示蒙层
                            Log.d(TAG, "initViewModel: 123");
                        } else {
                            // 隐藏蒙层
                            Log.d(TAG, "initViewModel: 1234");
                        }
                    } else if ("switchcamera".equals(notify.getType())) {

                        LogUtils.d(TAG, "initViewModel: 12345");
                    }
                };
        mRoomStore.getNotify().observe(this, notifyObserver);
    }

    private void setEndText(long time) {
        endTextView.setText("房间将在 " + TimeUtils.millis2String(time, "HH:mm:ss") + " 解散，用户将被移出");

        handler.postDelayed(leaveRunnable, time - System.currentTimeMillis());
    }

    /**
     * 滚动到底部（不带动画）
     */
    private void scrollToBottom() {
        LinearLayoutManager ll = (LinearLayoutManager) rv_chat.getLayoutManager();
        ll.scrollToPositionWithOffset(getBottomDataPosition(), 0);
    }

    private int getBottomDataPosition() {
        return mAdapter.getHeaderLayoutCount() + mAdapter.getData().size() - 1;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.iv_emoji) {
            tv_send_standby.setVisibility(View.VISIBLE);
            showEmoji(true);
        } else if (id == R.id.iv_admire) {
            showAdmireDialog();
        }
    }

    public void sendMessage(String result, int contentType, @Nullable long duration) {
        sendMessage(result, YLConstant.MessageType.MESSAGE_TYPE_CHAT, contentType, duration);
    }

    public void sendMessage(String result, int messageType, int contentType, @Nullable long duration) {
        Message message = createMessage();
        message.setContent(result);
        message.setContentType(contentType);
        message.setType(messageType);
        message.setTimeDuration(duration);
        message.setCreateTime(System.currentTimeMillis());
        message.setGroupId(mGroupId);
//        EventBus.getDefault().post(message);
        mImClient.sendMsg(message);

    }

    public void sendMessage(String result, int contentType) {
        sendMessage(result, contentType, 0);
    }

    private Message createMessage() {
        Message message = new Message();
        message.setAppKey(mImUserInfo.getAppKey());
        message.setFromUserName(mImUserInfo.getNickName());
        message.setFromUserAvatar(mImUserInfo.getHeadPortrait());
        message.setFromUserType(mImUserInfo.getType());
        message.setFromUserId(mImUserInfo.getId());
        message.setMyUserId(mImUserInfo.getId());
        message.setLiveType(2);
        message.setLiveId(mAppLiveInfo.getId());
        return message;
    }


    private void showEmoji(boolean isShow) {
        if (emojIcon != null) {
            emojIcon.togglePopupVisibility();
        }
    }

    private void showAdmireDialog() {
        if (mAdmireDialog == null) {
            mAdmireDialog = new AdmireDialog();
        }
        Bundle bundle = new Bundle();
        bundle.putString(Arguments.GROUP_ID, String.valueOf(mAppLiveInfo.getId()));
        bundle.putString(Arguments.SESSION_ID, mGroupId);
        bundle.putInt(Arguments.TYPE, 3);
        bundle.putString(Arguments.DATA, mAppLiveInfo.getAnchorImg());
        bundle.putString(Arguments.DATA1, mAppLiveInfo.getAnchorName());
        bundle.putString(Arguments.ID, String.valueOf(mAppLiveInfo.getAnchorId()));
        mAdmireDialog.setArguments(bundle);

        mAdmireDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (isShouldHideKeyboard(v, ev) && isShouldHideMore(iv_emoji, ev) && isShouldHideMore(tv_send_standby, ev)) {
                KeyboardUtils.hideSoftInput(this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationOnScreen(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom);
        }
        return false;
    }  // Return whether touch the view.

    private boolean isShouldHideMore(View v, MotionEvent event) {
        int[] l = {0, 0};
        v.getLocationOnScreen(l);
        int left = l[0],
                top = l[1],
                bottom = top + v.getHeight(),
                right = left + v.getWidth();
        return !(event.getRawX() > left && event.getRawX() < right
                && event.getRawY() > top && event.getRawY() < bottom);

    }

    // 直播间相关
    private void createRoom() {
        mOptions = new RoomOptions();
        mRoomStore = new RoomStore();
        loadRoomConfig();

        if (mRoomClient != null) {
            mRoomClient.join();
        }
    }

    private void destroyRoom() {
        if (mRoomClient != null) {
            mRoomClient.close();
            mRoomClient = null;
        }

        if (surfaceViewRenderer != null) {
            surfaceViewRenderer.release();
        }

        if (mRoomStore != null) {
            mRoomStore = null;
        }
    }

    private void loadRoomConfig() {
        // Room initial config.
        String mRoomId = String.valueOf(mAppLiveInfo.getId());
        String mPeerId = GlobalDataHelper.getInstance().getUserInfo().getUserUid();
        String mDisplayName = GlobalDataHelper.getInstance().getUserInfo().getNickname();
        boolean mForceH264 = false;
        boolean mForceVP9 = false;

        mOptions.setmAnchorId(String.valueOf(mAppLiveInfo.getAnchorId()));
        //mOptions.setMediasoupUrl("101.133.230.207"); // 192.168.1.208 101.133.230.207
        mOptions.setMediasoupUrl(YLConstant.MEDIA_HOST);
        // Room action config.
        mOptions.setProduce(false);
        mOptions.setConsume(true);
        mOptions.setForceTcp(false);

        mRoomClient = new RoomClient(this, mRoomStore, mRoomId, mPeerId, mDisplayName,
                mForceH264, mForceVP9, mOptions);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MediaManager.isPlaying()) {
            MediaManager.release();
        }
        destroyRoom();
        handler.removeCallbacks(heartbeatRunnable);
        handler.removeCallbacks(pauseTimeRunnable);
        handler.removeCallbacks(leaveRunnable);
        reportExit();
    }

    @Subscribe
    public void receiveMessage(Message message) {
        if (mGroupId == null || !mGroupId.equals(message.getGroupId())) {
            return;
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_USER) {
            if ("1".equals(message.getContent())) {
                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                ToastUtils.setBgColor(Color.parseColor("#FFE92F"));
                ToastUtils.showShort("欢迎" + message.getFromUserName() + " 进入直播间");
            }
        }
        if (!message.isShow()) {
            return;
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_REWORD) {
            ReceiveAmount receiveAmount = JsonUtil.decode(message.getContent(), ReceiveAmount.class);
            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
            ToastUtils.setBgColor(Color.parseColor("#FFE92F"));
            ToastUtils.showShort(message.getFromUserName() + "打赏了" + receiveAmount.getAmount() + receiveAmount.getCurrency());
        }

        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_END) {
            mImClient.sendMsg(message);
            if (mAppLiveInfo.getLiveStatus() != 30) {
                mAppLiveInfo.setLiveStatus(30);
                // 显示正在来的路上
                bgLiveImageView.setBackgroundResource(R.drawable.bg_end_live);
                bgLiveImageView.setVisibility(View.VISIBLE);
                endLinearLayout.setVisibility(View.VISIBLE);
                pausedLinearLayout.setVisibility(View.GONE);
                liveTitleTextView.setVisibility(View.GONE);
                setEndText(System.currentTimeMillis() + 15 * 60 * 1000);
            }
        }
        dataList.add(message);
        if (mAdapter != null) {
            mAdapter.notifyItemInserted(dataList.size() - 1);
            rv_chat.scrollToPosition(mAdapter.getData().size() - 1);
        }
    }

    public void reportJoin() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("imToken", GlobalDataHelper.getInstance().getImToken());
        params.put("liveId", mAppLiveInfo.getId());
        OkHttpUtils.postJson(ApiDomain.LIVE_WATCH_ADD,params, new GsonResponseHandler<WelcomeInfo>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(WelcomeInfo result) {
                if (result != null && !StringUtils.isEmpty(result.getWelcomeInfo())) {
                    Message message1 = new Message();
                    message1.setContent(Html.fromHtml(result.getWelcomeInfo()).toString().trim());
                    message1.setType(YLConstant.MessageType.MESSAGE_TYPE_SERVICE_IN);
                    message1.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
                    message1.setGroupId(mGroupId);
                    receiveMessage(message1);
                }
            }
        });
    }

    public void reportExit() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("imToken", GlobalDataHelper.getInstance().getImToken());
        params.put("liveId", mAppLiveInfo.getId());
        OkHttpUtils.postJson(ApiDomain.LIVE_WATCH_EXIT,params, new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(Boolean response) {

            }
        });
    }

    public void heartbeat() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("imToken", GlobalDataHelper.getInstance().getImToken());
        params.put("liveId", mAppLiveInfo.getId());
        OkHttpUtils.postJson(this, ApiDomain.LIVE_WATCH_HEARTBEAT, params, new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                handler.postDelayed(heartbeatRunnable,60*1000);
            }

            @Override
            public void onSuccess(Boolean response) {
                handler.postDelayed(heartbeatRunnable,60*1000);
            }
        });
    }

    Runnable heartbeatRunnable = () -> heartbeat();

    Runnable pauseTimeRunnable = new Runnable() {
        @Override
        public void run() {
            pauseTime++;
            pausedTextView.setText("主播暂时离开了，暂停时长 " + TimeUtils.millis2FitTimeSpan(pauseTime * 1000));
            handler.removeCallbacks(pauseTimeRunnable);
            handler.postDelayed(pauseTimeRunnable, 1000);
        }
    };

    Runnable leaveRunnable = () -> finish();
}
