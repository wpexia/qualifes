package com.qualifies.app.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.qualifies.app.config.Api;
import com.qualifies.app.util.AsyncHttpCilentUtil;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class MoneyManager {

    private static MoneyManager inst = new MoneyManager();

    public static MoneyManager getInstance() {
        return inst;
    }

    public void getNow(String token, Handler handler, Context context) {
        Thread getNowThread = new Thread(new GetBonusThread(token, "1", handler, context));
        getNowThread.start();
    }

    public void getHistory(String token, Handler handler, Context context) {
        Thread getHistoryThread = new Thread(new GetBonusThread(token, "4", handler, context));
        getHistoryThread.start();
    }

    class GetBonusThread implements Runnable {

        private String token;
        private String type;
        private Handler handler;
        private Context context;

        public GetBonusThread(String token, String type, Handler handler, Context context) {
            this.token = token;
            this.type = type;
            this.handler = handler;
            this.context = context;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams requestParams = new RequestParams();
            requestParams.put("token", token);
            requestParams.put("data[limit][m]", 0);
            requestParams.put("data[limit][n]", 1000);
            requestParams.put("data[type]", type);
            client.post(Api.url("get_bonus"), requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Api.dealSuccessRes(response, msg);
//                        Log.e("get_bonus", response.toString());
                        msg.obj = response.getJSONArray("data");
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Api.dealFailRes(msg);
                    handler.sendMessage(msg);
                }
            });
        }
    }

    public void addMoney(String sn, String token, Handler handler, Context context) {
        Thread addMoneyThread = new Thread(new AddMoneyHandler(sn, token, handler, context));
        addMoneyThread.start();
    }

    class AddMoneyHandler implements Runnable {
        private String sn;
        private String token;
        private Handler handler;
        private Context context;

        public AddMoneyHandler(String sn, String token, Handler handler, Context context) {
            this.sn = sn;
            this.token = token;
            this.handler = handler;
            this.context = context;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams requestParams = new RequestParams();
            requestParams.put("token", token);
            requestParams.put("data[bonus_sn]", sn);
            client.post(Api.url("add_code_bonus"), requestParams, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    //Log.e("addMoney", response.toString());
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
