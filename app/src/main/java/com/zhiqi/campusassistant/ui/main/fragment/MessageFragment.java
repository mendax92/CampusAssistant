package com.zhiqi.campusassistant.ui.main.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ming.base.activity.BaseActivity;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.decoration.HorizontalDividerItemDecoration;
import com.ming.base.widget.recyclerView.listener.OnItemChildClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.fragment.BaseRefreshListFragment;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.core.message.dagger.component.DaggerMessageComponent;
import com.zhiqi.campusassistant.core.message.dagger.module.MessageModule;
import com.zhiqi.campusassistant.core.message.entity.ModuleInfo;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.core.message.presenter.MessagePresenter;
import com.zhiqi.campusassistant.core.news.entity.NewsInfo;
import com.zhiqi.campusassistant.core.news.entity.NewsType;
import com.zhiqi.campusassistant.ui.contacts.activity.ContactsActivity;
import com.zhiqi.campusassistant.ui.leave.activity.LeaveChooseActivity;
import com.zhiqi.campusassistant.ui.leave.activity.LeaveRecordActivity;
import com.zhiqi.campusassistant.ui.login.util.LoginHelper;
import com.zhiqi.campusassistant.ui.main.activity.HomeActivity;
import com.zhiqi.campusassistant.ui.main.view.ITabView;
import com.zhiqi.campusassistant.ui.message.widget.MessageAdapter;
import com.zhiqi.campusassistant.ui.news.activity.NewsActivity;
import com.zhiqi.campusassistant.ui.onecard.activity.CampusCardActivity;
import com.zhiqi.campusassistant.ui.onecard.activity.CardQrCodeActivity;
import com.zhiqi.campusassistant.ui.repair.activity.RepairApprovalActivity;
import com.zhiqi.campusassistant.ui.repair.activity.RepairRecordActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ming on 2016/12/20.
 * 消息
 */
public class MessageFragment extends BaseRefreshListFragment<ModuleInfo> implements ITabView, ILoadView<List<ModuleInfo>> {

    @Inject
    MessagePresenter mPresenter;

    @Override
    public int onCreateView(Bundle savedInstanceState) {
        setActionbarTitle(R.string.common_message);
        return R.layout.frag_message;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDagger();
        initView();
    }

    private void initDagger() {
        DaggerMessageComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .messageModule(new MessageModule())
                .build()
                .inject(this);
    }

    private void initView() {
        setHasOptionsMenu(true);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.msg_module_top:
                        ModuleInfo item = mAdapter.getItem(position);
                        if (item != null) {
                            switch (item.module_id) {
                                case LeaveApply:
                                    startActivity(new Intent(getActivity(), LeaveRecordActivity.class));
                                    break;
                                case LeaveApproval:
                                    startActivity(new Intent(getActivity(), LeaveChooseActivity.class));
                                    break;
                                case RepairApply:
                                    startActivity(new Intent(getActivity(), RepairRecordActivity.class));
                                    break;
                                case RepairApproval:
                                    startActivity(new Intent(getActivity(), RepairApprovalActivity.class));
                                    break;
                                case CampusCard:
                                    startActivity(new Intent(getActivity(), CampusCardActivity.class));
                                    break;
                                case News:
                                    startActivity(new Intent(getActivity(), NewsActivity.class));
                                    break;
                                case StaffContacts:
                                case StudentContacts:
                                    startActivity(new Intent(getActivity(), ContactsActivity.class));
                                    break;
                                case CourseSchedule:
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    intent.putExtra(HomeActivity.CURRENT_PAGE_INDEX, 2);
                                    startActivity(intent);
                                    break;
                            }
                        }
                        break;
                }
            }
        });
        refresh();
    }

    @Override
    public void onLoadData(List<ModuleInfo> data) {
        if (data != null && !data.isEmpty()) {
            for (ModuleInfo info : data) {
                // 改变后面新闻展示类型
                if (ModuleType.News == info.module_id && info.data != null && !info.data.isEmpty() && info.data.size() > 1) {
                    List<NewsInfo> items = (List<NewsInfo>) info.data;
                    for (int i = 1; i < items.size(); i++) {
                        NewsInfo news = items.get(i);
                        news.show_type = TextUtils.isEmpty(news.thumbnails) ? NewsType.Title : NewsType.Fix;
                    }
                }
            }
        }
        super.onLoadData(data);
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryMessageList(this);
    }

    @Override
    protected BaseQuickAdapter<ModuleInfo> provideAdapter() {
        return new MessageAdapter();
    }

    @Override
    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return new ItemDecoration(new ItemDecoration.Builder(getContext()).backgroundResource(R.color.white).showLastDivider());
    }

    private class ItemDecoration extends HorizontalDividerItemDecoration {

        protected ItemDecoration(Builder builder) {
            super(builder);
        }

        @Override
        protected void setItemOffsets(Rect outRect, int position, RecyclerView parent) {
            super.setItemOffsets(outRect, position, parent);
            if (mAdapter.getDataCount() > 0) {
                int bottom = outRect.bottom + getResources().getDimensionPixelSize(R.dimen.common_margin_xs);
                outRect.set(outRect.left, outRect.top, outRect.right, bottom);
            }
        }
    }

    @Override
    public void onChecked() {
        invalidateActionbar();
    }

    @Override
    public void onUnchecked() {
        resetActionbar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.common_qr_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.qr_code:
                LoginHelper.checkBindPhone((BaseActivity) getActivity(), R.string.one_card_bind_phone_tip, new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getActivity(), CardQrCodeActivity.class));
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }
}
