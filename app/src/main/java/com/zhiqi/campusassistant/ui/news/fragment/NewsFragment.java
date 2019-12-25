package com.zhiqi.campusassistant.ui.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.fragment.BaseRefreshPageFragment;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.news.dagger.component.DaggerNewsComponent;
import com.zhiqi.campusassistant.core.news.dagger.module.NewsPresenterModule;
import com.zhiqi.campusassistant.core.news.entity.NewsInfo;
import com.zhiqi.campusassistant.core.news.presenter.NewsPresenter;
import com.zhiqi.campusassistant.ui.news.widget.HeadNewsPageAdapter;
import com.zhiqi.campusassistant.ui.news.widget.NewsItemAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ming on 2017/3/12.
 * 新闻列表
 */

public class NewsFragment extends BaseRefreshPageFragment<NewsInfo> {

    @Inject
    NewsPresenter mPresenter;

    @BindView(R.id.top_news)
    ViewPager headPager;

    private HeadNewsPageAdapter headPageAdapter;

    private int categoryId;

    public static NewsFragment newInstance(int categoryId) {
        NewsFragment frag = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstant.EXTRA_CATEGORY_ID, categoryId);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int onCreateView(Bundle savedInstanceState) {
        return R.layout.frag_news_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDagger();
        initView();
        initData();
    }

    private void initDagger() {
        DaggerNewsComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .newsPresenterModule(new NewsPresenterModule())
                .build()
                .inject(this);
    }

    private void initView() {
        headPageAdapter = new HeadNewsPageAdapter(getActivity());
        headPager.setAdapter(headPageAdapter);
        /*ViewGroup.LayoutParams params = headPager.getLayoutParams();
        params.height = 0;
        headPager.setLayoutParams(params);*/
        ViewGroup parent = (ViewGroup) headPager.getParent();
        parent.removeView(headPager);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.addHeaderView(headPager);
        headPager.setVisibility(View.GONE);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryId = bundle.getInt(AppConstant.EXTRA_CATEGORY_ID, 0);
        }
        refresh();
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryNewsList(categoryId, page, headLoad, this);
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.queryNewsList(categoryId, page, headLoad, this);
    }

    private ILoadView<List<NewsInfo>> headLoad = new ILoadView<List<NewsInfo>>() {
        @Override
        public void onLoadData(List<NewsInfo> data) {
            if (page == 1) {
                // 解决recyclerView滑动手势bug，不能设置为gone
                headPageAdapter.setData(data);
                if (data == null || data.isEmpty()) {
                    headPager.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    headPager.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailed(int errorCode, String message) {
        }
    };

    @Override
    protected BaseQuickAdapter<NewsInfo> provideAdapter() {
        return new NewsItemAdapter();
    }


    @Override
    public void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }
}
