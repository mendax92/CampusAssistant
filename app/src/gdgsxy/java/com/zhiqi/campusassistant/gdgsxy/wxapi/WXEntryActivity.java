package com.zhiqi.campusassistant.gdgsxy.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.ming.base.util.Log;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConfigs;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fl_contair);
        api = WXAPIFactory.createWXAPI(this, AppConfigs.APP_ID_WECHAT);
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        //微信登录
        if (resp instanceof SendAuth.Resp) {
            SendAuth.Resp sendResp = (SendAuth.Resp) resp;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Log.e("onResp", "ERR_OK");
                    //发送成功
                    if (sendResp != null) {
                        String code = sendResp.code;
                        //可从此入口微信登陆
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Log.e("onResp112", "ERR_USER_CANCEL");
                    //发送取消
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    Log.e("onResp", "ERR_AUTH_DENIED");
                    //发送被拒绝
                    break;
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                    Log.e("onResp", "ERR_UN_SUPPORT");
                    break;
                default:
                    //发送返回
                    Log.e("onResp", "ERROR_CODE_UN_KNOW");
                    break;
            }

        } else {
            onResp1(resp);
        }
        finish();
        int result = 0;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                ToastUtil.show(this,"分享成功");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtil.show(this,"取消分享");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                break;
            default:
                break;
        }
        finish();
    }

    //发送到微信请求的响应结果
    public void onResp1(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.e("onResp", "ERR_USER_ERR_OK");
                //发送成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.e("onResp", "ERR_USER_CANCEL");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.e("onResp", "ERR_AUTH_DENIED");
                //发送被拒绝
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                Log.e("onResp", "ERR_UN_SUPPORT");
                break;
            default:
                //发送返回
                Log.e("onResp", "ERROR_CODE_UN_KNOW");
                break;
        }
    }
}