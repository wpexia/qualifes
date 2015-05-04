package com.qualifies.app.manager;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.qualifies.app.config.Api;
import com.qualifies.app.util.Base64Helper;
import com.qualifies.app.util.RsaHelper;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;


public class LoginManager {
    private Handler handler;
    private String username;
    private String password;
    private String service = "log_user";
    private Context context;

    private static LoginManager inst = new LoginManager();

    public static LoginManager getInstance() {
        return inst;
    }

    public void login(String username, String password, Handler handler, Context context) {
        this.username = username;
        try {
            Log.e("raw", password);
            InputStream publicKeyIn = context.getAssets().open("rsa_public_key.pem");
            PublicKey publicKey = RsaHelper.loadPublicKey(publicKeyIn);
            this.password = RsaHelper.encryptDataFromStr(password, publicKey);
            this.password = URLEncoder.encode(this.password, "UTF-8");
            Log.e("password", this.password);


            InputStream privateKeyIn = context.getAssets().open("pkcs8_rsa_private_key.pem");
            PrivateKey privateKey = RsaHelper.loadPrivateKey(privateKeyIn);
            Log.e("decode", new String(RsaHelper.decryptData(Base64Helper.decode(this.password),privateKey)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.handler = handler;
        this.context = context;
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
            try {
                requestParams.put("pwd", password);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    Api.dealFailRes(msg);
                    handler.sendMessage(msg);
                }
            });
        }
    }

}
