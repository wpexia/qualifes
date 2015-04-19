package com.qualifies.app.manager;

import android.os.Handler;
import android.os.Message;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.qualifies.app.config.Api;
import org.apache.http.Header;
import org.json.JSONObject;

public class RegisterManager {

    private static RegisterManager inst = new RegisterManager();
    private Handler handler;
    private String username;
    private String password;
    private String code;
    private String service = "reg_user";

    public static RegisterManager getInst() {
        return inst;
    }

    public void getCode(String username, Handler handler) {
        this.username = username;
        this.handler = handler;
        Thread thread = new Thread(new GcThread());
        thread.start();
    }

    class GcThread implements Runnable {
        @Override
        public void run() {
            final Message msg = handler.obtainMessage();
            SyncHttpClient client = new SyncHttpClient();
            final RequestParams requestParams = new RequestParams();
            requestParams.put("_type", "gc");
            requestParams.put("phone", username);
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

    public void verify(String username, String code, Handler handler) {
        this.username = username;
        this.code = code;
        this.handler = handler;
        Thread thread = new Thread(new VerifyThread());
        thread.start();
    }

    class VerifyThread implements Runnable {
        @Override
        public void run() {
            final Message msg = handler.obtainMessage();
            SyncHttpClient client = new SyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("_type", "cod");
            requestParams.put("phone", username);
            requestParams.put("verify", code);
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

    public void register(String username, String code, String password, Handler handler) {
        this.username = username;
        this.code = code;
        this.password = password;
        this.handler = handler;
        Thread thread = new Thread(new RegisterThread());
        thread.start();
    }


    class RegisterThread implements Runnable {
        @Override
        public void run() {
            final Message msg = handler.obtainMessage();
            SyncHttpClient client = new SyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("_type", "reg");
            requestParams.put("phone", username);
            requestParams.put("verify", code);
            requestParams.put("pwd", password);
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