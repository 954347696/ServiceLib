package com.keepfun.aiservice.entity.event;

/**
 * @author yang
 * @description
 * @date 2020/12/14 4:12 PM
 */
public class DeleteGroupEvent {

    private String groupId;

    public DeleteGroupEvent(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
