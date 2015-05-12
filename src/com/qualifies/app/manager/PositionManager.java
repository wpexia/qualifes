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

public class PositionManager {
    private static PositionManager inst = new PositionManager();

    public static PositionManager getInstance() {
        return inst;
    }

    public void addPosition(String token, String consignee, String phone, String province, String city, String district, String tel, String defa, String address, Handler handler, Context context) {
        Thread addPositionThread = new Thread(new AddPositionThread(token, consignee, phone, province, city, district, tel, defa, address, handler, context));
        addPositionThread.start();
    }

    class AddPositionThread implements Runnable {
        private String token;
        private String consignee;
        private String phone;
        private String province;
        private String city;
        private String district;
        private String tel;
        private String defa;
        private String address;
        private Handler handler;
        private Context context;


        public AddPositionThread(String token, String consignee, String phone, String province, String city, String district, String tel, String defa, String address, Handler handler, Context context) {
            this.token = token;
            this.consignee = consignee;
            this.phone = phone;
            this.province = province;
            this.city = city;
            this.district = district;
            this.tel = tel;
            this.defa = defa;
            this.address = address;
            this.handler = handler;
            this.context = context;
        }

        @Override
        public void run() {
            AsyncHttpClient client = AsyncHttpCilentUtil.getInstence();
            PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
            client.setCookieStore(myCookieStore);
            final Message msg = handler.obtainMessage();
            RequestParams requestParams = new RequestParams();
            requestParams.put("token", token);
            requestParams.put("data[consignee]", consignee);
            requestParams.put("data[phone]", phone);
            requestParams.put("data[province]", province);
            requestParams.put("data[is_default]", defa);
            requestParams.put("data[city]", city);
            requestParams.put("data[address]", address);
            requestParams.put("data[district]", district);
            Log.e("addAddressParams", requestParams.toString());
            if (!tel.trim().equals("")) {
                requestParams.put("data[tel]", tel);
            }
            client.post(Api.url("add_address"), requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
                    Log.e("addPosition", response.toString());
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