package com.zhiqi.campusassistant.ui.location.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.TypedValue;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.ming.base.util.RxUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.core.location.dagger.component.DaggerLocationComponent;
import com.zhiqi.campusassistant.core.location.dagger.module.LocationModule;
import com.zhiqi.campusassistant.core.location.presenter.LocationPresenter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ming on 17-8-9.
 * 定位activity
 */

public class LocationActivity extends BaseToolbarActivity {

    private static final int DEFAULT_ZOOM = 17;

    @BindView(R.id.map)
    MapView mapView;

    @Inject
    LocationPresenter mPresenter;

    protected AMap aMap;

    private boolean isZoom = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutRes = onCreateView(savedInstanceState);
        if (layoutRes > 0) {
            setContentView(layoutRes);
        }
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    @LayoutRes
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_location;
    }


    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        init();
    }

    private void initDagger() {
        DaggerLocationComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .locationModule(new LocationModule())
                .build()
                .inject(this);
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            requestPermissions(true, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        mPresenter.bindMap(aMap);

        UiSettings settings = aMap.getUiSettings();
        settings.setZoomGesturesEnabled(true);
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        settings.setZoomControlsEnabled(true);
        settings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        setupLocationStyle();
    }

    private void setupLocationStyle() {

        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_navi_gps_locked));
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        // 定位、但不会移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);

        int color = getColorPrimary();

        int stroke = 0X0DFFFFFF & color;
        int fill = 0X26FFFFFF & color;
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(stroke);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(1);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(fill);

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
    }

    /**
     * 获取主题颜色
     *
     * @return
     */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (!isZoom) {
            isZoom = true;
            zoom();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        mPresenter.stopLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mPresenter.stopLocation();
    }

    @Override
    protected void onPermissionGranted(String[] permissions) {
        super.onPermissionGranted(permissions);
        setUpMap();
    }

    protected void zoom() {
        RxUtil.postOnMainThread(new Runnable() {
            @Override
            public void run() {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
            }
        }, 1000);
    }

    protected float getDefaultZoom() {
        return DEFAULT_ZOOM;
    }
}
