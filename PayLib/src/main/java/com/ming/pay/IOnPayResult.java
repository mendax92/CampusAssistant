package com.ming.pay;

/**
 * Created by ming on 2017/7/23.
 */

public interface IOnPayResult {

    void onSuccess();

    void onCancel(String errStr);

    void onError(int errCode, String errStr);


}
