package com.keepfun.aiservice.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.entity.RedDrawData;
import com.keepfun.aiservice.ui.view.roundedimageview.RoundedImageView;
import com.keepfun.blankj.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RedPacketsItemAdapter extends RefreshAdapter {
    private ArrayList<RedDrawData.DrawRecordPageVoBean.RedDrawBean> redPacketsDataList;
    private Context context;

    public RedPacketsItemAdapter(Context context, ArrayList<RedDrawData.DrawRecordPageVoBean.RedDrawBean> data) {
        super(context);
        this.redPacketsDataList = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_redpackets, parent, false);
        return new RedPacketsItemAdapter.RedPacketsHolder(item);
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, int position) {
        RedPacketsHolder redPacketsHolder = (RedPacketsHolder) holder;
        RedDrawData.DrawRecordPageVoBean.RedDrawBean data = redPacketsDataList.get(position);
        if (data != null) {
            Glide.with(context).asDrawable().load(data.getAvatar())
                    .placeholder(R.mipmap.service_bg_chat_default)
                    .into(redPacketsHolder.avatarImage);
            redPacketsHolder.nickname.setText(data.getNickname());

            Log.d("ch", "onBindView: data.getDrawTime() " + data.getDrawTime());
            redPacketsHolder.timerTxt.setText(data.getDrawTime());
            redPacketsHolder.money.setText(String.valueOf(data.getDrawAmount()));
        }
    }

    @Override
    public int getItemDataCount() {
        return redPacketsDataList != null ? redPacketsDataList.size() : 0;
    }

    public String formatTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public class RedPacketsHolder extends RecyclerView.ViewHolder {
        TextView nickname;
        RoundedImageView avatarImage;
        TextView timerTxt;
        TextView money;

        public RedPacketsHolder(@NonNull View itemView) {
            super(itemView);

            nickname = itemView.findViewById(R.id.user_red_nickname_txt);
            avatarImage = itemView.findViewById(R.id.user_red_avatar);
            timerTxt = itemView.findViewById(R.id.time_red_txt);
            money = itemView.findViewById(R.id.coin_red_num);
        }
    }
}