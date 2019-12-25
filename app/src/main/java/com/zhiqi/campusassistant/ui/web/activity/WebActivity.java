package com.zhiqi.campusassistant.ui.web.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.ming.base.util.Log;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.ui.web.fragment.WebFragment;

/**
 * Created by ming on 2017/3/13.
 * web浏览器activity
 */

public class WebActivity extends BaseToolbarActivity {

    private static final String TAG = "WebActivity";

    private WebFragment webFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_common_empty_framelayout);
        initView();
        initData();
    }

    private void initView() {
        try {
            webFragment = new WebFragment();
            webFragment.interceptKeyEvent(true);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, webFragment).commitAllowingStateLoss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        Uri uri = intent.getData();
        String url;
        if (uri != null) {
            url = uri.getQueryParameter(AppConstant.EXTRA_URL);
        } else {
            url = intent.getStringExtra(AppConstant.EXTRA_URL);
        }
        Log.i(TAG, "url : " + url);
        webFragment.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
