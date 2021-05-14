package com.keepfun.aiservice.entity;

import com.keepfun.blankj.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/12/14 11:02 AM
 */
public class GroupDetail {
    /**
     * 群名 ,
     */
    private String groupName;
    /**
     * 是否置顶 ,
     */
    private Boolean isTop = false;

    /**
     * 机器人主键 ,
     */
    private long robotId;
    /**
     * 机器人名称 ,
     */
    private String robotName;
    /**
     * 群公告 ,
     */
    private String notice;
    /**
     * 公告时间 ,
     */
    private String noticeTime;
    /**
     * 群成员
     */
    private List<GroupMember> vos;
    private int size;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getRobotId() {
        return robotId;
    }

    public void setRobotId(long robotId) {
        this.robotId = robotId;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public Boolean getTop() {
        return isTop;
    }

    public void setTop(Boolean top) {
        isTop = top;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public List<GroupMember> getVos() {
        if (vos == null) {
            vos = new ArrayList<>();
        }
        return vos;
    }

    public void setVos(List<GroupMember> vos) {
        this.vos = vos;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "GroupDetail{" +
                "groupName='" + groupName + '\'' +
                ", isTop=" + isTop +
                ", notice='" + notice + '\'' +
                ", noticeTime='" + noticeTime + '\'' +
                ", vos=" + vos +
                '}';
    }
}
