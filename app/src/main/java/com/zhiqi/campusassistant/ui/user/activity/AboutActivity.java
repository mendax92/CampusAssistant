package com.zhiqi.campusassistant.ui.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ming.base.util.DeviceUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 2017/3/18.
 * 关于软件
 */

public class AboutActivity extends BaseToolbarActivity {

    @BindView(R.id.version_name)
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        version.setText(getString(R.string.user_about_version, DeviceUtil.getPackageVersionName(this)));
    }

    @OnClick(R.id.grade)
    void onClick(View view) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
