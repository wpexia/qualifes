package com.qualifes.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifes.app.R;
import com.qualifes.app.config.Api;
import com.qualifes.app.ui.LoginActivity;
import com.qualifes.app.util.AsyncImageLoader;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.MathContext;

public class HomeBottomAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private JSONArray mData;
    private AsyncImageLoader imageLoader = new AsyncImageLoader();
    private Context context;


    public HomeBottomAdapter(Context context, JSONArray data) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mData = data;
        //Log.e("number", String.valueOf(data.length()));
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
//        Log.e("Bottom", String.valueOf(position));
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_page_list_item, null);
        }
        try {
            final JSONObject good = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.info_goods_name)).setText(good.getString("goods_name"));
            BigDecimal shopPrice = new BigDecimal(good.getString("shop_price"));
            BigDecimal marketPrice = new BigDecimal(good.getString("market_price"));
            BigDecimal result = shopPrice.divide(marketPrice, new MathContext(2)).multiply(new BigDecimal(10));
            ((TextView) convertView.findViewById(R.id.info_shop_price)).setText("￥" + shopPrice);
            ((TextView) convertView.findViewById(R.id.info_market_price)).setText("￥" + marketPrice);
            ((TextView) convertView.findViewById(R.id.info_discount)).setText(result + "折");
            ((TextView) convertView.findViewById(R.id.info_origin)).setText("产地 " + good.getString("origin"));
            ImageView image = (ImageView) convertView.findViewById(R.id.info_image);
            Bitmap cachedImage = imageLoader.loadDrawable(good.get("goods_img").toString(), image,
                    new AsyncImageLoader.ImageCallback() {
                        public void imageLoaded(Bitmap imageDrawable,
                                                ImageView imageView, String imageUrl) {
                            imageView.setImageBitmap(imageDrawable);
                        }
                    }, 5);
            if (cachedImage != null) {
                image.setImageBitmap(cachedImage);
            }
            //ImageCacheHelper.getImageCache().get(good.get("goods_img").toString(), image);
            final ImageButton star = (ImageButton) convertView.findViewById(R.id.star);
            if (good.getInt("is_coll") != 0) {
                star.setImageDrawable(context.getResources().getDrawable(R.drawable.star_red));
            } else {
                star.setOnClickListener(new View.OnClickListener() {
                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                            star.setImageDrawable(context.getResources().getDrawable(R.drawable.star_red));
                            star.setOnClickListener(null);
                        }
                    };

                    @Override
                    public void onClick(View v) {
                        SharedPreferences sp = context.getSharedPreferences("user", Activity.MODE_PRIVATE);
                        if (sp.contains("token")) {
                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params1 = new RequestParams();
                            final Message msg = handler.obtainMessage();
                            params1.put("token", sp.getString("token", ""));
                            try {
                                params1.put("data[goods_id]", good.getString("goods_id"));
                            } catch (JSONException e) {
                                e.fillInStackTrace();
                            }
                            client.post(Api.url("add_collect"), params1, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Api.dealSuccessRes(response, msg);
                                    handler.sendMessage(msg);
                                }
                            });
                        } else {
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
