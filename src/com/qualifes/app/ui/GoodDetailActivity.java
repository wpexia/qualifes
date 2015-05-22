package com.qualifes.app.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.*;
import com.qualifes.app.R;
import com.qualifes.app.config.Api;
import com.qualifes.app.ui.view.GoodCanshuActivity;
import com.qualifes.app.util.AsyncImageLoader;
import com.qualifes.app.util.DisplayParams;
import com.qualifes.app.util.DisplayUtil;
import com.qualifes.app.util.WXApi;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWebPage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class GoodDetailActivity extends Activity implements OnClickListener, GestureDetector.OnGestureListener {

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

    private GestureDetector gestureDetector = null;

    private ImageView detail_icon_shoppingcart;
    private Button detail_addto_shoppingcart;
    private Button detail_closing_cost;
    private int goods_id;
    HashSet<Bitmap> bitmapCache = new HashSet<Bitmap>();

    UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");


    private AsyncImageLoader imageLoader = new AsyncImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.good_details_activity);

        Intent intent = this.getIntent();
        if (intent.hasExtra("goods_id")) {
            Bundle bundle = intent.getBundleExtra("goods_id");
            goods_id = bundle.getInt("goods_id");
        } else {
            String url = intent.getDataString();
            url = url.substring(20, url.length() - 2);
            goods_id = Integer.parseInt(url);
        }
        init();
    }

    private void init() {
        client = new AsyncHttpClient();


        findViewById(R.id.detail_spec).setOnClickListener(this);
        findViewById(R.id.detail_gooddata).setOnClickListener(this);
        findViewById(R.id.detail_good_info).setOnClickListener(this);
        findViewById(R.id.detail_closing_cost).setOnClickListener(this);
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

        detail_closing_cost = (Button) findViewById(R.id.detail_closing_cost);
        detail_closing_cost.setOnClickListener(this);

        detail_addto_shoppingcart = (Button) findViewById(R.id.detail_addto_shoppingcart);
        detail_addto_shoppingcart.setOnClickListener(this);

        gestureDetector = new GestureDetector(getApplicationContext(), this);
        accessServer();


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        if (sp.contains("token")) {
            AsyncHttpClient client = new AsyncHttpClient();
            final Message msg = new Message();
            RequestParams params = new RequestParams();
            params.put("token", sp.getString("token", ""));
            params.put("data[limit][m]", "0");
            params.put("data[limit][n]", "100");
            client.get(Api.url("get_cart"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
//                    Log.e("get_cart", response.toString());
                    try {
                        msg.obj = response.getJSONObject("data").getJSONArray("data");
                        int number = response.getJSONObject("data").getJSONArray("data").length();
                        if (number > 0) {
                            ((TextView) findViewById(R.id.badge)).setText(String.valueOf(number));
                            findViewById(R.id.badge).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.badge).setVisibility(View.INVISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            findViewById(R.id.badge).setVisibility(View.INVISIBLE);
        }
    }

    private void accessServer() {
        final RequestParams params = new RequestParams();
        params.put("data[goods_id]", goods_id);
        params.put("data[type]", "j");
        final SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        if (sp.contains("token")) {
            params.put("token", sp.getString("token", ""));
        }
        client.get(ConnectionURL.getGoodsInfoURL(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<String> imgStrs = new ArrayList<String>();
                try {
                    final JSONObject dataObject = response.getJSONObject("data");
                    detail_good_name.setText(dataObject.getString("goods_name"));
                    final JSONArray imgsArray = dataObject.getJSONArray("goods_img");


                    if (dataObject.getInt("is_coll") != 0) {
                        detail_star.setImageDrawable(getResources().getDrawable(R.drawable.stat_red_circle));
                    } else {
                        if (sp.contains("token")) {
                            detail_star.setOnClickListener(new OnClickListener() {
                                Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        Toast.makeText(GoodDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                                        detail_star.setImageDrawable(getResources().getDrawable(R.drawable.stat_red_circle));
                                        detail_star.setOnClickListener(null);
                                    }
                                };

                                @Override
                                public void onClick(View v) {
                                    AsyncHttpClient client = new AsyncHttpClient();
                                    RequestParams params1 = new RequestParams();
                                    final Message msg = handler.obtainMessage();
                                    params1.put("token", sp.getString("token", ""));
                                    params1.put("data[goods_id]", goods_id);
                                    client.post(Api.url("add_collect"), params1, new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            Api.dealSuccessRes(response, msg);
                                            handler.sendMessage(msg);
                                        }
                                    });
                                }
                            });
                        } else {
                            detail_star.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(GoodDetailActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
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
                    findViewById(R.id.detail_share).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popup = new PopupMenu(GoodDetailActivity.this, v);
                            MenuInflater inflater = popup.getMenuInflater();
                            inflater.inflate(R.menu.main, popup.getMenu());
                            try {
                                Field[] fields = popup.getClass().getDeclaredFields();
                                for (Field field : fields) {
                                    if ("mPopup".equals(field.getName())) {
                                        field.setAccessible(true);
                                        Object menuPopupHelper = field.get(popup);
                                        Class<?> classPopupHelper = Class.forName(menuPopupHelper
                                                .getClass().getName());
                                        Method setForceIcons = classPopupHelper.getMethod(
                                                "setForceShowIcon", boolean.class);
                                        setForceIcons.invoke(menuPopupHelper, true);
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    int id = item.getItemId();
                                    try {
                                        if (id == R.id.friend) {
                                            UMWXHandler wxHandler = new UMWXHandler(GoodDetailActivity.this, WXApi.APP_ID, WXApi.APP_KEY);
                                            wxHandler.addToSocialSDK();
                                            UMImage shareImage = new UMImage(GoodDetailActivity.this, imgsArray.getString(0));
                                            WeiXinShareContent content = new WeiXinShareContent();
                                            content.setTargetUrl("http://www.qualifes.com/webview/release/goods_info_android/index.html?id=" + goods_id);
                                            content.setShareMedia(shareImage);
                                            content.setShareContent(dataObject.getString("goods_name"));
                                            content.setTitle(dataObject.getString("goods_name"));
                                            mController.setShareMedia(content);
                                            mController.directShare(GoodDetailActivity.this, SHARE_MEDIA.WEIXIN, new SocializeListeners.SnsPostListener() {
                                                @Override
                                                public void onComplete(SHARE_MEDIA arg0, int arg1,
                                                                       SocializeEntity arg2) {
//                                                    Toast.makeText(GoodDetailActivity.this, "分享完成", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onStart() {
//                                                    Toast.makeText(GoodDetailActivity.this, "开始分享", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            UMImage shareImage = new UMImage(GoodDetailActivity.this, imgsArray.getString(0));
                                            UMWXHandler wxCircleHandler = new UMWXHandler(GoodDetailActivity.this, WXApi.APP_ID, WXApi.APP_KEY);
                                            wxCircleHandler.setToCircle(true);
                                            wxCircleHandler.addToSocialSDK();
                                            CircleShareContent content = new CircleShareContent();
                                            content.setTargetUrl("http://www.qualifes.com/webview/release/goods_info_android/index.html?id=" + goods_id);
                                            content.setShareMedia(shareImage);
                                            content.setShareContent(dataObject.getString("goods_name"));
                                            content.setTitle(dataObject.getString("goods_name"));
                                            mController.setShareMedia(content);
                                            mController.directShare(GoodDetailActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE, new SocializeListeners.SnsPostListener() {
                                                @Override
                                                public void onComplete(SHARE_MEDIA arg0, int arg1,
                                                                       SocializeEntity arg2) {
//                                                    Toast.makeText(GoodDetailActivity.this, "分享完成", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onStart() {
//                                                    Toast.makeText(GoodDetailActivity.this, "开始分享", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    return true;
                                }
                            });
                            popup.show();
                        }
                    });

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
            case R.id.detail_good_info:
                Intent intent1 = new Intent(this, GoodInfoActivity.class);
                intent1.putExtra("url", "http://www.qualifes.com/webview/release/goods_info_ios/miaoshu.html?id=" + goods_id);
                startActivity(intent1);
                break;
            case R.id.detail_spec:
                startActivity(intent2);
                break;
            case R.id.detail_gooddata:
                Intent intent3 = new Intent(this, GoodCanshuActivity.class);
                intent3.putExtra("url", "http://www.qualifes.com/webview/release/goods_info_ios/canshu.html?id=" + goods_id);
                startActivity(intent3);
                break;
            case R.id.detail_closing_cost:
                Intent intent4 = new Intent(this, ShoppingCartActivity.class);
                startActivity(intent4);
                break;
            case R.id.detail_addto_shoppingcart:
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (gestureDetector.onTouchEvent(ev)) return true;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        DisplayParams params = DisplayParams.getInstance(getApplicationContext());
        if (e1.getY() > DisplayUtil.dip2px(320, params.scale)) {
//            Log.e("y", String.valueOf(e1.getY()));
//            Log.e("px", String.valueOf(DisplayUtil.px2dip(210, params.scale)));
            return false;
        }
        if (e2.getX() - e1.getX() > DisplayUtil.dip2px(100, params.scale)) {             // 从左向右滑动（左进右出）
            detail_imgs.stopFlipping();                // 点击事件后，停止自动播放
            detail_imgs.setAutoStart(false);
            Animation rInAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_right_in);    // 向右滑动左侧进入的渐变效果（alpha  0.1 -> 1.0）
            Animation rOutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            detail_imgs.setInAnimation(rInAnim);
            detail_imgs.setOutAnimation(rOutAnim);
            detail_imgs.showPrevious();
            return true;
        } else if (e2.getX() - e1.getX() < -DisplayUtil.dip2px(100, params.scale)) {         // 从右向左滑动（右进左出）
            detail_imgs.stopFlipping();                // 点击事件后，停止自动播放
            detail_imgs.setAutoStart(false);
            Animation lInAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_in);        // 向左滑动左侧进入的渐变效果（alpha 0.1  -> 1.0）
            Animation lOutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_out);    // 向左滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            detail_imgs.setInAnimation(lInAnim);
            detail_imgs.setOutAnimation(lOutAnim);
            detail_imgs.showNext();
            return true;
        }
        return false;
    }
}
