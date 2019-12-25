package com.zhiqi.campusassistant.core.repair.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.repair.dagger.module.RepairModule;
import com.zhiqi.campusassistant.core.upload.dagger.module.UploadModule;
import com.zhiqi.campusassistant.ui.repair.activity.RepairApplicantListActivity;
import com.zhiqi.campusassistant.ui.repair.activity.RepairApplyActivity;
import com.zhiqi.campusassistant.ui.repair.activity.RepairDetailActivity;
import com.zhiqi.campusassistant.ui.repair.activity.RepairRecordActivity;
import com.zhiqi.campusassistant.ui.repair.fragment.RepairApprovalFragment;

import dagger.Component;

/**
 * Created by ming on 2017/1/14.
 * repair component
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = {RepairModule.class, UploadModule.class})
public interface RepairComponent {

    void inject(RepairRecordActivity activity);

    void inject(RepairApplyActivity activity);

    void inject(RepairDetailActivity activity);

    void inject(RepairApplicantListActivity activity);

    void inject(RepairApprovalFragment fragment);
}
