package com.qualifies.app.ui;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import android.graphics.Bitmap;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.qualifies.app.R;
import com.qualifies.app.util.AsyncImageLoader;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ConnectionURL;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class GoodDetailActivity extends Activity implements OnClickListener {

    private AsyncHttpClient client;
    private ImageButton detail_back;
    private ImageButton detail_back_home;
    private ViewFlipper detail_imgs;
    private ImageButton detail_star;
    private ImageButton detail_share;
    private TextView detail_good_name;
    private TextView detail_goods_name;
    private TextView detail_shop_price;
    private TextView detail_market_price;
    private TextView detail_discount;
    private TextView detail_origin;
    private ImageButton detail_good_info_press;
    private ImageButton detail_spec_press;
    private ImageButton detail_gooddata_press;

    private ImageView detail_icon_shoppingcart;
    private Button detail_addto_shoppingcart;
    private Button detail_closing_cost;
    private int goods_id;
    HashSet<Bitmap> bitmapCache = new HashSet<Bitmap>();


    private AsyncImageLoader imageLoader = new AsyncImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.good_details_activity);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra("goods_id");
        goods_id = bundle.getInt("goods_id");
        init();
    }

    private void init() {
        client = new AsyncHttpClient();
        detail_back = (ImageButton) findViewById(R.id.detail_back);
        detail_back.setOnClickListener(this);

        detail_back_home = (ImageButton) findViewById(R.id.detail_back_home);
        detail_back_home.setOnClickListener(this);

        detail_imgs = (ViewFlipper) findViewById(R.id.detail_imgs);

        detail_star = (ImageButton) findViewById(R.id.detail_star);
        detail_star.setOnClickListener(this);

        detail_share = (ImageButton) findViewById(R.id.detail_share);
        detail_share.setOnClickListener(this);

        detail_good_name = (TextView) findViewById(R.id.detail_good_name);
        detail_goods_name = (TextView) findViewById(R.id.detail_goods_name);

        detail_shop_price = (TextView) findViewById(R.id.detail_shop_price);
        detail_market_price = (TextView) findViewById(R.id.detail_market_price);
        detail_discount = (TextView) findViewById(R.id.detail_discount);
        detail_origin = (TextView) findViewById(R.id.detail_origin);

        detail_good_info_press = (ImageButton) findViewById(R.id.detail_good_info_press);
        detail_good_info_press.setOnClickListener(this);

        detail_spec_press = (ImageButton) findViewById(R.id.detail_spec_press);
        detail_spec_press.setOnClickListener(this);

        detail_gooddata_press = (ImageButton) findViewById(R.id.detail_gooddata_press);
        detail_gooddata_press.setOnClickListener(this);

        detail_icon_shoppingcart = (ImageView) findViewById(R.id.detail_icon_shoppingcart);
        detail_closing_cost = (Button) findViewById(R.id.detail_closing_cost);
        detail_closing_cost.setOnClickListener(this);

        detail_addto_shoppingcart = (Button) findViewById(R.id.detail_addto_shoppingcart);
        detail_addto_shoppingcart.setOnClickListener(this);
        accessServer();
    }

    private void accessServer() {
        RequestParams params = new RequestParams();
        params.put("data[goods_id]", goods_id);
        params.put("data[type]", "j");
        client.get(ConnectionURL.getGoodsInfoURL(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<String> imgStrs = new ArrayList<String>();
                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    detail_good_name.setText(dataObject.getString("goods_name"));
                    JSONArray imgsArray = dataObject.getJSONArray("goods_img");


                    if (dataObject.getInt("is_coll") != 0) {
                        detail_star.setBackgroundResource(R.drawable.star_red);
                    }
                    detail_goods_name.setText(dataObject.getString("goods_name"));
                    BigDecimal shopPrice = new BigDecimal(dataObject.getString("shop_price"));
                    BigDecimal marketPrice = new BigDecimal(dataObject.getString("market_price"));
                    detail_shop_price.setText("￥" + shopPrice);
                    detail_market_price.setText("￥" + marketPrice);
                    detail_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    BigDecimal result = shopPrice.divide(marketPrice, new MathContext(2)).multiply(new BigDecimal(10));
                    detail_discount.setText(result + "折");

                    detail_origin.setText("产地  " + dataObject.getString("origin"));

                    for (int i = 0; i < imgsArray.length(); i++) {
                        ImageView iv = new ImageView(getApplicationContext());
                        Bitmap cachedImage = imageLoader.loadDrawable(imgsArray.getString(i), iv,
                                new AsyncImageLoader.ImageCallback() {
                                    public void imageLoaded(Bitmap imageDrawable,
                                                            ImageView imageView, String imageUrl) {
                                        imageView.setImageBitmap(imageDrawable);
                                        bitmapCache.add(imageDrawable);
                                    }
                                }, 3);
                        if (cachedImage != null) {
                            iv.setImageBitmap(cachedImage);
                            bitmapCache.add(cachedImage);
                        }

                        detail_imgs.addView(iv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

                        detail_imgs.setAutoStart(true);
                        detail_imgs.setFlipInterval(1500);
                        if (detail_imgs.isAutoStart() && imgsArray.length() > 1 && !detail_imgs.isFlipping()) {
                            Animation lInAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_in);        // 向左滑动左侧进入的渐变效果（alpha 0.1  -> 1.0）
                            Animation lOutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_out);    // 向左滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

                            detail_imgs.setInAnimation(lInAnim);
                            detail_imgs.setOutAnimation(lOutAnim);
                            detail_imgs.startFlipping();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Iterator<Bitmap> items = bitmapCache.iterator(); items.hasNext(); ) {
            Bitmap item = items.next();
            if (item != null)
                item.recycle();
        }
        bitmapCache.clear();
        System.gc();
    }

    @Override
    public void onClick(View v) {

        Intent intent2 = new Intent(GoodDetailActivity.this, GoodSpecActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("goods_id", goods_id);
        intent2.putExtras(bundle);

        switch (v.getId()) {
            case R.id.detail_back:
                GoodDetailActivity.this.finish();
                break;
            case R.id.detail_back_home:
                Intent intent = new Intent(GoodDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.detail_star:
                break;
            case R.id.detail_share:
                break;
            case R.id.detail_good_info_press:
                break;
            case R.id.detail_spec_press:
                startActivity(intent2);
                break;
            case R.id.detail_gooddata_press:
                break;
            case R.id.detail_closing_cost:
                break;
            case R.id.detail_addto_shoppingcart:
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
