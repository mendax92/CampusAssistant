package com.zhiqi.campusassistant.common.http;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.ming.base.retrofit2.request.JsonRequestBody;
import com.ming.base.retrofit2.request.NameValuePair;
import com.ming.base.util.ChannelUtil;
import com.ming.base.util.DeviceUtil;
import com.ming.base.util.GsonUtils;
import com.ming.base.util.Log;
import com.ming.base.util.MD5Util;
import com.ming.base.util.TimeUtil;
import com.ming.base.util.URLEncodedUtils;
import com.zhiqi.campusassistant.common.utils.UuidUtil;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.security.manager.SecurityManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ming on 2016/11/16.
 * 网络拦截器
 */

public class HttpInterceptor implements Interceptor {

    private static final String TAG = "HttpInterceptor";

    private static final String PARAM_APP_KEY = "app_key";

    private static final String PARAM_USER_ID = "user_id";

    private static final String PARAM_TIMESTAMP = "timestamp";

    private static final String PARAM_SIGN = "sign";

    private String deviceType;

    private String resolution;

    private String userAgent;

    private String channelId;

    private int version;


    HttpInterceptor(Context context) {
        deviceType = DeviceUtil.getDeviceType(context);
        int[] metrics = DeviceUtil.getWindWidthAndHeight(context);
        resolution = metrics[0] + "X" + metrics[1];
        channelId = ChannelUtil.getAppChannel(context);
        version = DeviceUtil.getPackageVersion(context);
        Map<String, String> userMap = new HashMap<>();
        userMap.put("Brand", android.os.Build.BRAND);
        userMap.put("Model", android.os.Build.MODEL);
        userMap.put("SystemVersion", String.valueOf(Build.VERSION.SDK_INT));
        userMap.put("NetworkOperator", DeviceUtil.getNetworkOperator(context));
        userMap.put("NetworkOperatorName", DeviceUtil.getNetworkOperatorName(context));
        userMap.put("DeviceId", UuidUtil.getUuid(context));
        userAgent = GsonUtils.toJson(userMap);
        Log.i(TAG, "userAgent:" + userAgent);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(newBuilder(request));
    }

    private List<NameValuePair> getFixParams() {
        // 当前时间-与服务器的时间差
        long rightTime = TimeUtil.getSimpleCurrentTime() - SecurityManager.getInstance().getTimeGap();
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new NameValuePair(PARAM_APP_KEY, AppConfigs.APP_KEY));
        pairs.add(new NameValuePair(PARAM_TIMESTAMP, String.valueOf(rightTime)));
        LoginUser user = LoginManager.getInstance().getLoginUser();
        if (user != null) {
            pairs.add(new NameValuePair(PARAM_USER_ID, String.valueOf(user.getUser_id())));
        }
        return pairs;
    }

    private Request newBuilder(Request request) {
        RequestBody requestBody = request.body();
        List<NameValuePair> pairs = new ArrayList<>();
        RequestBody replaceBody = null;
        if (requestBody != null) {
            Log.i(TAG, "request:" + request);
            Log.i(TAG, "contentType:" + requestBody.contentType());
            Log.i(TAG, "body:" + requestBody);
            if (requestBody instanceof FormBody) {
                FormBody body = (FormBody) requestBody;
                for (int i = 0; i < body.size(); i++) {
                    String value = body.value(i);
                    if (!TextUtils.isEmpty(value)) {
                        pairs.add(new NameValuePair(body.name(i), value));
                    }
                }
                if (!pairs.isEmpty()) {
                    TypeAdapter<Map<String, String>> adapter = GsonUtils.getGson().getAdapter(new TypeToken<Map<String, String>>() {
                    });
                    Map<String, String> form = new HashMap<>();
                    for (NameValuePair pair : pairs) {
                        form.put(pair.getName(), pair.getValue());
                    }
                    replaceBody = JsonRequestBody.newBuilder(GsonUtils.getGson(), adapter, form);
                }
            } else if (requestBody instanceof JsonRequestBody) {
                JsonRequestBody jsonBody = (JsonRequestBody) requestBody;
                pairs.addAll(SignUtil.parseObject(jsonBody.getValue()));
            }
        }
        String method = request.method();
        List<NameValuePair> urlPairs = URLEncodedUtils.parse(request.url().uri());
        if (urlPairs != null) {
            // get请求已get参数为基准，其他以body参数为基准
            if ("GET".equals(method)) {
                pairs = SignUtil.addValuePairs(urlPairs, pairs);
            } else {
                pairs = SignUtil.addValuePairs(pairs, urlPairs);
            }
        }
        List<NameValuePair> fixParams = getFixParams();
        SignUtil.addValuePairs(pairs, fixParams);
        LoginUser user = LoginManager.getInstance().getLoginUser();
        String token = user != null ? user.getToken() : null;
        String sign = SignUtil.generateSign(pairs, AppConfigs.APP_SECRET, token);
        fixParams.add(new NameValuePair(PARAM_SIGN, sign));
        String newUrl = URLEncodedUtils.format(request.url().toString(), fixParams);
        Log.i(TAG, "request url : " + newUrl);
        Request.Builder builder = request.newBuilder()
                .url(newUrl)
                .addHeader("DeviceType", deviceType)
                .addHeader("Resolution", resolution)
                .addHeader("User-Agent", userAgent)
                .addHeader("ChannelId", channelId)
                .addHeader("AppVersion", String.valueOf(version));
        /*if (newUrl.startsWith(HttpUrlConstant.TEST_HOST)) {
            builder.addHeader("x-api-key", "a84513cf085d415d836ff4e425c123f6");
        }*/
        if (replaceBody != null && "POST".equals(request.method())) {
            builder.post(replaceBody);
        }
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("UserToken", MD5Util.getMD5Value(token));
        }
        return builder.build();
    }
}
