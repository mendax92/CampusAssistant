package com.zhiqi.campusassistant.ui.onecard.activity;

import android.content.Intent;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.listener.OnItemClickListener;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseRefreshPageActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.onecard.dagger.component.DaggerCardPayComponent;
import com.zhiqi.campusassistant.core.onecard.dagger.module.CardPayModule;
import com.zhiqi.campusassistant.core.onecard.entity.CardOrderDetail;
import com.zhiqi.campusassistant.core.onecard.presenter.CardPayPresenter;
import com.zhiqi.campusassistant.ui.onecard.widget.OrderDetailAdapter;

import javax.inject.Inject;

/**
 * Created by minh on 18-2-2.
 * 校园卡订单列表
 */

public class CardOrderListActivity extends BaseRefreshPageActivity<CardOrderDetail> {

    @Inject
    CardPayPresenter mPresenter;

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
    }

    private void initDagger() {
        DaggerCardPayComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .cardPayModule(new CardPayModule())
                .build()
                .inject(this);
    }

    private void initView() {
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                CardOrderDetail detail = (CardOrderDetail) adapter.getItem(position);
                if (detail != null) {
                    Intent intent = new Intent(CardOrderListActivity.this, CardOrderDetailActivity.class);
                    intent.putExtra(AppConstant.EXTRA_PAY_ORDER_NO, detail.order_no);
                    startActivity(intent);
                }
            }
        });
        refresh();
    }

    @Override
    protected void onRefresh() {
        mPresenter.getOrderList(page, this);
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.getOrderList(page, this);
    }

    @Override
    protected BaseQuickAdapter<CardOrderDetail> provideAdapter() {
        return new OrderDetailAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
    }
}
