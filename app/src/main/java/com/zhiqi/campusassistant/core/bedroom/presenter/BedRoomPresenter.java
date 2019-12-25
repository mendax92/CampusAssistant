package com.zhiqi.campusassistant.core.bedroom.presenter;

import android.content.Context;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.presenter.SimplePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.core.bedroom.api.BedRoomApiService;
import com.zhiqi.campusassistant.core.bedroom.entity.BedChooseInfo;
import com.zhiqi.campusassistant.core.bedroom.entity.BedRoomInfo;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/7/30.
 * 床位presenter
 */

public class BedRoomPresenter extends SimplePresenter {

    private BedRoomApiService mApiService;

    public BedRoomPresenter(Context context, BedRoomApiService apiService) {
        super(context);
        this.mApiService = apiService;
    }

    /**
     * 获取宿舍信息
     *
     * @param loadView
     */
    public void loadRoomList(final ILoadView<BedRoomInfo> loadView) {
        Observable<BaseResultData<BedRoomInfo>> observable = mApiService.getRoomList();
        subscribe(observable, loadView);
    }

    /**
     * 获取选择宿舍信息
     *
     * @param roomId   宿舍id
     * @param loadView
     */
    public void loadRoomChooseInfo(long roomId, final ILoadView<BedChooseInfo> loadView) {
        Observable<BaseResultData<BedChooseInfo>> observable = mApiService.getBedChooseInfo(roomId);
        subscribe(observable, loadView);
    }


    /**
     * 选择床位
     *
     * @param roomId
     * @param bedId
     * @param requestView
     */
    public void chooseBed(long roomId, long bedId, final IRequestView requestView) {
        Observable<BaseResultData> observable = mApiService.chooseBed(roomId, bedId);
        requestSimple(observable, requestView);
    }
}
