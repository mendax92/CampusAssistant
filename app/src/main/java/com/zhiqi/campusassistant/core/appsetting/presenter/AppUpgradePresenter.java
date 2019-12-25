package com.zhiqi.campusassistant.core.appsetting.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.ming.base.activity.ActivityManager;
import com.ming.base.activity.BaseActivity;
import com.ming.base.http.HttpManager;
import com.ming.base.http.download.DownLoadBean;
import com.ming.base.http.download.DownLoadManager;
import com.ming.base.util.DeviceUtil;
import com.ming.base.util.FileUtil;
import com.ming.base.util.Log;
import com.ming.base.util.RxUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.presenter.BasePresenter;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.AppPathConfig;
import com.zhiqi.campusassistant.core.appsetting.api.AppSettingApiService;
import com.zhiqi.campusassistant.core.appsetting.entity.UpgradeInfo;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by ming on 2017/5/21.
 * app升级设置
 */

public class AppUpgradePresenter extends BasePresenter {

    private static final String TAG = "AppUpgradePresenter";

    private static final int DO_DELAY = 1500;

    private AppSettingApiService mApiService;

    private Context mContext;

    private boolean doUpgrade = false;

    public AppUpgradePresenter(Context context, AppSettingApiService apiService) {
        this.mContext = context.getApplicationContext();
        this.mApiService = apiService;
    }

    /**
     * 检测更新
     */
    public void checkUpgrade() {
        Log.i(TAG, "checkUpgrade");
        Observable<BaseResultData<UpgradeInfo>> observable = mApiService.checkUpgrade(DeviceUtil.getPackageVersion(mContext), AppConfigs.PLATFORM);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<UpgradeInfo>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<UpgradeInfo> result) {
                onLoadData(result.data);
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }

            private void onLoadData(final UpgradeInfo data) {
                int currentVersion = DeviceUtil.getPackageVersion(mContext);
                Log.i(TAG, "currentVersion:" + currentVersion + ", versionId:" + data.version_id);
                if (!released && DeviceUtil.getPackageVersion(mContext) < data.version_id) {
                    RxUtil.postOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!doUpgrade(data)) {
                                onLoadData(data);
                            }
                        }
                    }, DO_DELAY);
                }
            }
        });
    }

    private boolean doUpgrade(UpgradeInfo info) {
        try {
            if (doUpgrade) {
                return true;
            }
            Log.i(TAG, "doUpgrade");
            Activity activity = ActivityManager.getInstance().getCurrentActivity();
            if (activity != null && !activity.isFinishing()) {
                showUpgradeDialog(activity, info);
                doUpgrade = true;
                return true;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            doUpgrade = false;
            return false;
        }
        return false;
    }

    /**
     * 显示更新对话框
     *
     * @param activity
     */
    private void showUpgradeDialog(Activity activity, final UpgradeInfo info) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
                .title(R.string.version_upgrade_title)
                .autoDismiss(false)
                .cancelable(false)
                .content(info.update_log)
                .progress(false, 100, true)
                .positiveText(R.string.version_upgrade)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                        MDButton btn = dialog.getActionButton(DialogAction.POSITIVE);
                        btn.setEnabled(false);
                        btn = dialog.getActionButton(DialogAction.NEGATIVE);
                        if (btn != null) {
                            btn.setEnabled(false);
                        }
                        RxUtil.postOnIoThread(new Runnable() {
                            @Override
                            public void run() {
                                FileUtil.deleteFile(AppPathConfig.APP_UPGRADE_PATH);
                                DownLoadManager.getInstance()
                                        .download(info.path, AppPathConfig.APP_UPGRADE_PATH + File.separator + mContext.getPackageName() + ".apk")
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<DownLoadBean>() {
                                            @Override
                                            public void accept(final DownLoadBean downLoadBean) throws Exception {
                                                Log.i(TAG, "status:" + downLoadBean.status + ", progress:" + downLoadBean.getProgress());
                                                switch (downLoadBean.status) {
                                                    case Downloading:
                                                        dialog.setProgress(downLoadBean.getProgress());
                                                        break;
                                                    case Finish:
                                                        MDButton btn = dialog.getActionButton(DialogAction.POSITIVE);
                                                        btn.setEnabled(true);
                                                        dialog.setProgress(downLoadBean.getProgress());
                                                        if (FileUtil.isFileExist(downLoadBean.filePath)) {
                                                            btn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    installApk(downLoadBean.filePath);
                                                                }
                                                            });
                                                            installApk(downLoadBean.filePath);
                                                        } else {
                                                            btn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    dialog.dismiss();
                                                                    run();
                                                                }
                                                            });
                                                            ToastUtil.show(mContext, R.string.version_upgrade_error);
                                                        }
                                                        if (!info.forced_update) {
                                                            dialog.dismiss();
                                                        }
                                                        break;
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                throwable.printStackTrace();
                                                dialog.dismiss();
                                                ToastUtil.show(mContext, R.string.version_upgrade_error);
                                            }
                                        });
                            }
                        });
                    }
                });
        if (!info.forced_update) {
            builder.negativeText(R.string.common_cancel)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    });
        }
        builder.show();
    }

    private void installApk(final String file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && !mContext.getPackageManager().canRequestPackageInstalls()) {
            final Activity activity = ActivityManager.getInstance().getCurrentActivity();
            if (activity != null && activity instanceof BaseActivity) {
                ((BaseActivity) activity).addOnActivityResultListener(AppConstant.ACTIVITY_REQUEST_UNKNOWN_APP_SOURCES, new BaseActivity.OnActivityResultListener() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                        ((BaseActivity) activity).removeOnActivityResultListener(this);
                        if (AppConstant.ACTIVITY_REQUEST_UNKNOWN_APP_SOURCES == requestCode && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (mContext.getPackageManager().canRequestPackageInstalls()) {
                                installApk(file);
                            } else {
                                ToastUtil.show(activity, R.string.version_upgrade_permission_error);
                            }
                        }
                    }
                });
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                activity.startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_UNKNOWN_APP_SOURCES);
            }
        } else {
            realInstall(file);
        }
    }

    private void realInstall(String file) {
        Log.i(TAG, "install " + file);
        File apkFile = new File(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String provider = mContext.getApplicationInfo().packageName + ".provider";
            Log.i(TAG, "provider:" + provider);
            Uri contentUri = FileProvider.getUriForFile(mContext.getApplicationContext(), provider, apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }
}
