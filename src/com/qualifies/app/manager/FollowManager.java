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

public class FollowManager {
    private Handler handler;
    private String token;
    private String id;
    private int start;

    private static FollowManager inst = new FollowManager();

    public static FollowManager getInstance() {
        return inst;
    }

    public void getFollow(String token, Handler handler, Context context, int start) {
        this.token = token;
        this.handler = handler;
        this.start = start;

        Thread getFollowThread = new Thread(new GetFollowThread());
        getFollowThread.start();
    }

    class GetFollowThread implements Runnable {
        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            RequestParams requestParams = new RequestParams();
            final Message msg = handler.obtainMessage();
            requestParams.put("token", token);
            requestParams.put("data[limit][m]", String.valueOf(start));
            requestParams.put("data[limit][n]", "10");
            client.post(Api.url("get_ct_gs"), requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Api.dealSuccessRes(response, msg);
                    msg.obj = response;
//                    Log.e("follow", response.toString());
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

    public void delete(String token, String rec_id, Context context) {
        this.token = token;
        this.id = rec_id;
        Thread deleteFollow = new Thread(new DeleteFollow());
        deleteFollow.start();
    }

    class DeleteFollow implements Runnable {
        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            RequestParams requestParams = new RequestParams();
            final Message msg = handler.obtainMessage();
            requestParams.put("token", token);
            requestParams.put("data[rec_id]", id);
            client.post(Api.url("del_collect"), requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Api.dealSuccessRes(response, msg);
//                    Log.e("delete", response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }
}
