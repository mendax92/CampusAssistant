package com.ming.pay;

/**
 * Created by ming on 2017/7/23.
 */

public class PayException extends Exception {

    public PayException(String error) {
        super(error);
    }

    public PayException(String error, Throwable throwable) {
        super(error, throwable);
    }
}
