package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.qualifes.app.R;
import com.qualifes.app.manager.OrderManager;
import com.qualifes.app.manager.ZFBManager;
import com.qualifes.app.ui.adapter.OrderDetailListAdapter;
import com.qualifes.app.util.DisplayParams;
import com.qualifes.app.util.DisplayUtil;
import com.qualifes.app.util.WXApi;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailActivity extends Activity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetailnew);
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("user", MODE_PRIVATE);
        Intent intent = getIntent();
        String id = intent.getStringExtra("orderId");
        OrderManager manager = OrderManager.getInstance();

        manager.getOrderById(sp.getString("token", ""), id, getOrderByIdHandler);
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.price).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, SendFunctionPriceActivity.class);
                startActivity(intent);
            }
        });
    }


    Handler getOrderByIdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONObject data = (JSONObject) msg.obj;
                    int status;
                    if (data.getInt("shipping_status") == 0 && data.getInt("pay_status") == 0 && (data.getInt("order_status") == 0 || data.getInt("order_status") == 1)) {
                        status = 0;
                    } else if (data.getInt("order_status") == 1 &&
                            data.getInt("shipping_status") == 0 &&
                            data.getInt("pay_status") == 2) {
                        status = 1;
                    } else if ((data.getInt("order_status") == 1 &&
                            data.getInt("shipping_status") == 3 &&
                            data.getInt("pay_status") == 2) ||
                            (data.getInt("order_status") == 5 &&
                                    data.getInt("shipping_status") == 5 &&
                                    data.getInt("pay_status") == 2)) {
                        status = 2;
                    } else if (data.getInt("order_status") == 5 &&
                            data.getInt("shipping_status") == 1 &&
                            data.getInt("pay_status") == 2) {
                        status = 3;
                    } else if (data.getInt("order_status") == 5 &&
                            data.getInt("shipping_status") == 2 &&
                            data.getInt("pay_status") == 2) {
                        status = 4;
                    } else if (data.getInt("order_status") == 2 &&
                            data.getInt("shipping_status") == 0 &&
                            data.getInt("pay_status") == 0) {
                        status = 5;
                    } else if (data.getInt("order_status") == 3 &&
                            data.getInt("shipping_status") == 0 &&
                            data.getInt("pay_status") == 0) {
                        status = 6;
                    } else
                        status = 6;

                    String[] statusSt = {
                            "等待支付", "已付款",
                            "发货中",
                            "已发货",
                            "订单完成",
                            "订单取消",
                            "订单关闭"};
                    int[] statusImg = {R.drawable.icon_wait, R.drawable.icon_ok, R.drawable.icon_ok, R.drawable.icon_ok, R.drawable.icon_ok, R.drawable.icon_ok, R.drawable.icon_cancel};
                    ((TextView) findViewById(R.id.status)).setText(statusSt[status]);
                    ((ImageView) findViewById(R.id.statusImg)).setImageDrawable(getResources().getDrawable(statusImg[status]));
                    ((TextView) findViewById(R.id.goodsAmount)).setText("商品金额：" + data.getString("goods_amount"));
                    ((TextView) findViewById(R.id.shippingFee)).setText("运费：" + data.getString("shipping_fee"));
                    String amountStr;
                    if (status == 0) {
                        amountStr = data.getString("order_amount");
                    } else if (status < 5) {
                        amountStr = data.getString("money_paid");
                    } else {
                        amountStr = "0.00";
                    }
                    ((TextView) findViewById(R.id.orderAmount)).setText(amountStr);
                    LinearLayout foot = (LinearLayout) findViewById(R.id.footer);
                    final String orderId = data.getString("order_id");
                    if (status == 0) {
                        foot.setVisibility(View.VISIBLE);
                        findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OrderManager manager = OrderManager.getInstance();
                                SharedPreferences sp = getSharedPreferences("user", Activity.MODE_PRIVATE);

                                manager.cancleOrder(sp.getString("token", ""), orderId, handler);
                            }

                            Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            };

                        });
                        final int payId = data.getInt("pay_id");
                        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String payType = "alipay_app";
                                if (payId != 4) {
                                    payType = "weixin_app";
                                }
                                OrderManager manager = OrderManager.getInstance();
                                SharedPreferences sp = getSharedPreferences("user", Activity.MODE_PRIVATE);

                                manager.orderPay(sp.getString("token", ""), orderId, payType, handler);
                            }

                            Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    if (msg.what == 0) {
                                        try {
                                            if (payId != 4) {
                                                JSONObject data = ((JSONObject) msg.obj).getJSONObject("data");
                                                Log.e("WXPay", data.toString());
                                                final IWXAPI api = WXAPIFactory.createWXAPI(OrderDetailActivity.this, null);
                                                PayReq request = new PayReq();
                                                request.appId = WXApi.APP_ID;
                                                request.partnerId = data.getString("partnerid");
                                                request.prepayId = data.getString("prepayid");
                                                request.packageValue = data.getString("package");
                                                request.nonceStr = data.getString("noncestr");
                                                request.timeStamp = data.getString("timestamp");
                                                request.sign = data.getString("sign");
                                                api.registerApp(WXApi.APP_ID);
                                                api.sendReq(request);
                                            } else {
                                                String params = ((JSONObject) msg.obj).getString("data");
                                                ZFBManager.pay(params, OrderDetailActivity.this, ZFBPayHandler);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            };

                            Handler ZFBPayHandler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {

                                }
                            };
                        });


                    }
                    ((TextView) findViewById(R.id.orderSN)).setText(data.getString("order_sn"));
                    ((TextView) findViewById(R.id.consignee)).setText(data.getString("consignee"));
                    ((TextView) findViewById(R.id.mobile)).setText(data.getString("mobile"));
                    ((TextView) findViewById(R.id.bestTime)).setText(data.getString("best_time"));
                    ((TextView) findViewById(R.id.address)).setText(data.getString("address"));
                    JSONArray goods = data.getJSONArray("goods");
                    ListView listView = (ListView) findViewById(R.id.content);
                    DisplayParams displayParams = DisplayParams.getInstance(getApplicationContext());
                    int size = goods.length();
                    int itemHeight = DisplayUtil.dip2px(78, displayParams.scale);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size * itemHeight);
                    listView.setLayoutParams(params);
                    listView.setDividerHeight(0);
                    listView.setAdapter(new OrderDetailListAdapter(OrderDetailActivity.this, goods));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
