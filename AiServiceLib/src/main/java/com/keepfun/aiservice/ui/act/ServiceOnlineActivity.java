package com.keepfun.aiservice.ui.act;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keepfun.adapter.base.listener.OnUpFetchListener;
import com.keepfun.adapter.base.listener.UpFetchListenerImp;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.emoji.Actions.EmojIconActions;
import com.keepfun.aiservice.emoji.Helper.EmojiconEditText;
import com.keepfun.aiservice.entity.AppAdNoticeBean;
import com.keepfun.aiservice.entity.ImUserInfo;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.entity.RoomMediaBean;
import com.keepfun.aiservice.entity.ServiceBean;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.entity.event.ImReConnectEvent;
import com.keepfun.aiservice.entity.event.SessionEndEvent;
import com.keepfun.aiservice.event.CreateSessionMessage;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.threads.SessionCounterService;
import com.keepfun.aiservice.ui.adapter.ServiceChatAdapter;
import com.keepfun.aiservice.ui.adapter.ServiceChatAdapter2;
import com.keepfun.aiservice.ui.dialog.EndSessionDialog;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.ServiceOnlinePresenter;
import com.keepfun.aiservice.ui.view.ServiceAudioLayout;
import com.keepfun.aiservice.ui.view.ServiceTitleView;
import com.keepfun.aiservice.utils.GlideEngine;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.aiservice.utils.audio.MediaManager;
import com.keepfun.banner.Banner;
import com.keepfun.banner.adapter.BannerImageAdapter;
import com.keepfun.banner.holder.BannerImageHolder;
import com.keepfun.banner.indicator.CircleIndicator;
import com.keepfun.base.PanActivity;
import com.keepfun.base.WebActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.CollectionUtils;
import com.keepfun.blankj.util.ColorUtils;
import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.blankj.util.KeyboardUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.SpanUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.blankj.util.Utils;
import com.keepfun.easyphotos.EasyPhotos;
import com.keepfun.easyphotos.models.album.entity.Photo;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.FutureTask;


/**
 * @author yang
 * @description
 * @date 2020/9/2 1:46 PM
 */
public class ServiceOnlineActivity extends PanActivity<ServiceOnlinePresenter> implements View.OnClickListener {

    private ServiceTitleView titleView;
    //    private NestedScrollView scrollView;
    private View mRootView;
    private LinearLayout layout_broadcast;
    private TextView tv_broadcast;
    private RelativeLayout layout_banner;
    private Banner banner;
    private RecyclerView rv_chat;
    private EmojiconEditText et_input;
    private ServiceAudioLayout tv_voice;
    private ImageView iv_voice;
    private ImageView iv_keyboard;
    private ImageView iv_emoji;
    private ImageView iv_more;
    private TextView tv_send_standby;
    private View layout_video;
    private ImageView iv_pic;
    private LinearLayout layout_more;
    private TextView tv_tip_close;

    private static final int REQUEST_FOR_QS_PAPER = 0x11;
    private final int REQUEST_SELECTED_IMAGE = 0x12;
    public static final int REQUEST_FOR_EVALUATION = 0x13;
    /**
     * 聊天类型
     */
    private int mChatType = YLConstant.ChatType.CHAT_TYPE_COMMON;

    private ImClient mImClient;
    private ImUserInfo mImUserInfo;
    private List<Message> dataList;
    private ServiceChatAdapter mAdapter;
    private boolean isLoadingMore = false;
    private int pageIndex = 0;
    private EmojIconActions emojIcon;
    private ServiceEntity mCurrentService;
    private String mGroupId;

    private EndSessionDialog mEndSessionDialog;

    @Override
    public ServiceOnlinePresenter newP() {
        return new ServiceOnlinePresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_service_online;
    }

    @Override
    public void bindUI(View rootView) {
        titleView = findViewById(R.id.titleView);
//        scrollView = findViewById(R.id.scrollView);
        mRootView = findViewById(R.id.rootView);
        layout_broadcast = findViewById(R.id.layout_broadcast);
        tv_broadcast = findViewById(R.id.tv_broadcast);
        layout_banner = findViewById(R.id.layout_banner);
        banner = findViewById(R.id.banner);
        rv_chat = findViewById(R.id.rv_chat);
        et_input = findViewById(R.id.et_input);
        tv_voice = findViewById(R.id.tv_voice);
        iv_voice = findViewById(R.id.iv_voice);
        iv_keyboard = findViewById(R.id.iv_keyboard);
        iv_emoji = findViewById(R.id.iv_emoji);
        iv_more = findViewById(R.id.iv_more);
        tv_send_standby = findViewById(R.id.tv_send_standby);
        layout_video = findViewById(R.id.layout_video);
        iv_pic = findViewById(R.id.iv_pic);
        layout_more = findViewById(R.id.layout_more);
        tv_tip_close = findViewById(R.id.tv_tip_close);
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
    public void bindEvent() {
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
                if (mImClient != null && !StringUtils.isEmpty(message)) {
                    mImClient.sendMsg(YLConstant.MessageType.MESSAGE_TYPE_MONITOR, YLConstant.ContentType.CONTENT_TYPE_TEXT, message);
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
            if (mImClient != null && !StringUtils.isEmpty(message)) {
                sendMessage(message, YLConstant.ContentType.CONTENT_TYPE_TEXT);
                et_input.setText("");
                et_input.clearFocus();
                KeyboardUtils.hideSoftInput(this);
            }
        }));

        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            scrollToBottom();
        });

        findViewById(R.id.iv_broadcast_close).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_banner_close).setOnClickListener(new CheckClickListener(this));
        tv_tip_close.setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_voice).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_keyboard).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_emoji).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_more).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_pic).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_mute).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_video).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.layout_pic).setOnClickListener(new CheckClickListener(this));
    }

    @Override
    public void initData() {
        KeyboardUtils.fixAndroidBug5497(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mChatType = bundle.getInt(Arguments.DATA);
            mCurrentService = (ServiceEntity) bundle.getSerializable(Arguments.DATA1);
        }

        boolean showMore = mChatType == YLConstant.ChatType.CHAT_TYPE_VIP || mChatType == YLConstant.ChatType.CHAT_TYPE_VOICE || mChatType == YLConstant.ChatType.CHAT_TYPE_VIDEO;
        iv_more.setVisibility(showMore ? View.VISIBLE : View.GONE);
        layout_video.setVisibility(mChatType == YLConstant.ChatType.CHAT_TYPE_VOICE ? View.GONE : View.VISIBLE);
        iv_pic.setVisibility(showMore ? View.GONE : View.VISIBLE);

        mImUserInfo = GlobalDataHelper.getInstance().getImUserInfo();

        if (mChatType == YLConstant.ChatType.CHAT_TYPE_VIP) {
            titleView.setTitle("VIP客服");
        } else if (mChatType == YLConstant.ChatType.CHAT_TYPE_VOICE) {
            titleView.setTitle("语音客服");
        } else if (mChatType == YLConstant.ChatType.CHAT_TYPE_VIDEO) {
            titleView.setTitle("视频客服");
        } else {
            titleView.setTitle("在线客服");
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

        mImClient = ImClient.getInstance();
        switchInputType(false);
        initEmoji();
        initChatRecyclerView();

        if (mCurrentService == null) {
            //不存在未结束的会话时，请求对应客服
            getP().getService(mChatType, false, null, mChatType == YLConstant.ChatType.CHAT_TYPE_VIDEO ? ServiceMediaActivity.MEDIA_TYPE_VIDEO :
                    (mChatType == YLConstant.ChatType.CHAT_TYPE_VOICE ? ServiceMediaActivity.MEDIA_TYPE_MUTE : ServiceMediaActivity.MEDIA_TYPE_NULL));
        } else {
            createSessionSuccess(mCurrentService, null, ServiceMediaActivity.MEDIA_TYPE_NULL, true);
        }
        rv_chat.postDelayed(() -> requestChatHistory(true), 500);

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
        mAdapter = new ServiceChatAdapter(dataList);
        if (GlobalDataHelper.getInstance().getImUserInfo() == null) {
            return;
        }
//        mAdapter.setUserId(GlobalDataHelper.getInstance().getImUserInfo().getId());
        rv_chat.setAdapter(mAdapter);
        mAdapter.getUpFetchModule().setUpFetchEnable(true);
        mAdapter.getUpFetchModule().setOnUpFetchListener(() -> {
            if (isLoadingMore) {
                return;
            }
            requestChatHistory(false);
        });
        mAdapter.setOnOperationClickListener(message -> {
            if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_TRANSFORM_2_ARTIFICIAL) {
                boolean isIsSession = tv_tip_close.getVisibility() != View.GONE;
                if (StringUtils.isEmpty(mGroupId) || !mGroupId.equals(message.getGroupId()) || !isIsSession) {
                    ToastUtils.showShort("转入已失效，请重新询问");
                    return;
                }

                getP().checkServiceExist(YLConstant.ChatType.CHAT_TYPE_COMMON, new GsonResponseHandler<ServiceBean>() {
                    @Override
                    public void onFailure(String statusCode, String error_msg) {

                    }

                    @Override
                    public void onSuccess(ServiceBean response) {
                        if (response != null) {
                            getP().checkQuestionnaireSwitch(YLConstant.ChatType.CHAT_TYPE_COMMON, qsPaperSetBean -> {
                                Bundle bundle = new Bundle();
                                bundle.putInt(Arguments.DATA, YLConstant.ChatType.CHAT_TYPE_COMMON);
                                if (qsPaperSetBean.getNeedPaper() == 1) {
                                    bundle.putBoolean(Arguments.DATA1, qsPaperSetBean.getNeedCode() == 1);
                                    ActivityUtils.startActivityForResult(bundle, ServiceOnlineActivity.this, QuestionnaireActivity.class, REQUEST_FOR_QS_PAPER);
                                } else {
                                    //机器人转人工，请求客服
                                    getP().createSession(YLConstant.ChatType.CHAT_TYPE_COMMON, response.getService(), null, true, ServiceMediaActivity.MEDIA_TYPE_NULL);
                                }
                            });
                        }
                    }
                });

            }
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
        getP().getHistory(pageIndex, isPullDown);
    }

    public void getDataSuccess(PageBean<Message> pageBean, boolean isPullDown) {
        stopRefreshLoad();
        LogUtils.e("mChatList size 1 : " + mAdapter.getData().size());
//        if (isPullDown) {
//            dataList.clear();
//        }
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
                if (pageIndex == 1) {
                    for (Message message : pageBean.getDatas()) {
                        if (message.getId() > 0) {
                            ImClient.getInstance().setMessageRead(message);
                            break;
                        }
                    }

                }
                mAdapter.addData(0, list);

                if (isPullDown) {
                    scrollToBottom();
                }
            }, 500);
        }
    }

    private void stopRefreshLoad() {
        if (isLoadingMore) {
            isLoadingMore = false;
        }
        mAdapter.getUpFetchModule().setUpFetching(false);
    }

    public void getDataFailed(String message) {
        pageIndex--;
        stopRefreshLoad();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 创建会话成功
     *
     * @param serviceEntity
     */
    public void createSessionSuccess(ServiceEntity serviceEntity, @Nullable Message msg, int mediaType, boolean showWelcome) {
        if (serviceEntity == null) {
            tv_tip_close.setVisibility(View.GONE);
            return;
        }
        this.mCurrentService = serviceEntity;
        mGroupId = serviceEntity.getGroupId();
        GlobalDataHelper.getInstance().setData(ChatConst.SESSION_TYPE, serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_AI);
        GlobalDataHelper.getInstance().setData(ChatConst.SESSION_ID, Long.valueOf(serviceEntity.getGroupId()));
        GlobalDataHelper.getInstance().setData(ChatConst.CHAT_WAITING_NO, serviceEntity.getWaitNo());
        tv_tip_close.setVisibility(View.VISIBLE);
        String welcome = SpManager.getConfig().getString(SpManager.KEY_WELCOME_CONTENT);
        if (!StringUtils.isEmpty(welcome) && showWelcome) {
            Message message = new Message();
            message.setContent(Html.fromHtml(welcome).toString());
            message.setType(YLConstant.MessageType.MESSAGE_TYPE_SYSTEM);
            message.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
            message.setGroupId(mGroupId);
            message.setCreateTime(System.currentTimeMillis());
            receiveMessage(message);
        }
        boolean showMore = serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VIP || serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VOICE || serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VIDEO;
        if (!showMore) {
            iv_more.setVisibility(View.GONE);
            iv_pic.setVisibility(View.VISIBLE);
            showMore(false);
        }

        if (!StringUtils.isEmpty(serviceEntity.getWelcome()) && showWelcome) {
            Message message = new Message();
            message.setFromUserAvatar(serviceEntity.getAvatarUrl());
            message.setGroupId(serviceEntity.getGroupId());
            message.setFromUserName(serviceEntity.getAllName());
            message.setFromUserId(serviceEntity.getId());
            message.setContent(serviceEntity.getWelcome());
            message.setCreateTime(System.currentTimeMillis());
            message.setGroupId(mGroupId);
            message.setType(serviceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_AI ? YLConstant.MessageType.MESSAGE_TYPE_AI_RECEIVE : YLConstant.MessageType.MESSAGE_TYPE_CHAT);
            message.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
            receiveMessage(message);
        }

        if (serviceEntity.getWaitNo() > 0) {
            Message message1 = new Message();
            SpanUtils spanUtils = new SpanUtils();
            spanUtils.append("很抱歉，客服忙碌中，您前面排队").setForegroundColor(ColorUtils.getColor(R.color.color_55));
            spanUtils.append(serviceEntity.getWaitNo() + "人").setForegroundColor(ColorUtils.getColor(R.color.color_EE7F00));
            spanUtils.append("，请耐心等待，我们会尽快安排您的专属客服为您服务！").setForegroundColor(ColorUtils.getColor(R.color.color_55));
            message1.setContentStr(spanUtils.create());
            message1.setType(YLConstant.MessageType.MESSAGE_TYPE_SYSTEM);
            message1.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
            message1.setCreateTime(System.currentTimeMillis());
            message1.setGroupId(mGroupId);
            receiveMessage(message1);
            //若处于等待状态，不走音视频逻辑
            return;
        }
//        SessionCounterService.getSingleInstance().refreshTimer();

        if (mChatType == YLConstant.ChatType.CHAT_TYPE_VIDEO || mChatType == YLConstant.ChatType.CHAT_TYPE_VOICE) {
            if (serviceEntity.getConversationType() != YLConstant.ChatType.CHAT_TYPE_COMMON && serviceEntity.getConversationType() != YLConstant.ChatType.CHAT_TYPE_AI) {
                if (mediaType != ServiceMediaActivity.MEDIA_TYPE_NULL) {
                    startMedia(mediaType, false);
                }
            } else {
                Message message = new Message();
                message.setContent("您请求的音视频客服不在线，已为您转接普通客服”" + serviceEntity.getAllName() + "“，不支持语音视频通话，请输入您的问题");
                message.setType(YLConstant.MessageType.MESSAGE_TYPE_SYSTEM);
                message.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
                message.setCreateTime(System.currentTimeMillis());
                message.setGroupId(mGroupId);
                receiveMessage(message);
            }
        }
        if (msg != null) {
            mImClient.sendMsg(msg.getType(), msg.getContentType(), msg.getContent(), msg.getTimeDuration());
        }

    }

    @Subscribe
    public void receiveMsg(SessionEndEvent sessionEndEvent) {
        tv_tip_close.setVisibility(View.GONE);
        GlobalDataHelper.getInstance().setData(ChatConst.SESSION_ID, -1L);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_broadcast_close) {
            layout_broadcast.setVisibility(View.GONE);
        } else if (id == R.id.iv_banner_close) {
            layout_banner.setVisibility(View.GONE);
        } else if (id == R.id.tv_tip_close) {
            showEndDialog();
        } else if (id == R.id.iv_voice) {
            switchInputType(true);
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
        } else if (id == R.id.iv_pic || id == R.id.layout_pic) {
            selectedImage();
        } else if (id == R.id.layout_mute) {
            if (!sendBusyMessage()) {
                startMedia(ServiceMediaActivity.MEDIA_TYPE_MUTE, false);
            }
        } else if (id == R.id.layout_video) {
            if (!sendBusyMessage()) {
                startMedia(ServiceMediaActivity.MEDIA_TYPE_VIDEO, false);
            }
        }
    }

    private void showEndDialog() {
        if (mEndSessionDialog == null) {
            mEndSessionDialog = new EndSessionDialog(this);
        }
        mEndSessionDialog.show();
        mEndSessionDialog.setOnButtonClickListener(() -> SessionCounterService.getSingleInstance().stopSession());
    }

    public void startMedia(int mediaType, boolean isFrom) {
        Long sessionId = (Long) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_ID);
        if (sessionId == null || sessionId == -1) {
            //会话结束后，请求音视频，先获取客服
            getP().getService(mChatType, false, null, mediaType);
            return;
        }
        Bundle bundle = new Bundle();
        RoomMediaBean mediaBean = new RoomMediaBean();
        mediaBean.setFrom(isFrom);
        mediaBean.setMediaType(mediaType);
        if (mCurrentService != null) {
            mediaBean.setOtherAvatar(mCurrentService.getAvatarUrl());
            mediaBean.setOtherName(mCurrentService.getAllName());
        }
        mediaBean.setRoomId((Long) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_ID));
        mediaBean.setSelfId(mImUserInfo.getId());
        mediaBean.setGroupId(mGroupId);
        bundle.putSerializable(Arguments.DATA, mediaBean);
//        ActivityUtils.startActivity(bundle, ServiceMediaActivity.class);
        ServiceMediaActivity.start(mediaBean);
        SessionCounterService.getSingleInstance().stopCounterService();
    }

    private void switchInputType(boolean isVoice) {
        tv_voice.setVisibility(isVoice ? View.VISIBLE : View.GONE);
        iv_keyboard.setVisibility(isVoice ? View.VISIBLE : View.GONE);
        et_input.setVisibility(isVoice ? View.GONE : View.VISIBLE);
        iv_voice.setVisibility(isVoice ? View.GONE : View.VISIBLE);
    }

    private void showMore(boolean isShow) {
        layout_more.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void showEmoji(boolean isShow) {
        if (emojIcon != null) {
            emojIcon.togglePopupVisibility();
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

    @Subscribe
    public void receiveMessage(Message message) {
        LogUtils.e("stompLiveChatClient receive msg  :  " + message);
        if (mGroupId == null || !mGroupId.equals(message.getGroupId())) {
            return;
        }
        ImClient.getInstance().setMessageRead(message);

        if (message.getType() != YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_START && message.getType() != YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START) {
            Integer waitingNo = (Integer) GlobalDataHelper.getInstance().getData(ChatConst.CHAT_WAITING_NO);
            if (waitingNo != null && waitingNo <= 0) {
//                SessionCounterService.getSingleInstance().refreshTimer();
            }
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END || message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END) {
//            SessionCounterService.getSingleInstance().refreshTimer();
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_END) {
            setSessionEnd();
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SERVICE_IN) {
            GlobalDataHelper.getInstance().setData(ChatConst.CHAT_WAITING_NO, message.getWaitNo());
            GlobalDataHelper.getInstance().setData(ChatConst.SESSION_TYPE, message.getFromUserType() == 0);

        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_WAIT_CHANGE) {
            GlobalDataHelper.getInstance().setData(ChatConst.CHAT_WAITING_NO, message.getWaitNo());
            if (message.getWaitNo() > 0) {
                if (message.getWaitNo() > 0) {
                    sendBusyMessage();
                    //若处于等待状态，不走音视频逻辑
                    return;
                }
            }
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SERVICE_TRANS_TIP) {
            //实际客服类型
            ServiceEntity serviceEntity = getServiceEntity(message.getExt());
            if (serviceEntity == null) {
                return;
            }
            this.mCurrentService = serviceEntity;
            boolean showMore = mCurrentService.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VIP || mCurrentService.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VOICE || mCurrentService.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VIDEO;
            if (!showMore) {
                iv_more.setVisibility(View.GONE);
                iv_pic.setVisibility(View.VISIBLE);
            }
            if ((mChatType == YLConstant.ChatType.CHAT_TYPE_VOICE || mChatType == YLConstant.ChatType.CHAT_TYPE_VIDEO) && !showMore) {
                Message message1 = new Message();
                message1.setContent("您请求的音视频客服不在线，已为您转接普通客服”" + mCurrentService.getAllName() + "“，不支持语音视频通话，请输入您的问题");
                message1.setType(YLConstant.MessageType.MESSAGE_TYPE_SYSTEM);
                message1.setContentType(YLConstant.ContentType.CONTENT_TYPE_TEXT);
                message1.setCreateTime(System.currentTimeMillis());
                message1.setGroupId(mGroupId);
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
                    message1.setCreateTime(System.currentTimeMillis());
                    message1.setGroupId(mGroupId);
                    receiveMessage(message1);
                    return;
                }
            }
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
            message1.setCreateTime(System.currentTimeMillis());
            message1.setGroupId(mGroupId);
            receiveMessage(message1);
            return true;
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

    @Subscribe
    public void receiveMessage(CreateSessionMessage message) {
        //会话时，发现会话已结束，先创建会话
        getP().getService(mChatType, false, message.getMessage(), ServiceMediaActivity.MEDIA_TYPE_NULL);
    }

    public void endSuccess() {
        GlobalDataHelper.getInstance().setData(ChatConst.SESSION_ID, -1L);
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

    public void sendMessage(String result, int contentType) {
        sendMessage(result, contentType, 0);
    }

    public void sendMessage(String result, int contentType, @Nullable long duration) {
        boolean isAi = GlobalDataHelper.getInstance().getData(ChatConst.SESSION_TYPE) != null && (Boolean) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_TYPE);
        mImClient.sendMsg(isAi ? YLConstant.MessageType.MESSAGE_TYPE_AI_RECEIVE : YLConstant.MessageType.MESSAGE_TYPE_CHAT, contentType, result, duration);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == REQUEST_FOR_QS_PAPER) {
            }
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
        } else if (requestCode == REQUEST_FOR_QS_PAPER && data != null) {
            int chatType = data.getIntExtra(Arguments.DATA, YLConstant.ChatType.CHAT_TYPE_COMMON);
            getP().getService(chatType, true, null, ServiceMediaActivity.MEDIA_TYPE_NULL);
        } else if (requestCode == REQUEST_FOR_EVALUATION && data != null) {
            int position = data.getIntExtra(Arguments.POSITION, -1);
            if (position >= 0) {
                mAdapter.removeAt(position);
            }
        }
    }

    private Message createMessage() {
        Message message = new Message();
        message.setAppKey(mImUserInfo.getAppKey());
        message.setFromUserName(mImUserInfo.getNickName());
        message.setFromUserAvatar(mImUserInfo.getHeadPortrait());
        message.setFromUserType(mImUserInfo.getType());
        message.setFromUserId(mImUserInfo.getId());
        message.setMyUserId(mImUserInfo.getId());
        return message;
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

    private void setSessionEnd() {
        tv_tip_close.setVisibility(View.GONE);

        if (ServiceMediaActivity.getServiceMediaActivity() != null) {
            ServiceMediaActivity.getServiceMediaActivity().closeSmallWindow();
        }
    }

    public void setWaitNo(Integer waitNo) {
        GlobalDataHelper.getInstance().setData(ChatConst.CHAT_WAITING_NO, waitNo);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (MediaManager.isPlaying()) {
            MediaManager.release();
        }
    }
}
