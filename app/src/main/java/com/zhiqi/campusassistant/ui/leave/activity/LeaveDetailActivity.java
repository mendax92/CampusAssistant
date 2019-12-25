package com.zhiqi.campusassistant.ui.leave.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.ming.base.util.NumberUtil;
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
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.leave.dagger.component.DaggerLeaveComponent;
import com.zhiqi.campusassistant.core.leave.dagger.module.LeavePresenterModule;
import com.zhiqi.campusassistant.core.leave.entity.LeaveAction;
import com.zhiqi.campusassistant.core.leave.entity.LeaveDetails;
import com.zhiqi.campusassistant.core.leave.entity.LeaveRequest;
import com.zhiqi.campusassistant.core.leave.entity.LeaveStatus;
import com.zhiqi.campusassistant.core.leave.presenter.LeavePresenter;
import com.zhiqi.campusassistant.ui.leave.widget.LeaveActionAdapter;
import com.zhiqi.campusassistant.ui.leave.widget.LeaveHelper;
import com.zhiqi.campusassistant.ui.leave.widget.LeaveStepAdapter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ming on 2017/3/9.
 * 请假详情
 */

public class LeaveDetailActivity extends BaseLoadActivity<LeaveDetails> implements ILoadView<LeaveDetails>, IRequestView {

    @BindView(R.id.applicant_user)
    ImageView applicantUser;
    @BindView(R.id.applicant_name)
    TextView applicantName;
    @BindView(R.id.apply_time)
    TextView applyTime;
    @BindView(R.id.leave_status)
    TextView leaveStatus;
    @BindView(R.id.leave_type)
    TextView leaveType;
    @BindView(R.id.department_name)
    TextView departmentName;
    @BindView(R.id.leave_time)
    TextView leaveTime;
    @BindView(R.id.leave_detail)
    TextView leaveDetail;
    @BindView(R.id.leave_img_layout)
    View leaveImgLayout;
    @BindView(R.id.repair_img)
    RecyclerView leaveImgList;
    @BindView(R.id.leave_progress)
    RecyclerView leaveProgressList;
    @BindView(R.id.leave_action)
    LinearListView leaveActions;
    @BindView(R.id.leave_img_line)
    View imgsLine;
    @BindView(R.id.leave_action_layout)
    LinearLayout actionLayout;

    @Inject
    LeavePresenter mPresenter;

    private ImageAdapter imageAdapter;

    private LeaveActionAdapter actionAdapter;

    private LeaveStepAdapter stepAdapter;

    private long leaveId;

    private LeaveDetails details;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_leave_detail;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
        initData();
    }

    private void initDagger() {
        DaggerLeaveComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .leavePresenterModule(new LeavePresenterModule())
                .build()
                .inject(this);
    }

    private void initView() {
        ViewCompat.setNestedScrollingEnabled(leaveProgressList, false);

        imageAdapter = new ImageAdapter(this, null);
        ViewPreloadSizeProvider<ImageData> preloadSizeProvider = new ViewPreloadSizeProvider<>();
        RecyclerViewPreloader<ImageData> preloader =
                new RecyclerViewPreloader<>(GlideApp.with(this), imageAdapter, preloadSizeProvider, 3);
        leaveImgList.addOnScrollListener(preloader);

        leaveImgList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        leaveImgList.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        leaveImgList.setAdapter(imageAdapter);

        actionAdapter = new LeaveActionAdapter(this);
        leaveActions.setAdapter(actionAdapter);
        leaveActions.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                LeaveAction action = actionAdapter.getItem(position);
                doAction(action);
            }
        });

        stepAdapter = new LeaveStepAdapter();
        leaveProgressList.setLayoutManager(new LinearLayoutManager(this));
        leaveProgressList.setHasFixedSize(true);
        leaveProgressList.setAdapter(stepAdapter);

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        leaveId = intent.getLongExtra(AppConstant.EXTRA_LEAVE_ID, 0);
        if (leaveId <= 0) {
            finish();
            return;
        }
        refresh();
    }

    private void fillView() {
        if (details == null) {
            finish();
            return;
        }
        GlideApp.with(this).load(details.head)
                .placeholder(R.drawable.img_user_default_square_head)
                .into(applicantUser);
        applicantName.setText(details.applicant_name);
        applyTime.setText(details.apply_time);
        ViewCompat.setBackground(leaveStatus, LeaveHelper.getLeaveStatusBackground(this, details.status));
        leaveStatus.setText(LeaveStatus.Processing == details.status ? getString(R.string.leave_wait_status, details.approver) : details.status_name);
        leaveType.setText(getString(R.string.leave_describe, details.type_name, NumberUtil.format(details.total_days, 1)));
        String startTime = TimeUtil.getTime(details.start_time * 1000L, getString(R.string.common_date_format_minute));
        String endTime = TimeUtil.getTime(details.end_time * 1000L, getString(R.string.common_date_format_minute));
        leaveTime.setText(getString(R.string.leave_data_content, startTime, endTime));
        departmentName.setText(details.department);
        leaveDetail.setText(details.reason);

        if (details.images == null || details.images.isEmpty()) {
            imgsLine.setVisibility(View.GONE);
            leaveImgLayout.setVisibility(View.GONE);
        } else {
            imageAdapter.setNewData(details.images);
            imgsLine.setVisibility(View.VISIBLE);
            leaveImgLayout.setVisibility(View.VISIBLE);
        }
        if (details.user_actions == null || details.user_actions.isEmpty()) {
            actionLayout.setVisibility(View.GONE);
        } else {
            actionAdapter.setNewData(details.user_actions);
            actionLayout.setVisibility(View.VISIBLE);
        }

        if (details.steps == null) {
            leaveProgressList.setVisibility(View.GONE);
        } else {
            stepAdapter.setNewData(details.steps);
            leaveProgressList.setVisibility(View.VISIBLE);
        }
    }

    private void doAction(LeaveAction action) {
        if (action == null) {
            return;
        }
        switch (action) {
            case Edit:
                LeaveRequest request = LeaveHelper.convertDetails(details);
                Intent intent = new Intent(this, LeaveApplyActivity.class);
                intent.putExtra(AppConstant.EXTRA_LEAVE_REQUEST, request);
                startActivityForResult(intent, AppConstant.REQUEST_CODE_FOR_UPDATE);
                break;
            case Reject:
                showRejectDialog(action);
                break;
            case Agree:
                showActionDialog(R.string.leave_agree_dialog_title, action);
                break;
            case Cancel:
                showActionDialog(R.string.leave_cancel_dialog_title, action);
                break;
            default:
                requestAction(action, null);
                break;
        }
    }

    private void requestAction(LeaveAction action, String comment) {
        comment = TextUtils.isEmpty(comment) ? LeaveHelper.getLeaveActionText(this, action) : comment;
        ProgressDialogUtil.show(this, R.string.common_commit_ing);
        mPresenter.doAction(details.id, action, comment, this);
    }

    private void showRejectDialog(final LeaveAction action) {
        new MaterialDialog.Builder(this)
                .title(R.string.leave_reject_dialog_title)
                .inputRange(0, AppConstant.ARROVAL_COMMENT_MAX_LENGTH)
                .input(R.string.leave_reject_input_hint, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String comment = input.toString().trim();
                        requestAction(action, comment);
                    }
                })
                .negativeText(R.string.common_cancel)
                .show();
    }

    private void showActionDialog(int title, final LeaveAction action) {
        new MaterialDialog.Builder(this)
                .title(title)
                .inputRange(0, AppConstant.ARROVAL_COMMENT_MAX_LENGTH)
                .input(R.string.leave_comment_input_hint, 0, true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String comment = input.toString().trim();
                        requestAction(action, comment);
                    }
                })
                .negativeText(R.string.common_cancel)
                .show();
    }

    @Override
    public void onLoadData(LeaveDetails data) {
        super.onLoadData(data);
        if (data != null) {
            details = data;
            fillView();
        }
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryLeaveDetails(leaveId, this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        ProgressDialogUtil.dismiss();
        super.onDestroy();
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
}
