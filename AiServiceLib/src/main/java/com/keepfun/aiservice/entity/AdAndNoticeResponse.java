package com.keepfun.aiservice.entity;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/9/11 1:53 PM
 */
public class AdAndNoticeResponse {

    /**
     * 宣传图列表
     */
    private List<AppAdNoticeBean> adPicList;
    /**
     * 公告列表
     */
    private List<AppAdNoticeBean> noticeList;

    public List<AppAdNoticeBean> getAdPicList() {
        return adPicList;
    }

    public void setAdPicList(List<AppAdNoticeBean> adPicList) {
        this.adPicList = adPicList;
    }

    public List<AppAdNoticeBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<AppAdNoticeBean> noticeList) {
        this.noticeList = noticeList;
    }

    @Override
    public String toString() {
        return "AdAndNoticeResponse{" +
                "adPicList=" + adPicList +
                ", noticeList=" + noticeList +
                '}';
    }
}
