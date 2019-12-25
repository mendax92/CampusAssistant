package com.zhiqi.campusassistant.ui.bedroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.activity.BaseRefreshLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.bedroom.dagger.component.DaggerBedRoomComponent;
import com.zhiqi.campusassistant.core.bedroom.dagger.module.BedRoomModule;
import com.zhiqi.campusassistant.core.bedroom.entity.BedRoomDetail;
import com.zhiqi.campusassistant.core.bedroom.entity.BedRoomInfo;
import com.zhiqi.campusassistant.core.bedroom.presenter.BedRoomPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 2017/7/30.
 * 宿舍信息
 */

public class BedRoomInfoActivity extends BaseRefreshLoadActivity<BedRoomInfo> implements ILoadView<BedRoomInfo> {

    @BindView(R.id.bedroom)
    TextView bedRoomTxt;

    @BindView(R.id.location)
    TextView locationTxt;

    @BindView(R.id.bedroom_type)
    TextView typeNameTxt;

    @BindView(R.id.bedroom_img)
    ImageView roomImg;

    @BindView(R.id.choose)
    Button chooseBtn;

    @BindView(R.id.bedroom_item)
    LinearLayout itemLayout;

    @Inject
    BedRoomPresenter mPresenter;

    private BedRoomInfo bedRoomInfo;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_bedroom_info;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        init();
    }

    private void initDagger() {
        DaggerBedRoomComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .bedRoomModule(new BedRoomModule())
                .build()
                .inject(this);
    }

    private void init() {
        refresh();
    }

    @Override
    protected void onRefresh() {
        mPresenter.loadRoomList(this);
    }

    @Override
    public void onLoadData(BedRoomInfo data) {
        super.onLoadData(data);
        if (data != null) {
            bedRoomInfo = data;
            GlideApp.with(this).load(data.image).placeholder(R.drawable.ic_img_rectangle_default).into(roomImg);
            if (data instanceof BedRoomDetail) {
                bedRoomTxt.setText(getString(R.string.common_two_string, data.room, ((BedRoomDetail) data).bed_name));
            } else {
                bedRoomTxt.setText(data.room);
            }
            locationTxt.setText(data.location);
            typeNameTxt.setText(data.type_name);
            if (data.is_selected) {
                chooseBtn.setVisibility(View.GONE);
                itemLayout.setEnabled(true);
            } else {
                chooseBtn.setVisibility(View.VISIBLE);
                itemLayout.setEnabled(false);
            }
        }
    }

    @OnClick({R.id.choose, R.id.bedroom_item})
    void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.choose:
                intent = new Intent(this, BedChooseActivity.class);
                intent.putExtra(AppConstant.EXTRA_BEDROOM_ID, bedRoomInfo.room_id);
                startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_CHOOSE_BED);
                break;
            case R.id.bedroom_item:
                intent = new Intent(this, BedChosenDetailActivity.class);
                intent.putExtra(AppConstant.EXTRA_BEDROOM_DETAIL, (BedRoomDetail) bedRoomInfo);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.ACTIVITY_REQUEST_CHOOSE_BED == requestCode && RESULT_OK == resultCode) {
            refresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
    }
}
