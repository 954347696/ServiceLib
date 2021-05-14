package com.keepfun.aiservice.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/17 2:01 PM
 */
public class AppCsStaffListVo {

    /**
     * 专属客服列表 ,
     */
    private List<AppCsStaffVo> bindingCsList;
    /**
     * 客服群列表 ,
     */
    private List<AppCsStaffVo> csGroupList;
    /**
     * 客服直播间列表 ,
     */
    private List<AppCsStaffVo> csLiveList;
    /**
     * 值班客服列表
     */
    private List<AppCsStaffVo> onDutyCsList;

    public List<AppCsStaffVo> getBindingCsList() {
        if (bindingCsList == null) {
            bindingCsList = new ArrayList<>();
        }
        return bindingCsList;
    }

    public void setBindingCsList(List<AppCsStaffVo> bindingCsList) {
        this.bindingCsList = bindingCsList;
    }

    public List<AppCsStaffVo> getCsGroupList() {
        if (csGroupList == null) {
            csGroupList = new ArrayList<>();
        }
        return csGroupList;
    }

    public void setCsGroupList(List<AppCsStaffVo> csGroupList) {
        this.csGroupList = csGroupList;
    }

    public List<AppCsStaffVo> getCsLiveList() {
        if (csLiveList == null) {
            csLiveList = new ArrayList<>();
        }
        return csLiveList;
    }

    public void setCsLiveList(List<AppCsStaffVo> csLiveList) {
        this.csLiveList = csLiveList;
    }

    public List<AppCsStaffVo> getOnDutyCsList() {
        if (onDutyCsList == null) {
            onDutyCsList = new ArrayList<>();
        }
        return onDutyCsList;
    }

    public void setOnDutyCsList(List<AppCsStaffVo> onDutyCsList) {
        this.onDutyCsList = onDutyCsList;
    }

    @Override
    public String toString() {
        return "AppCsStaffListVo{" +
                "bindingCsList=" + bindingCsList +
                ", csGroupList=" + csGroupList +
                ", csLiveList=" + csLiveList +
                ", onDutyCsList=" + onDutyCsList +
                '}';
    }
}
