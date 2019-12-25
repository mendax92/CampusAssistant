package com.ming.pay;

import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by ming on 17-7-11.
 * 微信服务
 */

public class WeChatService implements IPayService {

    private IWXAPI mApi;

    private Map<String, IOnPayResult> results = new HashMap<>();

    @Override
    public void init(Context context, String appId) {
        mApi = WXAPIFactory.createWXAPI(context.getApplicationContext(), appId);
        mApi.registerApp(appId);
    }

    @Override
    public boolean isAppInstalled() {
        return mApi.isWXAppInstalled();
        //wwz
//        return mApi.isWXAppInstalled() && mApi.isWXAppSupportAPI();
    }

    @Override
    public void pay(IPayRequest<?> request, IOnPayResult onPayResult) throws PayException {
        if (!isAppInstalled()) {
            return;
        }

        Object object = request.getRequest();
        if (!(object instanceof PayReq)) {
            throw new PayException("com.ming.pay.PayException : PayRequest getRequest must return com.tencent.mm.opensdk.modelpay.PayReq");
        }
        PayReq req = (PayReq) object;
        if (onPayResult != null) {
            results.put(req.prepayId, onPayResult);
        }
        mApi.sendReq(req);
    }

    @Override
    public void checkResult() {
        if (results != null && !results.isEmpty()) {
            Set<Map.Entry<String, IOnPayResult>> entries = results.entrySet();
            Iterator<Map.Entry<String,IOnPayResult>> iter = entries.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, IOnPayResult> entry = iter.next();
                entry.getValue().onSuccess();
                iter.remove();
            }
        }
    }

    public boolean handleIntent(Intent intent, IWXAPIEventHandler handler) {
        return mApi.handleIntent(intent, handler);
    }

    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX
                && resp instanceof PayResp) {
            PayResp payResp = (PayResp) resp;
            IOnPayResult onPayResult = results.get(payResp.prepayId);
            if (onPayResult != null) {
                switch (resp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        onPayResult.onSuccess();
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        onPayResult.onCancel(payResp.errStr);
                        break;
                    default:
                        onPayResult.onError(payResp.errCode, payResp.errStr);
                        break;
                }
                results.remove(payResp.prepayId);
            }
        }
    }
}
