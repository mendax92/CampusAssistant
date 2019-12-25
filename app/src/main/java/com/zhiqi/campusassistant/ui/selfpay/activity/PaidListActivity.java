package com.zhiqi.campusassistant.ui.selfpay.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.listener.OnItemClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseRefreshPageActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.payment.dagger.component.DaggerSelfPayComponent;
import com.zhiqi.campusassistant.core.payment.dagger.module.SelfPayModule;
import com.zhiqi.campusassistant.core.payment.entity.SelfPaidInfo;
import com.zhiqi.campusassistant.core.payment.presenter.SelfPayPresenter;
import com.zhiqi.campusassistant.ui.selfpay.widget.SelfPaidAdapter;

import javax.inject.Inject;

/**
 * Created by ming on 17-8-4.
 * 自助缴费列表
 */

public class PaidListActivity extends BaseRefreshPageActivity<SelfPaidInfo> {

    @Inject
    SelfPayPresenter mPresenter;

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        init();
    }

    private void initDagger() {
        DaggerSelfPayComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .selfPayModule(new SelfPayModule())
                .build()
                .inject(this);
    }

    private void init() {
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                SelfPaidInfo paidInfo = mAdapter.getItem(position);
                if (paidInfo != null) {
                    switch (paidInfo.order_status) {
                        case Unpaid:
                            Intent intent = new Intent(PaidListActivity.this, PayInfoActivity.class);
                            intent.putExtra(AppConstant.EXTRA_PAY_EXPENSE_ID, paidInfo.expense_id);
                            startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_PAY_CODE);
                            break;
                        default:
                            intent = new Intent(PaidListActivity.this, PaidDetailActivity.class);
                            intent.putExtra(AppConstant.EXTRA_PAY_ORDER_ID, paidInfo.order_id);
                            startActivity(intent);
                            break;
                    }
                }
            }
        });
        refresh();
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.getPaidList(page, this);
    }

    @Override
    protected void onRefresh() {
        mPresenter.getPaidList(page, this);
    }

    @Override
    protected BaseQuickAdapter<SelfPaidInfo> provideAdapter() {
        return new SelfPaidAdapter();
    }

    @Override
    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int itemCount = parent.getAdapter().getItemCount();
                int top = getResources().getDimensionPixelSize(R.dimen.common_margin_xs);
                int bottom = 0;
                if (position == 0) {
                    top = getResources().getDimensionPixelSize(R.dimen.common_margin_s);
                } else if (position == itemCount - 1) {
                    bottom = getResources().getDimensionPixelSize(R.dimen.common_margin_xs);
                }
                outRect.set(0, top, 0, bottom);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.ACTIVITY_REQUEST_PAY_CODE == requestCode && RESULT_OK == resultCode) {
            refresh();
        }
    }
}
