package com.zhiqi.campusassistant.ui.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.core.news.dagger.component.DaggerNewsComponent;
import com.zhiqi.campusassistant.core.news.dagger.module.NewsPresenterModule;
import com.zhiqi.campusassistant.core.news.entity.CategoryInfo;
import com.zhiqi.campusassistant.core.news.presenter.NewsPresenter;
import com.zhiqi.campusassistant.ui.news.widget.NewPageAdapter;
import com.zhiqi.campusassistant.ui.user.activity.IntroduceActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ming on 2017/3/11.
 * 新闻activity
 */

public class NewsActivity extends BaseLoadActivity<List<CategoryInfo>> implements ILoadView<List<CategoryInfo>> {

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;

    @BindView(R.id.tab_viewpager)
    ViewPager mPager;

    @Inject
    NewsPresenter mPresenter;

    private NewPageAdapter mPagerAdapter;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_common_tabpager;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
        refresh();
    }

    private void initDagger() {
        DaggerNewsComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .newsPresenterModule(new NewsPresenterModule())
                .build()
                .inject(this);
    }

    private void initView() {
        mPagerAdapter = new NewPageAdapter(this);
        mPager.setAdapter(mPagerAdapter);
        mTablayout.setupWithViewPager(mPager);
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryNewsCategory(this);
    }

    @Override
    public void onLoadData(List<CategoryInfo> data) {
        mPagerAdapter.setData(data);
        super.onLoadData(data);
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
                intent.putExtra(AppConstant.EXTRA_APP_MODULE, ModuleType.News.getValue());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }
}
