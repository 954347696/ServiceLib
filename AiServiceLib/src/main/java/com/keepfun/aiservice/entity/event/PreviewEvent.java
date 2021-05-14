package com.keepfun.aiservice.entity.event;

import org.webrtc.VideoTrack;

public class PreviewEvent {
    public long id;
    public String type;
    public String action;

    String mNotifyContent1;
    String mNotifyContent2;
    String mNotifyContent3;

    VideoTrack videoTrack;

    public PreviewEvent(String type) {
        this.type = type;
    }

    public PreviewEvent(String type, String action) {
        this.type = type;
        this.action = action;
    }

    public PreviewEvent(String type, String mContent1, String mContent2, VideoTrack videoTrack) {
        this.type = type;
        this.mNotifyContent1 = mContent1;
        this.mNotifyContent2 = mContent2;
        this.videoTrack = videoTrack;
    }

    public PreviewEvent(String type, String mContent1, String mContent2, String mContent3, VideoTrack videoTrack) {
        this.type = type;

        this.mNotifyContent1 = mContent1;
        this.mNotifyContent2 = mContent2;
        this.mNotifyContent3 = mContent3;
        this.videoTrack = videoTrack;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getmNotifyContent1() {
        return mNotifyContent1;
    }

    public String getmNotifyContent2() {
        return mNotifyContent2;
    }

    public String getmNotifyContent3() {
        return mNotifyContent3;
    }

    public VideoTrack getVideoTrack() {
        return videoTrack;
    }
}
