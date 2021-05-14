package com.keepfun.aiservice.entity.event;

/**
 * @author yang
 * @description
 * @date 2020/12/14 4:12 PM
 */
public class SetUserTopEvent {

    private String csUserId;
    private boolean isTop;

    public SetUserTopEvent(String groupId, boolean isTop) {
        this.csUserId = groupId;
        this.isTop = isTop;
    }

    public String getCsUserId() {
        return csUserId;
    }

    public void setCsUserId(String csUserId) {
        this.csUserId = csUserId;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}
