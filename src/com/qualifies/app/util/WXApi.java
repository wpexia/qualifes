package com.qualifies.app.util;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;


public class WXApi {
    public static final String APP_ID = "wx6d7eadfe25abaa8f";
    private static final String APP_KEY = "c5053ff4e3fd9b04527be17eec48d9bc";
    private static final String APP_SECRET = "c5053ff4e3fd9b04527be17eec48d9bc";

    public static IWXAPI api;

    public static void sendPayReq(String sign, String partnerId, String prepayId, String nonceStr, String timeStamp, String packageValue) {

        PayReq request = new PayReq();
        request.appId = APP_ID;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;
        api.registerApp(APP_ID);
        api.sendReq(request);
    }
}
