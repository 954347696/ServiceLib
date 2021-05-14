package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/12/10 2:31 PM
 */
public class CreateSessionResp {
    /**
     * 会话id
     */
    private String groupId;
    /**
     * 客户等待人数, -1 不需要等待
     */
    private int waitNo;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getWaitNo() {
        return waitNo;
    }

    public void setWaitNo(int waitNo) {
        this.waitNo = waitNo;
    }

    @Override
    public String toString() {
        return "CreateSessionResp{" +
                "groupId='" + groupId + '\'' +
                ", waitNo=" + waitNo +
                '}';
    }
}
