package com.qualifes.app.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.qualifes.app.R;
import com.qualifes.app.manager.OrderManager;
import com.qualifes.app.manager.ZFBManager;
import com.qualifes.app.ui.adapter.OrderAmountAdapter;
import com.qualifes.app.ui.fragment.OrderConfirmNullFragment;
import com.qualifes.app.ui.fragment.OrderConfirmOkFragment;
import com.qualifes.app.util.WXApi;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderConfirmActivity extends Activity implements View.OnClickListener {

    private FragmentManager manager;
    private SharedPreferences sp;

    private OrderConfirmNullFragment nullFragment;
    private OrderConfirmOkFragment okFragment;
    private int fragmentId = 0;


    private JSONObject position = new JSONObject();
    private JSONArray goods;
    private int receiveTime = 0;
    private int payFunction = 0;
    private String moneyId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirm);
        manager = getFragmentManager();
        Intent intent = getIntent();
        try {
            goods = new JSONArray(intent.getStringExtra("goods"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    private void initView() {
        sp = getSharedPreferences("user", MODE_PRIVATE);
        findViewById(R.id.goods_detail).setOnClickListener(this);
        findViewById(R.id.receive_time).setOnClickListener(this);
        findViewById(R.id.pay_function).setOnClickListener(OrderConfirmActivity.this);
        findViewById(R.id.send_function).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);


        changeFragment();

        OrderManager manager = OrderManager.getInstance();
        manager.initOrder(sp.getString("token", ""), goods, initOrderHander);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    Handler initOrderHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONObject data = (JSONObject) msg.obj;
                    if (!data.getString("address").equals("")) {
                        position = data.getJSONObject("address");
                        position.put("consignee", position.getString("name"));
                        fragmentId = 1;
                        changeFragment();
//                    Log.e("position", position.toString());
                        changeView();
                    }
                    if (data.getString("bonus").equals("yes")) {
                        findViewById(R.id.havered).setVisibility(View.VISIBLE);
                    }
                    findViewById(R.id.redmoney).setOnClickListener(OrderConfirmActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.chooseposition: {
                Intent intent = new Intent(this, ChoosePositionActivity.class);
                intent.putExtra("position", position.toString());
                startActivityForResult(intent, 1);
            }
            break;
            case R.id.goods_detail: {
                Intent intent = new Intent(this, OrderGoodsDetailActivity.class);
                intent.putExtra("goods", goods.toString());
                startActivity(intent);
            }
            break;
            case R.id.receive_time: {
                Intent intent = new Intent(this, ReachTimeActivity.class);
                intent.putExtra("choose", receiveTime);
                startActivityForResult(intent, 2);
            }
            break;
            case R.id.send_function: {
                Intent intent = new Intent(this, SendFunctionPriceActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.pay_function: {
                Intent intent = new Intent(this, PayFunctionActivity.class);
                startActivityForResult(intent, 3);
            }
            break;
            case R.id.redmoney: {
                Intent intent = new Intent(this, MoneyChooseActivity.class);
                intent.putExtra("money", moneyId);
                intent.putExtra("goods", goods.toString());
                startActivityForResult(intent, 4);
            }
            break;
            case R.id.confirm: {
                if (payFunction == 0) {
                    Toast.makeText(getApplicationContext(), "请选择支付方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                String token = sp.getString("token", "");
                String positionId = "";
                try {
                    positionId = position.getString("address_id");
//                    Log.e("position", position.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String[] payFunctions = {"alipay_app", "weixin_app"};
                String payFunctionStr = payFunctions[payFunction - 1];
                OrderManager manager = OrderManager.getInstance();
                if (payFunction == 2) {
                    manager.creatOrder(token, positionId, payFunctionStr, receiveTime, moneyId, goods, createOrderWXHandler);
                } else if (payFunction == 1) {
                    manager.creatOrder(token, positionId, payFunctionStr, receiveTime, moneyId, goods, creatOrderZFBHandler);
                }
                finish();
            }
            break;
        }
    }


    Handler creatOrderZFBHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Log.e("ZFB", ((JSONObject) msg.obj).toString());
            try {
                String params = ((JSONObject) msg.obj).getString("data");
                ZFBManager.pay(params, OrderConfirmActivity.this, ZFBPayHandler);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    Handler ZFBPayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            Log.e("ZFBPay", result);
            Intent intent = new Intent(OrderConfirmActivity.this, OrderListActivity.class);
            startActivity(intent);
        }
    };


    Handler createOrderWXHandler = new Handler() {
        String sign;
        String partnerId;
        String prepayId;
        String nonceStr;
        String timeStamp;
        String packageValue;

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONObject data = ((JSONObject) msg.obj).getJSONObject("data");
                    Log.e("WXPay", data.toString());
                    sign = data.getString("sign");
                    partnerId = data.getString("partnerid");
                    prepayId = data.getString("prepayid");
                    nonceStr = data.getString("noncestr");
                    timeStamp = data.getString("timestamp");
                    packageValue = data.getString("package");

                    final IWXAPI api = WXAPIFactory.createWXAPI(OrderConfirmActivity.this, null);
                    PayReq request = new PayReq();
                    request.appId = WXApi.APP_ID;
                    request.partnerId = partnerId;
                    request.prepayId = prepayId;
                    request.packageValue = packageValue;
                    request.nonceStr = nonceStr;
                    request.timeStamp = timeStamp;
                    request.sign = sign;
                    api.registerApp(WXApi.APP_ID);
                    api.sendReq(request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            return;
        }
        switch (requestCode) {
            case 1: {
                try {
                    position = new JSONObject(data.getStringExtra("position"));
                    Log.e("position", position.toString());
                    fragmentId = 1;
                    changeFragment();
//                    Log.e("position", position.toString());
                    changeView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            case 2: {
                String[] strs = {"节假日工作日均可", "仅工作日", "仅节假日"};
                ((TextView) findViewById(R.id.timeContent)).setText(strs[resultCode - 1]);
                receiveTime = resultCode - 1;
            }
            break;
            case 3: {
                if (resultCode == 1) {
                    ((ImageView) findViewById(R.id.paylogo)).setImageDrawable(getResources().getDrawable(R.drawable.pay_zfb));
                    ((TextView) findViewById(R.id.paycontent)).setText("支付宝");
                }
                if (resultCode == 2) {
                    ((ImageView) findViewById(R.id.paylogo)).setImageDrawable(getResources().getDrawable(R.drawable.pay_wechat));
                    ((TextView) findViewById(R.id.paycontent)).setText("微信支付");
                }
                payFunction = resultCode;
            }
            break;
            case 4: {
                if (resultCode == 1) {
                    moneyId = data.getStringExtra("moneyId");
                    String moneyStr = data.getStringExtra("moneyName");
                    if (moneyStr.length() > 13) {
                        moneyStr = moneyStr.substring(0, 12) + "…";
                    }
                    ((TextView) findViewById(R.id.havered)).setText(moneyStr);
                    changeView();
                }
            }
        }
    }

    private void changeFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        if (fragmentId == 0) {
            if (nullFragment == null) {
                nullFragment = new OrderConfirmNullFragment();
                transaction.add(R.id.head, nullFragment);
            } else {
                transaction.show(nullFragment);
            }
        } else {
            if (okFragment == null) {
                okFragment = new OrderConfirmOkFragment();
                transaction.add(R.id.head, okFragment);
            } else {
                transaction.show(okFragment);
            }
        }
        transaction.commit();
    }

    private void changeView() {
        okFragment.changeView(position);
        OrderManager manager = OrderManager.getInstance();
        try {
            String addressId = position.getString("address_id");
            manager.getOrderPrice(sp.getString("token", ""), addressId, goods, moneyId, changViewHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    Handler changViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject data = (JSONObject) msg.obj;
                try {
                    ((TextView) findViewById(R.id.total)).setText("￥" + data.getDouble("order_amount"));
                    JSONArray print = new JSONArray();
                    JSONObject obj = new JSONObject();
                    obj.put("name", "商品金额");
                    obj.put("data", data.getDouble("goods_amount"));
                    print.put(obj);
                    obj = new JSONObject();
                    obj.put("name", "运费");
                    obj.put("data", data.getDouble("direct_fee") + data.getDouble("trade_fee"));
                    print.put(obj);
                    if (data.getDouble("first_reduction") > 0) {
                        obj = new JSONObject();
                        obj.put("name", "首单立减");
                        obj.put("data", -data.getDouble("first_reduction"));
                        print.put(obj);
                    }
                    if (data.getDouble("bonus_money") > 0) {
                        obj = new JSONObject();
                        obj.put("name", "红包抵扣");
                        obj.put("data", -data.getDouble("bonus_money"));
                        print.put(obj);
                    }
                    ListView listView = (ListView) findViewById(R.id.amount);
                    listView.setDividerHeight(0);
                    OrderAmountAdapter adapter = new OrderAmountAdapter(getApplicationContext(), print);
                    listView.setAdapter(adapter);
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    int totalHeight = 0;
                    View listItem = adapter.getView(0, null, listView);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight() * adapter.getCount();
                    params.height = totalHeight + listView.getDividerHeight() * (print.length() - 1);
                    listView.setLayoutParams(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void hideFragment(FragmentTransaction transaction) {
        if (nullFragment != null) {
            transaction.hide(nullFragment);
        }
        if (okFragment != null) {
            transaction.hide(okFragment);
        }
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
