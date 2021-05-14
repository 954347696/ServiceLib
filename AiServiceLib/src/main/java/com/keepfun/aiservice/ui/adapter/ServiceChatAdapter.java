package com.keepfun.aiservice.ui.adapter;

import com.keepfun.adapter.base.BaseMultiItemQuickAdapter;
import com.keepfun.adapter.base.module.UpFetchModule;
import com.keepfun.adapter.base.viewholder.BaseViewHolder;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.Message;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.impl.OnOperationClickListener;
import com.keepfun.aiservice.manager.ChatKit;
import com.keepfun.aiservice.ui.messageview.BaseMsgView;
import com.keepfun.aiservice.ui.messageview.UnknownMsgView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/9/8 4:16 PM
 */
public class ServiceChatAdapter extends BaseMultiItemQuickAdapter<Message, BaseViewHolder> implements UpFetchModule {

    public ServiceChatAdapter(List<Message> messages) {
        super(messages);
        addItemType(YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_TIP, R.layout.service_item_chat_tip);
        addItemType(YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_SYSTEM, R.layout.service_item_chat_system);
        addItemType(YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_END, R.layout.service_item_chat_end);
        addItemType(YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_DEFAULT, R.layout.service_item_chat_default);
        addItemType(YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_AI_2_ARTIFICIAL, R.layout.service_item_chat_ai_2_artificial);
        addItemType(YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_PIC, R.layout.service_item_chat_pic);
        addItemType(YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VIDEO, R.layout.service_item_chat_video);
        addItemType(YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_VOICE, R.layout.service_item_chat_voice);
        addItemType(YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_MEDIA, R.layout.service_item_chat_media);
        addItemType(YLConstant.MessageItemType.MESSAGE_ITEM_TYPE_ACTIVITY, R.layout.service_item_chat_activity);
    }

    private OnOperationClickListener onOperationClickListener;

    @Override
    protected void convert(@NotNull BaseViewHolder helper, Message message) {
        int itemType = message.getItemType();
        BaseMsgView baseMsgView = null;
        Class<? extends BaseMsgView> msgViewClass = ChatKit.getRegisterMessageView(itemType);
        if (msgViewClass == null) {
            baseMsgView = new UnknownMsgView();
        } else if (baseMsgView == null || baseMsgView.getClass() != msgViewClass) {
            try {
                baseMsgView = msgViewClass.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("baseMsgView newInstance failed.");
            }
        }
        baseMsgView.setContent(helper, message);
        baseMsgView.setOnOperationClickListener(onOperationClickListener);
        checkTime(baseMsgView, helper, message);

    }

    private void checkTime(BaseMsgView baseMsgView, @NotNull BaseViewHolder helper, Message message) {
        int position = getItemPosition(message);
        Message lastMessage = null;
        if (getItemPosition(message) != 0) {
            lastMessage = getItem(position - 1);
        }
        baseMsgView.checkTime(helper, message, lastMessage);
    }

    public void setOnOperationClickListener(OnOperationClickListener onOperationClickListener) {
        this.onOperationClickListener = onOperationClickListener;
    }


    public long getUserId() {
        return GlobalDataHelper.getInstance().getImUserInfo().getId();
    }
}
