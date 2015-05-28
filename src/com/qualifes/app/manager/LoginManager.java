package com.qualifes.app.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import com.loopj.android.http.*;
import com.qualifes.app.util.Api;
import com.qualifes.app.util.AsyncHttpCilentUtil;
import com.qualifes.app.util.RSAHelper;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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
            InputStream publickKeyIn = context.getAssets().open("public.der");
            PublicKey pk = RSAHelper.loadPublicKey(publickKeyIn);
            this.password = RSAHelper.encrypy(password, pk);
//            Log.e("password", URLEncoder.encode(this.password));
        } catch (IOException e) {
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
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
            client.setCookieStore(myCookieStore);
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
                    if(response.has("token")){
                        try {
                            SharedPreferences sp = context.getSharedPreferences("user", context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.remove("token");
                            editor.remove("username");
                            editor.putString("token",response.getString("token"));
                            editor.putString("username", username);
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
