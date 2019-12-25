package com.zhiqi.campusassistant.core.repair.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ming.base.http.HttpManager;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BasePageParam;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.presenter.CacheVersionDataPresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.repair.api.RepairApiService;
import com.zhiqi.campusassistant.core.repair.entity.RepairAction;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplicantInfo;
import com.zhiqi.campusassistant.core.repair.entity.RepairApprovalParam;
import com.zhiqi.campusassistant.core.repair.entity.RepairDetails;
import com.zhiqi.campusassistant.core.repair.entity.RepairDictionary;
import com.zhiqi.campusassistant.core.repair.entity.RepairInfo;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/1/14.
 * 维修presenter
 */

public class RepairPresenter extends CacheVersionDataPresenter<RepairDictionary> {

    private static final int APPLICANT = 1;
    private static final int HANDLER = 2;

    private RepairApiService mApiService;


    public RepairPresenter(Context context, RepairApiService apiService, AppDaoSession daoSession) {
        super(context, daoSession.getRxDao(daoSession.getCacheDataDao()));
        this.mApiService = apiService;
    }

    /**
     * 查询维修记录
     *
     * @param page
     * @param view
     */
    public void queryRecordList(final int page, final ILoadView<BasePageData<RepairInfo>> view) {
        Observable<BaseResultData<BasePageData<RepairInfo>>> observable = mApiService.queryRecordList(page, AppConfigs.DEFAULT_PAGE_SIZE);
        handleCache(CacheKey.REPAIR_RECORD_LIST, page != 1, new TypeToken<BasePageData<RepairInfo>>() {
        }.getType(), observable, view);
    }


    /**
     * 查询数据字典
     *
     * @param loadView
     */
    @Override
    public void loadVersionData(ILoadView<RepairDictionary> loadView) {
        loadVersionData(CacheKey.REPAIR_DICTIONARY, RepairDictionary.class, loadView);
    }

    @Override
    public void loadVersionDataFromNet(ILoadView<RepairDictionary> loadView) {
        Observable<BaseResultData<RepairDictionary>> observable = mApiService.queryDataDictionary();
        loadVersionDataFromNet(CacheKey.REPAIR_DICTIONARY, observable, loadView);
    }


    /**
     * 查询维修详情
     *
     * @param id
     * @param view
     */
    public void queryRepairDetails(long id, boolean isSelf, final ILoadView<RepairDetails> view) {
        Observable<BaseResultData<RepairDetails>> observable = mApiService.queryRepairDetails(id, isSelf ? APPLICANT : HANDLER);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<RepairDetails>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<RepairDetails> result) {
                if (!isReleased(view)) {
                    view.onLoadData(result.data);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(view)) {
                    view.onFailed(errorCode, message);
                }
            }
        });
    }

    /**
     * 审批操作
     *
     * @param repairId
     * @param action
     * @param comment
     */
    public void doAction(long repairId, RepairAction action, String comment, final IRequestView view) {
        Observable<BaseResultData> observable = mApiService.doAction(repairId, action.getValue(), comment);
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
     * 查询维修审批列表
     *
     * @param status
     * @param view
     */
    public void queryRepairApprovalList(int status, final int page, final ILoadView<BasePageData<RepairInfo>> view) {
        RepairApprovalParam param = new RepairApprovalParam();
        param.pageNo = page;
        if (status != 0) {
            param.status = status;
        }
        Observable<BaseResultData<BasePageData<RepairInfo>>> observable = mApiService.queryRepairApprovalList(param);
        handleCache(CacheKey.REPAIR_APPROVAL_LIST, page != 1, new TypeToken<BasePageData<RepairInfo>>() {
        }.getType(), observable, view);
    }

    /**
     * 查询报修人列表
     *
     * @return
     */
    public void queryRepairUserInfo(final int page, final ILoadView<BasePageData<RepairApplicantInfo>> view) {
        BasePageParam pageParam = new BasePageParam();
        pageParam.pageNo = page;
        Observable<BaseResultData<BasePageData<RepairApplicantInfo>>> observable = mApiService.queryRepairUserInfo(pageParam);
        handleCache(CacheKey.REPAIR_APPLICANT_LIST, page != 1, new TypeToken<BasePageData<RepairApplicantInfo>>() {
        }.getType(), observable, view);
    }
}
