package com.qualifies.app.ui;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import com.qualifies.app.R;
import com.qualifies.app.manager.ShoppingCartManager;
import com.qualifies.app.ui.adapter.SpecListViewAdapter;
import com.qualifies.app.util.AsyncImageLoader;
import com.qualifies.app.util.OfflineCartDbHelper;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ConnectionURL;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GoodSpecActivity extends Activity implements OnClickListener {


    private AsyncImageLoader imageLoader = new AsyncImageLoader();
    private AsyncHttpClient client;
    private SharedPreferences sp;
    private ImageButton spec_back;
    private ImageView spec_img;
    private TextView spec_goods_name;
    private TextView spec_shop_price;
    private TextView spec_origin;
    private ListView spec_listView;
    private Button spec_count_sub;
    private TextView spec_count_num;
    private Button spec_count_add;
    private Button spec_addto_shoppingcart;
    private int goods_id;
    private int max_sale_number;
    private int goods_number;
    private JSONArray specArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        goods_id = bundle.getInt("goods_id");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.good_spec_activity);
        init();
    }

    private void init() {
        client = new AsyncHttpClient();
        sp = getSharedPreferences("user", MODE_PRIVATE);

        spec_img = (ImageView) findViewById(R.id.spec_image);
        spec_goods_name = (TextView) findViewById(R.id.spec_goods_name);
        spec_shop_price = (TextView) findViewById(R.id.spec_shop_price);
        spec_origin = (TextView) findViewById(R.id.spec_origin);

        spec_back = (ImageButton) findViewById(R.id.spec_back);
        spec_back.setOnClickListener(this);


        spec_count_sub = (Button) findViewById(R.id.spec_count_sub);
        spec_count_sub.setOnClickListener(this);

        spec_count_add = (Button) findViewById(R.id.spec_count_add);
        spec_count_add.setOnClickListener(this);

        spec_count_num = (TextView) findViewById(R.id.spec_count_num);

        spec_listView = (ListView) findViewById(R.id.spec_listView);

        spec_addto_shoppingcart = (Button) findViewById(R.id.spec_addto_shoppingcart);
        spec_addto_shoppingcart.setOnClickListener(this);
        accessServer();
    }

    private void accessServer() {
        RequestParams params = new RequestParams();
        params.put("data[goods_id]", goods_id);
        params.put("data[type]", "g");

        client.get(ConnectionURL.getGoodsInfoURL(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    spec_goods_name.setText(dataObject.getString("goods_name"));
                    spec_shop_price.setText("￥" + dataObject.getString("shop_price"));
                    spec_origin.setText("产地 " + dataObject.getString("origin"));
                    goods_number = Integer.parseInt(dataObject.getString("goods_number"));
                    max_sale_number = Integer.parseInt(dataObject.getString("max_sale_number"));
                    if (max_sale_number > 0) {
                        spec_count_num.setText(String.valueOf(max_sale_number));
                    }

//                    ImageCacheHelper.getImageCache().get(dataObject.getString("goods_img"), spec_img);
                    Bitmap chche = imageLoader.loadDrawable(dataObject.getString("goods_img"), spec_img,
                            new AsyncImageLoader.ImageCallback() {
                                @Override
                                public void imageLoaded(Bitmap imageDrawable, ImageView imageView, String imageUrl) {
                                    imageView.setImageBitmap(imageDrawable);
                                }
                            }
                    );
                    if (chche != null) {
                        spec_img.setImageBitmap(chche);
                    }


                    JSONObject attribute = dataObject.getJSONObject("attribute");
                    specArray = attribute.getJSONArray("spe");

                    spec_listView.setAdapter(new SpecListViewAdapter(getApplicationContext(), specArray));

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spec_back:
                this.finish();
                break;
            case R.id.spec_count_sub:
                int numSub = Integer.parseInt(spec_count_num.getText().toString());
                if (numSub == 1 || numSub == max_sale_number) {
                    Toast.makeText(getApplicationContext(), "亲，不能再少了哦", Toast.LENGTH_SHORT).show();
                } else {
                    spec_count_num.setText(String.valueOf(--numSub));
                }
                break;
            case R.id.spec_count_add:
                int numAdd = Integer.parseInt(spec_count_num.getText().toString());
                if (numAdd < goods_number && numAdd < 100)
                    numAdd++;
                spec_count_num.setText(String.valueOf(numAdd));
                break;
            case R.id.spec_addto_shoppingcart:
                if (goodsAttrIDs() != null) {
                    if (sp.contains("token")) {
                        String[] goodsNum = {spec_count_num.getText().toString()};
                        String[] goodsId = {String.valueOf(goods_id)};
                        String[] goodsAttr = {goodsAttrIDs()};
                        ShoppingCartManager manager = ShoppingCartManager.getInstance();
                        manager.addShoppingCart(sp.getString("token", ""), goodsId, goodsAttr, goodsNum, addGoodsHandler, getApplicationContext());
                    } else {
                        OfflineCartDbHelper dbHelper = new OfflineCartDbHelper(getApplicationContext());
                        dbHelper.insert(String.valueOf(goods_id), goodsAttrIDs(), spec_count_num.getText().toString(), goodsAttrNames());
                        Toast.makeText(GoodSpecActivity.this, "添加离线购物车成功", Toast.LENGTH_SHORT).show();
                        GoodSpecActivity.this.finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "亲，请选择商品属性哦", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    Handler addGoodsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(GoodSpecActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
            if (msg.what == 0) {
                GoodSpecActivity.this.finish();
            }
        }
    };


    private String goodsAttrNames() {
        String result = "";
        if (specArray == null) {
            return "null";
        }
        for (int i = 0; i < specArray.length(); i++) {
            RelativeLayout layout = (RelativeLayout) spec_listView.getChildAt(i);
            GridView gridView = (GridView) layout.getChildAt(1);
            int select = -1;
            for (int j = 0; j < gridView.getChildCount(); j++) {
                if (((RelativeLayout) gridView.getChildAt(j)).getChildAt(0).isSelected())
                    select = j;
            }
            if (select == -1)
                return null;
            else {
                String id = null;
                try {
                    id = specArray.getJSONObject(i).getString("name") + specArray.getJSONObject(i).getJSONArray("values").getJSONObject(select).getString("label");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result += id;
            }
        }
//        Log.e("goodsAttr", result);
        return result;
    }

    private String goodsAttrIDs() {
        String result = "";
        if (specArray == null) {
            return "null";
        }
        for (int i = 0; i < specArray.length(); i++) {
            RelativeLayout layout = (RelativeLayout) spec_listView.getChildAt(i);
            GridView gridView = (GridView) layout.getChildAt(1);
            int select = -1;
            for (int j = 0; j < gridView.getChildCount(); j++) {
                if (((RelativeLayout) gridView.getChildAt(j)).getChildAt(0).isSelected())
                    select = j;
            }
            if (select == -1)
                return null;
            else {
                String id = null;
                try {
                    id = specArray.getJSONObject(i).getJSONArray("values").getJSONObject(select).getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("")) {
                    result = id;
                } else {
                    result += "," + id;
                }
            }
        }
//        Log.e("goodsAttr", result);
        return result;
    }
}