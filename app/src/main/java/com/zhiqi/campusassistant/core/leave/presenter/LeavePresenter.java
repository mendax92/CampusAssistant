package com.zhiqi.campusassistant.core.leave.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ming.base.http.HttpManager;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.presenter.CacheVersionDataPresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.leave.api.LeaveApiService;
import com.zhiqi.campusassistant.core.leave.entity.LeaveAction;
import com.zhiqi.campusassistant.core.leave.entity.LeaveApprovalParam;
import com.zhiqi.campusassistant.core.leave.entity.LeaveDetails;
import com.zhiqi.campusassistant.core.leave.entity.LeaveInfo;
import com.zhiqi.campusassistant.core.leave.entity.VacationData;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/1/1.
 * 请假presenter
 */

public class LeavePresenter extends CacheVersionDataPresenter<VacationData> {


    private LeaveApiService mApiService;

    public LeavePresenter(Context context, LeaveApiService apiService, AppDaoSession daoSession) {
        super(context, daoSession.getRxDao(daoSession.getCacheDataDao()));
        this.mApiService = apiService;
    }

    /**
     * 查询请假记录
     *
     * @param applicantId
     * @param page
     * @param view
     */
    public void queryRecordList(long applicantId, final int page, final ILoadView<BasePageData<LeaveInfo>> view) {
        Observable<BaseResultData<BasePageData<LeaveInfo>>> observable = mApiService.queryLeaveRecord(applicantId, page, AppConfigs.DEFAULT_PAGE_SIZE);
        handleCache(CacheKey.LEAVE_RECORD_LIST, page != 1, new TypeToken<BasePageData<LeaveInfo>>() {
        }.getType(), observable, view);
    }

    /**
     * 查询请假类型
     *
     * @param loadView
     */
    @Override
    public void loadVersionData(ILoadView<VacationData> loadView) {
        loadVersionData(CacheKey.LEAVE_VACATION_TYPE, VacationData.class, loadView);
    }

    @Override
    public void loadVersionDataFromNet(ILoadView<VacationData> loadView) {
        Observable<BaseResultData<VacationData>> observable = mApiService.queryVacationTypes();
        loadVersionDataFromNet(CacheKey.LEAVE_VACATION_TYPE, observable, loadView);
    }

    /**
     * 查询请假详情
     *
     * @param leaveId
     * @param loadView
     */
    public void queryLeaveDetails(long leaveId, final ILoadView<LeaveDetails> loadView) {
        Observable<BaseResultData<LeaveDetails>> observable = mApiService.queryLeaveDetails(leaveId);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<LeaveDetails>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<LeaveDetails> result) {
                if (!isReleased(loadView)) {
                    loadView.onLoadData(result.data);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onFailed(errorCode, message);
                }
            }
        });
    }

    /**
     * 审批操作
     *
     * @param leaveId
     * @param action
     * @param comment
     * @param view
     */
    public void doAction(long leaveId, LeaveAction action, String comment, final IRequestView view) {
        Observable<BaseResultData> observable = mApiService.doAction(leaveId, action.getValue(), comment);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData>(mContext) {
            @Override
            public void onSuccess(BaseResultData result) {
                if (!isReleased(view)) {
                    view.onQuest(HttpErrorCode.SUCCESS, result != null ? result.message : null);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(view)) {
                    view.onQuest(errorCode, message);
                }
            }
        });
    }

    /**
     * 查询请假申请列表
     *
     * @param status
     * @param page
     * @param view
     */
    public void queryLeaveApprovalList(int userType, int status, final int page, final ILoadView<BasePageData<LeaveInfo>> view) {
        LeaveApprovalParam param = new LeaveApprovalParam();
        param.pageNo = page;
        if (status != 0) {
            param.status = status;
        }
        param.user_type = userType;
        Observable<BaseResultData<BasePageData<LeaveInfo>>> observable = mApiService.queryLeaveApprovalList(param);
        handleCache(CacheKey.LEAVE_APPROVAL_LIST, page != 1, new TypeToken<BasePageData<LeaveInfo>>() {
        }.getType(), observable, view);
    }
}
