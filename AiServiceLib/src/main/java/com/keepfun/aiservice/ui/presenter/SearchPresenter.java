package com.keepfun.aiservice.ui.presenter;


import com.keepfun.aiservice.ui.act.SearchActivity;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.base.PanPresenter;

/**
 * @author yang
 * @description
 * @date 2020/8/28 11:21 AM
 */
public class SearchPresenter extends PanPresenter<SearchActivity> {

    public void getHistory() {
        getV().setHistoryData(SpManager.getHistoryList());
    }

}
