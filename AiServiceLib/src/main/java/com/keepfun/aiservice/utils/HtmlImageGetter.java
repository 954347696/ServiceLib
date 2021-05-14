package com.keepfun.aiservice.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.ScreenUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;


/**
 * @author yang
 * @description
 * @date 2020/10/30 3:50 PM
 */
public class HtmlImageGetter implements Html.ImageGetter {

    TextView container;
    URI baseUri;
    boolean matchParentWidth;
    static int urlNum = 0;

    public HtmlImageGetter(Context context, TextView textView) {
        this.container = textView;
        this.matchParentWidth = false;
    }

    public HtmlImageGetter(TextView textView, String baseUrl) {
        this.container = textView;
        if (baseUrl != null) {
            this.baseUri = URI.create(baseUrl);
        }
    }

    public HtmlImageGetter(TextView textView, String baseUrl, boolean matchParentWidth) {
        urlNum = 0;
        this.container = textView;
        this.matchParentWidth = matchParentWidth;
        if (baseUrl != null) {
            this.baseUri = URI.create(baseUrl);
        }
    }

    public Drawable getDrawable(String source) {
        UrlDrawable urlDrawable = new UrlDrawable();
        urlNum++;//限制详情页最多显示20张图片
        if (urlNum < 20) {
            // get the actual source
            ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable, this, container, matchParentWidth);
            asyncTask.execute(source);
        }
        // return reference to URLDrawable which will asynchronously load the image specified in the src tag
        return urlDrawable;
    }

    /**
     * Static inner {@link AsyncTask} that keeps a {@link WeakReference} to the {@link UrlDrawable}
     * and {@link HtmlImageGetter}.
     * <p/>
     * This way, if the AsyncTask has a longer life span than the UrlDrawable,
     * we won't leak the UrlDrawable or the HtmlRemoteImageGetter.
     */
    private static class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        private final WeakReference<UrlDrawable> drawableReference;
        private final WeakReference<HtmlImageGetter> imageGetterReference;
        private final WeakReference<View> containerReference;
        private String source;
        private boolean matchParentWidth;
        private float scale;

        public ImageGetterAsyncTask(UrlDrawable d, HtmlImageGetter imageGetter, View container, boolean matchParentWidth) {
            this.drawableReference = new WeakReference<>(d);
            this.imageGetterReference = new WeakReference<>(imageGetter);
            this.containerReference = new WeakReference<>(container);
            this.matchParentWidth = matchParentWidth;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result == null) {
                return;
            }

            scale = getScale(result);
            int trueWith = (int) (SizeUtils.dp2px(result.getIntrinsicWidth()) * scale);
            int trueHeight = (int) (SizeUtils.dp2px(result.getIntrinsicHeight()) * scale);
            if (trueWith > SizeUtils.dp2px(180)) {
                trueHeight = SizeUtils.dp2px(180) * trueHeight / trueWith;
                trueWith = SizeUtils.dp2px(180);
            }
            result.setBounds(0, 0, trueWith, trueHeight);

            final UrlDrawable urlDrawable = drawableReference.get();
            if (urlDrawable == null) {
                return;
            }
            // set the correct bound according to the result from HTTP call
            urlDrawable.setBounds(0, 0, trueWith, trueHeight);
            // change the reference of the current drawable to the result from the HTTP call
            urlDrawable.drawable = result;

            final HtmlImageGetter imageGetter = imageGetterReference.get();
            if (imageGetter == null) {
                return;
            }
            // redraw the image by invalidating the container
            imageGetter.container.invalidate();
            // re-set text to fix images overlapping text
            imageGetter.container.setText(imageGetter.container.getText());

        }

        /**
         * Get the Drawable from URL
         */
        public synchronized Drawable fetchDrawable(String urlString) {
            try {
//                InputStream is = fetch(urlString);
//                InputStream is = getInputStream(urlString);
//                Drawable drawable = Drawable.createFromStream(new URL(urlString).openStream(), "src");
                Drawable drawable = getDrawable(urlString);
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }

        private Drawable getDrawable(String url) {
            try {
                Drawable drawable = Glide.with(Utils.getApp()).load(url).submit().get();
                return drawable;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        private synchronized float getScale(Drawable drawable) {
            View container = containerReference.get();
            if (container == null) {
                return 1f;
            }
            float originalDrawableWidth = SizeUtils.dp2px(drawable.getIntrinsicWidth());
            float maxWidth = container.getWidth();
            if (maxWidth == 0) {
                maxWidth = ScreenUtils.getScreenWidth();
            }
            if (maxWidth != 0 && originalDrawableWidth > maxWidth) {
                return maxWidth * 1F / originalDrawableWidth;
            } else if (!matchParentWidth) {
                return 1f;
            } else {
                return maxWidth / originalDrawableWidth;
            }
        }

        private InputStream fetch(String urlString) throws IOException {
            URL url;
            final HtmlImageGetter imageGetter = imageGetterReference.get();
            if (imageGetter == null) {
                return null;
            }
            if (imageGetter.baseUri != null) {
                url = imageGetter.baseUri.resolve(urlString).toURL();
            } else {
                url = URI.create(urlString).toURL();
            }

            return (InputStream) url.getContent();
        }

        public InputStream getInputStream(String uri) {
            try {
                URL url = new URL(uri);
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.setRequestProperty("Accept", "application/xml");
                return connection.getInputStream();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}