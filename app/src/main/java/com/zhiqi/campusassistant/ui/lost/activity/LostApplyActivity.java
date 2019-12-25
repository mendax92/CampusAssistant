package com.zhiqi.campusassistant.ui.lost.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.ming.base.util.Log;
import com.ming.base.widget.AppEditText;
import com.ming.base.widget.ClearEditText;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.entity.ImageData;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.activity.BasePhotoPickerActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.ui.widget.PictureChooseAdapter;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.lost.dagger.component.DaggerLostFoundComponent;
import com.zhiqi.campusassistant.core.lost.dagger.module.LostFoundModule;
import com.zhiqi.campusassistant.core.lost.entity.LostApplyRequest;
import com.zhiqi.campusassistant.core.lost.entity.LostApplyType;
import com.zhiqi.campusassistant.core.lost.entity.LostTypeVersion;
import com.zhiqi.campusassistant.core.lost.presenter.LostApplyPresenter;
import com.zhiqi.campusassistant.core.lost.presenter.LostFoundPresenter;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.upload.OnUploadListener;
import com.zhiqi.campusassistant.core.upload.dagger.module.UploadModule;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyFunction;
import com.zhiqi.campusassistant.ui.login.activity.BindPhoneActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by ming on 2017/5/8.
 * 发布失物招领
 */

public class LostApplyActivity extends BasePhotoPickerActivity implements ILoadView<LostTypeVersion>, IRequestView {

    private static final String TAG = "LostApplyActivity";

    private static final int MAX_PICTURE_SIZE = 3;

    @BindView(R.id.lost_apply_type)
    RadioGroup pubTypeGroup;
    @BindView(R.id.phone_number)
    TextView phoneNumber;
    @BindView(R.id.user_bind_phone)
    TextView bindPhone;
    @BindView(R.id.lost_type)
    TextView lostType;
    @BindView(R.id.card_no)
    ClearEditText cardNo;
    @BindView(R.id.lost_describe)
    AppEditText lostDescribe;
    @BindView(R.id.leave_add_img)
    RecyclerView pickImgRecyclerView;
    @BindView(R.id.submit)
    Button submit;

    @Inject
    LostFoundPresenter mPresenter;

    @Inject
    LostApplyPresenter applyPresenter;

    private MaterialDialog retryDialog;

    private PictureChooseAdapter pictureChooseAdapter;

    private LostApplyRequest applyInfo;

    private LostTypeVersion typeVersion;

    private boolean isDataInvalid = false;

    private MaterialDialog dataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_apply);
        ButterKnife.bind(this);
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initView();
        initDagger();
        initData();
    }

    private void initDagger() {
        DaggerLostFoundComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .lostFoundModule(new LostFoundModule())
                .uploadModule(new UploadModule())
                .build()
                .inject(this);
    }

    private void initView() {
        refreshBindPhone();
        pubTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Log.i(TAG, "checkedId:" + checkedId);
            }
        });

        pictureChooseAdapter = new PictureChooseAdapter(MAX_PICTURE_SIZE, this);
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

    private void refreshBindPhone() {
        LoginUser user = LoginManager.getInstance().getLoginUser();
        if (user != null && !TextUtils.isEmpty(user.getPhone())) {
            bindPhone.setVisibility(View.GONE);
            phoneNumber.setText(user.getPhone());
        } else {
            bindPhone.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        applyInfo = new LostApplyRequest();
        applyInfo.publish_type = getCheckedApplyType();
        mPresenter.loadVersionData(this);
    }

    private LostApplyType getCheckedApplyType() {
        return pubTypeGroup.getCheckedRadioButtonId() == R.id.lost ? LostApplyType.Lost : LostApplyType.Found;
    }

    /**
     * 显示失物类型对话框
     */
    private void showLostType() {
        if (typeVersion != null && typeVersion.items != null && !typeVersion.items.isEmpty()) {
            List<String> typeNames = new ArrayList<>();
            for (LostTypeVersion.LostType type : typeVersion.items) {
                typeNames.add(type.type_name);
            }
            new MaterialDialog.Builder(this)
                    .title(R.string.leave_type)
                    .items(typeNames)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            LostTypeVersion.LostType selectType = typeVersion.items.get(position);
                            lostType.setText(selectType.type_name);
                            applyInfo.lost_type = selectType.id;
                            applyInfo.data_version = new LostApplyRequest.LostDataVersion(typeVersion.version);
                        }
                    })
                    .show();
        }
    }

    private void submit() {
        applyInfo.publish_type = getCheckedApplyType();
        applyInfo.card_num = cardNo.getText().toString();
        applyInfo.content = lostDescribe.getText().toString();
        applyInfo.phone = phoneNumber.getText().toString();
        applyInfo.images = pictureChooseAdapter.getOrigin();
        applyPresenter.requestLostApply(applyInfo, this, uploadListener);
        ProgressDialogUtil.show(this, R.string.common_commit_ing);
    }

    private OnUploadListener<LostApplyRequest> uploadListener = new OnUploadListener<LostApplyRequest>() {
        @Override
        public void onSuccess(UploadTask<LostApplyRequest> task, String message) {
            setResult(RESULT_OK);
            ProgressDialogUtil.success(message);
            applyPresenter.clearCache(task);
            finish();
        }

        @Override
        public void onFailed(final UploadTask<LostApplyRequest> task, int errorCode, String msg) {
            ProgressDialogUtil.dismiss();
            if (retryDialog != null) {
                return;
            }
            if (HttpErrorCode.ERROR_DATA_INVALID == errorCode) {
                onDataInvalid(task, msg);
                return;
            }
            retryDialog = new MaterialDialog.Builder(LostApplyActivity.this)
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
                            ProgressDialogUtil.show(LostApplyActivity.this, R.string.common_commit_ing);
                            applyPresenter.retryUpload(task, uploadListener);
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

    private void onDataInvalid(final UploadTask<LostApplyRequest> task, String msg) {
        if (dataDialog != null) {
            return;
        }
        lostType.setText("");
        dataDialog = new MaterialDialog.Builder(this)
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
                        ProgressDialogUtil.show(LostApplyActivity.this, R.string.common_update_ing);
                        mPresenter.loadVersionDataFromNet(LostApplyActivity.this);
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
    public void onLoadData(LostTypeVersion data) {
        this.typeVersion = data;
    }

    @Override
    public void onFailed(int errorCode, String message) {
        ToastUtil.show(this, message);
    }

    @OnTextChanged({R.id.phone_number, R.id.card_no, R.id.lost_describe})
    void onTextChanged(CharSequence s, int start, int before, int count) {
        if (phoneNumber.getText().length() > 0 && lostDescribe.getValidText().length() > 0
                && applyInfo != null && applyInfo.lost_type > 0) {
            submit.setEnabled(true);
        } else {
            submit.setEnabled(false);
        }
    }

    @OnClick({R.id.user_bind_phone, R.id.lost_type_layout, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_bind_phone:
                Intent intent = new Intent(this, BindPhoneActivity.class);
                intent.putExtra(AppConstant.EXTRA_BIND_PHONE_OPT, VerifyFunction.BindPhone.getValue());
                startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_VERIFY_CODE);
                break;
            case R.id.lost_type_layout:
                showLostType();
                break;
            case R.id.submit:
                submit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        applyPresenter.release();
        ProgressDialogUtil.dismiss();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.ACTIVITY_REQUEST_VERIFY_CODE == requestCode && RESULT_OK == resultCode) {
            refreshBindPhone();
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
}
