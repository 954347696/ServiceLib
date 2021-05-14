package com.keepfun.aiservice.manager;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ScreenUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.imageselector.PreviewImageActivity;
import com.zzhoujay.richtext.CacheType;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.RichTextConfig;
import com.zzhoujay.richtext.callback.ImageFixCallback;
import com.zzhoujay.richtext.callback.ImageGetter;
import com.zzhoujay.richtext.callback.ImageLoadNotify;
import com.zzhoujay.richtext.callback.OnImageLongClickListener;
import com.zzhoujay.richtext.ig.DefaultImageDownloader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2021/4/19 5:27 PM
 */
public class RichTextHelper {

    public static void showRich(TextView textView, String content) {
        RichText.initCacheDir(textView.getContext());
        RichText.fromHtml(content)
//                .from(result.getAnswer()) // 数据源
//                .type(RichType.html) // 数据格式,不设置默认是Html,使用fromMarkdown的默认是Markdown格式
                .autoFix(true) // 是否自动修复，默认true
//                .autoPlay(true) // gif图片是否自动播放
//                .showBorder(true) // 是否显示图片边框
//                .borderColor(Color.RED) // 图片边框颜色
//                .borderSize(3) // 边框尺寸
//                .borderRadius(50) // 图片边框圆角弧度
                .scaleType(ImageHolder.ScaleType.center_crop) // 图片缩放方式
                .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT) // 图片占位区域的宽高
                .fix(new ImageFixCallback() {
                    @Override
                    public void onInit(ImageHolder holder) {

                    }

                    @Override
                    public void onLoading(ImageHolder holder) {

                    }

                    @Override
                    public void onSizeReady(ImageHolder holder, int imageWidth, int imageHeight, ImageHolder.SizeHolder sizeHolder) {
                        if (imageWidth > ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30)) {
                            sizeHolder.setScale(((ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30)) * 1f) / imageWidth);
                        }
                    }

                    @Override
                    public void onImageReady(ImageHolder holder, int width, int height) {
                        LogUtils.e("onImageReady holder state : " + holder.getImageState());
                    }

                    @Override
                    public void onFailure(ImageHolder holder, Exception e) {

                    }
                }) // 设置自定义修复图片宽高
//                .fixLink(linkFixCallback) // 设置链接自定义回调
                .noImage(false) // 不显示并且不加载图片
                .resetSize(false) // 默认false，是否忽略img标签中的宽高尺寸（只在img标签中存在宽高时才有效），true：忽略标签中的尺寸并触发SIZE_READY回调，false：使用img标签中的宽高尺寸，不触发SIZE_READY回调
                .clickable(true) // 是否可点击，默认只有设置了点击监听才可点击
                .imageClick((imageUrls, position) -> {
                    //
                    LogUtils.e("imageClicked position : " + position + "    imageUrls : " + imageUrls);
                    PreviewImageActivity.openActivity(ActivityUtils.getTopActivity(), new ArrayList<>(imageUrls), position);
                }) // 设置图片点击回调
                .imageLongClick(new OnImageLongClickListener() {
                    @Override
                    public boolean imageLongClicked(List<String> imageUrls, int position) {
                        LogUtils.e("imageClicked position : " + position + "    imageUrls : " + imageUrls);
                        PreviewImageActivity.openActivity(ActivityUtils.getTopActivity(), new ArrayList<>(imageUrls), position);
                        return false;
                    }
                }) // 设置图片长按回调
//                .urlClick(onURLClickListener) // 设置链接点击回调
//                .urlLongClick(onUrlLongClickListener) // 设置链接长按回调
//                .placeHolder((holder, config, textView) -> getContext().getDrawable(R.mipmap.service_ic_banner_default)) // 设置加载中显示的占位图
//                .errorImage((holder, config, textView) -> getContext().getDrawable(R.mipmap.service_ic_banner_default))
//                .error(R.mipmap.service_ic_banner_default) // 设置加载失败的错误图
                .cache(CacheType.none) // 缓存类型，默认为Cache.ALL（缓存图片和图片大小信息和文本样式信息）
//                .imageGetter(new DefaultImageGetter()) // 设置图片加载器，默认为DefaultImageGetter，使用okhttp实现
                .imageGetter(new ImageGetter() {
                    @Override
                    public void registerImageLoadNotify(ImageLoadNotify imageLoadNotify) {

                    }

                    @Override
                    public Drawable getDrawable(ImageHolder holder, RichTextConfig config, TextView textView) {
                        Drawable drawable = null;
                        URL url;
                        LogUtils.e("");
                        try {
                            url = new URL(holder.getSource());
                            drawable = Drawable.createFromStream(url.openStream(), "");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                        float scale = 1;
                        if (drawable.getIntrinsicWidth() > ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30)) {
                            scale = ((ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30)) * 1f) / drawable.getIntrinsicWidth();
                        }
                        drawable.setBounds(0, 0, ((int) (drawable.getIntrinsicWidth() * scale)),
                                ((int) (drawable.getIntrinsicHeight() * scale)));

                        return drawable;
                    }

                    @Override
                    public void recycle() {

                    }
                })
                .imageDownloader(new DefaultImageDownloader()) // 设置DefaultImageGetter的图片下载器
//                .imageDownloader(new OkHttpImageDownloader()) // 设置DefaultImageGetter的图片下载器
                .bind(textView.getParent()) // 绑定richText对象到某个object上，方便后面的清理
                .done(imageLoadDone -> {}) // 解析完成回调
                .into(textView) // 设置目标TextView
        ;
    }
}
