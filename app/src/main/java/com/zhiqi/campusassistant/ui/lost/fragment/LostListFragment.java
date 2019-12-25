package com.zhiqi.campusassistant.ui.lost.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ming.base.util.AppUtil;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.listener.OnItemChildClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.fragment.BaseRefreshPageFragment;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.lost.dagger.component.DaggerLostFoundComponent;
import com.zhiqi.campusassistant.core.lost.dagger.module.LostFoundModule;
import com.zhiqi.campusassistant.core.lost.entity.LostApplyType;
import com.zhiqi.campusassistant.core.lost.entity.LostInfo;
import com.zhiqi.campusassistant.core.lost.presenter.LostFoundPresenter;
import com.zhiqi.campusassistant.ui.lost.activity.MyLostActivity;
import com.zhiqi.campusassistant.ui.lost.widget.LostListAdapter;

import javax.inject.Inject;

/**
 * Created by ming on 2017/5/5.
 * 失物招领fragment
 */

public class LostListFragment extends BaseRefreshPageFragment<LostInfo> {

    @Inject
    LostFoundPresenter mPresenter;

    private LostApplyType lostType;

    private String clickTel;

    public static LostListFragment newInstance(LostApplyType lostType) {
        Bundle args = new Bundle();
        args.putInt(AppConstant.EXTRA_LOST_TYPE, lostType.getValue());
        LostListFragment fragment = new LostListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        setHasOptionsMenu(true);
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.call:
                        clickTel = mAdapter.getItem(position).phone;
                        requestPermissions(false, Manifest.permission.CALL_PHONE);
                        break;
                }
            }
        });
    }


    private void initData() {
        Bundle args = getArguments();
        if (args != null) {
            lostType = LostApplyType.formatValue(args.getInt(AppConstant.EXTRA_LOST_TYPE, LostApplyType.Lost.getValue()));
        }
        if (lostType == null) {
            return;
        }
        refresh();
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.getLostList(lostType, page, this);
    }

    @Override
    protected void onRefresh() {
        mPresenter.getLostList(lostType, page, this);
    }

    @Override
    protected BaseQuickAdapter<LostInfo> provideAdapter() {
        return new LostListAdapter();
    }

    @Override
    public void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lost_module_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.introduce:
                Intent intent = new Intent(getActivity(), IntroduceActivity.class);
                intent.putExtra(AppConstant.EXTRA_APP_MODULE, ModuleType.Lost.getValue());
                startActivity(intent);
                return true;*/
            case R.id.add:
                startActivity(new Intent(getActivity(), MyLostActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPermissionGranted(String[] permissions) {
        super.onPermissionGranted(permissions);
        AppUtil.callTel(getActivity(), clickTel);
        clickTel = null;
    }
}
