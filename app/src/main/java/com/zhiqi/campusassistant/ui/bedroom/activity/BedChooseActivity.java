package com.zhiqi.campusassistant.ui.bedroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.bedroom.dagger.component.DaggerBedRoomComponent;
import com.zhiqi.campusassistant.core.bedroom.dagger.module.BedRoomModule;
import com.zhiqi.campusassistant.core.bedroom.entity.BedChooseInfo;
import com.zhiqi.campusassistant.core.bedroom.entity.BedInfo;
import com.zhiqi.campusassistant.core.bedroom.presenter.BedRoomPresenter;
import com.zhiqi.campusassistant.ui.bedroom.widget.BedChooseLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 2017/8/26.
 * 选择床位
 */

public class BedChooseActivity extends BaseLoadActivity<BedChooseInfo> implements ILoadView<BedChooseInfo> {

    @BindView(R.id.room)
    TextView roomTxt;

    @BindView(R.id.type_name)
    TextView typeNameTxt;

    @BindView(R.id.choose)
    TextView chooseTxt;

    @BindView(R.id.bed_choose_layout)
    BedChooseLayout bedChooseLayout;

    @BindView(R.id.commit)
    Button commitBtn;

    @Inject
    BedRoomPresenter mPresenter;

    private long roomId = 0;

    private BedInfo chooseBedInfo;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_bed_choose;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
    }

    private void initDagger() {
        DaggerBedRoomComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .bedRoomModule(new BedRoomModule())
                .build()
                .inject(this);
    }

    private void initView() {
        bedChooseLayout.setOnCheckChangedListener(checkChangedListener);
        Intent intent = getIntent();
        if (intent != null) {
            roomId = intent.getLongExtra(AppConstant.EXTRA_BEDROOM_ID, 0);
        }
        if (roomId > 0) {
            refresh();
        } else {
            finish();
        }
    }

    @Override
    protected void onRefresh() {
        mPresenter.loadRoomChooseInfo(roomId, this);
    }

    @Override
    public void onLoadData(BedChooseInfo data) {
        super.onLoadData(data);
        if (data != null) {
            roomTxt.setText(data.room);
            typeNameTxt.setText(data.type_name);
            bedChooseLayout.setData(data);
        }
    }

    private BedChooseLayout.OnCheckChangedListener checkChangedListener = new BedChooseLayout.OnCheckChangedListener() {
        @Override
        public void onCheckChangedListener(BedInfo bedInfo, boolean checked) {
            if (bedInfo != null) {
                if (checked) {
                    chooseBedInfo = bedInfo;
                    chooseTxt.setText(bedInfo.bed_name);
                    commitBtn.setEnabled(true);
                } else {
                    chooseBedInfo = null;
                    chooseTxt.setText("");
                    commitBtn.setEnabled(false);
                }
            }
        }
    };

    @OnClick(R.id.commit)
    void onClick() {
        if (chooseBedInfo != null) {
            ProgressDialogUtil.show(this, R.string.common_commit_ing);
            mPresenter.chooseBed(roomId, chooseBedInfo.bed_id, requestView);
        }
    }

    private IRequestView requestView = new IRequestView() {
        @Override
        public void onQuest(int errorCode, String message) {
            if (HttpErrorCode.SUCCESS == errorCode) {
                ProgressDialogUtil.success(message);
                setResult(RESULT_OK);
                finish();
            } else {
                ProgressDialogUtil.error(message);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
        ProgressDialogUtil.dismiss();
    }
}
