package com.zhiqi.campusassistant.core.payment.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.payment.dagger.module.SelfPayModule;
import com.zhiqi.campusassistant.ui.selfpay.activity.PaidDetailActivity;
import com.zhiqi.campusassistant.ui.selfpay.activity.PaidListActivity;
import com.zhiqi.campusassistant.ui.selfpay.activity.PayInfoActivity;
import com.zhiqi.campusassistant.ui.selfpay.activity.ToPayListActivity;

import dagger.Component;

/**
 * Created by ming on 2017/7/23.
 * 自助支付component
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = SelfPayModule.class)
public interface SelfPayComponent {

    void inject(ToPayListActivity activity);

    void inject(PayInfoActivity activity);

    void inject(PaidListActivity activity);

    void inject(PaidDetailActivity activity);
}
