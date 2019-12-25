package com.zhiqi.campusassistant.ui.selfpay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.ui.user.activity.IntroduceActivity;

import butterknife.OnClick;

/**
 * Created by ming on 2017/7/30.
 */

public class SelfPayActivity extends BaseToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_self);
    }

    @OnClick({R.id.pay_waiting, R.id.pay_detail})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_waiting:
                startActivity(new Intent(this, ToPayListActivity.class));
                break;
            case R.id.pay_detail:
                startActivity(new Intent(this, PaidListActivity.class));
                break;
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_module_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.introduce:
                Intent intent = new Intent(this, IntroduceActivity.class);
                intent.putExtra(AppConstant.EXTRA_APP_MODULE, ModuleType.LeaveApproval.getValue());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
