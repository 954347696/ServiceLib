package com.keepfun.base;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.keepfun.aiservice.R;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.StringUtils;


/**
 * @author yang
 * @description
 * @date 2020/9/29 3:55 PM
 */
public class WebActivity extends PanActivity {

    private TextView tv_title;
    private ProgressBar webView_progress;
    private WebView webView;

    private String url;

    public static void start(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        ActivityUtils.startActivity(bundle, WebActivity.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pan_webview;
    }

    @Override
    public void bindUI(View rootView) {
        tv_title = findViewById(R.id.tv_title);
        webView_progress = findViewById(R.id.webView_progress);
        webView = findViewById(R.id.webView);
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (webView_progress == null) {
                    return;
                }
                webView_progress.setProgress(newProgress);
                webView_progress.setVisibility(newProgress == 100 ? View.INVISIBLE : View.VISIBLE);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (StringUtils.isEmpty(title) || tv_title == null) {
                    return;
                }
                tv_title.setText(title);
            }
        });
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "ERROR URL Is Empty", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //关键点
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                WebView.HitTestResult hitTestResult = view.getHitTestResult();
                //hitTestResult==null解决重定向问题
                if (!TextUtils.isEmpty(request.getUrl().toString()) && hitTestResult == null) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }
                return false;
            }
        });

        loadUrl();
    }

    private void loadUrl() {
        if (url.startsWith("http")) {
            webView.loadUrl(url);
        } else {
            webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }
}
