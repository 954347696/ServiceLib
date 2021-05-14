package com.keepfun.aiservice.ui.view;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.keepfun.aiservice.R;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.utils.audio.MediaRecorderUtils;
import com.keepfun.blankj.util.ClickUtils;
import com.keepfun.blankj.util.ToastUtils;


/**
 * @author yang
 * @description
 * @date 2020/9/7 5:28 PM
 */
public class ServiceAudioLayout extends LinearLayout implements View.OnClickListener {

    public ServiceAudioLayout(Context context) {
        this(context, null);
    }

    public ServiceAudioLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ServiceAudioLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ServiceAudioLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    TextView tv_record;
    View layout_send;
    TextView tv_time;

    private Context context;
    private MediaRecorderUtils mMediaRecorderUtils;
    private String mSavePath;
    private long duration;
    private OnAudioSendListener mOnAudioSendListener;

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.service_layout_audio, this, true);
        tv_record = view.findViewById(R.id.tv_record);
        layout_send = view.findViewById(R.id.layout_send);
        tv_time = view.findViewById(R.id.tv_time);

        view.findViewById(R.id.tv_record).setOnClickListener(new CheckClickListener(this));
        view.findViewById(R.id.tv_cancel).setOnClickListener(new CheckClickListener(this));
        view.findViewById(R.id.tv_send).setOnClickListener(new CheckClickListener(this));

        mMediaRecorderUtils = new MediaRecorderUtils.Builder(context)
                //麦克
                .setAudioSource(MediaRecorder.AudioSource.MIC)
                //AMR
                .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                //AMR
                .setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
                //获取分贝的间隔
                .setDecibelSpace(500)
                .build();
        //设置最大录制时间 秒，需要在start前调用
        mMediaRecorderUtils.setMaximum(60);
        //音频录制监听回调
        mMediaRecorderUtils.setMediaRecorderCallBack(new MediaRecorderUtils.MediaRecorderCallBack() {
            @Override
            public void start() {
                //开始录制
                layout_send.setVisibility(VISIBLE);
                tv_record.setVisibility(GONE);
            }

            @Override
            public void stop(int second) {
                //结束录制
                if (second < 1) {
                    layout_send.setVisibility(GONE);
                    tv_record.setVisibility(VISIBLE);
                    ToastUtils.showShort("说话时间太短");
                } else {
                    mSavePath = mMediaRecorderUtils.getPath();
                    duration = second;
                }
            }

            @Override
            public void ioError(String ioError) {
                //错误
                layout_send.setVisibility(GONE);
                tv_record.setVisibility(VISIBLE);
            }

            @Override
            public void error(String error) {
                //错误
                layout_send.setVisibility(GONE);
                tv_record.setVisibility(VISIBLE);
            }

            @Override
            public void process(int second) {
                //已录制秒数
                long minutes = second / 60;
                long restSecond = second - minutes * 60;
                tv_time.setText((minutes == 0 ? "00" : (minutes > 9 ? minutes : "0" + minutes)) + ":" + (restSecond == 0 ? "00" : (restSecond > 9 ? restSecond : "0" + restSecond)));
                if (second > 60) {
                    duration = second;
                    mMediaRecorderUtils.stop();  //结束录音（保存录音文件）
                }
            }

            @Override
            public void decibel(int decibel) {
            }
        });

    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            if (mMediaRecorderUtils.isRecording()) {
                mMediaRecorderUtils.stop();
            }
            layout_send.setVisibility(GONE);
            tv_record.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (ClickUtils.isFastDoubleClick()) {
            return;
        }
        int id = v.getId();
        if (id == R.id.tv_record) {
            duration = 0;
            mSavePath = null;
            tv_time.setText("00:00");
            mMediaRecorderUtils.start();
        } else if (id == R.id.tv_cancel) {
            mMediaRecorderUtils.stop();
            layout_send.setVisibility(GONE);
            tv_record.setVisibility(VISIBLE);
        } else if (id == R.id.tv_send) {
            if (mMediaRecorderUtils.isRecording()) {
                mMediaRecorderUtils.stop();
            }
            layout_send.setVisibility(GONE);
            tv_record.setVisibility(VISIBLE);
            if (mOnAudioSendListener != null) {
                mOnAudioSendListener.onAudioSend(mSavePath, duration);
            }
        }
    }

    public void setOnAudioSendListener(OnAudioSendListener onAudioSendListener) {
        this.mOnAudioSendListener = onAudioSendListener;
    }

    public interface OnAudioSendListener {
        void onAudioSend(String filePath, long duration);
    }

}
