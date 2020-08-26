package com.zhiqi.campusassistant.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.ming.base.util.Log;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.ui.fragment.BaseLoadListFragment;
import com.zhiqi.campusassistant.common.ui.view.AppILoadView;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.utils.GlideImageLoader;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.core.app.dagger.component.DaggerAppComponent;
import com.zhiqi.campusassistant.core.app.dagger.module.AppModule;
import com.zhiqi.campusassistant.core.app.entity.BannerInfos;
import com.zhiqi.campusassistant.core.app.entity.CheckAppGoWhere;
import com.zhiqi.campusassistant.core.app.entity.ModuleCategory;
import com.zhiqi.campusassistant.core.app.presenter.AppPresenter;
import com.zhiqi.campusassistant.ui.main.view.ITabView;
import com.zhiqi.campusassistant.ui.main.widget.ModuleCategoryAdapter;
import com.zhiqi.campusassistant.ui.web.activity.WebActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Edmin on 2016/9/28
 * 应用
 */
public class AppFragment extends BaseLoadListFragment<ModuleCategory> implements ITabView, AppILoadView<BaseResultData<CheckAppGoWhere>>, ILoadView<List<ModuleCategory>> {

    @Inject
    AppPresenter mPresenter;
    @BindView(R.id.banner)
    Banner banner;

    @Override
    public int onCreateView(Bundle savedInstanceState) {
        setActionbarTitle(R.string.common_app);
        return R.layout.frag_application;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDagger();
        initView();
        refresh();
    }

    private void initDagger() {
        DaggerAppComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .appModule(new AppModule())
                .build()
                .inject(this);
    }

    private void initView() {
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
    }


    @Override
    public void onChecked() {
        invalidateActionbar();
        if (mPresenter != null) {
            refresh(true);
        }
    }

    @Override
    public void onUnchecked() {
    }

    @Override
    protected void onRefresh() {
        mPresenter.setAppILoadView(this);
        mPresenter.queryAppList(this);
        ((ModuleCategoryAdapter) mAdapter).setPresenter(mPresenter);
    }

    @Override
    protected BaseQuickAdapter<ModuleCategory> provideAdapter() {
        return new ModuleCategoryAdapter();
    }

    @Override
    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return null;
    }

    @Override
    public void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }

    public void setBannerData(final List<ModuleCategory> paramList) {
        if (paramList != null && paramList.size() > 0) {
            ArrayList<String> arrayList = new ArrayList();
            for (BannerInfos.BannerInfo bannerInfo : paramList.get(0).getData()) {
                arrayList.add(bannerInfo.getImgUrl());
            }
            //设置banner样式
            banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置图片集合
            banner.setImages(arrayList);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.DepthPage);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(3000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (paramList.get(0).getData().get(position).getType() == 1) {
                        goWeb(paramList.get(0).getData().get(position).getUrl());
                    }
                }
            });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }


    @Override
    public void checkAppToWhere(BaseResultData<CheckAppGoWhere> checkAppGo) {
        if (checkAppGo == null || checkAppGo.data == null || checkAppGo.data.getApps() == null) {
            return;
        }
        CheckAppGoWhere bean = checkAppGo.data;
        if (TextUtils.isEmpty(bean.getApps().getAppUrl())){
        }
        if (TextUtils.isEmpty(bean.getApps().getAppUrl())){
        }
        String path;
        if (!TextUtils.isEmpty(bean.getDatas())) {
            path = bean.getApps().getAppUrl().concat("?").concat(bean.getDatas());
        } else {
            path = bean.getApps().getAppUrl();
        }
        if (Integer.valueOf(bean.getApps().getAppType()) == bean.TYPE_APPLETS) {
            gotoWX(path);
        } else {
            goWeb(path);
        }
    }

    @Override
    public void onLoadData(List<ModuleCategory> data) {
        setBannerData(data);
        super.onLoadData(data);
    }

    public void gotoWX(String path) {
        String appId = AppConfigs.APP_ID_WECHAT; // 填应用AppId，APP在开放平台注册的id
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = AppConfigs.APP_ORIGINAL_ID_WECHAT_APPLETS; // 填小程序原始id
        req.path = path;
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST;// 可选打开 开发版，体验版和正式版
//        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    private void goWeb(String url) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url", url);
        getActivity().startActivity(intent);
    }
}
