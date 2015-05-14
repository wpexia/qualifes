package com.qualifies.app.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifies.app.config.Api;
import com.qualifies.app.util.AsyncHttpCilentUtil;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingCartManager {
    private static ShoppingCartManager inst = new ShoppingCartManager();

    public static ShoppingCartManager getInstance() {
        return inst;
    }

    public void addShoppingCart(String token, String[] goodsId, String[] goodsAttr, String[] goodsNum, Handler handler, Context context) {
        Thread addShoppingCartThread = new Thread(new AddShoppingCart(token, goodsId, goodsAttr, goodsNum, handler, context));
        addShoppingCartThread.start();
    }

    class AddShoppingCart implements Runnable {
        private String token;
        private String[] goodsId;
        private String[] goodsAttr;
        private String[] goodsNum;
        private Handler handler;
        private Context context;

        public AddShoppingCart(String token, String[] goodsId, String[] goodsAttr, String[] goodsNum, Handler handler, Context context) {
            this.token = token;
            this.goodsId = goodsId;
            this.goodsAttr = goodsAttr;
            this.goodsNum = goodsNum;
            this.handler = handler;
            this.context = context;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            RequestParams requestParams = new RequestParams();
            final Message msg = handler.obtainMessage();
            requestParams.put("token", token);
            for (int i = 0; i < goodsId.length; i++) {
                requestParams.put("data[" + i + "][goods_id]", goodsId[i]);
                requestParams.put("data[" + i + "][attribute]", goodsAttr[i]);
                requestParams.put("data[" + i + "][number]", goodsNum[i]);
            }
            client.post(Api.url("add_cart"), requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    Log.e("add_cart", response.toString());
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


    public void getShoppingCart(String token, Handler handler) {
        Thread getShoppingCartThread = new Thread(new GetShoppingCartThread(token, handler));
        getShoppingCartThread.start();
    }

    class GetShoppingCartThread implements Runnable {
        private String token;
        private Handler handler;

        public GetShoppingCartThread(String token, Handler handler) {
            this.token = token;
            this.handler = handler;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams params = new RequestParams();
            params.put("token", token);
            params.put("data[limit][m]", "0");
            params.put("data[limit][n]", "100");
            client.get(Api.url("get_cart"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
//                    Log.e("get_cart", response.toString());
                    try {
                        msg.obj = response.getJSONObject("data").getJSONArray("data");
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void getOfflineCart(String goodsId, Handler handler) {
        Thread getOfflineCartThread = new Thread(new GetOfflineCartThread(goodsId, handler));
        getOfflineCartThread.start();
    }

    class GetOfflineCartThread implements Runnable {
        private String goodsId;
        private Handler handler;

        public GetOfflineCartThread(String goodsId, Handler handler) {
            this.goodsId = goodsId;
            this.handler = handler;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams params = new RequestParams();
            params.put("goods[0][goods_id]", goodsId);
            client.post(Api.url("get_off_cart"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Api.dealSuccessRes(response, msg);
                        Log.e("get_off_cart", response.toString());
                        msg.obj = response.getJSONObject("data").getJSONArray("data");
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
