package com.zhiqi.campusassistant.ui.notice.activity;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseRefreshPageActivity;
import com.zhiqi.campusassistant.core.notice.dagger.component.DaggerNoticeComponent;
import com.zhiqi.campusassistant.core.notice.dagger.module.NoticeModule;
import com.zhiqi.campusassistant.core.notice.entity.NoticeInfo;
import com.zhiqi.campusassistant.core.notice.presenter.NoticePresenter;
import com.zhiqi.campusassistant.ui.notice.widget.NoticeAdapter;

import javax.inject.Inject;

/**
 * Created by ming on 2017/5/3.
 * 通知公告
 */

public class NoticeActivity extends BaseRefreshPageActivity<NoticeInfo> {

    @Inject
    NoticePresenter mPresenter;

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
    }

    private void initDagger() {
        DaggerNoticeComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .noticeModule(new NoticeModule())
                .build()
                .inject(this);
    }

    private void initView() {
        refresh();
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.queryNoticeList(page, this);
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryNoticeList(page, this);
    }

    @Override
    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int top = getResources().getDimensionPixelSize(R.dimen.common_margin_xs);
                if (position == 0) {
                    top = getResources().getDimensionPixelSize(R.dimen.common_margin_s);
                }
                outRect.set(0, top, 0, 0);
            }
        };
    }

    @Override
    protected BaseQuickAdapter<NoticeInfo> provideAdapter() {
        return new NoticeAdapter();
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }
}
