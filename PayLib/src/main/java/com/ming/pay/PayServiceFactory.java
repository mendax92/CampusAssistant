package com.ming.pay;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ming on 2017/7/23.
 * 支付服务工厂
 */

public class PayServiceFactory {

    private Map<PayPlatform, IPayService> services = new ConcurrentHashMap<>();

    private static PayServiceFactory instance;

    private static PayServiceFactory getInstance() {
        if (instance == null) {
            synchronized (PayServiceFactory.class) {
                if (instance == null) {
                    instance = new PayServiceFactory();
                }
            }
        }
        return instance;
    }

    private PayServiceFactory() {
    }

    public static IPayService getPayService(PayPlatform platform) {
        switch (platform) {
            case WECHAT:
                IPayService service = getInstance().services.get(platform);
                if (service == null) {
                    service = new WeChatService();
                    getInstance().services.put(platform, service);
                }
                return service;
            case ALIPAY:
                service = getInstance().services.get(platform);
                if (service == null) {
                    service = new AlipayService();
                    getInstance().services.put(platform, service);
                }
                return service;
        }
        return null;
    }

    public static void checkResult() {
        Set<Map.Entry<PayPlatform, IPayService>> entries = getInstance().services.entrySet();
        for (Map.Entry<PayPlatform, IPayService> entry : entries) {
            entry.getValue().checkResult();
        }
    }
}
