package com.qualifes.app.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.qualifes.app.util.Api;
import com.qualifes.app.util.AsyncHttpCilentUtil;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchManager {

    private static SearchManager inst = new SearchManager();

    public static SearchManager getInstance() {
        return inst;
    }

    public void getHot(Context context, Handler handler) {
        Thread hotThread = new Thread(new HotThread(context, handler));
        hotThread.start();
    }

    class HotThread implements Runnable {
        Context context;
        Handler handler;

        public HotThread(Context context, Handler handler) {
            this.context = context;
            this.handler = handler;
        }


        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            client.get(Api.url("get_hot"), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        Api.dealSuccessRes(response, msg);
                        //Log.e("getSearchHot", response.toString());
                        msg.obj = response.getJSONArray("data");
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
}

