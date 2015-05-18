package com.qualifies.app.manager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifies.app.config.Api;
import com.qualifies.app.util.AsyncHttpCilentUtil;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderManager {
    private static OrderManager inst = new OrderManager();

    public static OrderManager getInstance() {
        return inst;
    }

    public void initOrder(String token, JSONArray goods, Handler handler) {
        Thread initOrderThread = new Thread(new InitOrderThread(token, goods, handler));
        initOrderThread.start();
    }

    private static class InitOrderThread implements Runnable {
        String token;
        JSONArray goods;
        Handler handler;

        public InitOrderThread(String token, JSONArray goods, Handler handler) {
            this.token = token;
            this.goods = goods;
            this.handler = handler;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams params = new RequestParams();
            params.put("token", token);
//            Log.e("goods", goods.toString());
            for (int i = 0; i < goods.length(); i++) {
                try {
                    JSONObject obj = goods.getJSONObject(i);
                    params.put("data[goods][" + String.valueOf(i) + "][goods_id]", obj.getString("goods_id"));
                    params.put("data[goods][" + String.valueOf(i) + "][number]", obj.getString("goods_number"));
                    params.put("data[goods][" + String.valueOf(i) + "][attribute]", obj.getString("goods_attr_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            client.post(Api.url("order_info"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
//                    Log.e("order_info", response.toString());
                    try {
                        msg.obj = response.getJSONObject("data");
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    public void getOrderPrice(String token, String addressId, JSONArray goods, String bonusId, Handler handler) {
        Thread getOrderPriceThread = new Thread(new GetOrderPriceThread(token, addressId, goods, bonusId, handler));
        getOrderPriceThread.start();
    }


    private static class GetOrderPriceThread implements Runnable {
        private String token;
        private String addressId;
        private JSONArray goods;
        private String bonusId;
        private Handler handler;

        public GetOrderPriceThread(String token, String addressId, JSONArray goods, String bonusId, Handler handler) {
            this.token = token;
            this.addressId = addressId;
            this.goods = goods;
            this.bonusId = bonusId;
            this.handler = handler;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams params = new RequestParams();
            params.put("token", token);
            params.put("data[address_id]", addressId);
            if (!bonusId.equals("")) {
                params.put("data[bonus_id]", bonusId);
            }
            for (int i = 0; i < goods.length(); i++) {
                try {
                    JSONObject obj = goods.getJSONObject(i);
                    params.put("data[goods][" + String.valueOf(i) + "][goods_id]", obj.getString("goods_id"));
                    params.put("data[goods][" + String.valueOf(i) + "][number]", obj.getString("goods_number"));
                    params.put("data[goods][" + String.valueOf(i) + "][attribute]", obj.getString("goods_attr_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            client.post(Api.url("get_order_total"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    Log.e("get_order_total", response.toString());
                    try {
                        msg.obj = response.getJSONObject("data");
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


    public void creatOrder(String token, String addressId, String payType, JSONArray goods, Handler handler) {
        Thread creatOrderThread = new Thread(new CreatOrderThread(token, addressId, payType, goods, handler));
        creatOrderThread.start();
    }

    private static class CreatOrderThread implements Runnable {
        private String token;
        private String addressId;
        private String payType;
        private JSONArray goods;
        private Handler handler;

        public CreatOrderThread(String token, String addressId, String payType, JSONArray goods, Handler handler) {
            this.token = token;
            this.addressId = addressId;
            this.payType = payType;
            this.goods = goods;
            this.handler = handler;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams params = new RequestParams();
            params.put("token", token);
            params.put("data[referer]", "android");
            params.put("data[address_id]", addressId);
            params.put("data[pay_type]", payType);
            for (int i = 0; i < goods.length(); i++) {
                try {
                    JSONObject obj = goods.getJSONObject(i);
                    params.put("data[goods][" + String.valueOf(i) + "][goods_id]", obj.getString("goods_id"));
                    params.put("data[goods][" + String.valueOf(i) + "][number]", obj.getString("goods_number"));
                    params.put("data[goods][" + String.valueOf(i) + "][attribute]", obj.getString("goods_attr_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            client.post(Api.url("add_order"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
//                    Log.e("add_order", response.toString());
                    msg.obj = response;
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
