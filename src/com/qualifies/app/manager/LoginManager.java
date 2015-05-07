package com.qualifies.app.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.qualifies.app.config.Api;
import org.apache.http.Header;
import org.json.JSONObject;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;


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
            InputStream f = context.getAssets().open("public.der");
            DataInputStream dis = new DataInputStream(f);
            byte[] keyByte = new byte[(int)f.available()];
            dis.readFully(keyByte);
            dis.close();

            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyByte);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pk = kf.generatePublic(spec);

            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            byte[] cipherText = cipher.doFinal(password.getBytes());
            this.password = new String(Base64.encode(cipherText, Base64.DEFAULT));
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
                requestParams.put("pwd", URLEncoder.encode(password, "utf-8"));
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
