package com.zhiqi.campusassistant.ui.repair.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.ming.base.util.GsonUtils;
import com.ming.base.util.Log;
import com.ming.base.util.TimeUtil;
import com.ming.base.widget.AppEditText;
import com.ming.base.widget.ClearEditText;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.entity.ImageData;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.activity.BasePhotoPickerActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.ui.widget.DateTimePickerView;
import com.zhiqi.campusassistant.common.ui.widget.PictureChooseAdapter;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.repair.dagger.component.DaggerRepairComponent;
import com.zhiqi.campusassistant.core.repair.dagger.module.RepairModule;
import com.zhiqi.campusassistant.core.repair.entity.CampusDistrict;
import com.zhiqi.campusassistant.core.repair.entity.CampusSubArea;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplicantInfo;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplyRequest;
import com.zhiqi.campusassistant.core.repair.entity.RepairDictionary;
import com.zhiqi.campusassistant.core.repair.entity.RepairItem;
import com.zhiqi.campusassistant.core.repair.entity.RepairType;
import com.zhiqi.campusassistant.core.repair.presenter.RepairApplyPresenter;
import com.zhiqi.campusassistant.core.repair.presenter.RepairPresenter;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.upload.OnUploadListener;
import com.zhiqi.campusassistant.core.upload.dagger.module.UploadModule;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by ming on 2017/1/15.
 * 维修申请
 */

public class RepairApplyActivity extends BasePhotoPickerActivity implements ILoadView<RepairDictionary>, IRequestView {

    private static final String TAG = "RepairApplyActivity";

    private static final int REPAIR_APPLICANT_REQUEST = 101;

    @BindView(R.id.repair_type)
    TextView repairType;
    @BindView(R.id.repair_describe)
    AppEditText repairDescribe;
    @BindView(R.id.repair_add_img)
    RecyclerView pickImgRecyclerView;
    @BindView(R.id.repair_name)
    ClearEditText repairName;
    @BindView(R.id.repair_phone)
    EditText repairPhone;
    @BindView(R.id.repair_campus_area)
    TextView repairCampusArea;
    @BindView(R.id.repair_break_area)
    TextView repairBreakArea;
    @BindView(R.id.repair_location)
    EditText repairLocation;
    @BindView(R.id.repair_appointment_time)
    TextView repairAppointmentTime;
    @BindView(R.id.submit)
    Button submit;

    @Inject
    RepairPresenter mPresenter;
    @Inject
    RepairApplyPresenter applyPresenter;

    private PictureChooseAdapter pictureChooseAdapter;

    private MaterialDialog retryDialog;

    private RepairDictionary dictionary;

    private RepairApplyRequest applyRequest;

    private long appointment;

    private boolean update;

    private boolean isDataInvalid = false;

    private MaterialDialog dataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_apply);
        initView();
        initDagger();
        initData();
    }

    private void initDagger() {
        DaggerRepairComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .repairModule(new RepairModule())
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
    }

    @OnClick({R.id.repair_type_layout, R.id.repair_campus_area_layout,
            R.id.repair_break_area_layout, R.id.repair_appointment_layout,
            R.id.applicant_list, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repair_type_layout:
                showTypeDialog();
                break;
            case R.id.repair_campus_area_layout:
                showCampusDialog();
                break;
            case R.id.repair_break_area_layout:
                showLocationDialog();
                break;
            case R.id.repair_appointment_layout:
                showTimeDialog();
                break;
            case R.id.applicant_list:
                Intent intent = new Intent(this, RepairApplicantListActivity.class);
                if (dictionary != null) {
                    intent.putExtra(AppConstant.EXTRA_CAMPUS_VERSION_DATA, GsonUtils.toJson(dictionary));
                }
                startActivityForResult(intent, REPAIR_APPLICANT_REQUEST);
                break;
            case R.id.submit:
                applyRequest.detail = repairDescribe.getValidText().toString();
                applyRequest.applicant_name = repairName.getValidText().toString();
                applyRequest.phone = repairPhone.getText().toString();
                applyRequest.address = repairLocation.getText().toString();
                applyRequest.images = pictureChooseAdapter.getOrigin();
                if (update) {
                    applyPresenter.updateRepairApply(applyRequest, this, uploadListener);
                } else {
                    applyPresenter.requestRepairApply(applyRequest, this, uploadListener);
                }
                ProgressDialogUtil.show(this, R.string.common_commit_ing);
                break;
        }
    }

    private OnUploadListener<RepairApplyRequest> uploadListener = new OnUploadListener<RepairApplyRequest>() {
        @Override
        public void onSuccess(UploadTask<RepairApplyRequest> task, String message) {
            setResult(RESULT_OK);
            ProgressDialogUtil.success(message);
            applyPresenter.clearCache(task);
            finish();
        }

        @Override
        public void onFailed(final UploadTask<RepairApplyRequest> task, int errorCode, String msg) {
            ProgressDialogUtil.dismiss();
            if (retryDialog != null) {
                return;
            }
            if (HttpErrorCode.ERROR_DATA_INVALID == errorCode) {
                onDataInvalid(task, msg);
                return;
            }
            retryDialog = new MaterialDialog.Builder(RepairApplyActivity.this)
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
                            ProgressDialogUtil.show(RepairApplyActivity.this, R.string.common_commit_ing);
                            applyPresenter.retryUpload(task, update, uploadListener);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            applyPresenter.clearCache(task);
                        }
                    })
                    .show();
        }
    };

    @OnTextChanged(value = {R.id.repair_type, R.id.repair_name, R.id.repair_phone,
            R.id.repair_campus_area, R.id.repair_break_area, R.id.repair_location})
    void onTextChanged() {
        if (repairType.getText().toString().trim().length() == 0
                || repairName.getText().toString().trim().length() == 0
                || repairPhone.getText().toString().trim().length() == 0
                || repairCampusArea.getText().toString().trim().length() == 0
                || repairBreakArea.getText().toString().trim().length() == 0
                || repairLocation.getText().toString().trim().length() == 0
                || repairPhone.getText().length() < AppConstant.LOGIN_VERIFY_LENGTH) {
            submit.setEnabled(false);
        } else {
            submit.setEnabled(true);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(AppConstant.EXTRA_REPAIR_REQUEST)) {
            applyRequest = intent.getParcelableExtra(AppConstant.EXTRA_REPAIR_REQUEST);
            if (applyRequest != null) {
                update = true;
                fillView();
            } else {
                finish();
                return;
            }
        } else {
            applyRequest = new RepairApplyRequest();
        }
        mPresenter.loadVersionData(this);
        LoginUser user = LoginManager.getInstance().getLoginUser();
        if (!update && user != null) {
            repairName.setText(user.getReal_name());
            repairPhone.setText(user.getPhone());
        }
    }

    private void fillView() {
        repairDescribe.setText(applyRequest.detail);
        repairName.setText(applyRequest.applicant_name);
        repairPhone.setText(applyRequest.phone);
        repairLocation.setText(applyRequest.address);
        pictureChooseAdapter.setNewData(applyRequest.imageDatas);
        if (applyRequest.appointment > 0) {
            repairAppointmentTime.setText(TimeUtil.getTime(applyRequest.appointment * 1000L, TimeUtil.FORMAT_MINUTE));
        }
    }

    private void showTypeDialog() {
        if (dictionary == null || dictionary.maintenance_item == null) {
            return;
        }
        final List<RepairType> items = dictionary.maintenance_item.items;
        if (items == null || items.isEmpty()) {
            return;
        }
        CharSequence[] typeNames = new CharSequence[items.size()];
        for (int i = 0; i < typeNames.length; i++) {
            typeNames[i] = items.get(i).value;
        }
        new MaterialDialog.Builder(this)
                .title(R.string.repair_project)
                .items(typeNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        RepairType selectType = items.get(position);
                        repairType.setText(selectType.value);
                        applyRequest.type = selectType.type;
                    }
                })
                .show();
    }


    private void showCampusDialog() {
        if (dictionary == null || dictionary.campus_subarea == null) {
            return;
        }
        final List<CampusDistrict> items = dictionary.campus_subarea.items;
        if (items == null || items.isEmpty()) {
            return;
        }
        CharSequence[] campusNames = new CharSequence[items.size()];
        for (int i = 0; i < campusNames.length; i++) {
            campusNames[i] = items.get(i).value;
        }
        new MaterialDialog.Builder(this)
                .title(R.string.repair_campus_area_label)
                .items(campusNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        CampusDistrict select = items.get(position);
                        repairCampusArea.setText(select.value);
                        if (select.district_id != applyRequest.district_id) {
                            applyRequest.district_id = select.district_id;
                            repairBreakArea.setText("");
                        }
                    }
                })
                .show();
    }

    private void showLocationDialog() {
        if (applyRequest.district_id == 0) {
            ToastUtil.show(this, R.string.repair_select_campus);
            return;
        }
        if (dictionary == null || dictionary.campus_subarea == null
                || dictionary.campus_subarea.items == null) {
            return;
        }
        CampusDistrict selectDistrict = null;
        for (CampusDistrict district : dictionary.campus_subarea.items) {
            if (applyRequest.district_id == district.district_id) {
                selectDistrict = district;
                break;
            }
        }
        if (selectDistrict == null) {
            return;
        }
        final List<CampusDistrict.CampusLocation> items = selectDistrict.subareas;
        if (items == null || items.isEmpty()) {
            return;
        }
        CharSequence[] names = new CharSequence[items.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = items.get(i).value;
        }
        new MaterialDialog.Builder(this)
                .title(R.string.repair_project)
                .items(names)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        CampusDistrict.CampusLocation loaction = items.get(position);
                        repairBreakArea.setText(loaction.value);
                        applyRequest.location_id = loaction.location_id;
                    }
                })
                .show();
    }

    private void showTimeDialog() {
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
                        appointment = mDate.getTimeInMillis();

                    }
                });
        new MaterialDialog.Builder(this)
                .title(R.string.repair_appointment_time)
                .positiveText(R.string.common_confirm)
                .negativeText(R.string.common_cancel)
                .customView(timePickerView, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (appointment <= System.currentTimeMillis()) {
                            ToastUtil.show(RepairApplyActivity.this, R.string.repair_appointment_error);
                            return;
                        }
                        repairAppointmentTime.setText(TimeUtil.getTime(appointment, TimeUtil.FORMAT_MINUTE));
                        applyRequest.appointment = appointment / 1000L;
                    }
                })
                .show();
    }

    private void fillDataVersion() {
        if (dictionary == null) {
            return;
        }
        RepairItem item = dictionary.maintenance_item;
        if (applyRequest.type > 0 && item != null && item.items != null) {
            for (RepairType type : item.items) {
                if (applyRequest.type == type.type) {
                    repairType.setText(type.value);
                    break;
                }
            }
        }
        CampusSubArea subArea = dictionary.campus_subarea;
        if (subArea != null && subArea.items != null) {
            if (applyRequest.district_id > 0) {
                for (CampusDistrict district : subArea.items) {
                    if (applyRequest.district_id == district.district_id) {
                        repairCampusArea.setText(district.value);
                        if (applyRequest.location_id > 0 && district.subareas != null) {
                            for (CampusDistrict.CampusLocation location : district.subareas) {
                                if (applyRequest.location_id == location.location_id) {
                                    repairBreakArea.setText(location.value);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private void onDataInvalid(final UploadTask<RepairApplyRequest> task, String msg) {
        if (dataDialog != null) {
            return;
        }
        repairType.setText("");
        repairCampusArea.setText("");
        repairBreakArea.setText("");
        dataDialog = new MaterialDialog.Builder(RepairApplyActivity.this)
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
                            applyPresenter.clearCache(task);
                        }
                        ProgressDialogUtil.show(RepairApplyActivity.this, R.string.common_update_ing);
                        mPresenter.loadVersionDataFromNet(RepairApplyActivity.this);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (task != null) {
                            applyPresenter.clearCache(task);
                        }
                    }
                })
                .show();
    }

    @Override
    public void onLoadData(RepairDictionary data) {
        this.dictionary = data;
        RepairApplyRequest.DataVersion version = new RepairApplyRequest.DataVersion();
        if (data.maintenance_item != null) {
            version.maintenance_item = data.maintenance_item.version;
        }
        if (data.campus_subarea != null) {
            version.campus_subarea = data.campus_subarea.version;
        }
        applyRequest.data_version = version;
        fillDataVersion();
        Log.i(TAG, "dictionary:" + dictionary);
        if (isDataInvalid) {
            isDataInvalid = false;
            ProgressDialogUtil.success(getString(R.string.common_update_success));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && REPAIR_APPLICANT_REQUEST == requestCode) {
            RepairApplicantInfo applicantInfo = data.getParcelableExtra(AppConstant.EXTRA_APPLICANT_INFO);
            if (applicantInfo == null) {
                return;
            }
            CampusSubArea subArea = dictionary.campus_subarea;
            if (subArea != null && subArea.items != null) {
                if (applicantInfo.district_id > 0) {
                    for (CampusDistrict district : subArea.items) {
                        if (applicantInfo.district_id == district.district_id) {
                            repairCampusArea.setText(district.value);
                            applyRequest.district_id = applicantInfo.district_id;
                            if (applicantInfo.location_id > 0 && district.subareas != null) {
                                for (CampusDistrict.CampusLocation location : district.subareas) {
                                    if (applicantInfo.location_id == location.location_id) {
                                        applyRequest.location_id = applicantInfo.location_id;
                                        repairBreakArea.setText(location.value);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            repairName.setText(applicantInfo.applicant_name);
            repairPhone.setText(applicantInfo.phone);
            repairLocation.setText(applicantInfo.address);
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        applyPresenter.release();
        ProgressDialogUtil.dismiss();
        super.onDestroy();
    }
}
