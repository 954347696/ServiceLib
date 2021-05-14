package com.keepfun.aiservice.entity.event;

/**
 * @author yang
 * @description
 * @date 2020/12/14 4:12 PM
 */
public class SetGroupTopEvent {

    private String groupId;
    private boolean isTop;

    public SetGroupTopEvent(String groupId, boolean isTop) {
        this.groupId = groupId;
        this.isTop = isTop;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}
