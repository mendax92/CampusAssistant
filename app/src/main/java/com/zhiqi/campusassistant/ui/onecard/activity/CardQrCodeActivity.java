package com.zhiqi.campusassistant.ui.onecard.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ming.base.util.RxUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.utils.ACache;
import com.zhiqi.campusassistant.common.utils.GlideImageLoader;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.app.entity.BannerInfos;
import com.zhiqi.campusassistant.core.app.entity.ModuleCategory;
import com.zhiqi.campusassistant.core.onecard.dagger.component.DaggerCardPayComponent;
import com.zhiqi.campusassistant.core.onecard.dagger.module.CardPayModule;
import com.zhiqi.campusassistant.core.onecard.entity.CardQrCodeInfo;
import com.zhiqi.campusassistant.core.onecard.presenter.CardPayPresenter;
import com.zhiqi.campusassistant.ui.web.activity.WebActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ming on 2018/2/3.
 * 校园卡二维码界面
 */

public class CardQrCodeActivity extends BaseLoadActivity<CardQrCodeInfo> implements ILoadView<CardQrCodeInfo> {

    @BindView(R.id.open_layout)
    View openLayout;
    @BindView(R.id.qr_layout)
    View qrLayout;
    @BindView(R.id.close_tip)
    TextView tipTxt;
    @BindView(R.id.qr_code)
    ImageView qrCodeImg;
    @BindView(R.id.qr_open)
    Button openBtn;
    @BindView(R.id.tv_refresh_qr)
    TextView tvRefreshQr;
    @BindView(R.id.banner)
    Banner banner;
    @Inject
    CardPayPresenter mPresenter;

    private CardQrCodeInfo qrCodeInfo;

    private Disposable updateDisposable;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_one_card_qr_code;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        refresh();
    }

    private void initDagger() {
        DaggerCardPayComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .cardPayModule(new CardPayModule())
                .build()
                .inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_more_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onRefresh() {
        mPresenter.getQrCodeInfo(this);
    }

    @Override
    public void onLoadData(CardQrCodeInfo data) {
        super.onLoadData(data);
        if (data != null) {
            qrCodeInfo = data;
            setBannerData(data);
            if (!qrCodeInfo.getIs_release()) {
                openLayout.setVisibility(View.VISIBLE);
                qrLayout.setVisibility(View.GONE);
                openBtn.setVisibility(View.GONE);
                tipTxt.setText(R.string.one_card_not_release_tip);
            } else if (!data.getIs_open()) {
                openLayout.setVisibility(View.VISIBLE);
                qrLayout.setVisibility(View.GONE);
                openBtn.setVisibility(View.VISIBLE);
                tipTxt.setText(R.string.one_card_not_open_tip);
            } else {
                openLayout.setVisibility(View.GONE);
                qrLayout.setVisibility(View.VISIBLE);
                updateQrCodeImg();
            }
            invalidateOptionsMenu();
        }
    }

    public void setBannerData(final CardQrCodeInfo data) {
        if (data != null && data.getData() != null && data.getData().size() > 0) {
            ArrayList<String> arrayList = new ArrayList();
            for (BannerInfos.BannerInfo bannerInfo : data.getData()) {
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
                    if (data.getData().get(position).getType() == 1) {
                        Intent intent = new Intent(CardQrCodeActivity.this, WebActivity.class);
                        intent.putExtra("url", data.getData().get(position).getUrl());
                        startActivity(intent);
                    }
                }
            });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }

    @OnClick({R.id.qr_open, R.id.qr_code})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.qr_open:
                Intent intent = new Intent(this, CheckCardPwdActivity.class);
                intent.putExtra(AppConstant.EXTRA_CARD_HAS_PAY_PASSWORD, qrCodeInfo.has_pay_password);
                startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_ONE_CARD_VERIFY_PWD);
                break;
            case R.id.qr_code://如果获取点击二维码 立刻从网络获取数据
                mPresenter.getQrCodeInfoFromNet(this);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.more:
                showOperateDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.ACTIVITY_REQUEST_ONE_CARD_VERIFY_PWD == requestCode && RESULT_OK == resultCode) {
            if (updateDisposable != null) {
                updateDisposable.dispose();
            }
            refresh();
        }
    }

    private void showOperateDialog() {
        CharSequence[] sequence;
        final boolean isOpen = qrCodeInfo != null && qrCodeInfo.getIs_release() && qrCodeInfo.getIs_open();
        if (isOpen) {
            sequence = getResources().getTextArray(R.array.card_qr_operate);
        } else {
            sequence = getResources().getTextArray(R.array.card_qr_operate2);
        }
        new MaterialDialog.Builder(this)
                .items(sequence)
                .itemsGravity(GravityEnum.CENTER)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        switch (position) {
                            case 0:
                                if (qrCodeInfo != null && !TextUtils.isEmpty(qrCodeInfo.instructions_url)) {
                                    Intent intent = new Intent(CardQrCodeActivity.this, WebActivity.class);
                                    intent.putExtra(AppConstant.EXTRA_URL, qrCodeInfo.instructions_url);
                                    startActivity(intent);
                                }
                                break;
                            case 1:
                                if (isOpen) {
                                    mPresenter.openPayment(false, null, openPaymentRequest);
                                }
                                break;
                        }
                    }
                })
                .show();
    }

    private IRequestView openPaymentRequest = new IRequestView() {
        @Override
        public void onQuest(int errorCode, String message) {
            ToastUtil.show(CardQrCodeActivity.this, message);
            if (HttpErrorCode.SUCCESS == errorCode) {
                refresh();
            }
        }
    };


    private void updateQrCodeImg() {
        if (qrCodeInfo == null || qrCodeInfo.qrcode_list == null || qrCodeInfo.qrcode_list.isEmpty()) {
            return;
        }
        CardQrCodeInfo.QrInfo qrInfo = qrCodeInfo.qrcode_list.get(0);
        if (qrInfo == null) {
            return;
        }
        qrInfo.isShow = true;
        mPresenter.saveQrCode(qrCodeInfo);
        mPresenter.getQrImage(qrInfo.qrcode, new Observer<Bitmap>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Bitmap bitmap) {
                qrCodeImg.setImageBitmap(bitmap);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.show(CardQrCodeActivity.this, R.string.one_card_qr_generate_error);
                qrCodeImg.setImageResource(R.drawable.ic_img_square_l);
            }

            @Override
            public void onComplete() {
            }
        });
        //每5分钟自动刷新
        updateDisposable = RxUtil.postOnMainThread(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        }, 5 * 60 * 1000);
    }

    @Override
    protected void onDestroy() {
        setResult(RESULT_OK);
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
        if (updateDisposable != null) {
            updateDisposable.dispose();
        }
    }
}
