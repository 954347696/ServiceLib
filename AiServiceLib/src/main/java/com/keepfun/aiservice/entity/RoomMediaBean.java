package com.keepfun.aiservice.entity;

import java.io.Serializable;

/**
 * @author yang
 * @description
 * @date 2020/9/10 5:52 PM
 */
public class RoomMediaBean implements Serializable {
    private long roomId;
    private long selfId;
    private long otherId;
    private String otherAvatar;
    private String otherName;
    private String groupId;
    /**
     * 1 语音 2 视频
     */
    private int mediaType;
    private boolean isFrom = false;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getSelfId() {
        return selfId;
    }

    public void setSelfId(long selfId) {
        this.selfId = selfId;
    }

    public long getOtherId() {
        return otherId;
    }

    public void setOtherId(long otherId) {
        this.otherId = otherId;
    }

    public String getOtherAvatar() {
        return otherAvatar;
    }

    public void setOtherAvatar(String otherAvatar) {
        this.otherAvatar = otherAvatar;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 1 语音 2 视频
     */
    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isFrom() {
        return isFrom;
    }

    public void setFrom(boolean from) {
        isFrom = from;
    }
}
