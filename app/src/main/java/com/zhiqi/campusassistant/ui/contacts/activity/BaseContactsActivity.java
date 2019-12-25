package com.zhiqi.campusassistant.ui.contacts.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.decoration.HorizontalDividerItemDecoration;
import com.ming.base.widget.recyclerView.listener.OnItemClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseRefreshListActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.user.dagger.component.DaggerUserInfoComponent;
import com.zhiqi.campusassistant.core.user.dagger.module.UserInfoModule;
import com.zhiqi.campusassistant.core.user.entity.ContactsList;
import com.zhiqi.campusassistant.core.user.entity.DepartmentInfo;
import com.zhiqi.campusassistant.core.user.entity.UserInfo;
import com.zhiqi.campusassistant.core.user.presenter.UserInfoPresenter;
import com.zhiqi.campusassistant.ui.contacts.widget.UserInfoListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ming on 17-11-1.
 * 联系人基础activity
 */

public abstract class BaseContactsActivity extends BaseRefreshListActivity<Object> implements ILoadView<ContactsList> {

    @Inject
    UserInfoPresenter mPresenter;

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
    }

    private void initDagger() {
        DaggerUserInfoComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .userInfoModule(new UserInfoModule())
                .build().inject(this);
    }

    private void initView() {
        mRecyclerView.addOnItemTouchListener(itemClickListener);
    }


    @Override
    protected BaseQuickAdapter<Object> provideAdapter() {
        return new UserInfoListAdapter();
    }

    @Override
    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return new HorizontalDividerItemDecoration.Builder(this)
                .backgroundResource(R.color.white)
                .showLastDivider()
                .margin(getResources().getDimensionPixelSize(R.dimen.contacts_list_margin), 0)
                .build();
    }

    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
            Object item = mAdapter.getItem(position);
            if (item instanceof DepartmentInfo) {
                Intent intent = new Intent(BaseContactsActivity.this, ContactsActivity.class);
                intent.putExtra(AppConstant.EXTRA_DEPARTMENT_ID, ((DepartmentInfo) item).id);
                startActivity(intent);
            } else {
                Intent intent = new Intent(BaseContactsActivity.this, UserInfoActivity.class);
                intent.putExtra(AppConstant.EXTRA_USER_INFO, (UserInfo) item);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onLoadData(ContactsList data) {
        if (data == null) {
            return;
        }
        List<Object> contacts = new ArrayList<>();
        if (data.children != null) {
            contacts.addAll(data.children);
        }
        if (data.persons != null) {
            contacts.addAll(data.persons);
        }
        super.onLoadData(contacts);
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }
}
