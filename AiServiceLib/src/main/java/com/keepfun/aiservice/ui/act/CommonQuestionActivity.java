package com.keepfun.aiservice.ui.act;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.QuestionType;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.entity.event.QuestionDataEvent;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.adapter.QtPageAdapter;
import com.keepfun.aiservice.ui.fragment.CommonQuestionFragment;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.presenter.QuestionActivityPresenter;
import com.keepfun.aiservice.ui.view.QuestionTabView;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.magicindicator.MagicIndicator;
import com.keepfun.magicindicator.ViewPagerHelper;
import com.keepfun.magicindicator.buildins.UIUtil;
import com.keepfun.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.keepfun.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.keepfun.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.keepfun.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.keepfun.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;


/**
 * @author yang
 * @description
 * @date 2020/8/27 8:18 PM
 */
public class CommonQuestionActivity extends PanActivity<QuestionActivityPresenter> {

    private View layoutTip;
    private TextView tv_unread_count;
    MagicIndicator magicIndicator;
    ViewPager vp;

    private String keywords;
    private List<QuestionType> result;

    private ServiceEntity mServiceEntity;

    @Override
    public QuestionActivityPresenter newP() {
        return new QuestionActivityPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_common_question;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void bindUI(View rootView) {
        layoutTip = findViewById(R.id.layoutTip);
        tv_unread_count = findViewById(R.id.tv_unread_count);
        magicIndicator = findViewById(R.id.tab_layout);
        vp = findViewById(R.id.vp);
    }

    @Override
    public void bindEvent() {
        layoutTip.setOnClickListener(new CheckClickListener(v -> {
            if (mServiceEntity != null) {
                Bundle bundle = new Bundle();
                bundle.putInt(Arguments.DATA, mServiceEntity.getConversationType());
                bundle.putSerializable(Arguments.DATA1, mServiceEntity);
                ActivityUtils.startActivity(bundle, ServiceOnlineActivity.class);
            }
        }));
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            keywords = bundle.getString(Arguments.KEYWORDS);
        }
        getP().getTabLabels();
        getIngSession();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (layoutTip != null) {
            getIngSession();
        }
    }


    public void getIngSession() {
        OkHttpUtils.postJson(ApiDomain.GET_ING_SESSION + "?sourceType=0", new GsonResponseHandler<ServiceEntity>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getIngSessionSuccess(null);
            }

            @Override
            public void onSuccess(ServiceEntity serviceEntity) {
                getIngSessionSuccess(serviceEntity);
            }
        });
    }

    public void getIngSessionSuccess(ServiceEntity entity) {
        this.mServiceEntity = entity;
        layoutTip.setVisibility(entity != null && entity.getUnread() > 0 ? View.VISIBLE : View.GONE);
        if (entity != null) {
            tv_unread_count.setText(String.valueOf(entity.getUnread()));
        }
    }


    public void getLabelsSuccess(List<QuestionType> result) {
        this.result = result;
        if (result == null || result.size() == 0) {
            return;
        }
        CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return result.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                QuestionTabView tabTitleView = new QuestionTabView(context);
                tabTitleView.setTabText(result.get(index).getName());
                tabTitleView.setNormalColor(getResources().getColor(R.color.textColor));
                tabTitleView.setSelectedColor(getResources().getColor(R.color.color_55));
                tabTitleView.setOnClickListener((View v) -> {
                    vp.setCurrentItem(index, false);
                });
                return tabTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setYOffset(UIUtil.dip2px(context, 0));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(SizeUtils.dp2px(3));
                indicator.setLineWidth(SizeUtils.dp2px(24));
                indicator.setRoundRadius(0f);
                indicator.setColors(getResources().getColor(R.color.color_FFE610));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, vp);
        QtPageAdapter pageAdapter = new QtPageAdapter(getSupportFragmentManager(), getFragments(result));
        vp.setAdapter(pageAdapter);
        vp.setOffscreenPageLimit(result.size());
    }


    private List<Fragment> getFragments(List<QuestionType> result) {
        List<Fragment> fragments = new ArrayList<>();
        for (QuestionType questionType : result) {
            fragments.add(CommonQuestionFragment.getInstance(questionType.getCode(), keywords));
        }
        return fragments;
    }

    private int lastDataIndex = Integer.MAX_VALUE;

    @Subscribe
    public void switchVp(QuestionDataEvent questionDataEvent) {
        if (questionDataEvent.hasData) {
            int currentIndex = vp.getCurrentItem();
            if (result.get(currentIndex).getCode().equals(questionDataEvent.title)) {
                return;
            }
            int index = 0;
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).getCode().equals(questionDataEvent.title)) {
                    index = i;
                    break;
                }
            }
            if (index < lastDataIndex) {
                vp.setCurrentItem(index);
                lastDataIndex = index;
            }
        }
    }

    @Subscribe
    public void receiveMessage(Message message) {
        if (mServiceEntity == null || !mServiceEntity.getGroupId().equals(message.getGroupId())) {
            return;
        }
        if (message.getType() == YLConstant.MessageType.MESSAGE_TYPE_SESSION_END) {
            mServiceEntity.setUnread(0);
        } else {
            mServiceEntity.setUnread(mServiceEntity.getUnread() + 1);
        }
        getIngSessionSuccess(mServiceEntity);
    }


}
