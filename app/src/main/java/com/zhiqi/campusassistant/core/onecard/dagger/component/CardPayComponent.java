package com.zhiqi.campusassistant.core.onecard.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.onecard.dagger.module.CardPayModule;
import com.zhiqi.campusassistant.ui.onecard.activity.CampusCardActivity;
import com.zhiqi.campusassistant.ui.onecard.activity.CardBalanceActivity;
import com.zhiqi.campusassistant.ui.onecard.activity.CardOrderDetailActivity;
import com.zhiqi.campusassistant.ui.onecard.activity.CardOrderListActivity;
import com.zhiqi.campusassistant.ui.onecard.activity.CardPaymentActivity;
import com.zhiqi.campusassistant.ui.onecard.activity.CardQrCodeActivity;
import com.zhiqi.campusassistant.ui.onecard.activity.CheckCardPwdActivity;

import dagger.Component;

@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = CardPayModule.class)
public interface CardPayComponent {

    void inject(CardBalanceActivity activity);

    void inject(CardPaymentActivity activity);

    void inject(CardOrderDetailActivity activity);

    void inject(CardOrderListActivity activity);

    void inject(CardQrCodeActivity activity);

    void inject(CheckCardPwdActivity activity);

    void inject(CampusCardActivity activity);
}
