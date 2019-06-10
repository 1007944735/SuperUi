package com.sgevf.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class WebLoadingActivity extends AppCompatActivity {
    public final static String URL = "url";
    public final static String SHOWPROGRESS = "show_progress";
    private WebView webView;
    private ProgressBar progressBar;
    private String url;
    private boolean showProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createLayout());
        initData();
        initWebView();
        load();
    }

    private View createLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView = new WebView(this);
        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setVisibility(View.GONE);
        layout.addView(progressBar);
        layout.addView(webView);
        return layout;
    }

    private void initWebView() {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebChromeClient(new mWebChromeClient());
        webView.setWebViewClient(new mWebViewClient());
    }

    private void initData() {
        url = getIntent().getStringExtra(URL);
        showProgress=getIntent().getBooleanExtra(SHOWPROGRESS,true);
    }

    public void load() {
        webView.loadUrl(url);
    }

    private class mWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (showProgress) {
                progressBar.setProgress(newProgress);
            }
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

    public WebView getWebView(){
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
}
