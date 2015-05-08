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
import org.json.JSONObject;

public class HistoryManager {
    private String service = "get_browse";
    private Handler handler;
    private String token;
    private Context context;
    private int start;

    private static HistoryManager inst = new HistoryManager();

    public static HistoryManager getInstance() {
        return inst;
    }

    public void getHistory(String token, Handler handler,Context context,int start) {
        this.token = token;
        this.handler = handler;
        this.context = context;
        this.start = start;
        Thread historyThread = new Thread(new HistoryThread());
        historyThread.start();
    }


    class HistoryThread implements Runnable {
        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
            client.setCookieStore(myCookieStore);
            RequestParams requestParams = new RequestParams();
            final Message msg = handler.obtainMessage();
            requestParams.put("token",token);
            requestParams.put("data[limit][m]", String.valueOf(start));
            requestParams.put("data[limit][n]", "10");
            client.post(Api.url(service), requestParams, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Api.dealSuccessRes(response, msg);
                    msg.obj = response;
//                    Log.e("history", response.toString());
                    handler.sendMessage(msg);
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


}
