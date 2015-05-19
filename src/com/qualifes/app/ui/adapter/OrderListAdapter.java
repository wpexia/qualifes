package com.qualifes.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.qualifes.app.R;
import com.qualifes.app.manager.OrderManager;
import com.qualifes.app.manager.ZFBManager;
import com.qualifes.app.ui.OrderDetailActivity;
import com.qualifes.app.ui.WuLiuActivity;
import com.qualifes.app.util.AsyncImageLoader;
import com.qualifes.app.util.DisplayParams;
import com.qualifes.app.util.DisplayUtil;
import com.qualifes.app.util.WXApi;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderListAdapter extends BaseAdapter {
    private AsyncImageLoader imageLoader = new AsyncImageLoader();
    LayoutInflater inflater;
    JSONArray mData;
    final Context context;
    private Map<String, Bitmap> bitCache = new HashMap<>();

    public OrderListAdapter(Context context, JSONArray data) {
        inflater = LayoutInflater.from(context);
        mData = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            final JSONObject obj = mData.getJSONObject(position);
            JSONArray goods = obj.getJSONArray("goods");
            if (goods.length() < 2) {
                convertView = inflater.inflate(R.layout.orderlistitem, parent, false);
                ((TextView) convertView.findViewById(R.id.sn)).setText(obj.getString("order_sn"));
                ((TextView) convertView.findViewById(R.id.add_time)).setText("下单时间：" + obj.getString("add_time"));
                JSONObject good = goods.getJSONObject(0);
                ((TextView) convertView.findViewById(R.id.name)).setText(good.getString("goods_name"));
                ((TextView) convertView.findViewById(R.id.money)).setText("￥" + obj.getString("order_amount"));
                ImageView image = (ImageView) convertView.findViewById(R.id.image);
                final String imageUrl = good.getString("goods_thumb").toString();
                if (!bitCache.containsKey(imageUrl)) {
                    Bitmap cachedImage = imageLoader.loadDrawable(imageUrl, image,
                            new AsyncImageLoader.ImageCallback() {
                                public void imageLoaded(Bitmap imageDrawable,
                                                        ImageView imageView, String imageUrl) {
                                    bitCache.put(imageUrl, imageDrawable);
                                    imageView.setImageBitmap(imageDrawable);
                                }
                            }, 1);
                    if (cachedImage != null) {
                        bitCache.put(imageUrl, cachedImage);
                        image.setImageBitmap(cachedImage);
                    }
                } else {
                    image.setImageBitmap(bitCache.get(imageUrl));
                }
                try {
                    JSONArray attrArr = obj.getJSONArray("good_attr");
                    String st = "";
                    for (int i = 0; i < attrArr.length(); i++) {
                        st += attrArr.getString(i) + " ";
                    }
                    ((TextView) convertView.findViewById(R.id.attr)).setText(st);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                convertView = inflater.inflate(R.layout.orderlistitemmany, parent, false);
                ((TextView) convertView.findViewById(R.id.sn)).setText(obj.getString("order_sn"));
                ((TextView) convertView.findViewById(R.id.add_time)).setText("下单时间：" + obj.getString("add_time"));
                ((TextView) convertView.findViewById(R.id.money)).setText("￥" + obj.getString("order_amount"));

                GridView grid = (GridView) convertView.findViewById(R.id.gridView);
                DisplayParams displayParams = DisplayParams.getInstance(context);
                int size = goods.length();
                int itemWidth = DisplayUtil.dip2px(60, displayParams.scale);
                int gridViewWidth = size * itemWidth + (size - 1) * DisplayUtil.dip2px(10, displayParams.scale);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                grid.setLayoutParams(params);
                grid.setColumnWidth(itemWidth);
                grid.setStretchMode(GridView.NO_STRETCH);
                grid.setNumColumns(size);
                grid.setHorizontalScrollBarEnabled(false);
                grid.setVerticalScrollBarEnabled(false);
                grid.setHorizontalSpacing(DisplayUtil.dip2px(10, displayParams.scale));
                grid.setAdapter(new OrderListGridAdapter(context, goods));
            }
            final String orderId = obj.getString("order_id");
            convertView.findViewById(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("orderId", orderId);
                    context.startActivity(intent);
                }
            });
            int status;
            if (obj.getInt("shipping_status") == 0 && obj.getInt("pay_status") == 0 && (obj.getInt("order_status") == 0 || obj.getInt("order_status") == 1)) {
                status = 0;
            } else if (obj.getInt("order_status") == 1 &&
                    obj.getInt("shipping_status") == 0 &&
                    obj.getInt("pay_status") == 2) {
                status = 1;
            } else if ((obj.getInt("order_status") == 1 &&
                    obj.getInt("shipping_status") == 3 &&
                    obj.getInt("pay_status") == 2) ||
                    (obj.getInt("order_status") == 5 &&
                            obj.getInt("shipping_status") == 5 &&
                            obj.getInt("pay_status") == 2)) {
                status = 2;
            } else if (obj.getInt("order_status") == 5 &&
                    obj.getInt("shipping_status") == 1 &&
                    obj.getInt("pay_status") == 2) {
                status = 3;
            } else if (obj.getInt("order_status") == 5 &&
                    obj.getInt("shipping_status") == 2 &&
                    obj.getInt("pay_status") == 2) {
                status = 4;
            } else if (obj.getInt("order_status") == 2 &&
                    obj.getInt("shipping_status") == 0 &&
                    obj.getInt("pay_status") == 0) {
                status = 5;
            } else if (obj.getInt("order_status") == 3 &&
                    obj.getInt("shipping_status") == 0 &&
                    obj.getInt("pay_status") == 0) {
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
            ((TextView) convertView.findViewById(R.id.status)).setText(statusSt[status]);
            Button button = (Button) convertView.findViewById(R.id.button);
            if (status == 0) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String payType = "alipay_app";
                        try {
                            if (obj.getInt("pay_id") != 4) {
                                payType = "weixin_app";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        OrderManager manager = OrderManager.getInstance();
                        SharedPreferences sp = context.getSharedPreferences("user", Activity.MODE_PRIVATE);

                        manager.orderPay(sp.getString("token", ""), orderId, payType, handler);
                    }

                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 0) {
                                try {
                                    if (obj.getInt("pay_id") != 4) {
                                        JSONObject data = ((JSONObject) msg.obj).getJSONObject("data");
                                        Log.e("WXPay", data.toString());
                                        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
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
                                        ZFBManager.pay(params, (Activity) context, ZFBPayHandler);
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

            } else if (status < 5) {
                button.setText("物流追踪");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, WuLiuActivity.class);
                        intent.putExtra("orderId", orderId);
                        context.startActivity(intent);
                    }
                });
            } else {
                button.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
