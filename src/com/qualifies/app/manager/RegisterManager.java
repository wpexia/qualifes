package com.qualifies.app.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.loopj.android.http.*;
import com.qualifies.app.config.Api;
import com.qualifies.app.util.AsyncHttpCilentUtil;
import com.qualifies.app.util.RSAHelper;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RegisterManager {

    private static RegisterManager inst = new RegisterManager();
    private Handler handler;
    private String username;
    private String password;
    private String code;
    private String service = "reg_user";
    private Context context;

    public static RegisterManager getInst() {
        return inst;
    }

    public void getCode(String username, Handler handler,Context context) {
        this.username = username;
        this.context = context;
        this.handler = handler;
        Thread thread = new Thread(new GcThread());
        thread.start();
    }

    class GcThread implements Runnable {
        @Override
        public void run() {
            final Message msg = handler.obtainMessage();
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
            client.setCookieStore(myCookieStore);
            final RequestParams requestParams = new RequestParams();
            requestParams.put("data[_type]", "gc");
            requestParams.put("data[phone]", username);
            client.post(Api.url(service), requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    handler.sendMessage(msg);
                }

                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers, String responseString,
                                      Throwable throwable) {
                    Api.dealFailRes(msg);
                    handler.sendMessage(msg);
                }
            });
        }
    }


    public void register(String username, String code, String password, Handler handler, Context context) {
        this.username = username;
        this.code = code;
        try {
            InputStream publickKeyIn = context.getAssets().open("public.der");
            PublicKey pk = RSAHelper.loadPublicKey(publickKeyIn);
            this.password = RSAHelper.encrypy(password, pk);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.handler = handler;
        this.context = context;
        Thread thread = new Thread(new RegisterThread());
        thread.start();
    }


    class RegisterThread implements Runnable {
        @Override
        public void run() {
            final Message msg = handler.obtainMessage();
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
            client.setCookieStore(myCookieStore);
            RequestParams requestParams = new RequestParams();
//            JSONObject json = new JSONObject();
//            try {
//                json.put("_type","reg");
//
//            } catch (JSONException e) {
//
//            }
            requestParams.put("data[_type]", "reg");
            requestParams.put("data[phone]", username);
            requestParams.put("data[pwd]", URLEncoder.encode(password));
            requestParams.put("data[verify]", code);
            client.post(Api.url(service), requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    handler.sendMessage(msg);
                }

                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers, String responseString,
                                      Throwable throwable) {
                    Api.dealFailRes(msg);
                    handler.sendMessage(msg);
                }
            });

        }
    }
}
