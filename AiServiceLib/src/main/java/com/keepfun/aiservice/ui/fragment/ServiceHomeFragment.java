package com.keepfun.aiservice.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.Question;
import com.keepfun.aiservice.entity.RoomMediaBean;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.entity.ServiceUser;
import com.keepfun.aiservice.entity.event.LoginExitEvent;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.ui.act.CommonQuestionActivity;
import com.keepfun.aiservice.ui.act.FeedbackActivity;
import com.keepfun.aiservice.ui.act.QuestionnaireActivity;
import com.keepfun.aiservice.ui.act.SearchActivity;
import com.keepfun.aiservice.ui.act.ServiceMediaActivity;
import com.keepfun.aiservice.ui.act.ServiceOnlineActivity;
import com.keepfun.aiservice.ui.act.SkinListActivity;
import com.keepfun.aiservice.ui.adapter.GuessQuestionAdapter;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.ServicePresenter;
import com.keepfun.aiservice.ui.view.HomeTabView;
import com.keepfun.aiservice.ui.view.ServiceTitleView;
import com.keepfun.base.PanFragment;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.AppUtils;
import com.keepfun.blankj.util.ClickUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @author yang
 * @description
 * @date 2020/10/26 11:26 AM
 */
public class ServiceHomeFragment extends PanFragment<ServicePresenter> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.service_fragment_main;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    @Override
    public ServicePresenter newP() {
        return new ServicePresenter();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    ServiceTitleView titleView;
    LinearLayout layout_menu;
    LinearLayout layout_below;
    HomeTabView htv_tab_vip;
    HomeTabView htv_tab_video;
    HomeTabView htv_tab_voice;
    HomeTabView htv_tab_3;
    View view_temp1;
    View view_temp2;
    View view_temp3;
    RecyclerView recyclerView;
    ImageView iv_guess_refresh;
    TextView tv_version;

    private ServiceEntity mServiceEntity;
    public boolean canGo = true;

    @Override
    public void bindUI(View rootView) {
        titleView = rootView.findViewById(R.id.titleView);
        layout_menu = rootView.findViewById(R.id.layout_menu);
        layout_below = rootView.findViewById(R.id.layout_below);
        htv_tab_vip = rootView.findViewById(R.id.htv_tab_vip);
        htv_tab_video = rootView.findViewById(R.id.htv_tab_video);
        htv_tab_voice = rootView.findViewById(R.id.htv_tab_voice);
        htv_tab_3 = rootView.findViewById(R.id.htv_tab_voice);
        view_temp1 = rootView.findViewById(R.id.view_temp1);
        view_temp2 = rootView.findViewById(R.id.view_temp2);
        view_temp3 = rootView.findViewById(R.id.view_temp3);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        iv_guess_refresh = rootView.findViewById(R.id.iv_guess_refresh);
        tv_version = rootView.findViewById(R.id.tv_version);
    }

    private List<Question> questions;
    private GuessQuestionAdapter mAdapter;
    private PopupWindow menuPop;
    private static final int REQUEST_FOR_QS_PAPER = 0x11;

    @Override
    public void initData() {
        ServiceUser serviceUser = GlobalDataHelper.getInstance().getUserInfo();
        if (serviceUser != null) {
            layout_below.setVisibility(serviceUser.getWithVipLine() == 0 && serviceUser.getWithVipVideo() == 0 && serviceUser.getWithVoiceCs() == 0 ? View.GONE : View.VISIBLE);
            htv_tab_vip.setVisibility(serviceUser.getWithVipLine() == 1 ? View.VISIBLE : View.GONE);
            view_temp1.setVisibility(serviceUser.getWithVipLine() == 1 ? View.GONE : View.INVISIBLE);
            htv_tab_video.setVisibility(serviceUser.getWithVipVideo() == 1 ? View.VISIBLE : View.GONE);
            view_temp2.setVisibility(serviceUser.getWithVipVideo() == 1 ? View.GONE : View.INVISIBLE);
            htv_tab_voice.setVisibility(serviceUser.getWithVoiceCs() == 1 ? View.VISIBLE : View.GONE);
            view_temp3.setVisibility(serviceUser.getWithVoiceCs() == 1 ? View.GONE : View.INVISIBLE);
        }
        tv_version.setText("当前版本：" + AppUtils.getAppVersionName());
        initRecyclerView();


        getP().getIngSession();
        getP().getGuessQuestions();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded() && isVisible()) {
            htv_tab_3.postDelayed(() -> getP().getIngSession(), 500);
        }
        canGo = true;
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(getContext())
                .colorResId(R.color.color_EE)
                .size(SizeUtils.dp2px(0.5f))
                .margin(SizeUtils.dp2px(15))
                .showLastDivider()
                .build();
        recyclerView.addItemDecoration(decor);
        if (questions == null) {
            questions = new ArrayList<>();
        }
        mAdapter = new GuessQuestionAdapter(questions);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.service_home_view_empty, null);
        mAdapter.setEmptyView(emptyView);
        recyclerView.setAdapter(mAdapter);
    }

    @MainThread
    public void getQuestionSuccess(List<Question> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
        iv_guess_refresh.post(() -> {
            iv_guess_refresh.clearAnimation();
            mAdapter.notifyDataSetChanged();
        });
    }

    public void getIngSessionSuccess(ServiceEntity entity) {
        this.mServiceEntity = entity;
        htv_tab_3.setUnReadCount(0);
        htv_tab_vip.setUnReadCount(0);
        htv_tab_video.setUnReadCount(0);
        htv_tab_voice.setUnReadCount(0);
        if (mServiceEntity != null && mServiceEntity.getUnread() > 0) {
            htv_tab_vip.setUnReadCount(mServiceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VIP ? mServiceEntity.getUnread() : 0);
            htv_tab_video.setUnReadCount(mServiceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VIDEO ? mServiceEntity.getUnread() : 0);
            htv_tab_voice.setUnReadCount(mServiceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_VOICE ? mServiceEntity.getUnread() : 0);
            htv_tab_3.setUnReadCount(mServiceEntity.getConversationType() == YLConstant.ChatType.CHAT_TYPE_AI ? mServiceEntity.getUnread() : 0);
        }
    }

    @Override
    public void bindEvent() {
        titleView.setRightListener(v -> showPop(v));
        titleView.setLeftListener(v -> getActivity().finish());
        findViewById(R.id.tv_search).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.htv_tab_questions).setOnClickListener(new CheckClickListener(this));
        View htv_tab_3 = findViewById(R.id.htv_tab_3);
        htv_tab_3.setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_guess_refresh).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_more).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.htv_tab_feedback).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.htv_tab_video).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.htv_tab_vip).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.htv_tab_voice).setOnClickListener(new CheckClickListener(this));
    }

    @Override
    public void onClick(View view) {
        if (ClickUtils.isFastDoubleClick()) {
            return;
        }
        int id = view.getId();
        if (id == R.id.htv_tab_questions || id == R.id.tv_more) {
            ActivityUtils.startActivity(CommonQuestionActivity.class);
        } else if (id == R.id.htv_tab_video) {
            showLoading();
            new Thread(() -> getP().getHistorySession(YLConstant.ChatType.CHAT_TYPE_VIDEO)).start();
        } else if (id == R.id.htv_tab_voice) {
            showLoading();
            new Thread(() -> getP().getHistorySession(YLConstant.ChatType.CHAT_TYPE_VOICE)).start();
        } else if (id == R.id.htv_tab_vip) {
            showLoading();
            new Thread(() -> getP().getHistorySession(YLConstant.ChatType.CHAT_TYPE_VIP)).start();
        } else if (id == R.id.htv_tab_3) {
            showLoading();
            new Thread(() -> getP().getHistorySession(YLConstant.ChatType.CHAT_TYPE_AI)).start();
        } else if (id == R.id.tv_search) {
            ActivityUtils.startActivity(SearchActivity.class);
        } else if (id == R.id.iv_guess_refresh) {
            Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);
            iv_guess_refresh.startAnimation(operatingAnim);
            getP().getGuessQuestions();
        } else if (id == R.id.htv_tab_feedback) {
            ActivityUtils.startActivity(FeedbackActivity.class);
        }
    }

    private void transfer2Human(int type) {
        getP().checkQuestionnaireSwitch(type, qsPaperSetBean -> {
            dismissLoading();
            Bundle bundle = new Bundle();
            bundle.putInt(Arguments.DATA, type);
            if (qsPaperSetBean.getNeedPaper() == 1) {
                bundle.putBoolean(Arguments.DATA1, qsPaperSetBean.getNeedCode() == 1);
                ActivityUtils.startActivityForResult(bundle, this, QuestionnaireActivity.class, REQUEST_FOR_QS_PAPER);
            } else {
                ActivityUtils.startActivity(bundle, ServiceOnlineActivity.class);
            }

        });
    }

    public void getServiceResult(int type, ServiceEntity serviceEntity) {
        LogUtils.e("getServiceResult serviceEntity : " + serviceEntity);
        if (serviceEntity != null) {
            dismissLoading();
            Bundle bundle = new Bundle();
            bundle.putInt(Arguments.DATA, type);
            bundle.putSerializable(Arguments.DATA1, serviceEntity);
            ActivityUtils.startActivity(bundle, ServiceOnlineActivity.class);
        } else {
            getP().getService(type);
        }
    }

    public void checkServiceExist(int type) {
        if (type == YLConstant.ChatType.CHAT_TYPE_AI) {
            dismissLoading();
            Bundle bundle = new Bundle();
            bundle.putInt(Arguments.DATA, YLConstant.ChatType.CHAT_TYPE_AI);
            ActivityUtils.startActivity(bundle, ServiceOnlineActivity.class);
        } else {
            transfer2Human(type);
        }
    }

    public void checkServiceNoExist() {
        canGo= true;
        dismissLoading();
    }

    private void showPop(View v) {
        if (menuPop == null) {
            View popView = LayoutInflater.from(getContext()).inflate(R.layout.service_menu_home, null);
            menuPop = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            popView.findViewById(R.id.tv_change_skin).setOnClickListener(v12 -> {
                if (ClickUtils.isFastDoubleClick()) {
                    return;
                }

                ActivityUtils.startActivity(SkinListActivity.class);
                menuPop.dismiss();
            });
            popView.findViewById(R.id.tv_exit).setOnClickListener(v1 -> {
                if (ClickUtils.isFastDoubleClick()) {
                    return;
                }
                menuPop.dismiss();

                ServiceMediaActivity.closeSmallWindow();
                EventBus.getDefault().post(new LoginExitEvent());
            });
        }
        //设置popwindow显示位置
        menuPop.showAsDropDown(v);
        //获取popwindow焦点
        menuPop.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        menuPop.setOutsideTouchable(true);
        final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        //代表透明程度，范围为0 - 1.0f
        lp.alpha = 0.7f;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
        menuPop.setOnDismissListener(() -> {
            lp.alpha = 1.0f;
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getActivity().getWindow().setAttributes(lp);
        });
        menuPop.update();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == REQUEST_FOR_QS_PAPER) {
            }
            return;
        }
        if (requestCode == REQUEST_FOR_QS_PAPER && data != null) {
            int chatType = data.getIntExtra(Arguments.DATA, YLConstant.ChatType.CHAT_TYPE_VIP);
            Bundle bundle = new Bundle();
            bundle.putInt(Arguments.DATA, chatType);
            ActivityUtils.startActivity(bundle, ServiceOnlineActivity.class);
        }
    }

    @Subscribe
    public void receiveMessage(Message message) {
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SERVICE_IN) {
            getP().getIngSession();
            return;
        }
        if (mServiceEntity == null || !mServiceEntity.getGroupId().equals(message.getGroupId())) {
            return;
        }
        mServiceEntity.setUnread(message.getUnRead());
        mServiceEntity.setUnread(mServiceEntity.getUnread());
        getIngSessionSuccess(mServiceEntity);
    }

}
