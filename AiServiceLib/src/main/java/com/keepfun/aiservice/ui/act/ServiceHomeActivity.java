package com.keepfun.aiservice.ui.act;

import android.view.View;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.ui.fragment.ServiceHomeFragment;
import com.keepfun.base.PanActivity;

/**
 * @author yang
 * @description
 * @date 2020/10/26 11:19 AM
 */
public class ServiceHomeActivity extends PanActivity {

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_home;
    }

    private ServiceHomeFragment fragment;

    @Override
    public void bindUI(View rootView) {

    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void initData() {
        if (fragment == null) {
            fragment = new ServiceHomeFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
    }
}
