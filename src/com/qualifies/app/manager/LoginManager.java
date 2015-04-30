package com.qualifies.app.manager;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.qualifies.app.config.Api;
import com.qualifies.app.util.RSAUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.InputStream;
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
        this.password = password;
        this.handler = handler;
        this.context = context;
        try {
            InputStream publicIn = context.getAssets().open("public_key.pem");
            PublicKey publicKey = RSAUtils.loadPublicKey(publicIn);
            byte[] encryptByte = RSAUtils.encryptData(password.getBytes(), publicKey);
            String afterencrypt = Base64.encodeToString(encryptByte, Base64.NO_PADDING);
            this.password = afterencrypt;
            Log.e("encode", afterencrypt);
            publicIn.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            InputStream privateIn = context.getAssets().open("private_key.pem");
            PrivateKey privateKey = RSAUtils.loadPrivateKey(privateIn);
            byte[] decodeByte = RSAUtils.decryptData(Base64.decode(this.password, Base64.NO_PADDING), privateKey);
            String afterdecrypy = Base64.encodeToString(decodeByte, Base64.NO_PADDING);
            Log.e("test   ", afterdecrypy);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
                    Api.dealFailRes(msg);
                    handler.sendMessage(msg);
                }
            });
        }
    }

}
