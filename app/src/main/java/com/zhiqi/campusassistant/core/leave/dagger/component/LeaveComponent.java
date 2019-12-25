package com.zhiqi.campusassistant.core.leave.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.leave.dagger.module.LeavePresenterModule;
import com.zhiqi.campusassistant.core.upload.dagger.module.UploadModule;
import com.zhiqi.campusassistant.ui.leave.activity.LeaveApplyActivity;
import com.zhiqi.campusassistant.ui.leave.activity.LeaveDetailActivity;
import com.zhiqi.campusassistant.ui.leave.activity.LeaveRecordActivity;
import com.zhiqi.campusassistant.ui.leave.fragment.LeaveApprovalFragment;

import dagger.Component;

/**
 * Created by ming on 2017/1/1.
 */

@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = {LeavePresenterModule.class, UploadModule.class})
public interface LeaveComponent {

    void inject(LeaveRecordActivity activity);

    void inject(LeaveApplyActivity activity);

    void inject(LeaveDetailActivity activity);

    void inject(LeaveApprovalFragment fragment);
}
