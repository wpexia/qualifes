package com.qualifes.app.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.*;
import android.os.Process;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifes.app.util.Api;
import com.qualifes.app.util.AsyncHttpCilentUtil;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeManger {

    final private String service = "get_visual";

    private static HomeManger inst = new HomeManger();

    public static HomeManger getInstance() {
        return inst;
    }

    public void getLogo(String logo, Handler handler, Context context) {
        Thread getLogoThread = new Thread(new GetLogoThread(logo, handler, context));
        getLogoThread.start();
    }

    class GetLogoThread implements Runnable {
        Handler handler;
        Context context;
        String logo;

        public GetLogoThread(String logo, Handler handler, Context context) {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
            this.logo = logo;
            this.handler = handler;
            this.context = context;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams requestParams = new RequestParams();
            requestParams.put("type", "activity");
            requestParams.put("logo", logo);
            client.get(Api.url(service), requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        Api.dealSuccessRes(response, msg);
                        msg.obj = response.getJSONArray("data");
//                        Log.e("get_visual", response.toString());
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Api.dealFailRes(msg);
                    handler.sendMessage(msg);
                }
            });
        }
    }

    public void getGoodsInfo(String logo, Handler handler, Context context) {
        Thread getGoodInfoThread = new Thread(new GetGoodInfoThread(logo, handler, context));
        getGoodInfoThread.start();
    }

    class GetGoodInfoThread implements Runnable {
        private String logo;
        private Handler handler;
        private Context context;

        public GetGoodInfoThread(String logo, Handler handler, Context context) {
            this.logo = logo;
            this.handler = handler;
            this.context = context;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams requestParams = new RequestParams();
            requestParams.put("type", "goods");
            requestParams.put("logo", logo);
            SharedPreferences sp = context.getSharedPreferences("user", Activity.MODE_PRIVATE);
            if (sp.contains("token")) {
                requestParams.put("token", sp.getString("token", ""));
            }
            client.get(Api.url(service), requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    try {
                        msg.obj = response.getJSONObject("data").getJSONArray("goods");
//                        Log.e("goods",msg.obj.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.sendMessage(msg);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Api.dealFailRes(msg);
                    handler.sendMessage(msg);
                }
            });
        }

    }
}