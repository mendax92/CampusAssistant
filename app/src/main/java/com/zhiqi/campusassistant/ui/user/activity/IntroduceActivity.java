package com.zhiqi.campusassistant.ui.user.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.ui.main.widget.AppHelper;

import butterknife.BindView;

/**
 * Created by ming on 2017/3/20.
 * 功能介绍
 */

public class IntroduceActivity extends BaseToolbarActivity {

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.introduce)
    TextView introduce;

    private ModuleType moduleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            moduleType = ModuleType.formatValue(intent.getIntExtra(AppConstant.EXTRA_APP_MODULE, -1));
            if (moduleType == null) {
                finish();
                return;
            }
        }
        Drawable drawable = AppHelper.getAppIcon(this, moduleType);
        icon.setImageDrawable(drawable);
        title.setText(AppHelper.getAppName(this, moduleType));
    }
}
