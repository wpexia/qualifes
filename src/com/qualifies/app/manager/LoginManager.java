package com.qualifies.app.manager;


import android.os.Handler;
import android.os.Message;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.qualifies.app.config.Api;
import org.apache.http.Header;
import org.json.JSONObject;

public class LoginManager {
    private Handler handler;
    private String username;
    private String password;
    private String service = "log_user";

    private static LoginManager inst = new LoginManager();

    public static LoginManager getInstance() {
        return inst;
    }

    public void login(String username, String password, Handler handler) {
        this.username = username;
        this.password = password;
        this.handler = handler;
        Thread loginThread = new Thread(new LoginThread());
        loginThread.start();
    }

    class LoginThread implements Runnable {

        @Override
        public void run() {
            SyncHttpClient client = new SyncHttpClient();
            RequestParams requestParams = new RequestParams();
            final Message msg = handler.obtainMessage();
            requestParams.put("_type", "log");
            requestParams.put("phone", username);
            requestParams.put("pwd", password);
            client.post(Api.url(service), requestParams, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    handler.sendMessage(msg);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Api.dealFailRes(msg);
                    handler.sendMessage(msg);
                }
            });
        }
    }

}
