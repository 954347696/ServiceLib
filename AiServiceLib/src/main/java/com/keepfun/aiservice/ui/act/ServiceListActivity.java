package com.keepfun.aiservice.ui.act;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.AppCsStaffListVo;
import com.keepfun.aiservice.entity.AppCsStaffVo;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.event.DeleteGroupEvent;
import com.keepfun.aiservice.entity.event.LoginExitEvent;
import com.keepfun.aiservice.entity.event.SetGroupTopEvent;
import com.keepfun.aiservice.entity.event.SetUserTopEvent;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.dialog.ReceiveCallDialog;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.KeyboardUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.aiservice.ui.adapter.ServiceListAdapter;
import com.keepfun.aiservice.entity.ServiceListItem;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.ToastUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/8 10:10 AM
 */
public class ServiceListActivity extends PanActivity implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_service_list;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    private EditText et_search;
    private ImageView iv_clear;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_services;
    private ServiceListAdapter mServiceListAdapter;
    private List<ServiceListItem> mData;

    private String searchWords;

    private ReceiveCallDialog mReceiveCallDialog;

    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    public void bindUI(View rootView) {
        et_search = findViewById(R.id.et_search);
        iv_clear = findViewById(R.id.iv_clear);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rv_services = findViewById(R.id.rv_services);
        findViewById(R.id.tv_service_center).setVisibility(GlobalDataHelper.getInstance().isShowHome() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initData() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        rv_services.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(com.keepfun.aiservice.R.color.transport)
                .size(SizeUtils.dp2px(7f))
                .build();
        rv_services.addItemDecoration(decor);
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mServiceListAdapter == null) {
            mServiceListAdapter = new ServiceListAdapter(mData);
        }
        View service_view_list_empty = LayoutInflater.from(this).inflate(R.layout.service_view_list_empty, null, false);
        mServiceListAdapter.setEmptyView(service_view_list_empty);
        rv_services.setAdapter(mServiceListAdapter);

        handler.post(getServiceRunnable);

    }

    public Runnable getServiceRunnable = () -> getServiceList();

    @Override
    public void bindEvent() {
        findViewById(R.id.tv_service_center).setOnClickListener(this);
        findViewById(R.id.iv_clear).setOnClickListener(this);
        et_search.setOnClickListener(new CheckClickListener(this));
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchWords = et_search.getText().toString().trim();
                    KeyboardUtils.hideSoftInput(ServiceListActivity.this);
                    getServiceList();
                }
                return false;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchWords = et_search.getText().toString().trim();
                iv_clear.setVisibility(StringUtils.isEmpty(searchWords) ? View.GONE : View.VISIBLE);
                getServiceList();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> getServiceList());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mServiceListAdapter != null) {
            handler.removeCallbacks(getServiceRunnable);
            handler.postDelayed(getServiceRunnable, 500);
        }
    }


    private void getServiceList() {
        OkHttpUtils.getClient().cancel(this);
        if (!(ActivityUtils.getTopActivity() instanceof ServiceListActivity)) {
            handler.removeCallbacks(getServiceRunnable);
            handler.postDelayed(getServiceRunnable, 30 * 1000);
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(searchWords)) {
            params.put("searchName", searchWords);
        }
        params.put("imToken", GlobalDataHelper.getInstance().getImToken());
        OkHttpUtils.postJson(this, ApiDomain.GET_SERVICE_LIST, params, new GsonResponseHandler<AppCsStaffListVo>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getServiceListSuccess(new AppCsStaffListVo());
            }

            @Override
            public void onSuccess(AppCsStaffListVo staffListVo) {
                getServiceListSuccess(staffListVo);
            }
        });
    }

    private void getServiceListSuccess(AppCsStaffListVo staffListVo) {
        if (staffListVo == null) {
            staffListVo = new AppCsStaffListVo();
        }
        handler.removeCallbacks(getServiceRunnable);
        handler.postDelayed(getServiceRunnable, 30 * 1000);

        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        mData.clear();
        if (StringUtils.isEmpty(searchWords) || staffListVo.getOnDutyCsList().size() != 0) {
            ServiceListItem entity1 = new ServiceListItem(staffListVo.getOnDutyCsList(), 1);
            // 模拟 默认第0个是展开的
            entity1.setExpanded(false);
            mData.add(entity1);
        }
        if (StringUtils.isEmpty(searchWords) || staffListVo.getBindingCsList().size() != 0) {
            ServiceListItem entity2 = new ServiceListItem(staffListVo.getBindingCsList(), 2);
            // 模拟 默认第0个是展开的
            entity2.setExpanded(false);
            mData.add(entity2);
        }
        if (StringUtils.isEmpty(searchWords) || staffListVo.getCsGroupList().size() != 0) {
            ServiceListItem entity3 = new ServiceListItem(staffListVo.getCsGroupList(), 3);
            // 模拟 默认第0个是展开的
            entity3.setExpanded(false);
            mData.add(entity3);
        }

        if (StringUtils.isEmpty(searchWords) || staffListVo.getCsLiveList().size() != 0) {
            ServiceListItem entity4 = new ServiceListItem(staffListVo.getCsLiveList(), 4);
            // 模拟 默认第0个是展开的
            entity4.setExpanded(false);
            mData.add(entity4);
        }

        mServiceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_service_center) {
            ActivityUtils.startActivity(ServiceHomeActivity.class);
        } else if (id == R.id.iv_clear) {
            et_search.setText("");
        }
    }

    @Subscribe
    public void receiveMsg(DeleteGroupEvent event) {
        getServiceList();
    }

    @Subscribe
    public void receiveMsg(SetGroupTopEvent event) {
        getServiceList();
    }

    @Subscribe
    public void receiveMsg(SetUserTopEvent sessionEndEvent) {
        getServiceList();
    }

    @Subscribe
    public void receiveMsg(LoginExitEvent sessionEndEvent) {
        for (Activity activity : ActivityUtils.getActivityList()) {
            if (activity instanceof ServiceListActivity) {
                activity.finish();
                break;
            }
            activity.finish();
        }
    }

    @Subscribe
    public void receiveMessage(Message message) {
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_START || message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_START) {
            if (GlobalDataHelper.getInstance().getImUserInfo().getId() != message.getFromUserId()) {
                if (!(ActivityUtils.getTopActivity() instanceof ServiceMediaActivity)) {
                    showCallDialog(message);
                }
            }
        }
//        else if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VIDEO_END || message.getType() == YLConstant.MessageType.MESSAGE_TYPE_MEDIA_VOICE_END) {
//            if (GlobalDataHelper.getInstance().getImUserInfo().getId() != message.getFromUserId()) {
//                if (mReceiveCallDialog != null && mReceiveCallDialog.isVisible()) {
//                    mReceiveCallDialog.dismiss();
//                }
//            }
//        }
        else if (message.mediaEnd()) {
            if (mReceiveCallDialog != null && mReceiveCallDialog.isVisible()) {
                mReceiveCallDialog.dismiss();
            }
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_CANCEL) {
            handler.removeCallbacks(getServiceRunnable);
            handler.postDelayed(getServiceRunnable, 500);
        }
        if (ActivityUtils.getTopActivity() instanceof ServiceListActivity) {
            if (message.isShow() && StringUtils.isEmpty(searchWords)) {
                //判断是否有群id，有则是群消息，反之为单聊消息
                if (message.getLiveId() != null && message.getLiveType() == 1) {
                    for (ServiceListItem serviceListItem : mServiceListAdapter.getData()) {
                        if (serviceListItem.getType() == 3) {
                            //判断是否是新的群组，新的群组就刷新列表，反之更新对应的最后一台消息
                            boolean isNew = true;
                            for (AppCsStaffVo csStaffVo : serviceListItem.getChildNode()) {
                                if (csStaffVo.getGroupId() == Long.valueOf(message.getGroupId())) {
                                    isNew = false;
                                    csStaffVo.setMessageVo(message);
                                    mServiceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                            if (isNew) {
                                getServiceList();
                            }
                            break;
                        }
                    }
                } else if (message.getLiveType() != null && message.getLiveType() != 2) {
                    boolean isNew = true;
                    for (ServiceListItem serviceListItem : mServiceListAdapter.getData()) {
                        if (serviceListItem.getType() == 1 || serviceListItem.getType() == 2) {
                            //判断是否是新的群组，新的群组就刷新列表，反之更新对应的最后一台消息
                            boolean isNewTemp = false;
                            for (AppCsStaffVo csStaffVo : serviceListItem.getChildNode()) {
                                if (message.getFromUserId() == GlobalDataHelper.getInstance().getImUserInfo().getId()) {
                                    if (!StringUtils.isEmpty(message.getToSourceId()) && message.getToSourceId().endsWith(String.valueOf(csStaffVo.getCsUserId()))) {
                                        isNew = false;
                                        isNewTemp = true;
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(message.getFromSourceId()) && message.getFromSourceId().endsWith(String.valueOf(csStaffVo.getCsUserId()))) {
                                        isNew = false;
                                        isNewTemp = true;
                                    }
                                }
                                if (isNewTemp) {
                                    csStaffVo.setMessageVo(message);
                                    rv_services.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mServiceListAdapter.notifyDataSetChanged();
                                        }
                                    }, 500);
                                    break;
                                }
                            }
                        }
                    }
                    if (isNew) {
                        getServiceList();
                    }
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (isShouldHideKeyboard(v, ev)) {
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


    private void showCallDialog(Message message) {
        if (mReceiveCallDialog != null && mReceiveCallDialog.isVisible()) {
            mReceiveCallDialog.dismiss();
        }
        mReceiveCallDialog = new ReceiveCallDialog();
        mReceiveCallDialog.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Arguments.DATA, message);
        mReceiveCallDialog.setArguments(bundle);
        mReceiveCallDialog.show(((PanActivity) ActivityUtils.getTopActivity()).getSupportFragmentManager(), "ReceiveCallDialog");
    }

    //退出时的时间
    private long mExitTime;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 1500) {
            ToastUtils.showShort("再按一次退出应用");
            mExitTime = System.currentTimeMillis();
        } else {
            if (ServiceMediaActivity.getServiceMediaActivity() != null) {
                ServiceMediaActivity.getServiceMediaActivity().closeSmallWindow();
            }
            ImClient.getInstance().close();
            ActivityUtils.goHome();
        }
//        super.onBackPressed();
    }
}
