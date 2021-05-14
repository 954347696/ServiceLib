package com.keepfun.aiservice.constants;

/**
 * @author yang
 * @description
 * @date 2020/10/22 9:41 AM
 */
public interface ApiDomain {
    /**
     * 获取accessToken
     */
    String GET_ACCESS_TOKEN = "/api/auth/v1/getAccessToken";
    /**
     * 登记用户信息
     */
    String REGISTER_INFO = "/api/user/v1/regInfo";
    /**
     * 客户端(移动，h5)用户登录Im
     */
    String IM_LOGIN = "/api/custom/im/login";
    /**
     * 猜你想问列表
     */
    String GUESS_ASK_LIST = "/api/center/commQuestion/v1/guessAskList";
    /**
     * 常见问题类型
     */
    String QUESTION_TYPE_LIST = "/api/center/commQuestion/v1/typeList";
    /**
     * 常见问题
     */
    String COMM_QUESTION_LIST = "/api/center/commQuestion/v1/list";
    /**
     * 问题帮助结果反馈
     */
    String QUESTION_HELP_FEEDBACK = "/api/center/commQuestion/v1/helpFeedback";
    /**
     * 关联问题
     */
    String RELATION_QUESTION_LIST = "/api/center/commQuestion/v1/relationList";
    /**
     * 反馈列表
     */
    String FEEDBACK_LIST = "/api/feedback/list";
    /**
     * 详情
     */
    String FEEDBACK_DETAIL = "/api/feedback/info/";
    /**
     * 反馈类型
     */
    String FEEDBACK_LABELS = "/api/feedback/type/list/feedback";
    /**
     * 反馈
     */
    String FEEDBACK_SAVE = "/api/feedback/save";

    /**
     * 反馈图片上传接口
     */
    String FEEDBACK_UPLOAD_IMAGE = "/api/feedback/upload/image";
    /**
     * 反馈已解决
     */
    String FEEDBACK_FINISH = "/api/feedback/update/finish";

    /**
     * 检查询前问卷设置
     */
    String QUESTION_PAPER_CHECK = "/api/center/questionPaper/v1/checkSetInfo";
    /**
     * 获取询前问卷内容
     */
    String QUESTION_PAPER_CONTENT = "/api/center/questionPaper/v1/content";
    /**
     * 提交询前问卷
     */
    String QUESTION_PAPER_COMMIT = "/api/center/questionPaper/v1/submit";

    /**
     * 获取人工客服
     */
    String ACHIEVE_SERVICE = "/api/artificial/service";
    /**
     * 分配默认机器人
     */
    String ACHIEVE_AI_SERVICE = "/api/system/chatbot/robot";
    /**
     * 转人工
     */
    String EVALUATE_MANPOWER = "/api/evaluate/manpower";
    /**
     * 分配默认机器人
     */
    String HISTORY_SESSION = "/api/artificial/service/session";
    /**
     * 创建会话
     */
    String CREATE_SESSION = "/api/create/chat";
    /**
     * 获取欢迎导语
     */
    String WELCOME_CONTENT = "/api/center/welcomeIntro/v1/content";
    /**
     * App端3分钟无聊天自动结束会话
     */
    String FINISH_CHAT = "/api/finish/chat";
    /**
     * 根据给定的groupID查询历史对话 分页
     */
    String ONLINE_HISTORY = "/api/history/dialogue/page";
    /**
     * 客服评价
     */
    String EVALUATE_SAVE = "/api/evaluate/save";
    /**
     * 公告和宣传图列表
     */
    String NOTICE_AND_AD_PIC_LIST = "/api/center/adNotice/v1/noticeAndAdPicList";
    /**
     * 营销活动 用户操作
     */
    String ACTIVITY_ACTION = "/api/market/activity/action";
    /**
     * 图片上传
     */
    String UPLOAD_IMAGE = "/api/upload/image";
    String UPLOAD_FILE = "/api/upload/file";

    /**
     * 皮肤列表
     */
    String SKIN_LIST = "/api/skin/app/list";
    /**
     * 获取图形验证码
     */
    String GET_IMAGE_CODE = "/api/common/v1/imgCodeMaker";

    /**
     * 群详情
     */
    String GET_GROUP_INFO = "/app/group/info";
    /**
     * 群成员列表
     */
    String GET_GROUP_MEMBER_LIST = "/app/group/member/list";
    /**
     * 群置顶/取消置顶
     */
    String SET_GROUP_TOP = "/app/group/top";
    /**
     * 退群
     */
    String DELETE_GROUP = "/app/group/delete";
    /**
     * 获取聊天记录
     */
    String GROUP_MESSAGE_LIST = "/app/group/message/list";

    /**
     * 拆红包
     */
    String REDPACKET_OPEN = "/api/red/v1/redOpen";

    /**
     * 红包详情
     */
    String REDPACKET_DETAIL = "/api/red/v1/redDrawDetail";

    /**
     * 红包检查
     */
    String REDPACKET_CHECK = "/api/red/v1/redCheck";

    /**
     * 打赏
     */
    String REDPACKET_REWARD = "/api/red/v1/reward";
    /**
     * 获取用户账户信息
     */
    String GET_ACCOUNT_INFO = "/api/user/v1/getAccountInfo";
    /**
     * 客服列表信息
     */
    String GET_SERVICE_LIST = "/api/customer/my/csStaff/list";
    /**
     * 直播间详情
     */
    String GET_LIVE_INFO = "/api/live/info/detail/";
    /**
     * 客服聊天置顶/取消
     */
    String SET_CSUSER_TOP = "/api/customer/csStaff/chat/top";

    /**
     * 观看直播
     */
    String LIVE_WATCH_ADD = "/api/live/watch/add";
    /**
     * 结束观看
     */
    String LIVE_WATCH_EXIT = "/api/live/watch/exit";
    /**
     * 心跳检测
     */
    String LIVE_WATCH_HEARTBEAT = "/api/live/watch/heart/beat";

    String GET_CS_STAFF_INFO = "/api/customer/csStaff/info";
    String GET_UNREAD_MESSAGES = "/api/chat/unread/message/list";
    /**
     * 获取用户禁言状态
     */
    String GET_GROUP_SILENT_STATUS = "/app/group/status";
    /**
     * 观看直播检查
     */
    String CHECK_WATCH_STATUS = "/api/live/watch/check";
    /**
     * 获取未结束会话信息(带未读消息数)
     */
    String GET_ING_SESSION = "/api/artificial/ing/session";
    /**
     * 根据会话id获取会话信息
     */
    String GET_SESSION_BY_ID = "/api/artificial/session/info";
    /**
     * 接通/拒绝/挂断通知im
     */
    String NOTIFY_MEDIA_RESULT = "/api/notice/im";
}
