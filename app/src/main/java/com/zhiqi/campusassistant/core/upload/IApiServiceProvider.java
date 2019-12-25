package com.zhiqi.campusassistant.core.upload;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/5/10.
 * 上传API服务提供者
 */

public interface IApiServiceProvider {

    /**
     * 提供API observer
     *
     * @param request
     * @param isUpdate
     * @param <T>
     * @return
     */
    <T extends BaseUploadBean> Observable<BaseResultData<List<String>>> provideApiObserver(T request, boolean isUpdate);
}
