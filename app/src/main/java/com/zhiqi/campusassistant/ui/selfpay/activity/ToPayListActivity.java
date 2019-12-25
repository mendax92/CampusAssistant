package com.zhiqi.campusassistant.ui.selfpay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.decoration.HorizontalDividerItemDecoration;
import com.ming.base.widget.recyclerView.listener.OnItemClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.activity.BaseRefreshPageActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.payment.dagger.component.DaggerSelfPayComponent;
import com.zhiqi.campusassistant.core.payment.dagger.module.SelfPayModule;
import com.zhiqi.campusassistant.core.payment.entity.StudentExpense;
import com.zhiqi.campusassistant.core.payment.presenter.SelfPayPresenter;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.ui.selfpay.widget.StudentExpenseAdapter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ming on 17-8-1.
 * 待缴费列表
 */

public class ToPayListActivity extends BaseRefreshPageActivity<StudentExpense> {

    @BindView(R.id.user_header)
    ImageView userHeader;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.class_name)
    TextView classNameTxt;
    @BindView(R.id.user_number_text)
    TextView userNumber;

    @Inject
    SelfPayPresenter mPresenter;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_pay_to_list;
    }

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
        LoginUser user = LoginManager.getInstance().getLoginUser();
        if (user != null) {
            GlideApp.with(this).load(user.getHead()).placeholder(R.drawable.ic_user_default_head).into(userHeader);
            userName.setText(user.getReal_name());
            userNumber.setText(user.getUser_no());
            classNameTxt.setText(user.getPosition());
        }
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                StudentExpense expense = mAdapter.getItem(position);
                if (expense != null) {
                    Intent intent = new Intent(ToPayListActivity.this, PayInfoActivity.class);
                    intent.putExtra(AppConstant.EXTRA_PAY_EXPENSE_ID, expense.expense_id);
                    startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_PAY_CODE);
                }
            }
        });
        refresh();
    }

    @Override
    protected void onRefresh() {
        if (mPresenter != null) {
            mPresenter.getStudentExpense(page, this);
        }
    }

    @Override
    protected void onLoadMore(int page) {
        if (mPresenter != null) {
            mPresenter.getStudentExpense(page, this);
        }
    }

    @Override
    protected BaseQuickAdapter<StudentExpense> provideAdapter() {
        return new StudentExpenseAdapter();
    }

    @Override
    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return new HorizontalDividerItemDecoration.Builder(this)
                .showLastDivider()
                .backgroundResource(R.color.white)
                .marginProvider(new HorizontalDividerItemDecoration.MarginProvider() {
                    @Override
                    public int dividerLeftMargin(int position, RecyclerView parent) {
                        if (position < mAdapter.getItemCount() - 1) {
                            return getResources().getDimensionPixelSize(R.dimen.common_margin_s);
                        }
                        return 0;
                    }

                    @Override
                    public int dividerRightMargin(int position, RecyclerView parent) {
                        return 0;
                    }
                })
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.ACTIVITY_REQUEST_PAY_CODE == requestCode && RESULT_OK == resultCode) {
            refresh();
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.release();
        }
        super.onDestroy();
    }
}
