package com.keepfun.aiservice.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.keepfun.adapter.base.BaseMultiItemQuickAdapter;
import com.keepfun.adapter.base.BaseQuickAdapter;
import com.keepfun.adapter.base.module.UpFetchModule;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.constants.RedPacketConstants;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.ActiveBean;
import com.keepfun.aiservice.entity.AiQuestion;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.entity.ReceiveRedPacket;
import com.keepfun.aiservice.entity.RedDrawData;
import com.keepfun.aiservice.entity.RedPacketStatusData;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.manager.RichTextHelper;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.RedPacketsActivity;
import com.keepfun.aiservice.ui.act.ServiceEvaluationActivity;
import com.keepfun.aiservice.ui.act.ServiceOnlineActivity;
import com.keepfun.aiservice.ui.act.VideoPreviewActivity;
import com.keepfun.aiservice.ui.dialog.OpenRedPacketsDialog;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.ui.view.ExpandableTextView;
import com.keepfun.aiservice.utils.GlideRoundTransform;
import com.keepfun.aiservice.utils.HtmlImageGetter;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.aiservice.utils.audio.MediaManager;
import com.keepfun.base.WebActivity;
import com.keepfun.blankj.constant.TimeConstants;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.CollectionUtils;
import com.keepfun.blankj.util.ColorUtils;
import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ScreenUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.SpanUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.TimeUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.blankj.util.Utils;
import com.keepfun.glide.GlideImageView;
import com.keepfun.imageselector.PreviewImageActivity;
import com.zzhoujay.richtext.CacheType;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.RichTextConfig;
import com.zzhoujay.richtext.callback.Callback;
import com.zzhoujay.richtext.callback.ImageFixCallback;
import com.zzhoujay.richtext.callback.ImageGetter;
import com.zzhoujay.richtext.callback.ImageLoadNotify;
import com.zzhoujay.richtext.callback.OnImageLongClickListener;
import com.zzhoujay.richtext.ig.DefaultImageDownloader;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


/**
 * @author yang
 * @description
 * @date 2020/9/8 4:16 PM
 */
public class ServiceChatAdapter2 extends BaseQuickAdapter<Message, BaseViewHolder> implements UpFetchModule {
    private Context context;

    public ServiceChatAdapter2(Context context, List<Message> messages) {
        super(R.layout.service_item_chat_list, messages);
        this.context = context;
    }

    private long mUserId;
    private OnOperationClickListener onOperationClickListener;
    private AnimationDrawable voiceAnim;

    public void setUserId(long mUserId) {
        this.mUserId = mUserId;
    }

    public long getUserId() {
        return mUserId;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, Message message) {
        int itemType = message.getItemType();
        if (message.hideTime()) {
            helper.setGone(R.id.tv_time, true);
        } else {
            checkTime(helper, message);
        }
        helper.setGone(R.id.tv_tip, true);
        helper.setGone(R.id.layout_end, true);
        helper.setGone(R.id.layout_other_content, true);
        helper.setGone(R.id.layout_self_content, true);

        if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_TIP) {
            LogUtils.e("message.getContentStr() : " + message.getContentStr());
            helper.setGone(R.id.tv_tip, false);
            helper.setText(R.id.tv_tip, message.getContentStr());
            helper.setBackgroundResource(R.id.tv_tip, R.drawable.service_bg_14_radius_white);
        } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_SYSTEM) {
            LogUtils.e("message.getContentStr() : " + message.getContentStr());
            helper.setGone(R.id.tv_tip, false);
            helper.setText(R.id.tv_tip, message.getContentStr());
            helper.setBackgroundResource(R.id.tv_tip, R.drawable.service_bg_transport);
        } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_END) {
            helper.setGone(R.id.layout_end, false);
            helper.itemView.setOnClickListener(new CheckClickListener(v -> {
                if (StringUtils.isEmpty(message.getGroupId())) {
                    ToastUtils.showShort("该会话评价失效");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(Arguments.DATA, message);
                bundle.putInt(Arguments.POSITION, getItemPosition(message));
                ActivityUtils.startActivityForResult(bundle, (Activity) getContext(), ServiceEvaluationActivity.class, ServiceOnlineActivity.REQUEST_FOR_EVALUATION);
            }));
        } else if (mUserId == message.getFromUserId()
                && itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_AI_2_ARTIFICIAL
                && itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_RED_PACKET
                && itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_ACTIVITY) {
            helper.setGone(R.id.layout_self_content, false);

            helper.setGone(R.id.tv_content_self, true);
            helper.setGone(R.id.layout_pic_self, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_PIC);
            helper.setGone(R.id.layout_video_self, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VIDEO);
            helper.setGone(R.id.layout_voice_self, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VOICE);
            helper.setGone(R.id.layout_media_self, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_MEDIA);

            if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_PIC) {
                GlideImageView iv_pic_self = helper.getView(R.id.iv_pic_self);
                View layout_load_self_pic = helper.getView(R.id.layout_load_self_pic);
                iv_pic_self.centerCrop().error(R.mipmap.service_ic_load_failed).apply(new RequestOptions().transform(new GlideRoundTransform(getContext(), 10))).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .load(message.getContent(), R.color.placeholder, (isComplete, percentage, bytesRead, totalBytes) -> {
                            LogUtils.e("load percentage: " + percentage + " totalBytes: " + totalBytes + " bytesRead: " + bytesRead);
                            if (isComplete) {
                                layout_load_self_pic.setVisibility(View.GONE);
                            } else {
                                layout_load_self_pic.setVisibility(View.VISIBLE);
                            }
                        });
                iv_pic_self.setOnClickListener(v -> {
                    ArrayList<String> images = new ArrayList<>();
                    images.add(message.getContent());
                    PreviewImageActivity.openActivity(((Activity) getContext()), images, 0);
                });
            } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VIDEO) {
                GlideImageView iv_video_self = helper.getView(R.id.iv_video_self);
                View layout_load_self_video = helper.getView(R.id.layout_load_self_video);
                iv_video_self.centerCrop().error(R.mipmap.service_ic_load_failed).apply(new RequestOptions().transform(new GlideRoundTransform(getContext(), 10))).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .load(message.getContent(), R.color.placeholder, (isComplete, percentage, bytesRead, totalBytes) -> {
                            LogUtils.e("load percentage: " + percentage + " totalBytes: " + totalBytes + " bytesRead: " + bytesRead);
                            if (isComplete) {
                                layout_load_self_video.setVisibility(View.GONE);
                            } else {
                                layout_load_self_video.setVisibility(View.VISIBLE);
                            }
                        });
                helper.setText(R.id.tv_duration_self, TimeUtils.millis2FitTimeSpan(message.getTimeDuration()));
                iv_video_self.setOnClickListener(v -> {
                    VideoPreviewActivity.start(message.getContent());
                });
            } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VOICE) {
                if (message.getTimeDuration() != 0) {
                    helper.setText(R.id.tv_self_voice_time, message.getTimeDuration() + "s");
                } else {
                    helper.setText(R.id.tv_self_voice_time, "已取消");
                }
                ImageView iv_other_voice = helper.getView(R.id.iv_self_voice);
                helper.itemView.setOnClickListener(new CheckClickListener(v -> {
                    if (voiceAnim != null && voiceAnim.isRunning()) {
                        voiceAnim.stop();
                        MediaManager.release();
                    }
                    voiceAnim = (AnimationDrawable) iv_other_voice.getDrawable();
                    playVoice(message.getContent());
                }));
            } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_MEDIA) {
                if (message.getTimeDuration() != 0) {
                    helper.setText(R.id.tv_self_media_time, "时长：" + formatTime(message.getTimeDuration()));
                } else {
                    helper.setText(R.id.tv_self_media_time, "已取消");
                }
            } else {
                helper.setGone(R.id.tv_content_self, false);
                ExpandableTextView tv_content_self = helper.getView(R.id.tv_content_self);
                if (message.getStatus() == 3) {
                    tv_content_self.setText("信息含违规内容，已被管理员撤回", getItemPosition(message));
                } else {
                    tv_content_self.setText(message.getContent(), getItemPosition(message));
                }
            }
        } else {
            helper.setGone(R.id.layout_other_content, false);
            ImageView iv_avatar = helper.getView(R.id.iv_avatar);
            Glide.with(getContext()).load(message.getFromUserAvatar()).placeholder(R.mipmap.service_bg_chat_default).apply(new RequestOptions().circleCrop()).into(iv_avatar);
            helper.setText(R.id.tv_nickname, message.getFromUserName());

            helper.setGone(R.id.tv_content_other, true);
            helper.setGone(R.id.layout_pic_other, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_PIC);
            helper.setGone(R.id.layout_video, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VIDEO);
            helper.setGone(R.id.layout_voice_other, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VOICE);
            helper.setGone(R.id.layout_media_other, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_MEDIA);
            helper.setGone(R.id.layout_activity, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_ACTIVITY);
            helper.setGone(R.id.layout_transfer, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_AI_2_ARTIFICIAL);
            helper.setGone(R.id.layout_packet, itemType != YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_RED_PACKET);

            if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_PIC) {
                GlideImageView iv_pic_other = helper.getView(R.id.iv_pic_other);
                View layout_load_other_pic = helper.getView(R.id.layout_load_other_pic);
                iv_pic_other.centerCrop().error(R.mipmap.service_ic_load_failed).apply(new RequestOptions().transform(new GlideRoundTransform(getContext(), 10))).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .load(message.getContent(), R.color.placeholder, (isComplete, percentage, bytesRead, totalBytes) -> {
                            LogUtils.e("load percentage: " + percentage + " totalBytes: " + totalBytes + " bytesRead: " + bytesRead);
                            if (isComplete) {
                                layout_load_other_pic.setVisibility(View.GONE);
                            } else {
                                layout_load_other_pic.setVisibility(View.VISIBLE);
                            }
                        });
                iv_pic_other.setOnClickListener(v -> {
                    ArrayList<String> images = new ArrayList<>();
                    images.add(message.getContent());
                    PreviewImageActivity.openActivity(((Activity) getContext()), images, 0);
                });
            } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VIDEO) {
                GlideImageView iv_video_other = helper.getView(R.id.iv_video_other);
                View layout_load_other_video = helper.getView(R.id.layout_load_other_video);
                iv_video_other.centerCrop().error(R.mipmap.service_ic_load_failed).apply(new RequestOptions().transform(new GlideRoundTransform(getContext(), 10))).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .load(message.getContent(), R.color.placeholder, (isComplete, percentage, bytesRead, totalBytes) -> {
                            LogUtils.e("load percentage: " + percentage + " totalBytes: " + totalBytes + " bytesRead: " + bytesRead);
                            if (isComplete) {
                                layout_load_other_video.setVisibility(View.GONE);
                            } else {
                                layout_load_other_video.setVisibility(View.VISIBLE);
                            }
                        });
                helper.setText(R.id.tv_duration_other, TimeUtils.millis2FitTimeSpan(message.getTimeDuration()));
                iv_video_other.setOnClickListener(v -> {
                    VideoPreviewActivity.start(message.getContent());
                });
            } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VOICE) {
                if (message.getTimeDuration() != 0) {
                    helper.setText(R.id.tv_other_voice_time, message.getTimeDuration() + "s");
                } else {
                    helper.setText(R.id.tv_other_voice_time, "客服结束通话");
                }
                ImageView iv_other_voice = helper.getView(R.id.iv_other_voice);
                helper.itemView.setOnClickListener(new CheckClickListener(v -> {
                    if (voiceAnim != null && voiceAnim.isRunning()) {
                        voiceAnim.stop();
                        MediaManager.release();
                    }
                    voiceAnim = (AnimationDrawable) iv_other_voice.getDrawable();
                    playVoice(message.getContent());
                }));
            } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_MEDIA) {
                if (message.getTimeDuration() != 0) {
                    helper.setText(R.id.tv_other_media_time, "时长：" + formatTime(message.getTimeDuration()));
                } else {
                    helper.setText(R.id.tv_other_media_time, "客服结束通话");
                }
            } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_AI_2_ARTIFICIAL) {
                helper.getView(R.id.tv_ai_2_artificial).setOnClickListener(new CheckClickListener(v -> {
                    if (System.currentTimeMillis() - message.getCreateTime() > 4 * TimeConstants.MIN) {
                        ToastUtils.showShort("时间太久了，内容已失效");
                        return;
                    }
                    Integer waitingNo = (Integer) GlobalDataHelper.getInstance().getData(ChatConst.CHAT_WAITING_NO);
                    if (waitingNo != null && waitingNo > 0) {
                        ToastUtils.showShort("您已正在排队了，请稍候！");
                        return;
                    }
                    Long sessionId = (Long) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_ID);
                    Boolean isAi = GlobalDataHelper.getInstance().getData(ChatConst.SESSION_TYPE) != null && (Boolean) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_TYPE);
                    if (sessionId != null && sessionId != -1 && isAi != null && !isAi) {
                        ToastUtils.showShort("您已在会话中啦~");
                        return;
                    }
                    if (onOperationClickListener != null) {
                        onOperationClickListener.onOperationClick(message);
                    }
                }));
            } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_ACTIVITY) {
                ActiveBean activeBean = JsonUtil.decode(message.getContent(), ActiveBean.class);
                helper.setText(R.id.tv_activity_title, activeBean.getTitle());
                TextView tv_activity_content = helper.getView(R.id.tv_activity_content);
                String[] str = activeBean.getContent().split("</p>");
                String actSpan = "";
                for (String s : str) {
                    LogUtils.e("activity content str : " + s);
                    if (s.trim().startsWith("<p>")) {
                        actSpan += (s.trim() + "\n" + "</p>");
                    } else {
                        actSpan += (s.trim());
                    }
                }
                LogUtils.e("activity content : " + actSpan);
//                tv_activity_content.setText(Html.fromHtml(actSpan, new HtmlImageGetter(getContext(), tv_activity_content), null));
                setHtml(actSpan, tv_activity_content);
                TextView tv_activity_link = helper.getView(R.id.tv_activity_link);
                SpanUtils spanUtils = SpanUtils.with(tv_activity_link);
                if (!StringUtils.isEmpty(activeBean.getUrl())) {
                    spanUtils.append("活动链接：" + activeBean.getUrl()).setClickSpan(new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            WebActivity.start(activeBean.getUrl());
                            report(message.getGroupId(), activeBean);
                        }
                    });
                }
                tv_activity_link.setText(spanUtils.create());
            } else if (itemType == YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_RED_PACKET) {
                helper.itemView.setOnClickListener(new CheckClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo
                        checkPacket(message);
                    }
                }));
            } else {
                helper.setGone(R.id.tv_content_other, false);
                ExpandableTextView tv_content_other = helper.getView(R.id.tv_content_other);
                if (message.getStatus() == 3) {
                    tv_content_other.setText("信息含违规内容，已被管理员撤回", getItemPosition(message));
                } else if (message.getType() != YLConstant.MessageType.MESSAGE_TYPE_AI_SEND) {
//                    tv_content_other.setText(Html.fromHtml(message.getContent(), new HtmlImageGetter(getContext(), tv_content_other.getTextView()), null), getItemPosition(message));
                    tv_content_other.setText(message.getContent(), getItemPosition(message));
                } else {
                    if (!StringUtils.isEmpty(message.getContent()) && JsonUtils.isJsonFormat(message.getContent())) {
                        List<AiQuestion> questions = JsonUtils.jsonToArrayList(JsonUtils.encode(JsonUtils.decode(message.getContent())), AiQuestion.class);
                        SpanUtils spanUtils = new SpanUtils();
                        spanUtils.append("猜你想问").setBold().setFontSize(15, true).setForegroundColor(Color.parseColor("#444444"));
                        for (int i = 0; i < questions.size(); i++) {
                            AiQuestion question = questions.get(i);
                            spanUtils.append("\n");
                            spanUtils.append((i + 1) + "." + question.getQuestion()).setBold().setFontSize(14, true).setForegroundColor(Color.parseColor("#444444"))
                                    .setClickSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(@NonNull View widget) {
                                            Message message1 = JsonUtil.decode(JsonUtil.encode(message), Message.class);
                                            message1.setContent(question.getContent());
                                            EventBus.getDefault().post(message1);
                                        }

                                        @Override
                                        public void updateDrawState(@NonNull TextPaint ds) {
                                            ds.setColor(ColorUtils.getColor(R.color.color_44));
                                            ds.setUnderlineText(false);
                                        }

                                    });
//                            spanUtils.append(question.getContent()).setFontSize(14, true).setForegroundColor(Color.parseColor("#555555"));
                            spanUtils.append("\n");
                        }
                        tv_content_other.setText(spanUtils.create(), getItemPosition(message));
                    } else {
//                        tv_content_other.setText(Html.fromHtml(message.getContent(), new HtmlImageGetter(getContext(), tv_content_other.getTextView()), null), getItemPosition(message));
                        tv_content_other.setText(message.getContent(), getItemPosition(message));

                    }
                }
            }
        }
    }

    public void setHtml(String content, TextView textView) {
        RichTextHelper.showRich(textView, content);
    }


    public void playVoice(String url) {
        if (MediaManager.isPlaying()) {
            MediaManager.release();
        } else {
            MediaManager.release();
            if (!voiceAnim.isRunning()) {
                voiceAnim.start();
            }
            MediaManager.playSound(url, mp -> {
                if (voiceAnim.isRunning()) {
                    voiceAnim.stop();
                }
                MediaManager.release();
            });
        }
    }

    private synchronized void report(String groupId, ActiveBean activeBean) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("marketId", activeBean.getId());
        params.put("sessionId", groupId);
        params.put("type", 1);
        OkHttpUtils.postJson(ApiDomain.ACTIVITY_ACTION, params, new GsonResponseHandler<Object>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(Object response) {

            }
        });
    }

    private void checkTime(@NotNull BaseViewHolder helper, Message message) {
        TextView tv_time = helper.getView(R.id.tv_time);
        if (tv_time != null) {
            if (getItemPosition(message) == 0) {
                tv_time.setVisibility(View.VISIBLE);
            } else {
                int position = getItemPosition(message);
                Message lastMessage = getItem(position - 1);
                if (lastMessage.nextShowTime() || message.getCreateTime() - lastMessage.getCreateTime() > 1 * 60 * 1000) {
                    tv_time.setVisibility(View.VISIBLE);
                } else {
                    tv_time.setVisibility(View.GONE);
                }
            }
            tv_time.setText(TimeUtils.millis2String(message.getCreateTime(), "MM-dd HH:mm"));
        }
    }

    private String formatTime(long time) {
        long minutes = time / 60;
        long seconds = time % 60;
        return (minutes > 9 ? String.valueOf(minutes) : "0" + minutes) + ":" + (seconds > 9 ? String.valueOf(seconds) : "0" + seconds);
    }

    public void setOnOperationClickListener(OnOperationClickListener onOperationClickListener) {
        this.onOperationClickListener = onOperationClickListener;
    }

    public interface OnOperationClickListener {
        void onOperationClick(Message message);
    }

    private synchronized void checkPacket(Message message) {
        ReceiveRedPacket receiveRedPacket = JsonUtil.decode(message.getContent(), ReceiveRedPacket.class);
        OkHttpUtils.postJson(ApiDomain.REDPACKET_CHECK + "?redId=" + receiveRedPacket.getId(), new GsonResponseHandler<RedPacketStatusData>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(RedPacketStatusData redPacketStatusData) {
                if (redPacketStatusData != null) {
                    if (redPacketStatusData.getCurDrawStatus() == 1) {
                        showRedPacketsPop(message, redPacketStatusData.getRedId(), redPacketStatusData.getRedSendUserVo().getNickname(), redPacketStatusData.getRedSendUserVo().getUserId(), redPacketStatusData.getRedSendUserVo().getAvatar(),
                                RedPacketConstants.RED_PACKET_STATUS_HAS_OBTAIN, redPacketStatusData.getCurDrawAmount());
                    } else {
                        showRedPacketsPop(message, redPacketStatusData.getRedId(), redPacketStatusData.getRedSendUserVo().getNickname(), redPacketStatusData.getRedSendUserVo().getUserId(), redPacketStatusData.getRedSendUserVo().getAvatar(),
                                redPacketStatusData.getRedStatus(), redPacketStatusData.getCurDrawAmount());
                    }
                }
            }
        });
    }

    /**
     * redStatus红包状态(0-正常未领完 1-已领完 2-已过期)
     */
    private synchronized void showRedPacketsPop(Message message, long redId, String sendName, long sendUserId, String userAvatar, int redStatus, float amount) {
        OpenRedPacketsDialog.Builder builder = new OpenRedPacketsDialog.Builder(getContext(), redStatus)
                .setName(sendName)
                .setImage(userAvatar);

        builder.setAmount(amount);
        builder.setFullScreen(true);
        builder.setOpenButton((DialogInterface dialog, int which) -> {
            if (redStatus == RedPacketConstants.RED_PACKET_STATUS_NORMAL) {
                int sourceType = message.getLiveId() == null ? 1 : (message.getLiveType() == 1 ? 2 : 3);
                HashMap<String, Object> params = new HashMap<>();
                params.put("redId", redId);
                params.put("token", GlobalDataHelper.getInstance().getImToken());
                params.put("imGroupId", message.getGroupId());
                params.put("sourceId", sourceType == 1 ? message.getGroupId() : String.valueOf(message.getLiveId()));
                params.put("sourceType", sourceType);
                OkHttpUtils.postJson(ApiDomain.REDPACKET_OPEN, params, new GsonResponseHandler<RedDrawData>() {
                    @Override
                    public void onFailure(String statusCode, String error_msg) {

                    }

                    @Override
                    public void onSuccess(RedDrawData redDrawData) {
                        if (redDrawData != null) {
                            int nStatus = RedPacketConstants.RED_PACKET_STATUS_HAS_OBTAIN;
                            if (redDrawData.getCurDrawStatus() == RedPacketConstants.RED_PACKET_OBTAIN_DONE) {
                                nStatus = RedPacketConstants.RED_PACKET_STATUS_HAS_OBTAIN;
                            } else if (redDrawData.getIsAllDraw().equals("1")) {
                                nStatus = RedPacketConstants.RED_PACKET_STATUS_DONE;
                            }

                            showRedPacketsPop(message, redId, redDrawData.getRedSendUserVo().getNickname(),
                                    redDrawData.getRedSendUserVo().getUserId(), redDrawData.getRedSendUserVo().getAvatar(),
                                    nStatus, redDrawData.getCurDrawAmount());
                        }
                    }
                });
            } else {
                HashMap<String, Object> params = new HashMap<>();
                params.put("size", 20);
                params.put("page", 1);
                HashMap<String, Object> param = new HashMap<>();
                param.put("redId", redId);
                params.put("param", param);
                OkHttpUtils.postJson(ApiDomain.REDPACKET_DETAIL, params, new GsonResponseHandler<RedDrawData>() {
                    @Override
                    public void onFailure(String statusCode, String error_msg) {
                    }

                    @Override
                    public void onSuccess(RedDrawData redDrawData) {
                        if (redDrawData != null) {
                            Bundle data = new Bundle();
                            data.putLong("redId", redId);
                            ActivityUtils.startActivity(data, RedPacketsActivity.class);
                        }
                    }
                });
            }
        });

        OpenRedPacketsDialog dialog = builder.create();
        dialog.show();
    }
}
