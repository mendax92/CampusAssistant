package com.zhiqi.campusassistant.app.dagger.component;

import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.app.dagger.module.ApplicationModule;
import com.zhiqi.campusassistant.app.dagger.module.DatabaseModule;
import com.zhiqi.campusassistant.app.dagger.module.HttpServiceModule;
import com.zhiqi.campusassistant.app.dagger.module.InitAppModule;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.app.api.AppApiService;
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
import com.zhiqi.campusassistant.core.upload.service.UploadManager;
import com.zhiqi.campusassistant.core.user.api.UserInfoApiService;
import com.zhiqi.campusassistant.core.usercenter.api.SecurityApiService;
import com.zhiqi.campusassistant.core.usercenter.api.UserCenterApiService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Edmin on 2016/8/29 0029.
 */
@Singleton
@Component(modules = {ApplicationModule.class, HttpServiceModule.class, DatabaseModule.class, InitAppModule.class})
public interface ApplicationComponent {
    AssistantApplication inject(AssistantApplication assistantApplication);

    AssistantApplication getApplication();

    AppDaoSession getDaoSession();

    LoginApiService getLoginApiService();

    HttpTimestampService getHttpTimestampService();

    UserInfoApiService getUserInfoApiService();

    CourseApiService getCourseApiService();

    MessageApiService getMessageApiService();

    LeaveApiService getLeaveApiService();

    RepairApiService getRepairApiService();

    UploadApiService getUploadApiService();

    AppApiService getAppApiService();

    UploadManager getUploadManager();

    NewsApiService getNewsApiService();

    UserCenterApiService getUserCenterApiService();

    JPushApiService getJPushApiService();

    SecurityApiService getSecurityApiService();

    NoticeApiService getNoticeApiService();

    LostFoundApiService getLostFoundApiService();

    ScoresApiService getScoresApiService();

    SelfPayApiService getSelfPayApiService();

    BedRoomApiService getBedRoomApiService();

    EntranceApiService getEntranceApiService();

    CampusLocationApiService getCampusLocationApiService();

    CardPayApiService getCardPayApiService();
}
