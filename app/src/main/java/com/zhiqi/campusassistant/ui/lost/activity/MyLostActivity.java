package com.zhiqi.campusassistant.ui.lost.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.decoration.HorizontalDividerItemDecoration;
import com.ming.base.widget.recyclerView.listener.OnItemChildClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.activity.BaseRefreshPageActivity;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.ui.widget.BaseEmptyView;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.lost.dagger.component.DaggerLostFoundComponent;
import com.zhiqi.campusassistant.core.lost.dagger.module.LostFoundModule;
import com.zhiqi.campusassistant.core.lost.entity.MyLostInfo;
import com.zhiqi.campusassistant.core.lost.presenter.LostFoundPresenter;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.ui.lost.widget.MyLostAdapter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by ming on 2017/5/7.
 * 我的失物
 */

public class MyLostActivity extends BaseRefreshPageActivity<MyLostInfo> {

    private static final int REQUEST_APPLY = 101;

    @Inject
    LostFoundPresenter mPresenter;

    private TextView userName;
    private ImageView userHeader;

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        contentView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        initDagger();
        initView();
        initData();
    }

    private void initDagger() {
        DaggerLostFoundComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .lostFoundModule(new LostFoundModule())
                .build()
                .inject(this);
    }

    private void initView() {
        View headView = LayoutInflater.from(this).inflate(R.layout.view_lost_my_header, null);
        userName = ButterKnife.findById(headView, R.id.user_name);
        userHeader = ButterKnife.findById(headView, R.id.user_header);
        ButterKnife.findById(headView, R.id.lost_add).setOnClickListener(addClick);
        LoginUser user = LoginManager.getInstance().getLoginUser();
        if (user != null) {
            userName.setText(user.getNickname());
            GlideApp.with(this)
                    .load(user.getAvatar())
                    .placeholder(R.drawable.ic_user_square_header)
                    .into(userHeader);
        }
        mAdapter.addHeaderView(headView);
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.delete:
                        MyLostInfo info = mAdapter.getItem(position);
                        if (info != null) {
                            doDelete(position, info);
                        }
                        break;
                }
            }
        });
    }

    private void initData() {
        refresh();
    }

    private View.OnClickListener addClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(MyLostActivity.this, LostApplyActivity.class), REQUEST_APPLY);
        }
    };

    private void doDelete(final int position, final MyLostInfo info) {
        new MaterialDialog.Builder(this)
                .title(R.string.common_delete)
                .positiveText(R.string.common_delete)
                .negativeText(R.string.common_cancel)
                .content(R.string.common_confirm_delete)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.deleteLost(info.id, new IRequestView() {
                            @Override
                            public void onQuest(int errorCode, String message) {
                                ToastUtil.show(MyLostActivity.this, message);
                                if (HttpErrorCode.SUCCESS == errorCode) {
                                    mAdapter.remove(position);
                                }

                            }
                        });
                    }
                })
                .show();
    }


    @Override
    protected void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mPresenter.getMyLostList(page, this);
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.getMyLostList(page, this);
    }

    @Override
    protected BaseEmptyView provideEmptyView() {
        return null;
    }

    @Override
    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return new HorizontalDividerItemDecoration.Builder(this)
                .backgroundResource(R.color.white)
                .margin(getResources().getDimensionPixelSize(R.dimen.lost_my_margin_left), 0)
                .showLastDivider().build();
    }

    @Override
    protected BaseQuickAdapter<MyLostInfo> provideAdapter() {
        return new MyLostAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_APPLY == requestCode && RESULT_OK == resultCode) {
            refresh();
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }
}
