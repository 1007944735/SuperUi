package com.sgevf.ui.widget;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.sgevf.ui.R;

@SuppressLint("SetJavaScriptEnabled")
public class WebLoadingActivity extends AppCompatActivity {
    public final static String URL = "url";
    public final static String SHOWPROGRESS = "show_progress";
    private WebView webView;
    private ProgressBar progressBar;
    private FrameLayout videoFullContainer;
    private String url;
    private boolean showProgress;
    private boolean debug = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_web_view);
        initView();
        initData();
        initWebView();
        load();
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_bar);
        webView = findViewById(R.id.web_view);
        videoFullContainer = findViewById(R.id.video_full_container);
    }

    private void initWebView() {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        setting.setDomStorageEnabled(true);
        webView.setWebChromeClient(new mWebChromeClient());
        webView.setWebViewClient(new mWebViewClient());
        if (debug) {
            webView.setWebContentsDebuggingEnabled(true);
        }
    }

    private void initData() {
        url = getIntent().getStringExtra(URL);
        showProgress = getIntent().getBooleanExtra(SHOWPROGRESS, true);
    }

    public void load() {
        webView.loadUrl(url);
    }

    private class mWebChromeClient extends WebChromeClient {
        private CustomViewCallback mCallback;

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (showProgress) {
                progressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            mCallback = callback;
            fullScreen();
            webView.setVisibility(View.GONE);
            videoFullContainer.setVisibility(View.VISIBLE);
            videoFullContainer.addView(view);

        }

        @Override
        public void onHideCustomView() {
            fullScreen();
            webView.setVisibility(View.VISIBLE);
            videoFullContainer.setVisibility(View.GONE);
            videoFullContainer.removeAllViews();
        }
    }

    private void fullScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private class mWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (showProgress) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (showProgress) {
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    public WebView getWebView() {
        return webView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public String getUrl() {
        return url;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
