package com.zhiqi.campusassistant.core.location.presenter;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.ming.base.util.DeviceUtil;
import com.ming.base.util.Log;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.utils.ToastUtil;

/**
 * Created by ming on 17-8-10.
 * 定位管理
 */

public class LocationPresenter {

    private static final String TAG = "LocationPresenter";

    private Context mContext;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mapListener;
    private LocationListener locationListener;
    private long interval = 0;

    public LocationPresenter(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 绑定地图
     *
     * @param map
     */
    public void bindMap(AMap map) {
        if (map != null) {
            map.setLocationSource(locationSource);
        }
    }

    private LocationSource locationSource = new LocationSource() {

        /**
         * 激活定位
         */
        @Override
        public void activate(LocationSource.OnLocationChangedListener listener) {
            mapListener = listener;
            startLocation();
        }

        /**
         * 停止定位
         */
        @Override
        public void deactivate() {
            stopLocation();
        }
    };

    /**
     * 开始定位
     */
    public void startLocation() {
        if (!DeviceUtil.isOpenGPS(mContext)) {
            ToastUtil.show(mContext, R.string.location_no_permission);
            return;
        }
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(mapLocationListener);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //设置定位间隔,单位毫秒,默认为2000ms
            if (interval > 0) {
                mLocationOption.setInterval(interval);
            }
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private AMapLocationListener mapLocationListener = new AMapLocationListener() {

        /**
         * 定位成功后回调函数
         */
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            Log.i(TAG, "location:" + amapLocation);
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    if (mapListener != null) {
                        mapListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                    }
                    if (locationListener != null) {
                        locationListener.onLocationSuccess(amapLocation);
                    }
                } else {
                    String errText = mContext.getString(R.string.location_error_msg);
                    Log.e("AmapErr", amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo());
                    ToastUtil.show(mContext, errText);
                    if (locationListener != null) {
                        locationListener.onLocationFailed(amapLocation.getErrorCode(), amapLocation.getErrorInfo());
                    }
                }
            }
        }
    };

    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public interface LocationListener {

        void onLocationSuccess(AMapLocation location);

        void onLocationFailed(int errorCode, String msg);
    }
}
