package com.keepfun.aiservice.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * @author yang
 * @description
 * @date 2020/11/30 2:02 PM
 */
public class CaptchaImageUtil {

    public final char[] charSource = {
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p',
            'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
            'z', 'x', 'c', 'v', 'b', 'n', 'm',
            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P',
            'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L',
            'Z', 'X', 'C', 'V', 'B', 'N', 'M',
    };

    private static CaptchaImageUtil mImageCodeUtil = null;
    private Random mRandom = null;
    private String codeText = null;//验证码
    private int mBitmapWidth = 200;//默认宽
    private int mBitmapHeight = 100;//默认高
    private int padding = 10;//默认内边距
    private int imageBackgroundColor = Color.parseColor("#d5d5d5");
    private int lineTotal = 6;//干绕线数
    private int textLength = 4;//默认验证码长度

    private CaptchaImageUtil() {
        mRandom = new Random();
    }

    public static CaptchaImageUtil getInstance() {
        if (mImageCodeUtil == null) {
            synchronized (CaptchaImageUtil.class) {
                if (mImageCodeUtil == null) {
                    mImageCodeUtil = new CaptchaImageUtil();
                }
            }
        }
        return mImageCodeUtil;
    }

    /**
     * 创建图形验证码
     */
    public Bitmap createImageCode(String text) {
        //创建bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);
        //创建画布
        Canvas canvas = new Canvas(targetBitmap);
        codeText = text;
        textLength = codeText.length();
        //画笔
        Paint mPaint = new Paint();
        //画底图
        canvas.drawColor(imageBackgroundColor);
        //画干绕线
        for (int i = 0; i < lineTotal; i++) {
            mPaint.setColor(getRandomColor());
            drawLine(canvas, mPaint);
        }
        //画验证码
        for (int i = 0; i < codeText.length(); i++) {
            mPaint.setTextSize(getTextSize());
            mPaint.setColor(getRandomColor());
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(codeText.substring(i, i + 1),
                    getXPosition(mBitmapWidth, padding, i),
                    getYPosition(mBitmapHeight, padding), mPaint);
        }

        return targetBitmap;
    }


    /**
     * 获取随机的颜色
     */
    public int getRandomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }

    /**
     * 随机获取文字的大小
     */
    public int getTextSize() {
        //默认文字大小最小为20
        return getTextSize(20);
    }

    public int getTextSize(int defaultSize) {
        //先计算内容的大小
        int tempSize = mBitmapHeight - padding * 2 - defaultSize;
        //随机生成
        tempSize = mRandom.nextInt(tempSize);
        //前面减了再加上
        tempSize += defaultSize;
        return tempSize;
    }

    public void setBitmapHeight(int mBitmapHeight) {
        this.mBitmapHeight = mBitmapHeight;
    }

    public void setBitmapWidth(int mBitmapWidth) {
        this.mBitmapWidth = mBitmapWidth;
    }

    /**
     * 获取位置
     */
    public int getXPosition(int width, int padding, int index) {
        int textWidth = (width - padding * 2) / textLength;
        return padding + textWidth * (index - 1) + textWidth * 3 / 2;
    }

    /**
     * 获取位置
     */
    public int getYPosition(int height, int padding) {
        return mRandom.nextInt(height / 2 - padding) + padding + height / 2;
    }

    /**
     * 画干绕线
     */
    private void drawLine(Canvas canvas, Paint paint) {
        paint.setStrokeWidth(mRandom.nextInt(5));
        canvas.drawLine(mRandom.nextInt(mBitmapWidth), mRandom.nextInt(mBitmapHeight), mRandom.nextInt(mBitmapWidth), mRandom.nextInt(mBitmapHeight), paint);
    }
}
