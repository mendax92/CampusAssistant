package com.zhiqi.campusassistant.ui.location.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.location.entity.CampusLocationInfo;
import com.zhiqi.campusassistant.core.location.presenter.LocationPresenter;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * Created by ming on 17-8-11.
 * 校园引导
 */

public class CampusNavActivity extends LocationActivity implements LocationPresenter.LocationListener {

    private boolean moveCurrent = true;

    /**
     * 校园位置
     */
    private LatLng campuslatLng;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_campus_nav;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        init();
        initData();
    }

    private void init() {
        mPresenter.setLocationListener(this);
        aMap.clear();

        campuslatLng = new LatLng(AppConfigs.APP_LOCATION_LAT, AppConfigs.APP_LOCATION_LNG);
        //描点
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(campuslatLng);
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_location_marker)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        aMap.addMarker(markerOption);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        ArrayList<CampusLocationInfo> list = intent.getParcelableArrayListExtra(AppConstant.EXTRA_LOCATION_INFO);
        if (list != null && !list.isEmpty()) {
            moveCurrent = false;
            // 设置定位列表
            for (int i = 0; i < list.size(); i++) {
                CampusLocationInfo info = list.get(i);
                GlideApp.with(this).asBitmap().load(info.icon).into(new SimpleListener(info, i == 0));
            }
        }
    }

    /**
     * 加载完图片后，定位位置
     */
    private class SimpleListener extends SimpleTarget<Bitmap> {

        private CampusLocationInfo location;

        private boolean locateTo;

        SimpleListener(CampusLocationInfo location, boolean locateTo) {
            this.location = location;
            this.locateTo = locateTo;
        }

        @Override
        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            if (resource != null) {
                locate(resource);
            } else {
                locate(BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_location_marker));
            }
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            locate(BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_location_marker));
        }

        private void locate(Bitmap bitmap) {
            LatLng latLng = new LatLng(location.latitude, location.longitude);
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            MarkerOptions markerOption = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .setFlat(true)//设置marker平贴地图效果
                    .title(location.title)
                    .snippet(location.subtitle);
            aMap.addMarker(markerOption);
            if (locateTo) {
                locateTo(latLng);
            }
        }
    }


    @OnClick(value = {R.id.loc_current, R.id.loc_target})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.loc_current:
                locateCurrent();
                break;
            case R.id.loc_target:
                locateCampus();
                break;
        }
    }

    /**
     * 定位当前
     */
    private void locateCurrent() {
        moveCurrent = true;
        zoom();
    }

    /**
     * 定位学校
     */
    private void locateCampus() {
        locateTo(campuslatLng);
    }

    private void locateTo(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, getDefaultZoom()));
        aMap.animateCamera(cameraUpdate);
        zoom();
    }

    @Override
    public void onLocationSuccess(AMapLocation location) {
        if (location != null && moveCurrent) {
            moveCurrent = false;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()), getDefaultZoom()));
            aMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void onLocationFailed(int errorCode, String msg) {

    }
}
