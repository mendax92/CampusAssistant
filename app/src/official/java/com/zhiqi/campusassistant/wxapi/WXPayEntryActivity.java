package com.zhiqi.campusassistant.wxapi;

import com.ming.pay.wxapi.BaseWXPayEntryActivity;
import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Created by ming on 2017/7/23.
 */

public class WXPayEntryActivity extends BaseWXPayEntryActivity {

    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        finish();
    }
}
