package com.keepfun.aiservice.ui.act;

import android.Manifest;
import android.os.Bundle;
import android.text.Editable;
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

import com.keepfun.adapter.base.listener.OnUpFetchListener;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.emoji.Actions.EmojIconActions;
import com.keepfun.aiservice.emoji.Helper.EmojiconEditText;
import com.keepfun.aiservice.entity.CsGroupInfo;
import com.keepfun.aiservice.entity.GroupMember;
import com.keepfun.aiservice.entity.ImUserInfo;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.PageBean;
import com.keepfun.aiservice.entity.StatusBean;
import com.keepfun.aiservice.entity.event.DeleteGroupEvent;
import com.keepfun.aiservice.entity.event.SetGroupTopEvent;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.ui.adapter.AtMemberAdapter;
import com.keepfun.aiservice.ui.adapter.ServiceChatAdapter2;
import com.keepfun.aiservice.ui.dialog.AtMemberDialog;
import com.keepfun.aiservice.ui.dialog.GroupAdmireDialog;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.ChatGroupPresenter;
import com.keepfun.aiservice.ui.view.ServiceAudioLayout;
import com.keepfun.aiservice.ui.view.ServiceTitleView;
import com.keepfun.aiservice.utils.audio.MediaManager;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.ClickUtils;
import com.keepfun.blankj.util.KeyboardUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/11 9:44 AM
 */
public class ChatGroupActivity extends PanActivity<ChatGroupPresenter> implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.service_activity_chat_group;
    }

    @Override
    public ChatGroupPresenter newP() {
        return new ChatGroupPresenter();
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


    private ServiceTitleView title;
    private View mRootView;
    private RecyclerView rvChat;
    private EmojiconEditText et_input;
    private ServiceAudioLayout tv_voice;
    private ImageView iv_voice;
    private ImageView iv_keyboard;
    private ImageView iv_emoji;
    private EmojIconActions emojIcon;
    private TextView tv_send_standby;

    private CsGroupInfo mGroupInfo;
    private List<Message> dataList;
    private ServiceChatAdapter2 mAdapter;
    private boolean isLoadingMore = false;
    private int pageIndex = 0;

    private ImClient mImClient;
    private ImUserInfo mImUserInfo;

    private PopupWindow menuPop;
    private GroupAdmireDialog mAdmireDialog;

    private AtMemberDialog mAtMemberDialog;

    private boolean isSilent = false;
    private boolean isSilentAll = false;

    @Override
    public void bindUI(View rootView) {
        title = findViewById(R.id.title);
        mRootView = findViewById(R.id.rootView);
        rvChat = findViewById(R.id.rvChat);
        et_input = findViewById(R.id.et_input);
        tv_voice = findViewById(R.id.tv_voice);
        iv_voice = findViewById(R.id.iv_voice);
        iv_keyboard = findViewById(R.id.iv_keyboard);
        iv_emoji = findViewById(R.id.iv_emoji);
        tv_send_standby = findViewById(R.id.tv_send_standby);
    }

    @Override
    public void initData() {
        KeyboardUtils.fixAndroidBug5497(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mGroupInfo = (CsGroupInfo) bundle.getSerializable(Arguments.DATA);
            title.setTitle(mGroupInfo.getGroupName());
        }

        mImClient = ImClient.getInstance();
        mImUserInfo = GlobalDataHelper.getInstance().getImUserInfo();

        initEmoji();
        switchInputType(false);

        initChatRecyclerView();

        rvChat.post(() -> {
            getP().getSilentStatus(String.valueOf(mGroupInfo.getId()));
            requestChatHistory(false);
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
                    if (message.endsWith("@") && mGroupInfo.getRobotId() != null) {
                        showAtDialog();
                    }
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

        findViewById(R.id.iv_voice).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_keyboard).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_emoji).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_admire).setOnClickListener(new CheckClickListener(this));
    }

    public void setSilentStatus(StatusBean status) {
        this.isSilent = status.getStatus() == 1;
        this.isSilentAll = status.getRoot() == 2;
    }

    private void showAtDialog() {
        if (mAtMemberDialog == null) {
            mAtMemberDialog = new AtMemberDialog(this);
        }
        mAtMemberDialog.show();
        GroupMember groupMember = new GroupMember();
        groupMember.setName(mGroupInfo.getRobotName());
        groupMember.setId(mGroupInfo.getRobotId());
        List<GroupMember> groupMembers = new ArrayList<>();
        groupMembers.add(groupMember);
        mAtMemberDialog.setMembers(groupMembers);
        mAtMemberDialog.setOnMemberClickListener(groupMember1 -> {
            String content = et_input.getText().toString().trim();
            et_input.setText(content + groupMember1.getName() + " ");
        });
    }

    private void requestChatHistory(boolean isPullDown) {
        if (isPullDown) {
            pageIndex = 1;
        } else {
            pageIndex++;
        }
        mAdapter.getUpFetchModule().setUpFetching(true);
        getP().getHistory(String.valueOf(mGroupInfo.getId()), pageIndex, isPullDown);
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
            rvChat.postDelayed(() -> {
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
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(getContext())
                .colorResId(R.color.transport)
                .size(SizeUtils.dp2px(10f))
                .showLastDivider()
                .build();
        rvChat.addItemDecoration(decor);
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        mAdapter = new ServiceChatAdapter2(this, dataList);
        if (GlobalDataHelper.getInstance().getImUserInfo() == null) {
            return;
        }
        mAdapter.setUserId(GlobalDataHelper.getInstance().getImUserInfo().getId());
        rvChat.setAdapter(mAdapter);
        mAdapter.getUpFetchModule().setUpFetchEnable(true);
        mAdapter.getUpFetchModule().setOnUpFetchListener(new OnUpFetchListener() {
            @Override
            public void onUpFetch() {
                if (isLoadingMore) {
                    return;
                }
                requestChatHistory(false);
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
        LinearLayoutManager ll = (LinearLayoutManager) rvChat.getLayoutManager();
        ll.scrollToPositionWithOffset(getBottomDataPosition(), 0);
    }

    private int getBottomDataPosition() {
        return mAdapter.getHeaderLayoutCount() + mAdapter.getData().size() - 1;
    }


    private void switchInputType(boolean isVoice) {
        tv_voice.setVisibility(isVoice ? View.VISIBLE : View.GONE);
        iv_keyboard.setVisibility(isVoice ? View.VISIBLE : View.GONE);
        et_input.setVisibility(isVoice ? View.GONE : View.VISIBLE);
        iv_voice.setVisibility(isVoice ? View.GONE : View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_voice) {
            switchInputType(true);
        } else if (id == R.id.iv_keyboard) {
            switchInputType(false);
        } else if (id == R.id.iv_emoji) {
            switchInputType(false);
            tv_send_standby.setVisibility(View.VISIBLE);
            showEmoji(true);
        } else if (id == R.id.iv_admire) {
            showAdmireDialog();
        }
    }


    private void showEmoji(boolean isShow) {
        if (emojIcon != null) {
            emojIcon.togglePopupVisibility();
        }
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
        bundle.putLong(Arguments.DATA, mGroupInfo.getId());
        ActivityUtils.startActivity(bundle, GroupChatDetailActivity.class);
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

    private void showAdmireDialog() {
        if (mAdmireDialog == null) {
            mAdmireDialog = new GroupAdmireDialog();
        }
        Bundle bundle = new Bundle();
        bundle.putString(Arguments.GROUP_ID, String.valueOf(mGroupInfo.getId()));
        bundle.putString(Arguments.DATA, String.valueOf(mGroupInfo.getGroupId()));
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
        message.setGroupId(String.valueOf(mGroupInfo.getGroupId()));
        if (result.contains("@" + mGroupInfo.getRobotName()) && messageType != YLConstant.MessageType.MESSAGE_TYPE_MONITOR) {
            message.setToUserId(mGroupInfo.getRobotId());
            message.setType(YLConstant.MessageType.MESSAGE_TYPE_AI_RECEIVE);
        }
        if (!isSilent && !isSilentAll) {
//            EventBus.getDefault().post(message);
            mImClient.sendMsg(message);
        } else {
            if (message.getType() != YLConstant.MessageType.MESSAGE_TYPE_MONITOR) {
                ToastUtils.showShort("您已被群管员禁言，请文明聊天");
            }
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
        message.setMyUserId(mImUserInfo.getId());
        message.setLiveType(1);
        message.setLiveId(mGroupInfo.getId());
        return message;
    }


    @Subscribe
    public void receiveMsg(DeleteGroupEvent event) {
        if (Long.valueOf(event.getGroupId()) == mGroupInfo.getId()) {
            finish();
        }
    }

    @Subscribe
    public void receiveMsg(Message message) {
        if (!String.valueOf(mGroupInfo.getGroupId()).equals(message.getGroupId())) {
            return;
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SILENT_ALL) {
            try {
                JSONObject jsonObject = new JSONObject(message.getContent());
                isSilentAll = jsonObject.optInt("root") == 2;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SILENT) {
            if (message.getToUserId() == GlobalDataHelper.getInstance().getImUserInfo().getId()) {
                isSilent = "1".equals(message.getContent());
            }
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_CANCEL) {
            if (String.valueOf(mGroupInfo.getGroupId()).equals(message.getGroupId())) {
                finish();
            }
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_ROLLBACK) {
            String messageId = message.getContent();
            for (int i = mAdapter.getData().size() - 1; i >= 0; i--) {
                Message message1 = mAdapter.getData().get(i);
                if (Long.valueOf(messageId) == message1.getId()) {
                    message1.setStatus(3);
                    mAdapter.notifyItemChanged(i);
                    break;
                }
            }

            return;
        }
        if (!message.isShow()) {
            return;
        }
        dataList.add(message);
        if (mAdapter != null) {
            mAdapter.notifyItemInserted(dataList.size() - 1);
            rvChat.scrollToPosition(mAdapter.getData().size() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MediaManager.isPlaying()) {
            MediaManager.release();
        }
    }

    @Subscribe
    public void receiveMsg(SetGroupTopEvent event) {
        if (Long.valueOf(event.getGroupId()) == mGroupInfo.getId()) {
            mGroupInfo.setIsTop(event.isTop() ? 1 : 0);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        String messageId = null;
        for (int i = mAdapter.getData().size() - 1; i >= 0; i--) {
            messageId = String.valueOf(mAdapter.getData().get(i).getId());
            if (!StringUtils.isEmpty(messageId)) {
                if (Long.valueOf(messageId) > 0) {
                    Message message = createMessage();
                    message.setType(42);
                    message.setLastReadMessageId(messageId);
                    message.setContent("1");
                    message.setGroupId(mAdapter.getData().get(i).getGroupId());
                    message.setContentType(1);
                    LogUtils.e("lastMessage : " + message);
                    mImClient.sendMsg(message);
                }
                break;
            }
        }
    }
}
