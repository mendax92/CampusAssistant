package com.zhiqi.campusassistant.ui.repair.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.ming.base.util.AppUtil;
import com.ming.base.util.TimeUtil;
import com.ming.base.widget.listView.LinearListView;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.entity.ImageData;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.ui.widget.ImageAdapter;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.repair.dagger.component.DaggerRepairComponent;
import com.zhiqi.campusassistant.core.repair.dagger.module.RepairModule;
import com.zhiqi.campusassistant.core.repair.entity.CampusDistrict;
import com.zhiqi.campusassistant.core.repair.entity.RepairAction;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplyRequest;
import com.zhiqi.campusassistant.core.repair.entity.RepairDetails;
import com.zhiqi.campusassistant.core.repair.entity.RepairDictionary;
import com.zhiqi.campusassistant.core.repair.presenter.RepairPresenter;
import com.zhiqi.campusassistant.ui.repair.widget.RepairActionAdapter;
import com.zhiqi.campusassistant.ui.repair.widget.RepairHelper;
import com.zhiqi.campusassistant.ui.repair.widget.RepairStepAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ming on 2017/1/31.
 * 维修详情
 */

public class RepairDetailActivity extends BaseLoadActivity<RepairDetails> implements ILoadView<RepairDetails>, IRequestView {

    @BindView(R.id.applicant_user)
    ImageView applicantUser;
    @BindView(R.id.applicant_name)
    TextView applicantName;
    @BindView(R.id.apply_time)
    TextView applyTime;
    @BindView(R.id.repair_status)
    TextView repairStatus;
    @BindView(R.id.repair_type)
    TextView repairType;
    @BindView(R.id.repair_no)
    TextView repairNo;
    @BindView(R.id.repair_detail)
    TextView repairDetail;
    @BindView(R.id.repair_img_layout)
    View repairImgLayout;
    @BindView(R.id.repair_img)
    RecyclerView repairImgList;
    @BindView(R.id.repair_contact)
    TextView repairContact;
    @BindView(R.id.repair_area)
    TextView repairArea;
    @BindView(R.id.repair_appointment_time)
    TextView repairAppointmentTime;
    @BindView(R.id.repair_progress)
    RecyclerView repairProgressList;
    @BindView(R.id.repair_action)
    LinearListView repairActions;
    @BindView(R.id.repair_img_line)
    View imgsLine;
    @BindView(R.id.repair_action_layout)
    LinearLayout actionLayout;

    @Inject
    RepairPresenter mPresenter;

    private ImageAdapter imageAdapter;

    private RepairActionAdapter actionAdapter;

    private RepairDetails repairData;

    private RepairDictionary repairDictionary;

    private RepairStepAdapter stepAdapter;

    private long repairId;

    private boolean isSelf;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_repair_detail;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
        initData();
    }

    private void initDagger() {
        DaggerRepairComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .repairModule(new RepairModule())
                .build()
                .inject(this);
    }

    private void initView() {
        ViewCompat.setNestedScrollingEnabled(repairProgressList, false);
        imageAdapter = new ImageAdapter(this, null);

        ViewPreloadSizeProvider<ImageData> preloadSizeProvider = new ViewPreloadSizeProvider<>();
        RecyclerViewPreloader<ImageData> preloader =
                new RecyclerViewPreloader<>(GlideApp.with(this), imageAdapter, preloadSizeProvider, 5);
        repairImgList.addOnScrollListener(preloader);

        repairImgList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        repairImgList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int itemCount = parent.getAdapter().getItemCount();
                int left = getResources().getDimensionPixelSize(R.dimen.common_margin_xxs);
                int right = left;
                if (position == 0) {
                    left = getResources().getDimensionPixelSize(R.dimen.common_margin_xs);
                } else if (position == itemCount - 1) {
                    right = getResources().getDimensionPixelSize(R.dimen.common_margin_xs);
                }
                outRect.set(left, 0, right, 0);
            }
        });
        repairImgList.setAdapter(imageAdapter);

        actionAdapter = new RepairActionAdapter(this);
        repairActions.setAdapter(actionAdapter);
        repairActions.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                RepairAction action = actionAdapter.getItem(position);
                doAction(action);
            }
        });

        stepAdapter = new RepairStepAdapter();
        repairProgressList.setLayoutManager(new LinearLayoutManager(this));
        repairProgressList.setHasFixedSize(true);
        repairProgressList.setAdapter(stepAdapter);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        repairId = intent.getLongExtra(AppConstant.EXTRA_REPAIR_ID, 0);
        if (repairId <= 0) {
            finish();
            return;
        }
        isSelf = intent.getBooleanExtra(AppConstant.EXTRA_IS_SELF, false);
        mPresenter.loadVersionData(dictionaryLoad);
        refresh();
    }

    private ILoadView<RepairDictionary> dictionaryLoad = new ILoadView<RepairDictionary>() {
        @Override
        public void onLoadData(RepairDictionary data) {
            repairDictionary = data;
        }

        @Override
        public void onFailed(int errorCode, String message) {
            ToastUtil.show(RepairDetailActivity.this, message);
        }
    };

    private void fillView() {
        if (repairData == null) {
            finish();
            return;
        }
        GlideApp.with(this).load(repairData.user_head)
                .placeholder(R.drawable.img_user_default_square_head)
                .into(applicantUser);
        applicantName.setText(repairData.applicant_name);
        applyTime.setText(getString(R.string.apply_apply_date, repairData.apply_time));
        ViewCompat.setBackground(repairStatus, RepairHelper.getRepairStatusBackground(this, repairData.status));
        repairStatus.setText(repairData.status_name);
        repairType.setText(repairData.type_name);
        repairNo.setText(repairData.order_no);
        repairDetail.setText(repairData.detail);
        repairContact.setText(repairData.phone);
        if (repairData.appointment > 0) {
            repairAppointmentTime.setText(TimeUtil.getTime(repairData.appointment * 1000L, TimeUtil.FORMAT_MINUTE));
        } else {
            repairAppointmentTime.setText("");
        }
        if (repairData.images == null || repairData.images.isEmpty()) {
            imgsLine.setVisibility(View.GONE);
            repairImgLayout.setVisibility(View.GONE);
        } else {
            imageAdapter.setNewData(repairData.images);
            repairImgLayout.setVisibility(View.VISIBLE);
            imgsLine.setVisibility(View.VISIBLE);
        }
        if (repairData.user_actions == null || repairData.user_actions.isEmpty()) {
            actionLayout.setVisibility(View.GONE);
        } else {
            actionAdapter.setNewData(repairData.user_actions);
            actionLayout.setVisibility(View.VISIBLE);
        }
        if (repairDictionary != null) {
            List<CampusDistrict> districts = repairDictionary.campus_subarea != null ? repairDictionary.campus_subarea.items : null;
            String district = "";
            String location = "";
            if (districts != null) {
                for (CampusDistrict campusDistrict : districts) {
                    if (repairData.district_id == campusDistrict.district_id) {
                        district = campusDistrict.value;
                        List<CampusDistrict.CampusLocation> subareas = campusDistrict.subareas;
                        if (subareas != null) {
                            for (CampusDistrict.CampusLocation campusLocation : subareas) {
                                if (repairData.location_id == campusLocation.location_id) {
                                    location = campusLocation.value;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            String address = getString(R.string.repair_address_concat, district, location, repairData.address);
            repairArea.setText(address);
        }
        if (repairData.steps == null) {
            repairProgressList.setVisibility(View.GONE);
        } else {
            stepAdapter.setNewData(repairData.steps);
            repairProgressList.setVisibility(View.VISIBLE);
        }
    }

    private void doAction(RepairAction action) {
        if (action == null) {
            return;
        }
        switch (action) {
            case Contact:
                requestPermissions(false, Manifest.permission.CALL_PHONE);
                break;
            case Edit:
                RepairApplyRequest request = RepairHelper.convertDetails(repairData);
                Intent intent = new Intent(this, RepairApplyActivity.class);
                intent.putExtra(AppConstant.EXTRA_REPAIR_REQUEST, request);
                startActivityForResult(intent, AppConstant.REQUEST_CODE_FOR_UPDATE);
                break;
            case Reject:
                showRejectDialog(action);
                break;
            default:
                requestAction(action, null);
                break;
        }
    }

    private void requestAction(RepairAction action, String comment) {
        ProgressDialogUtil.show(this, R.string.common_commit_ing);
        mPresenter.doAction(repairData.id, action, comment, this);
    }

    private void showRejectDialog(final RepairAction action) {
        new MaterialDialog.Builder(this)
                .title(R.string.repair_reject_dialog_title)
                .inputRange(0, AppConstant.ARROVAL_COMMENT_MAX_LENGTH)
                .input(R.string.repair_reject_input_hint, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String comment = input.toString().trim();
                        requestAction(action, comment.length() == 0 ? null : comment);
                    }
                })
                .negativeText(R.string.common_cancel)
                .show();
    }

    @Override
    public void onLoadData(RepairDetails data) {
        super.onLoadData(data);
        if (data != null) {
            repairData = data;
            fillView();
        }
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryRepairDetails(repairId, isSelf, this);
    }

    @Override
    public void onQuest(int errorCode, String message) {
        if (HttpErrorCode.SUCCESS == errorCode) {
            ProgressDialogUtil.success(message);
            refresh();
            setResult(RESULT_OK);
        } else {
            ProgressDialogUtil.error(message);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE_FOR_UPDATE == requestCode && RESULT_OK == resultCode) {
            setResult(RESULT_OK);
            refresh();
        }
    }

    @Override
    protected void onPermissionGranted(String[] permissions) {
        super.onPermissionGranted(permissions);
        AppUtil.callTel(this, repairData.phone);
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        ProgressDialogUtil.dismiss();
        super.onDestroy();
    }
}
