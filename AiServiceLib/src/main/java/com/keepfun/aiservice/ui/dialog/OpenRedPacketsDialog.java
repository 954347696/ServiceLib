package com.keepfun.aiservice.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.RedPacketConstants;
import com.keepfun.aiservice.ui.view.roundedimageview.RoundedImageView;
import com.keepfun.aiservice.utils.UIUtils;

public class OpenRedPacketsDialog extends Dialog {
    public boolean bFullScreen = false;
    public OpenRedPacketsDialog(Context context) {
        super(context, R.style.UpdateDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        if (bFullScreen) {
            params.width = (int) (UIUtils.getScreenWidth(getContext()) * 0.4);
            params.height = (int) (UIUtils.getScreenHeight(getContext()) * 0.98) ;
        }
        else {
            params.width = (int) (UIUtils.getScreenWidth(getContext()) * 0.8);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }

        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
    }

    public static class Builder {
        private Context context;
        private String user_Name;//发红包者的名称
        private String user_Image;
        private ImageView openImageView;
        //红包状态(0-正常未领完 1-已领完 2-已过期)
        private int redStatus = 1;
        private boolean bFullScreen = false;
        Animation rotateAnimation;

        private float curDrawAmount;

        private OnClickListener openButtonClickListener;
        private OnClickListener closeButtonClickListener;

        public Builder(Context context, int redStatus) {
            this.context = context;
            this.redStatus = redStatus;
        }

        public Builder setImage(String image) {
            this.user_Image = image;
            return this;
        }

        public Builder setAmount(float amount) {
            this.curDrawAmount = amount;
            return this;
        }

        public Builder setFullScreen(boolean bFullScreen) {
            this.bFullScreen = !bFullScreen;
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param name
         * @return
         */
        public Builder setName(String name) {
            this.user_Name = name;
            return this;
        }

        public Builder setCloseButton(OnClickListener listener) {
            this.closeButtonClickListener = listener;
            return this;
        }

        public Builder setOpenButton(OnClickListener listener) {
            this.openButtonClickListener = listener;
            return this;
        }

        public void startAnimation() {
            if (openImageView != null) {
                openImageView.setAnimation(rotateAnimation);
                openImageView.startAnimation(rotateAnimation);
            }
        }

        public void stopAnimation() {
            if (openImageView != null) {
                openImageView.clearAnimation();
            }
        }


        public OpenRedPacketsDialog create() {
            final OpenRedPacketsDialog dialog = new OpenRedPacketsDialog(context);
            dialog.bFullScreen = bFullScreen;

            //加载布局
            View layout = LayoutInflater.from(context).inflate(R.layout.dialog_send_redpackets, null);
            openImageView = layout.findViewById(R.id.open_btn);

            TextView textView1 = layout.findViewById(R.id.text_describe);
            TextView textView2 = layout.findViewById(R.id.text_expired);
            TextView textView3 = layout.findViewById(R.id.redpackets_describe);

            if (redStatus == RedPacketConstants.RED_PACKET_STATUS_NORMAL) {
                openImageView.setVisibility(View.VISIBLE);
            } else if (redStatus == RedPacketConstants.RED_PACKET_STATUS_EXPIRE) {
                textView1.setText("无法领取");
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                openImageView.setVisibility(View.GONE);
            } else if (redStatus == RedPacketConstants.RED_PACKET_STATUS_DONE) {
                textView1.setText("该红包已被抢光");
                textView3.setVisibility(View.VISIBLE);
                openImageView.setVisibility(View.GONE);
            }else if (redStatus == RedPacketConstants.RED_PACKET_STATUS_HAS_OBTAIN){
                // 已领取红包
                textView1.setTextSize(26);
                textView1.setText(String.valueOf(curDrawAmount) + " K币");
                textView3.setVisibility(View.VISIBLE);
                openImageView.setVisibility(View.GONE);
            }

            rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);

            //设置发红包者姓名
            ((TextView) layout.findViewById(R.id.user_name)).setText(user_Name + "的红包");
            Glide.with(context)
                    .asDrawable()
                    .load(user_Image)
                    .error(R.mipmap.service_bg_chat_default)
                    .into((RoundedImageView) layout.findViewById(R.id.user_image));

            //设置拆红包的按钮
            if (openButtonClickListener != null) {
                if (redStatus == RedPacketConstants.RED_PACKET_STATUS_NORMAL) {
                    (layout.findViewById(R.id.open_btn)).setOnClickListener((View v) -> {
                        startAnimation();
                        openImageView.postDelayed(() -> {
                            dialog.dismiss();
                            openButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }, 2000);
                    });
                } else if (redStatus == RedPacketConstants.RED_PACKET_STATUS_DONE ||
                        redStatus == RedPacketConstants.RED_PACKET_STATUS_EXPIRE ||
                        redStatus == RedPacketConstants.RED_PACKET_STATUS_HAS_OBTAIN) {
                    (layout.findViewById(R.id.redpackets_describe)).setOnClickListener((View v) -> {
                        openButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    });
                }
            }

            //设置关闭按钮

            (layout.findViewById(R.id.close_redpackets)).setOnClickListener((View v) -> {
                stopAnimation();
                dialog.dismiss();
            });

            //点击dialog之外的区域禁止取消dialog
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(layout);

            return dialog;
        }
    }
}
