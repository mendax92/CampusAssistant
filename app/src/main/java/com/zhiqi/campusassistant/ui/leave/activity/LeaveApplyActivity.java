package com.zhiqi.campusassistant.ui.leave.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.ming.base.util.NumberUtil;
import com.ming.base.util.TimeUtil;
import com.ming.base.widget.AppEditText;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.entity.ImageData;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.activity.BasePhotoPickerActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.ui.widget.DateTimePickerView;
import com.zhiqi.campusassistant.common.ui.widget.DecimalFilter;
import com.zhiqi.campusassistant.common.ui.widget.PictureChooseAdapter;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.leave.dagger.component.DaggerLeaveComponent;
import com.zhiqi.campusassistant.core.leave.dagger.module.LeavePresenterModule;
import com.zhiqi.campusassistant.core.leave.entity.LeaveRequest;
import com.zhiqi.campusassistant.core.leave.entity.VacationData;
import com.zhiqi.campusassistant.core.leave.entity.VacationType;
import com.zhiqi.campusassistant.core.leave.presenter.LeaveApplyPresenter;
import com.zhiqi.campusassistant.core.leave.presenter.LeavePresenter;
import com.zhiqi.campusassistant.core.upload.OnUploadListener;
import com.zhiqi.campusassistant.core.upload.dagger.module.UploadModule;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by ming on 2017/1/1.
 */

public class LeaveApplyActivity extends BasePhotoPickerActivity implements ILoadView<VacationData>, IRequestView {

    @BindView(R.id.leave_type)
    TextView leaveType;
    @BindView(R.id.leave_start_time)
    TextView leaveStartTime;
    @BindView(R.id.leave_end_time)
    TextView leaveEndTime;
    @BindView(R.id.leave_sum_time)
    EditText leaveSumTime;
    @BindView(R.id.leave_reason)
    AppEditText leaveReason;
    @BindView(R.id.leave_add_img)
    RecyclerView pickImgRecyclerView;
    @BindView(R.id.submit)
    Button submit;

    @Inject
    LeavePresenter mPresenter;

    @Inject
    LeaveApplyPresenter mApplyPresenter;

    private PictureChooseAdapter pictureChooseAdapter;

    private MaterialDialog retryDialog;

    private LeaveRequest mLeaveRequest;

    private VacationData vacationData;

    private long startTime;

    private long endTime;

    private boolean update = false;

    private boolean isDataInvalid = false;

    private MaterialDialog dataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_apply);
        initDagger();
        initView();
        initData();
    }

    private void initDagger() {
        DaggerLeaveComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .leavePresenterModule(new LeavePresenterModule())
                .uploadModule(new UploadModule())
                .build()
                .inject(this);
    }

    private void initView() {
        pictureChooseAdapter = new PictureChooseAdapter(this);
        ViewPreloadSizeProvider<ImageData> preloadSizeProvider = new ViewPreloadSizeProvider<>();
        RecyclerViewPreloader<ImageData> preloader =
                new RecyclerViewPreloader<>(GlideApp.with(this), pictureChooseAdapter, preloadSizeProvider, 3);
        pickImgRecyclerView.addOnScrollListener(preloader);

        pickImgRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pickImgRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        pickImgRecyclerView.setAdapter(pictureChooseAdapter);
        leaveSumTime.setFilters(new InputFilter[]{new DecimalFilter(1)});
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(AppConstant.EXTRA_LEAVE_REQUEST)) {
            mLeaveRequest = intent.getParcelableExtra(AppConstant.EXTRA_LEAVE_REQUEST);
            if (mLeaveRequest != null) {
                update = true;
                fillView();
            } else {
                finish();
                return;
            }
        } else {
            mLeaveRequest = new LeaveRequest();
        }
        mPresenter.loadVersionData(this);
    }

    private void fillView() {
        leaveReason.setText(mLeaveRequest.reason);
        leaveSumTime.setText(NumberUtil.format(mLeaveRequest.total_days, 1));
        pictureChooseAdapter.setNewData(mLeaveRequest.imageDatas);
        if (mLeaveRequest.start_time > 0) {
            leaveStartTime.setText(TimeUtil.getTime(mLeaveRequest.start_time * 1000L, TimeUtil.FORMAT_MINUTE));
        }
        if (mLeaveRequest.end_time > 0) {
            leaveEndTime.setText(TimeUtil.getTime(mLeaveRequest.end_time * 1000L, TimeUtil.FORMAT_MINUTE));
        }
    }

    @OnTextChanged(value = {R.id.leave_sum_time, R.id.leave_reason,
            R.id.leave_type, R.id.leave_start_time, R.id.leave_end_time})
    void onTextChanged() {
        if (leaveSumTime.getText().length() <= 0 || leaveReason.getValidText().length() <= 0
                || leaveType.getText().length() <= 0 || leaveStartTime.getText().length() <= 0
                || leaveEndTime.getText().length() <= 0) {
            submit.setEnabled(false);
        } else {
            submit.setEnabled(true);
        }
    }

    @OnClick({R.id.leave_type_layout, R.id.leave_start_layout, R.id.leave_end_layout, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leave_type_layout:
                showVacationTypeDialog();
                break;
            case R.id.leave_start_layout:
                onStartTimeSelect();
                break;
            case R.id.leave_end_layout:
                onEndTimeSelect();
                break;
            case R.id.submit:
                commit();
                break;
        }
    }

    private void commit() {
        if (mLeaveRequest.end_time <= mLeaveRequest.start_time) {
            ToastUtil.show(this, R.string.leave_time_tip);
            return;
        }
        String totalDays = leaveSumTime.getText().toString();
        mLeaveRequest.total_days = Float.parseFloat(totalDays);
        mLeaveRequest.reason = leaveReason.getText().toString();
        mLeaveRequest.images = pictureChooseAdapter.getOrigin();
        if (update) {
            mApplyPresenter.updateLeaveApply(mLeaveRequest, this, uploadListener);
        } else {
            mApplyPresenter.requestLeaveApply(mLeaveRequest, this, uploadListener);
        }
        ProgressDialogUtil.show(this, R.string.common_commit_ing);
    }

    private OnUploadListener<LeaveRequest> uploadListener = new OnUploadListener<LeaveRequest>() {
        @Override
        public void onSuccess(UploadTask<LeaveRequest> task, String message) {
            ProgressDialogUtil.success(message);
            mApplyPresenter.clearCache(task);
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void onFailed(final UploadTask<LeaveRequest> task, int errorCode, String msg) {
            ProgressDialogUtil.dismiss();
            if (retryDialog != null) {
                return;
            }
            if (HttpErrorCode.ERROR_DATA_INVALID == errorCode) {
                onDataInvalid(task, msg);
                return;
            }
            retryDialog = new MaterialDialog.Builder(LeaveApplyActivity.this)
                    .title(R.string.common_failed)
                    .positiveText(R.string.upload_retry)
                    .negativeText(R.string.common_cancel)
                    .content(msg)
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            retryDialog = null;
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (retryDialog != null) {
                                retryDialog.dismiss();
                            }
                            ProgressDialogUtil.show(LeaveApplyActivity.this, R.string.common_commit_ing);
                            mApplyPresenter.retryUpload(task, update, uploadListener);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mApplyPresenter.clearCache(task);
                        }
                    })
                    .show();
        }
    };

    private void onDataInvalid(final UploadTask<LeaveRequest> task, String msg) {
        if (dataDialog != null) {
            return;
        }
        dataDialog = new MaterialDialog.Builder(LeaveApplyActivity.this)
                .title(R.string.common_failed)
                .positiveText(R.string.apply_data_invalid)
                .negativeText(R.string.common_cancel)
                .content(msg)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dataDialog = null;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dataDialog != null) {
                            dataDialog.dismiss();
                        }
                        isDataInvalid = true;
                        if (task != null) {
                            mApplyPresenter.clearCache(task);
                        }
                        ProgressDialogUtil.show(LeaveApplyActivity.this, R.string.common_update_ing);
                        mPresenter.loadVersionDataFromNet(LeaveApplyActivity.this);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (task != null) {
                            mApplyPresenter.clearCache(task);
                        }
                    }
                })
                .show();
    }

    private void onStartTimeSelect() {
        DateTimePickerView timePickerView = new DateTimePickerView(this);
        timePickerView
                .setOnDateTimeChangedListener(new DateTimePickerView.OnDateTimeChangedListener() {
                    @Override
                    public void onDateTimeChanged(DateTimePickerView view, int year, int month, int day, int hour, int minute) {
                        Calendar mDate = Calendar.getInstance();
                        mDate.set(Calendar.YEAR, year);
                        mDate.set(Calendar.MONTH, month);
                        mDate.set(Calendar.DAY_OF_MONTH, day);
                        mDate.set(Calendar.HOUR_OF_DAY, hour);
                        mDate.set(Calendar.MINUTE, minute);
                        mDate.set(Calendar.SECOND, 0);
                        startTime = mDate.getTimeInMillis();
                    }
                });
        new MaterialDialog.Builder(this)
                .title(R.string.leave_start_time)
                .positiveText(R.string.common_confirm)
                .negativeText(R.string.common_cancel)
                .customView(timePickerView, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        leaveStartTime.setText(TimeUtil.getTime(startTime, TimeUtil.FORMAT_MINUTE));
                        mLeaveRequest.start_time = startTime / 1000L;
                    }
                })
                .show();
    }

    private void onEndTimeSelect() {
        DateTimePickerView timePickerView = new DateTimePickerView(this);
        timePickerView
                .setOnDateTimeChangedListener(new DateTimePickerView.OnDateTimeChangedListener() {
                    @Override
                    public void onDateTimeChanged(DateTimePickerView view, int year, int month, int day, int hour, int minute) {
                        Calendar mDate = Calendar.getInstance();
                        mDate.set(Calendar.YEAR, year);
                        mDate.set(Calendar.MONTH, month);
                        mDate.set(Calendar.DAY_OF_MONTH, day);
                        mDate.set(Calendar.HOUR_OF_DAY, hour);
                        mDate.set(Calendar.MINUTE, minute);
                        mDate.set(Calendar.SECOND, 0);
                        endTime = mDate.getTimeInMillis();
                    }
                });
        new MaterialDialog.Builder(this)
                .title(R.string.leave_end_time)
                .positiveText(R.string.common_confirm)
                .negativeText(R.string.common_cancel)
                .customView(timePickerView, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        leaveEndTime.setText(TimeUtil.getTime(endTime, TimeUtil.FORMAT_MINUTE));
                        mLeaveRequest.end_time = endTime / 1000L;
                    }
                })
                .show();
    }

    private void showVacationTypeDialog() {
        if (vacationData == null || vacationData.items == null || vacationData.items.isEmpty()) {
            return;
        }
        final List<VacationType> items = vacationData.items;
        List<String> typeNames = new ArrayList<>();
        for (VacationType type : items) {
            typeNames.add(type.type_name);
        }

        new MaterialDialog.Builder(this)
                .title(R.string.leave_type)
                .items(typeNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        VacationType selectType = items.get(position);
                        leaveType.setText(selectType.type_name);
                        mLeaveRequest.type = selectType.id;
                        mLeaveRequest.data_version = new LeaveRequest.LeaveData(vacationData.version);
                    }
                })
                .show();
    }

    @Override
    public void onLoadData(VacationData data) {
        if (data != null) {
            this.vacationData = data;
            for (VacationType type : vacationData.items) {
                if (type.id == mLeaveRequest.type) {
                    leaveType.setText(type.type_name);
                    break;
                }
            }
            if (isDataInvalid) {
                isDataInvalid = false;
                ProgressDialogUtil.success(getString(R.string.common_update_success));
            }
        }
    }

    @Override
    public void onFailed(int errorCode, String message) {
        if (isDataInvalid) {
            isDataInvalid = false;
            ProgressDialogUtil.error(message);
        } else {
            ToastUtil.show(this, message);
        }
    }


    @Override
    public void onQuest(int errorCode, String message) {
        if (HttpErrorCode.SUCCESS == errorCode) {
            ProgressDialogUtil.success(message);
            setResult(RESULT_OK);
            finish();
        } else {
            if (HttpErrorCode.ERROR_DATA_INVALID == errorCode) {
                ProgressDialogUtil.dismiss();
                onDataInvalid(null, message);
                return;
            }
            ProgressDialogUtil.error(message);
        }
    }

    @Override
    protected void onDestroy() {
        mApplyPresenter.release();
        mPresenter.release();
        ProgressDialogUtil.dismiss();
        super.onDestroy();
    }
}
