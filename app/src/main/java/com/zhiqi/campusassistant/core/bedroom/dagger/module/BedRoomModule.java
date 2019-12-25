package com.zhiqi.campusassistant.core.bedroom.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.core.bedroom.api.BedRoomApiService;
import com.zhiqi.campusassistant.core.bedroom.presenter.BedRoomPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/7/30.
 * 宿舍床位module
 */

@Module
public class BedRoomModule {

    @ActivityScoped
    @Provides
    BedRoomPresenter provideBedRoomPresenter(AssistantApplication context, BedRoomApiService apiService) {
        return new BedRoomPresenter(context.getApplicationContext(), apiService);
    }
}
