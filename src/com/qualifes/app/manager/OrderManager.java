package com.qualifes.app.manager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifes.app.config.Api;
import com.qualifes.app.util.AsyncHttpCilentUtil;
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


    public void creatOrder(String token, String addressId, String payType, int receiveTime, String moneyId, JSONArray goods, Handler handler) {
        Thread creatOrderThread = new Thread(new CreatOrderThread(token, addressId, payType, receiveTime, moneyId, goods, handler));
        creatOrderThread.start();
    }

    private static class CreatOrderThread implements Runnable {
        private String token;
        private String addressId;
        private String payType;
        private JSONArray goods;
        private int receiveTime;
        private String moneyId;
        private Handler handler;

        public CreatOrderThread(String token, String addressId, String payType, int receiveTime, String moneyId, JSONArray goods, Handler handler) {
            this.token = token;
            this.addressId = addressId;
            this.payType = payType;
            this.goods = goods;
            this.handler = handler;
            this.moneyId = moneyId;
            this.receiveTime = receiveTime;
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
            if (!moneyId.equals("")) {
                params.put("data[bonus_id]", moneyId);
            }
            if (payType.equals("weixin_app")) {
                params.put("data[pay_id]", "8");
            }
            params.put("data[best_time]", String.valueOf(receiveTime));
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


    public void getOrder(String token, String payStatus, String shippingStatus, String orderStatus, Handler handler) {
        Thread getOrderThread = new Thread(new GetOrderThread(token, payStatus, shippingStatus, orderStatus, handler));
        getOrderThread.start();
    }

    private static class GetOrderThread implements Runnable {
        String token;
        String payStatus;
        String shippingStatus;
        String orderStatus;
        Handler handler;

        public GetOrderThread(String token, String payStatus, String shippingStatus, String orderStatus, Handler handler) {
            this.token = token;
            this.payStatus = payStatus;
            this.shippingStatus = shippingStatus;
            this.orderStatus = orderStatus;
            this.handler = handler;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams params = new RequestParams();
            params.put("token", token);
            params.put("data[limit][m]", 0);
            params.put("data[limit][n]", 100);
            if (!orderStatus.trim().equals("")) {
                params.put("data[order_status]", orderStatus);
            }
            if (!payStatus.trim().equals("")) {
                params.put("data[pay_status]", payStatus);
            }
            if (!shippingStatus.trim().equals("")) {
                params.put("data[shipping_status]", shippingStatus);
            }
            client.post(Api.url("get_order"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    Log.e("get_order", response.toString());
                    try {
                        msg.obj = response.getJSONObject("data").getJSONArray("data");
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        msg.what = 1;
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                }
            });
        }
    }


    public void getOrderById(String token, String id, Handler handler) {
        Thread getOrderByIdThread = new Thread(new GetOrderByIdThread(token, id, handler));
        getOrderByIdThread.start();
    }


    private static class GetOrderByIdThread implements Runnable {
        String token;
        String id;
        Handler handler;

        public GetOrderByIdThread(String token, String id, Handler handler) {
            this.token = token;
            this.id = id;
            this.handler = handler;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams params = new RequestParams();
            params.put("token", token);
            params.put("data[order_id]", id);
            client.get(Api.url("get_order"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    Log.e("getOrderById", response.toString());
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
                }
            });
        }
    }

    public void getOrderFollow(String token, String orderId, Handler handler) {
        Thread thread = new Thread(new GetOrderFollowThread(token, orderId, handler));
        thread.start();
    }

    private static class GetOrderFollowThread implements Runnable {
        String token;
        String orderId;
        Handler handler;

        public GetOrderFollowThread(String token, String orderId, Handler handler) {
            this.token = token;
            this.orderId = orderId;
            this.handler = handler;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams params = new RequestParams();
            params.put("token", token);
            params.put("data[order_id]", orderId);
            client.post(Api.url("get_courier"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    try {
                        msg.obj = response.getJSONArray("data").getString(0);
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


    public void orderPay(String token, String orderId, String payType, Handler handler) {
        Thread thread = new Thread(new OrderPayThread(token, orderId, payType, handler));
        thread.start();
    }

    private static class OrderPayThread implements Runnable {
        String token;
        String orderId;
        String payType;
        Handler handler;

        public OrderPayThread(String token, String orderId, String payType, Handler handler) {
            this.token = token;
            this.payType = payType;
            this.orderId = orderId;
            this.handler = handler;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams params = new RequestParams();
            params.put("token", token);
            params.put("data[order_id]", orderId);
            params.put("data[pay_type]", payType);
            client.post(Api.url("order_pay"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.e("order_pay", response.toString());
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


    public void cancleOrder(String token, String orderId, Handler handler) {
        Thread thread = new Thread(new CancleOrderThread(token, orderId, handler));
        thread.start();
    }

    private static class CancleOrderThread implements Runnable {
        String token;
        String orderId;
        Handler handler;

        public CancleOrderThread(String token, String orderId, Handler handler) {
            this.token = token;
            this.orderId = orderId;
            this.handler = handler;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            final Message msg = handler.obtainMessage();
            RequestParams params = new RequestParams();
            params.put("token", token);
            params.put("data[order_id]", orderId);
            client.get(Api.url("cel_order"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
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
