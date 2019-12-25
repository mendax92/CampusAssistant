package com.zhiqi.campusassistant.app.dagger.module;

import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.http.CloupusApiAdapter;
import com.zhiqi.campusassistant.common.http.LongApiAdapter;
import com.zhiqi.campusassistant.core.app.api.AppApiService;
import com.zhiqi.campusassistant.core.appsetting.api.AppSettingApiService;
import com.zhiqi.campusassistant.core.bedroom.api.BedRoomApiService;
import com.zhiqi.campusassistant.core.course.api.CourseApiService;
import com.zhiqi.campusassistant.core.entrance.api.EntranceApiService;
import com.zhiqi.campusassistant.core.jpush.api.JPushApiService;
import com.zhiqi.campusassistant.core.leave.api.LeaveApiService;
import com.zhiqi.campusassistant.core.location.api.CampusLocationApiService;
import com.zhiqi.campusassistant.core.login.api.LoginApiService;
import com.zhiqi.campusassistant.core.lost.api.LostFoundApiService;
import com.zhiqi.campusassistant.core.message.api.MessageApiService;
import com.zhiqi.campusassistant.core.news.api.NewsApiService;
import com.zhiqi.campusassistant.core.notice.api.NoticeApiService;
import com.zhiqi.campusassistant.core.onecard.api.CardPayApiService;
import com.zhiqi.campusassistant.core.payment.api.SelfPayApiService;
import com.zhiqi.campusassistant.core.repair.api.RepairApiService;
import com.zhiqi.campusassistant.core.scores.api.ScoresApiService;
import com.zhiqi.campusassistant.core.security.api.HttpTimestampService;
import com.zhiqi.campusassistant.core.upload.api.UploadApiService;
import com.zhiqi.campusassistant.core.user.api.UserInfoApiService;
import com.zhiqi.campusassistant.core.usercenter.api.SecurityApiService;
import com.zhiqi.campusassistant.core.usercenter.api.UserCenterApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edmin on 2016/8/30 0030.
 * restAdapter提供http api 接口
 */
@Module
public class HttpServiceModule {

    @Provides
    @Singleton
    CloupusApiAdapter provideCloupusApiAdapter(AssistantApplication context) {
        return new CloupusApiAdapter(context);
    }

    @Provides
    @Singleton
    LongApiAdapter provideLongApiAdapter() {
        return new LongApiAdapter();
    }

    @Provides
    LoginApiService provideLoginApiService(CloupusApiAdapter apiAdapter) {
        return apiAdapter.getService(LoginApiService.class);
    }

    @Provides
    HttpTimestampService provideHttpTimestampService(CloupusApiAdapter apiAdapter) {
        return apiAdapter.getService(HttpTimestampService.class);
    }

    @Provides
    UserInfoApiService provideUserInfoApiService(CloupusApiAdapter apiAdapter) {
        return apiAdapter.getService(UserInfoApiService.class);
    }

    @Provides
    CourseApiService provideCourseApiService(CloupusApiAdapter apiAdapter) {
        return apiAdapter.getService(CourseApiService.class);
    }

    @Provides
    MessageApiService provideMessageApiService(CloupusApiAdapter apiAdapter) {
        return apiAdapter.getService(MessageApiService.class);
    }

    @Provides
    LeaveApiService provideLeaveApiService(CloupusApiAdapter apiAdapter) {
        return apiAdapter.getService(LeaveApiService.class);
    }

    @Provides
    RepairApiService provideRepairApiService(CloupusApiAdapter apiAdapter) {
        return apiAdapter.getService(RepairApiService.class);
    }

    @Provides
    UploadApiService provideUploadApiService(LongApiAdapter apiAdapter) {
        return apiAdapter.getService(UploadApiService.class);
    }

    @Provides
    AppApiService provideAppApiService(CloupusApiAdapter apiAdapter) {
        return apiAdapter.getService(AppApiService.class);
    }

    @Provides
    NewsApiService provideNewsApiService(CloupusApiAdapter apiAdapter) {
        return apiAdapter.getService(NewsApiService.class);
    }

    @Provides
    UserCenterApiService provideUserCenterApiService(CloupusApiAdapter apiAdapter) {
        return apiAdapter.getService(UserCenterApiService.class);
    }

    @Provides
    JPushApiService provideJPushApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(JPushApiService.class);
    }

    @Provides
    SecurityApiService provideSecurityApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(SecurityApiService.class);
    }

    @Provides
    NoticeApiService provideNoticeApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(NoticeApiService.class);
    }

    @Provides
    LostFoundApiService provideLostFoundApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(LostFoundApiService.class);
    }

    @Provides
    ScoresApiService provideScoresApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(ScoresApiService.class);
    }

    @Provides
    AppSettingApiService provideAppSettingApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(AppSettingApiService.class);
    }

    @Provides
    SelfPayApiService provideSelfPayApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(SelfPayApiService.class);
    }

    @Provides
    BedRoomApiService provideBedRoomApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(BedRoomApiService.class);
    }

    @Provides
    EntranceApiService provideEntranceApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(EntranceApiService.class);
    }

    @Provides
    CampusLocationApiService provideCampusLocationApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(CampusLocationApiService.class);
    }

    @Provides
    CardPayApiService provideCardPayApiService(CloupusApiAdapter restApiAdapter) {
        return restApiAdapter.getService(CardPayApiService.class);
    }
}
