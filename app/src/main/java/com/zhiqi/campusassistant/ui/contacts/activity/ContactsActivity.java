package com.zhiqi.campusassistant.ui.contacts.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.user.entity.UserRole;
import com.zhiqi.campusassistant.ui.user.activity.IntroduceActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 2016/12/2.
 * 通讯录
 */

public class ContactsActivity extends BaseContactsActivity {


    @BindView(R.id.appbar)
    AppBarLayout mAppbar;

    private int departmentId = 0;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_contacts_list;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int visiblePosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    if (visiblePosition == 0) {
                        mAppbar.setExpanded(true, true);
                    }
                }
            }
        });
    }

    @OnClick({R.id.search_layout})
    void onClick(View view) {
        View animationView = findViewById(R.id.search_layout);
        Intent intent = new Intent(this, SearchContactsActivity.class);
        ActivityOptionsCompat options;
        if (Build.VERSION.SDK_INT >= 21) {
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, animationView, "search");
        } else {
            options = ActivityOptionsCompat.makeScaleUpAnimation(animationView, (int) animationView.getX(), (int) animationView.getY(), 0, 0);
        }
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    private void initData() {
        Intent bundle = getIntent();
        if (bundle != null) {
            departmentId = bundle.getIntExtra(AppConstant.EXTRA_DEPARTMENT_ID, 0);
        }
        refresh();
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryContactList(departmentId, this);
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
                intent.putExtra(AppConstant.EXTRA_APP_MODULE,
                        UserRole.Student == LoginManager.getInstance().getLoginUser().getRole_type() ?
                                ModuleType.StudentContacts.getValue() : ModuleType.StaffContacts.getValue());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
