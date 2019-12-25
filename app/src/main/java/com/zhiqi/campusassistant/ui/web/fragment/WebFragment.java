package com.zhiqi.campusassistant.ui.web.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.fragment.BaseToolbarFragment;

import butterknife.BindView;

/**
 * Created by ming on 2017/3/13.
 * web浏览器fragment
 */

public class WebFragment extends BaseToolbarFragment {

    @BindView(R.id.web_view)
    WebView mWebView;

    @BindView(R.id.web_progress)
    ProgressBar mProgressBar;

    private String url;

    private boolean isInterceptKeyEvent;

    @Override
    public int onCreateView(Bundle savedInstanceState) {
        return R.layout.frag_web;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setSaveFormData(true);
        settings.setSupportMultipleWindows(false);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                onProgressChanged(100);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setActionbarTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                WebFragment.this.onProgressChanged(newProgress);
            }
        });
        loadUrl(url);
    }

    public void loadUrl(String url) {
        this.url = url;
        if (!TextUtils.isEmpty(url) && mWebView != null) {
            mWebView.loadUrl(url);
        }
    }

    private void onProgressChanged(int progress) {
        if (progress < 100 && View.GONE == mProgressBar.getVisibility()) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else if (progress == 100) {
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
                }
            }, 300);
        }
        mProgressBar.setProgress(progress);

    }

    public void interceptKeyEvent(boolean interceptKeyEvent) {
        isInterceptKeyEvent = interceptKeyEvent;
    }

    @Override
    public boolean isDispatchKeyEvent() {
        return true;
    }

    @Override
    public boolean onBackPressed() {
        if (isInterceptKeyEvent && mWebView != null) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }
}
