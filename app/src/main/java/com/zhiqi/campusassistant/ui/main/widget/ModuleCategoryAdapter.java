package com.zhiqi.campusassistant.ui.main.widget;

import android.content.Intent;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.ming.base.activity.BaseActivity;
import com.ming.base.util.Log;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.ming.base.widget.recyclerView.listener.OnItemClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.app.entity.AppInfo;
import com.zhiqi.campusassistant.core.app.entity.ModuleCategory;
import com.zhiqi.campusassistant.core.app.presenter.AppPresenter;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.ui.bedroom.activity.BedRoomInfoActivity;
import com.zhiqi.campusassistant.ui.contacts.activity.ContactsActivity;
import com.zhiqi.campusassistant.ui.entrance.activity.EntranceGuideActivity;
import com.zhiqi.campusassistant.ui.leave.activity.LeaveChooseActivity;
import com.zhiqi.campusassistant.ui.leave.activity.LeaveRecordActivity;
import com.zhiqi.campusassistant.ui.location.activity.CampusNavActivity;
import com.zhiqi.campusassistant.ui.login.util.LoginHelper;
import com.zhiqi.campusassistant.ui.lost.activity.LostFoundActivity;
import com.zhiqi.campusassistant.ui.news.activity.NewsActivity;
import com.zhiqi.campusassistant.ui.notice.activity.NoticeActivity;
import com.zhiqi.campusassistant.ui.onecard.activity.CampusCardActivity;
import com.zhiqi.campusassistant.ui.repair.activity.RepairApprovalActivity;
import com.zhiqi.campusassistant.ui.repair.activity.RepairRecordActivity;
import com.zhiqi.campusassistant.ui.scores.activity.ScoresListActivity;
import com.zhiqi.campusassistant.ui.selfpay.activity.SelfPayActivity;
import com.zhiqi.campusassistant.ui.web.activity.WebActivity;

/**
 * Created by ming on 2017/5/2.
 * 应用分类adapter
 */

public class ModuleCategoryAdapter extends BaseQuickAdapter<ModuleCategory> {

    private SparseArrayCompat<OnItemClickListener> itemListeners = new SparseArrayCompat<>();
    AppPresenter mPresenter;

    public ModuleCategoryAdapter() {
        super(R.layout.item_module_category, null);
    }

    public void setPresenter(AppPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, ModuleCategory item, int position) {
        helper.setText(R.id.category_name, item.module_category_name);
        RecyclerView recyclerView = helper.getView(R.id.category_app);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        AppAdapter itemAdapter = new AppAdapter();
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        itemAdapter.setNewData(item.list);
        OnItemClickListener listener = getItemClickListener(position);
        recyclerView.removeOnItemTouchListener(listener);
        recyclerView.addOnItemTouchListener(listener);
    }

    private OnItemClickListener getItemClickListener(int position) {
        OnItemClickListener listener = itemListeners.get(position);
        if (listener == null) {
            listener = new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                    AppInfo item = ((AppAdapter) adapter).getItem(position);
                    if (item == null || item.module_id == null || !item.is_release) {
                        return;
                    }
                    if (!TextUtils.isEmpty(item.detail_information) && item.detail_information.equals("#")) {
                        goToNative(item);
                    } else {
                        Log.e("wwz","001");
                        if (mPresenter != null) {
                            Log.e("wwz","002");
                            mPresenter.checkAppToWhere(item.detail_information);
                        }
                    }
                }
            };
            itemListeners.put(position, listener);
        }
        return listener;
    }

    private void goToNative(AppInfo item) {
        switch (item.module_id) {
            case LeaveApply:
                mContext.startActivity(new Intent(mContext, LeaveRecordActivity.class));
                break;
            case LeaveApproval:
                mContext.startActivity(new Intent(mContext, LeaveChooseActivity.class));
                break;
            case RepairApply:
                mContext.startActivity(new Intent(mContext, RepairRecordActivity.class));
                break;
            case RepairApproval:
                mContext.startActivity(new Intent(mContext, RepairApprovalActivity.class));
                break;
            case CampusCard:
                LoginHelper.checkBindPhone((BaseActivity) mContext, R.string.one_card_bind_phone_tip, new Runnable() {
                    @Override
                    public void run() {
                        mContext.startActivity(new Intent(mContext, CampusCardActivity.class));
                    }
                });
                break;
            case News:
                mContext.startActivity(new Intent(mContext, NewsActivity.class));
                break;
            case StaffContacts:
            case StudentContacts:
                mContext.startActivity(new Intent(mContext, ContactsActivity.class));
                break;
            case Notice:
                mContext.startActivity(new Intent(mContext, NoticeActivity.class));
                break;
            case Lost:
                mContext.startActivity(new Intent(mContext, LostFoundActivity.class));
                break;
            case Score:
                mContext.startActivity(new Intent(mContext, ScoresListActivity.class));
                break;
            case AutoPayment:
                mContext.startActivity(new Intent(mContext, SelfPayActivity.class));
                break;
            case FreshmanBed:
                mContext.startActivity(new Intent(mContext, BedRoomInfoActivity.class));
                break;
            case FreshmanGuide:
                mContext.startActivity(new Intent(mContext, CampusNavActivity.class));
                break;
            case EntranceGuide:
                mContext.startActivity(new Intent(mContext, EntranceGuideActivity.class));
                break;
            case CampusNetwork:
                LoginUser user = LoginManager.getInstance().getLoginUser();
                if (user != null) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra(AppConstant.EXTRA_URL, String.format(HttpUrlConstant.OA_CAMPUS_NETWORK, user.getUser_id()));
                    mContext.startActivity(intent);
                }
                break;
        }
    }
}
