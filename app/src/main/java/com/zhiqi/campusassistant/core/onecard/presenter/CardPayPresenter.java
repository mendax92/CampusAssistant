package com.zhiqi.campusassistant.core.onecard.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.Contents;
import com.google.zxing.client.Intents;
import com.google.zxing.client.encode.QRCodeEncoder;
import com.ming.base.greendao.rx2.RxDao;
import com.ming.base.http.HttpManager;
import com.ming.base.rx2.SimpleObserver;
import com.ming.base.util.GsonUtils;
import com.ming.base.util.Log;
import com.ming.base.util.MD5Util;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.utils.FileUtil;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.core.app.entity.BannerInfos;
import com.zhiqi.campusassistant.core.app.entity.ModuleCategory;
import com.zhiqi.campusassistant.core.onecard.api.CardPayApiService;
import com.zhiqi.campusassistant.core.onecard.entity.CardBalanceInfo;
import com.zhiqi.campusassistant.core.onecard.entity.CardOrderDetail;
import com.zhiqi.campusassistant.core.onecard.entity.CardOrderInfo;
import com.zhiqi.campusassistant.core.onecard.entity.CardPayRequest;
import com.zhiqi.campusassistant.core.onecard.entity.CardQrCodeInfo;
import com.zhiqi.campusassistant.core.onecard.entity.CardTopUpInfo;
import com.zhiqi.campusassistant.core.payment.entity.OrderResult;
import com.zhiqi.campusassistant.core.payment.entity.PayType;
import com.zhiqi.campusassistant.core.payment.presenter.BasePayPresenter;

import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ming on 2018/1/21.
 * 一卡通支付presenter
 */

public class CardPayPresenter extends BasePayPresenter {

    //    private static final long MAX_TIME_CACHE =60*60* 1000;//1小时
    private static final long MAX_TIME_CACHE = 12 * 60 * 60 * 1000;//12小时
    private CardPayApiService mApiService;
    private RxDao<CacheData, String> mDao;

    public CardPayPresenter(Context context, CardPayApiService apiService, AppDaoSession daoSession) {
        super(context);
        this.mApiService = apiService;
        this.mDao = daoSession.getRxDao(daoSession.getCacheDataDao());
    }

    /**
     * 获取余额信息
     */
    public void getBalance(ILoadView<CardBalanceInfo> loadView) {
        Observable<BaseResultData<CardBalanceInfo>> observable = mApiService.getBalanceInfo();
        subscribe(observable, loadView);
    }

    /**
     * 加载充值信息
     *
     * @param loadView 加载view
     */
    public void loadTopUpInfo(ILoadView<CardTopUpInfo> loadView) {
        Observable<BaseResultData<CardTopUpInfo>> observable = mApiService.loadTopUpInfo();
        subscribe(observable, loadView);
    }

    /**
     * 一卡通充值
     *
     * @param request 充值信息
     */
    public boolean topUpCard(CardPayRequest request, PayType payType, ILoadView<OrderResult> loadView) {
        Observable<BaseResultData<CardOrderInfo>> observable = mApiService.getCardOrderInfo(request);
        CheckResultProvider<CardOrderInfo> provider = new CheckResultProvider<CardOrderInfo>() {
            @Override
            public Observable<BaseResultData<OrderResult>> provideObservable(CardOrderInfo orderInfo) {
                return mApiService.checkOrderResult(orderInfo.order_id);
            }
        };
        switch (payType) {
            case ALIPAY:
                return false;
            case WECHAT:
                payWeChat(observable, provider, loadView);
                return true;
        }
        return false;
    }

    /**
     * 获取订单详情，orderId、orderNo任选其一
     *
     * @param orderId 订单ID
     * @param orderNo 订单号
     */
    public void getOrderDetail(long orderId, String orderNo, ILoadView<CardOrderDetail> loadView) {
        subscribe(mApiService.getOrderDetail(orderId, orderNo), loadView);
    }

    /**
     * 获取订单列表
     */
    public void getOrderList(int page, ILoadView<BasePageData<CardOrderDetail>> loadView) {
        subscribe(mApiService.getOrderList(page, AppConfigs.DEFAULT_PAGE_SIZE), loadView);
    }

    /**
     * 获取二维码信息
     */
    public void getQrCodeInfo(final ILoadView<CardQrCodeInfo> loadView) {
        mDao.load(CacheKey.CACHE_QRCODE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(SimpleObserver.create(new Consumer<CacheData>() {
                    @Override
                    public void accept(CacheData cacheData) throws Exception {
                        CardQrCodeInfo data = null;
                        if (cacheData != null) {
                            data = getCacheCardQrCode(cacheData);
                        }
                        //本地为空或者缓存超过12小时，则加载网络数据
                        if (data == null || data.qrcode_list == null || data.qrcode_list.size() == 0 || System.currentTimeMillis() - cacheData.getCacheTime() >= MAX_TIME_CACHE) {
                            getQrCodeInfoFromNet(loadView);
                        } else if (!isReleased(loadView)) {
                            loadView.onLoadData(data);
                        }
                    }
                }));
    }

    /**
     * 获取获取缓存二维码信息
     */
    private CardQrCodeInfo getCacheCardQrCode(CacheData cacheData) {
        CardQrCodeInfo data = (CardQrCodeInfo) cacheData.getData(new TypeToken<CardQrCodeInfo>() {
        }.getType());
        Iterator<CardQrCodeInfo.QrInfo> iter = data.qrcode_list.iterator();
        while (iter.hasNext()) {
            CardQrCodeInfo.QrInfo info = iter.next();
            // 将展示过的数据 删除  --- 这样每次 直接获取到的 都是没展示过的
            if (info.isShow) {
                iter.remove();
            }
        }
        return data;
    }

    /**
     * 直接网络获取二维码信息
     */
    public void getQrCodeInfoFromNet(final ILoadView<CardQrCodeInfo> loadView) {
        HttpManager.subscribe(mApiService.getQrCodeInfo(10), new OnHttpFilterCallback<BaseResultData<CardQrCodeInfo>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<CardQrCodeInfo> result) {
                if (result.data != null) {
                    getAdver(loadView, result);
                } else {
                    if (!isReleased(loadView)) {
                        loadView.onFailed(0, "");
                    }
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onFailed(errorCode, message);
                }
            }
        });
    }

    private void getAdver(final ILoadView<CardQrCodeInfo> loadView, final BaseResultData<CardQrCodeInfo> result) {
        Observable<BaseResultData<List<BannerInfos.BannerInfo>>> observable = mApiService.getAdver(BannerInfos.flag_qrcode_index);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<List<BannerInfos.BannerInfo>>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<List<BannerInfos.BannerInfo>> data) {
                result.data.setData(data.data);
                if (!isReleased(loadView)) {
                    loadView.onLoadData(result.data);
                }
                if (result.data != null && result.data.qrcode_list != null && result.data.qrcode_list.size() > 0 && !TextUtils.isEmpty(result.data.qrcode_list.get(0).qrcode)) {
                    CacheData<CardQrCodeInfo> cacheData = new CacheData<>(CacheKey.CACHE_QRCODE, result.data);
                    cacheData.setCacheTime(System.currentTimeMillis());
                    mDao.insertOrReplace(cacheData).subscribe();
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onFailed(errorCode, message);
                }
            }
        });
    }

    /**
     * 开启支付功能
     *
     * @param open     true is open, false is close
     * @param password 支付密码
     */
    public void openPayment(boolean open, String password, IRequestView requestView) {
        requestSimple(mApiService.openPayment(open ? 1 : 0, MD5Util.getMD5Value(password)), requestView);
    }

    /**
     * 获取qr图片
     *
     * @param qrCode   二维码
     * @param observer 回调
     */
    public void getQrImage(final String qrCode, Observer<Bitmap> observer) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                try {

                    int dimension = mContext.getResources().getDimensionPixelSize(R.dimen.one_card_qr_img_size);
                    Intent intent = new Intent(Intents.Encode.ACTION);
                    intent.putExtra(Intents.Encode.DATA, qrCode);
                    intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
                    intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
                    QRCodeEncoder encoder = new QRCodeEncoder(mContext, intent, dimension, false);
                    emitter.onNext(encoder.encodeAsBitmap());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    emitter.onError(ex);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 保存二维码信息
     */
    public void saveQrCode(CardQrCodeInfo info) {
        CacheData<CardQrCodeInfo> cacheData = new CacheData<>(CacheKey.CACHE_QRCODE, info);
        cacheData.setCacheTime(System.currentTimeMillis());
        mDao.insertOrReplace(cacheData).subscribe();
    }
}
