package com.keepfun.aiservice.ui.act;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import com.keepfun.aiservice.entity.ImUserInfo;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.entity.RoomMediaBean;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.entity.event.ImReConnectEvent;
import com.keepfun.aiservice.entity.event.SessionEndEvent;
import com.keepfun.aiservice.entity.event.SetUserTopEvent;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.threads.SessionCounterService;
import com.keepfun.aiservice.ui.adapter.ServiceChatAdapter2;
import com.keepfun.aiservice.ui.dialog.AdmireDialog;
import com.keepfun.aiservice.ui.dialog.EndSessionDialog;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.OnDutyPresenter;
import com.keepfun.aiservice.ui.view.ServiceAudioLayout;
import com.keepfun.aiservice.ui.view.ServiceTitleView;
import com.keepfun.aiservice.utils.GlideEngine;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.aiservice.utils.audio.MediaManager;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.ClickUtils;
import com.keepfun.blankj.util.CollectionUtils;
import com.keepfun.blankj.util.ColorUtils;
import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.blankj.util.KeyboardUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ScreenUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.SpanUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.blankj.util.Utils;
import com.keepfun.easyphotos.EasyPhotos;
import com.keepfun.easyphotos.models.album.entity.Photo;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * @author yang
 * @description
 * @date 2020/12/16 3:19 PM
 */
public class OnDutyActivity extends PanActivity<OnDutyPresenter> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_chat_on_duty;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    @Override
    public OnDutyPresenter newP() {
        return new OnDutyPresenter();
    }

    private ServiceTitleView title;
    private View mRootView;
    private RecyclerView rv_chat;
    private TextView tv_tip_close;
    private EmojiconEditText et_input;
    private ServiceAudioLayout tv_voice;
    private ImageView iv_voice;
    private ImageView iv_keyboard;
    private ImageView iv_emoji;
    private TextView tv_send_standby;
    private LinearLayout layout_more;
    private LinearLayout layout_video;
    private LinearLayout layout_mute;
    private LinearLayout layout_admire;

    private PopupWindow menuPop;
    private EmojIconActions emojIcon;
    private ImUserInfo mImUserInfo;
    private ImClient mImClient;
    private AdmireDialog mAdmireDialog;
    private Integer waitNo;

    private List<Message> dataList;
    private ServiceChatAdapter2 mAdapter;
    private boolean isLoadingMore = false;
    private int pageIndex = 0;
    private ServiceEntity mCurrentService;
    private ServiceEntity mTransService;

    private String mGroupId;
    private boolean mIsInSession = false;
    private EndSessionDialog mEndSessionDialog;

    private final int REQUEST_SELECTED_IMAGE = 0x12;
    public static final int REQUEST_FOR_EVALUATION = 0x13;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void bindUI(View rootView) {
        title = findViewById(R.id.title);
        mRootView = findViewById(R.id.rootView);
        rv_chat = findViewById(R.id.rvChat);
        tv_tip_close = findViewById(R.id.tv_tip_close);
        et_input = findViewById(R.id.et_input);
        tv_voice = findViewById(R.id.tv_voice);
        iv_voice = findViewById(R.id.iv_voice);
        iv_keyboard = findViewById(R.id.iv_keyboard);
        iv_emoji = findViewById(R.id.iv_emoji);
        tv_send_standby = findViewById(R.id.tv_send_standby);
        layout_more = findViewById(R.id.layout_more);
        layout_video = findViewById(R.id.layout_video);
        layout_mute = findViewById(R.id.layout_mute);
        layout_admire = findViewById(R.id.layout_admire);
    }

    @Override
    public void initData() {
        KeyboardUtils.fixAndroidBug5497(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCurrentService = (ServiceEntity) bundle.getSerializable(Arguments.DATA1);
            setGroupId(mCurrentService.getGroupId());
            title.setTitle(mCurrentService.getName());

            if (mCurrentService.getAccountType() == 1) {
                title.setRightGone();
                layout_admire.setVisibility(View.GONE);
            }
        }

        mImClient = ImClient.getInstance();
        mImUserInfo = GlobalDataHelper.getInstance().getImUserInfo();
        switchInputType(false);
        initEmoji();
        initChatRecyclerView();

        rv_chat.post(new Runnable() {
            @Override
            public void run() {
                if (!StringUtils.isEmpty(mGroupId) && !"-1".equals(mGroupId)) {
                    createSessionSuccess(mCurrentService, null, ServiceMediaActivity.MEDIA_TYPE_NULL, true);
                } else {
                    getP().createSession(mCurrentService, null, ServiceMediaActivity.MEDIA_TYPE_NULL);
                }

                requestChatHistory(false);
            }
        });
    }

    @Override
    public void bindEvent() {
        title.setRightListener(v -> showPop(v));
        tv_voice.setOnAudioSendListener((filePath, duration) -> {
            //
            LogUtils.e("onAudioSend filePath : " + filePath);
            getP().uploadFile(filePath, duration);
        });
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
                if (mImClient != null && !StringUtils.isEmpty(message)) {
                    sendMessage(message, YLConstant.ContentType.CONTENT_TYPE_TEXT);
                    et_input.setText("");
                }
            }
            return false;
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

        tv_tip_close.setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_voice).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_keyboard).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_emoji).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_more).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_mute).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_video).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_pic).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_admire).setOnClickListener(new CheckClickListener(this));
    }

    private void initChatRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(false);
        rv_chat.setLayoutManager(linearLayoutManager);
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
        mAdapter.setUserId(GlobalDataHelper.getInstance().getImUserInfo().getId());
        rv_chat.setAdapter(mAdapter);
        mAdapter.getUpFetchModule().setUpFetchEnable(true);
        mAdapter.getUpFetchModule().setOnUpFetchListener(() -> {
            if (isLoadingMore) {
                return;
            }
            requestChatHistory(false);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAdapter != null) {
            scrollToBottom();
        }
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


    private void requestChatHistory(boolean isPullDown) {
        if (isPullDown) {
            pageIndex = 1;
        } else {
            pageIndex++;
        }
        mAdapter.getUpFetchModule().setUpFetching(true);
        getP().getHistory(mCurrentService.getId(), pageIndex, isPullDown);
    }

    public void getDataSuccess(PageBean<Message> pageBean, boolean isPullDown) {
        stopRefreshLoad();
        if (isPullDown) {
            dataList.clear();
        }
        if (pageBean == null || pageBean.getDatas().isEmpty()) {
            pageIndex--;
            mAdapter.getUpFetchModule().setUpFetchEnable(false);
        } else {
            rv_chat.postDelayed(() -> {
//                Collections.sort(pageBean.getDatas(), (o1, o2) -> (int) (o2.getCreateTime() - o1.getCreateTime()));
                List<Message> list = new ArrayList<>();
                for (Message message : pageBean.getDatas()) {
                    if (message.isInHistory()) {
                        list.add(0, message);
                    }
                }
                mAdapter.addData(0, list);
                if (isPullDown) {
                    scrollToBottom();
                }
            }, 500);
        }
    }

    public void getDataFailed(String message) {
        pageIndex--;
        stopRefreshLoad();
        mAdapter.notifyDataSetChanged();
    }

    private void stopRefreshLoad() {
        if (isLoadingMore) {
            isLoadingMore = false;
        }
        mAdapter.getUpFetchModule().setUpFetching(false);
    }


    private void showPop(View v) {
//        if (menuPop == null) {
//            View popView = LayoutInflater.from(getContext()).inflate(R.layout.service_menu_chat_detail, null);
//            menuPop = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            popView.findViewById(R.id.tv_detail).setOnClickListener(v12 -> {
        if (ClickUtils.isFastDoubleClick()) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(Arguments.DATA, mCurrentService);
        ActivityUtils.startActivity(bundle, OnDutyDetailActivity.class);
//                menuPop.dismiss();
//            });
//
//        }
//        //设置popwindow显示位置
//        menuPop.showAsDropDown(v);
//        //获取popwindow焦点
//        menuPop.setFocusable(true);
//        //设置popwindow如果点击外面区域，便关闭。
//        menuPop.setOutsideTouchable(true);
//        final WindowManager.LayoutParams lp = getWindow().getAttributes();
//        //代表透明程度，范围为0 - 1.0f
//        lp.alpha = 0.7f;
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        getWindow().setAttributes(lp);
//        menuPop.setOnDismissListener(() -> {
//            lp.alpha = 1.0f;
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            getWindow().setAttributes(lp);
//        });
//        menuPop.update();
    }

    private void initEmoji() {
        emojIcon = new EmojIconActions(this, mRootView, et_input, iv_emoji);
        emojIcon.setIconsIds(R.mipmap.service_ic_online_keyboard, R.mipmap.service_ic_online_smile);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                tv_send_standby.setVisibility(View.VISIBLE);
            }

            @Override
            public void onKeyboardClose() {
                tv_send_standby.setVisibility(View.GONE);
            }

            @Override
            public void toggleKeyboard() {
                et_input.findFocus();
                KeyboardUtils.showSoftInput(et_input);
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_voice) {
            switchInputType(true);
        } else if (id == R.id.tv_tip_close) {
            showEndDialog();
        } else if (id == R.id.iv_keyboard) {
            switchInputType(false);
        } else if (id == R.id.iv_emoji) {
            showMore(false);
            switchInputType(false);
            tv_send_standby.setVisibility(View.VISIBLE);
            showEmoji(true);
        } else if (id == R.id.iv_more) {
            if (layout_more.getVisibility() != View.VISIBLE) {
                showMore(true);
            } else {
                showMore(false);
            }
        } else if (id == R.id.layout_pic) {
            selectedImage();
        } else if (id == R.id.layout_mute) {
            if (!sendBusyMessage()) {
                startMedia(ServiceMediaActivity.MEDIA_TYPE_MUTE, false);
            }
        } else if (id == R.id.layout_video) {
            if (!sendBusyMessage()) {
                startMedia(ServiceMediaActivity.MEDIA_TYPE_VIDEO, false);
            }
        } else if (id == R.id.layout_admire) {
            showAdmireDialog();
        }
    }

    private void switchInputType(boolean isVoice) {
        tv_voice.setVisibility(isVoice ? View.VISIBLE : View.GONE);
        iv_keyboard.setVisibility(isVoice ? View.VISIBLE : View.GONE);
        et_input.setVisibility(isVoice ? View.GONE : View.VISIBLE);
        iv_voice.setVisibility(isVoice ? View.GONE : View.VISIBLE);
    }

    private void showEndDialog() {
        if (mEndSessionDialog == null) {
            mEndSessionDialog = new EndSessionDialog(this);
        }
        mEndSessionDialog.show();
        mEndSessionDialog.setOnButtonClickListener(() -> SessionCounterService.getSingleInstance().stopSession());
    }


    private void showMore(boolean isShow) {
        layout_more.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }


    private void showEmoji(boolean isShow) {
        if (emojIcon != null) {
            emojIcon.togglePopupVisibility();
        }
    }

    // 打开图库
    private void selectedImage() {
        EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                .setPuzzleMenu(false)
                .setCleanMenu(false)
                .setFileProviderAuthority(Utils.getApp().getPackageName() + ".utilcode.provider")
                .setCount(1)
                .setVideo(true)
                .setGif(true)
                .start(REQUEST_SELECTED_IMAGE);
    }

    private void showAdmireDialog() {
        if (mAdmireDialog == null) {
            mAdmireDialog = new AdmireDialog();
        }
        Bundle bundle = new Bundle();
        bundle.putString(Arguments.GROUP_ID, mGroupId);
        bundle.putString(Arguments.SESSION_ID, mGroupId);
        if (mTransService != null) {
            bundle.putString(Arguments.DATA, mTransService.getAvatarUrl());
            bundle.putString(Arguments.DATA1, mTransService.getAllName());
            bundle.putString(Arguments.ID, String.valueOf(mTransService.getId()));
        } else {
            bundle.putString(Arguments.DATA, mCurrentService.getAvatarUrl());
            bundle.putString(Arguments.DATA1, mCurrentService.getAllName());
            bundle.putString(Arguments.ID, String.valueOf(mCurrentService.getId()));
        }
        bundle.putInt(Arguments.TYPE, 1);
        mAdmireDialog.setArguments(bundle);

        mAdmireDialog.show(getSupportFragmentManager(), "");
    }


    public void startMedia(int mediaType, boolean isFrom) {
        Long sessionId = (Long) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_ID);
        if (sessionId == null || sessionId == -1) {
            //会话结束后，请求音视频，先获取客服
            getP().createSession(mCurrentService, null, mediaType);
            return;
        }
        Bundle bundle = new Bundle();
        RoomMediaBean mediaBean = new RoomMediaBean();
        mediaBean.setFrom(isFrom);
        mediaBean.setMediaType(mediaType);
        if (mTransService != null) {
            mediaBean.setOtherAvatar(mTransService.getAvatarUrl());
            mediaBean.setOtherName(mTransService.getAllName());
        } else if (mCurrentService != null) {
            mediaBean.setOtherAvatar(mCurrentService.getAvatarUrl());
            mediaBean.setOtherName(mCurrentService.getAllName());
        }
        mediaBean.setRoomId((Long) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_ID));
        mediaBean.setSelfId(mImUserInfo.getId());
        mediaBean.setGroupId(mGroupId);
        bundle.putSerializable(Arguments.DATA, mediaBean);
        ServiceMediaActivity.start(mediaBean);
//        ActivityUtils.startActivity(bundle, ServiceMediaActivity.class);
        SessionCounterService.getSingleInstance().stopCounterService();
    }

    private boolean sendBusyMessage() {
        Integer waitingNo = (Integer) GlobalDataHelper.getInstance().getData(ChatConst.CHAT_WAITING_NO);
        if (waitingNo != null && waitingNo < 1) {
            return false;
        } else {
            Message message1 = new Message();
            SpanUtils spanUtils = new SpanUtils();
            spanUtils.append("很抱歉，客服忙碌中，您前面排队").setForegroundColor(ColorUtils.getColor(R.color.color_55));
            spanUtils.append(waitingNo + "人").setForegroundColor(ColorUtils.getColor(R.color.color_EE7F00));
            spanUtils.append("，请耐心等待，我们会尽快安排您的专属客服为您服务！").setForegroundColor(ColorUtils.getColor(R.color.color_55));
            message1.setContentStr(spanUtils.create());
            message1.setType(YLConstant.MessageType.MESSAGE_TYPE_SYSTEM);
            message1.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
            message1.setGroupId(mGroupId);
            message1.setCreateTime(System.currentTimeMillis());
            receiveMessage(message1);
            return true;
        }
    }

    public void createSessionSuccess(ServiceEntity serviceEntity, Message message, int mediaType, boolean showWelcome) {
        serviceEntity.setTop(mCurrentService.isTop());
        mCurrentService = serviceEntity;
        tv_tip_close.setVisibility(View.VISIBLE);
        if (mCurrentService.getAccountType() != 1) {
            layout_admire.setVisibility(View.VISIBLE);
        }
        setGroupId(mCurrentService.getGroupId());
        setWaitNo(mCurrentService.getWaitNo());
        LogUtils.e("createSessionSuccess waitNo : " + waitNo + "    mGroupId : " + mGroupId);
        GlobalDataHelper.getInstance().setData(ChatConst.SESSION_TYPE, serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_AI);

        layout_video.setVisibility(serviceEntity.getConversationType() == 1 || serviceEntity.getConversationType() == 2 ? View.VISIBLE : View.GONE);
        layout_mute.setVisibility(serviceEntity.getConversationType() == 1 || serviceEntity.getConversationType() == 2 || serviceEntity.getConversationType() == 3 ? View.VISIBLE : View.GONE);

        String welcome = SpManager.getConfig().getString(SpManager.KEY_WELCOME_CONTENT);
        if (!StringUtils.isEmpty(welcome) && showWelcome) {
            Message message1 = new Message();
            message1.setContent(Html.fromHtml(welcome).toString());
            message1.setType(YLConstant.MessageType.MESSAGE_TYPE_SYSTEM);
            message1.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
            message1.setGroupId(mGroupId);
            message1.setCreateTime(System.currentTimeMillis());
            receiveMessage(message1);
        }
        if (waitNo > 0) {
            Message message1 = new Message();
            SpanUtils spanUtils = new SpanUtils();
            spanUtils.append("很抱歉，客服忙碌中，您前面排队").setForegroundColor(ColorUtils.getColor(R.color.color_55));
            spanUtils.append(serviceEntity.getWaitNo() + "人").setForegroundColor(ColorUtils.getColor(R.color.color_EE7F00));
            spanUtils.append("，请耐心等待，我们会尽快安排您的专属客服为您服务！").setForegroundColor(ColorUtils.getColor(R.color.color_55));
            message1.setContentStr(spanUtils.create());
            message1.setType(YLConstant.MessageType.MESSAGE_TYPE_SYSTEM);
            message1.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
            message1.setGroupId(mGroupId);
            message1.setCreateTime(System.currentTimeMillis());
            receiveMessage(message1);
            //若处于等待状态，不走音视频逻辑
            return;
        }
//        SessionCounterService.getSingleInstance().refreshTimer();

        if (message != null) {
            message.setGroupId(mGroupId);
            mImClient.sendMsg(message);
        }

        if (mediaType != ServiceMediaActivity.MEDIA_TYPE_NULL) {
            startMedia(mediaType, false);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (isShouldHideMore(layout_more, ev)) {
                showMore(false);
            }
            if (isShouldHideKeyboard(v, ev) && isShouldHideMore(iv_emoji, ev) && isShouldHideMore(tv_send_standby, ev)) {
                KeyboardUtils.hideSoftInput(this);
            } else if (v instanceof EditText) {
                showMore(false);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;

        }
        if (requestCode == REQUEST_SELECTED_IMAGE) {
            ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
            LogUtils.e("selected:" + JsonUtils.encode(resultPhotos));
            if (!CollectionUtils.isEmpty(resultPhotos)) {
                Photo photo = resultPhotos.get(0);
                if (photo.type.contains("video")) {
                    if (photo.size > 20 * 1024 * 1024) {
                        ToastUtils.showShort("视频限制每个20M以内，请处理后重新上传");
                        return;
                    }
                } else {
                    if (photo.size > 5 * 1024 * 1024) {
                        ToastUtils.showShort("图片每张限制5M以内，请处理后重新上传");
                        return;
                    }
                }
                getP().uploadImage(photo);
            } else {
                showToast("图像选取失败");
                LogUtils.e("-----change bg failed-----");
            }
        } else if (requestCode == REQUEST_FOR_EVALUATION && data != null) {
            int position = data.getIntExtra(Arguments.POSITION, -1);
            if (position >= 0) {
                mAdapter.removeAt(position);
            }
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
        message.setToSourceId(String.valueOf(mCurrentService.getId()));
        if (mIsInSession) {
            message.setGroupId(mGroupId);
//            EventBus.getDefault().post(message);
            mImClient.sendMsg(message);
        } else {
            getP().createSession(mCurrentService, message, ServiceMediaActivity.MEDIA_TYPE_NULL);
        }
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
        if (waitNo != null) {
            message.setWaitNo(waitNo);
        }
        message.setMyUserId(mImUserInfo.getId());
        return message;
    }

    @Subscribe
    public void receiveMessage(Message message) {
        LogUtils.e("stompLiveChatClient receive msg  1 :  " + message);
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SERVICE_IN) {
            if (message.getFromSourceId().contains(String.valueOf(mCurrentService.getId()))) {
                tv_tip_close.setVisibility(View.VISIBLE);
                setGroupId(message.getGroupId());
            }
        }
        if (mGroupId == null || !mGroupId.equals(message.getGroupId())) {
            return;
        }
        ImClient.getInstance().setMessageRead(message);

        if (message.getType() != YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START && message.getType() != YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_START) {
//            SessionCounterService.getSingleInstance().refreshTimer();
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SERVICE_IN) {
            setWaitNo(message.getWaitNo());
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_WAIT_CHANGE) {
            setWaitNo(message.getWaitNo());
            if (message.getWaitNo() > 0) {
                sendBusyMessage();
                //若处于等待状态，不走音视频逻辑
                return;
            }
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SERVICE_TRANS_TIP) {
            //实际客服类型
            ServiceEntity serviceEntity = getServiceEntity(message.getExt());
            if (serviceEntity == null) {
                return;
            }
            this.mTransService = serviceEntity;
            boolean showMore = mTransService.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VIP || mTransService.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VOICE || mTransService.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VIDEO;
            layout_video.setVisibility(serviceEntity.getConversationType() == 1 || serviceEntity.getConversationType() == 2 || serviceEntity.getConversationType() == 3 ? View.VISIBLE : View.GONE);
            layout_mute.setVisibility(serviceEntity.getConversationType() == 1 || serviceEntity.getConversationType() == 2 ? View.VISIBLE : View.GONE);

            if (!showMore) {
                Message message1 = new Message();
                message1.setContent("您请求的音视频客服不在线，已为您转接普通客服”" + mTransService.getAllName() + "“，不支持语音视频通话，请输入您的问题");
                message1.setType(YLConstant.MessageType.MESSAGE_TYPE_SYSTEM);
                message1.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
                message1.setGroupId(mGroupId);
                message1.setCreateTime(System.currentTimeMillis());
                receiveMessage(message1);
                return;
            }
        }
        if (message.getItemType() == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_MEDIA) {
            if (message.getFromUserId() != mAdapter.getUserId() && message.getTimeDuration() == 0) {
                if ("1".equals(message.getContent())) {
                    Message message1 = new Message();
                    message1.setContent("当前接待客服音视频忙线中，已为您接入图文会话，您可以发送消息咨询");
                    message1.setType(YLConstant.MessageType.MESSAGE_TYPE_SYSTEM);
                    message1.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
                    message1.setGroupId(mGroupId);
                    message1.setCreateTime(System.currentTimeMillis());
                    receiveMessage(message1);
                    return;
                }
            }
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_END) {
            setSessionEnd();
        }
        if (!message.isShow()) {
            return;
        }
        dataList.add(message);
        if (mAdapter != null) {
            mAdapter.notifyItemInserted(dataList.size() - 1);
            rv_chat.scrollToPosition(mAdapter.getData().size() - 1);
        }
    }

    private void setSessionEnd() {
        setGroupId("-1");
        mTransService = null;
        tv_tip_close.setVisibility(View.GONE);
        layout_admire.setVisibility(View.GONE);
//            mImClient.sendMsg(message);

        if (ServiceMediaActivity.getServiceMediaActivity() != null) {
            ServiceMediaActivity.getServiceMediaActivity().closeSmallWindow();
        }
    }

    public void setWaitNo(Integer waitNo) {
        this.waitNo = waitNo;
        GlobalDataHelper.getInstance().setData(ChatConst.CHAT_WAITING_NO, waitNo);
    }

    public void setGroupId(String groupId) {
        mIsInSession = !(StringUtils.isEmpty(groupId) || "-1".equals(groupId));
        this.mGroupId = groupId;
        GlobalDataHelper.getInstance().setData(ChatConst.SESSION_ID, Long.valueOf(groupId));
    }

    private ServiceEntity getServiceEntity(Object object) {
        if (object == null) {
            return null;
        }
        List<Object> list = JsonUtil.parseArrayList(JsonUtil.encode(object), Object.class);
        if (list == null || list.size() < 9) {
            return null;
        }
        ServiceEntity serviceEntity = new ServiceEntity();
        try {
            serviceEntity.setConversationType(((Double) list.get(3)).intValue());
            serviceEntity.setShopId(Long.valueOf((String) list.get(4)));
            serviceEntity.setAvatarUrl((String) list.get(5));
            serviceEntity.setName((String) list.get(6));
            serviceEntity.setNameEn((String) list.get(7));
            serviceEntity.setId(new BigDecimal(String.valueOf(list.get(8))).longValue());
        } catch (Exception e) {
            LogUtils.e("transfer error e : " + e.getMessage());
        }
        return serviceEntity;
    }

    @Subscribe
    public void receiveMsg(SessionEndEvent sessionEndEvent) {
        tv_tip_close.post(new Runnable() {
            @Override
            public void run() {
                tv_tip_close.setVisibility(View.GONE);
                layout_admire.setVisibility(View.GONE);
                mIsInSession = false;
            }
        });

    }

    @Subscribe
    public void receiveMsg(SetUserTopEvent sessionEndEvent) {
        if (Long.valueOf(sessionEndEvent.getCsUserId()) == mCurrentService.getId()) {
            mCurrentService.setTop(sessionEndEvent.isTop());
        }
    }

    @Subscribe
    public void imReconnect(ImReConnectEvent event) {
        getP().getUnReadMessages(mGroupId);
    }

    public void getUnReadList(List<Message> messages) {
        if (!CollectionUtils.isEmpty(messages)) {
            rv_chat.postDelayed(() -> {
                Collections.sort(messages, (o1, o2) -> (int) (o2.getCreateTime() - o1.getCreateTime()));
                List<Message> list = new ArrayList<>();
                for (Message message : messages) {
                    ImClient.getInstance().setMessageRead(message);
                    if (message.isInHistory()) {
                        list.add(0, message);
                    }
                }
                mAdapter.addData(list);
                LogUtils.e("stompLiveChatClient receive msg size 2 :  " + dataList.size());

                scrollToBottom();
            }, 500);
        }

        getSessionById(mGroupId);
    }

    private void getSessionById(String groupId) {
        OkHttpUtils.postJson(ApiDomain.GET_SESSION_BY_ID + "?groupId=" + groupId, new GsonResponseHandler<ServiceEntity>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(ServiceEntity serviceEntity) {
                if (serviceEntity != null) {
                    if (serviceEntity.getIsFinish() == 0) {
                        setSessionEnd();
                    } else if (serviceEntity.getIsFinish() == 1) {
                        mCurrentService = serviceEntity;
                        setWaitNo(0);
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MediaManager.isPlaying()) {
            MediaManager.release();
        }
    }
}
