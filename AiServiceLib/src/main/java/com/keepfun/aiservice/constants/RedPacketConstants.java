package com.keepfun.aiservice.constants;

public interface RedPacketConstants {
    /**
     * 当前用户领取状态(0-待领取 1-已领取 2-限制领取)
     */
    int RED_PACKET_OBTAIN_WAITING = 0;
    int RED_PACKET_OBTAIN_DONE = 1;
    int RED_PACKET_OBTAIN_LIMIT = 2;

    /**
     * 红包状态(0-正常未领完 1-已领完 2-已过期)
     */
    int RED_PACKET_STATUS_NORMAL = 0;
    int RED_PACKET_STATUS_DONE = 1;
    int RED_PACKET_STATUS_EXPIRE = 2;

    int RED_PACKET_STATUS_HAS_OBTAIN = 3;
}
