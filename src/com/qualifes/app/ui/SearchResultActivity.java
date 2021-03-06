package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ConnectionURL;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifes.app.R;
import com.qualifes.app.ui.adapter.SearchAdapter;
import com.qualifes.app.util.DisplayParams;
import com.qualifes.app.util.DisplayUtil;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResultActivity extends Activity implements OnClickListener, OnItemClickListener {

    ImageButton home_search_result_back;
    EditText home_search_result_input;
    TextView home_search_result_search;

    TextView search_result_order_default;
    TextView search_result_order_price;
    TextView search_result_order_sales;
    TextView search_result_order_popu;

    ListView home_search_result_items;
    SearchAdapter adapter;

    AsyncHttpClient client;
    boolean flag = true;
    List<HashMap<String, Object>> data;
    boolean isPrice = false;

    String ids;

    String[] from = {"goods_thumbBitMap", "goods_name", "shop_price", "market_price", "discount", "origin"};
    int[] to = {R.id.search_info_image, R.id.search_info_goods_name, R.id.search_info_shop_price,
            R.id.search_info_market_price, R.id.search_info_discount, R.id.search_info_origin};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_result_activity);
        init();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (!bundle.containsKey("flag")) {
                String searchKeyWord = bundle.getString("searchKeyWord");
                if (searchKeyWord != null) {
                    home_search_result_input.setText(searchKeyWord);
                }
            } else {
                flag = false;
                ids = bundle.getString("searchKeyWord");
//                ids = "49";
            }
        }

        search_result_order_default.callOnClick();
    }

    private void init() {
        client = new AsyncHttpClient();
        data = new ArrayList<HashMap<String, Object>>();

        home_search_result_back = (ImageButton) findViewById(R.id.home_search_result_back);
        home_search_result_back.setOnClickListener(this);




        home_search_result_search = (TextView) findViewById(R.id.home_search_result_search);
        home_search_result_search.setOnClickListener(this);

        home_search_result_input = (EditText) findViewById(R.id.home_search_result_input);
        home_search_result_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    home_search_result_search.callOnClick();
                    return true;
                }
                return false;
            }
        });

        search_result_order_default = (TextView) findViewById(R.id.search_result_order_default);
        search_result_order_default.setOnClickListener(this);

        search_result_order_price = (TextView) findViewById(R.id.search_result_order_price);
        search_result_order_price.setOnClickListener(this);

        search_result_order_sales = (TextView) findViewById(R.id.search_result_order_sales);
        search_result_order_sales.setOnClickListener(this);

        search_result_order_popu = (TextView) findViewById(R.id.search_result_order_popu);
        search_result_order_popu.setOnClickListener(this);

        home_search_result_items = (ListView) findViewById(R.id.home_search_result_items);
        home_search_result_items.setDividerHeight(0);
        home_search_result_items.setOnItemClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search_result_back:
                this.finish();
                break;
            case R.id.home_search_result_search:
            case R.id.search_result_order_default:
                search_result_order_default.setSelected(true);
                search_result_order_price.setSelected(false);
                findViewById(R.id.tri).setVisibility(View.INVISIBLE);
                search_result_order_sales.setSelected(false);
                search_result_order_popu.setSelected(false);
                isPrice = false;
                if (home_search_result_input.getText().toString().isEmpty() && flag) {
                    Toast.makeText(getApplicationContext(), "请输入搜索的商品信息", Toast.LENGTH_SHORT).show();
                } else {
                    String input = home_search_result_input.getText().toString();
                    data = new ArrayList<HashMap<String, Object>>();

                    RequestParams params = new RequestParams();
                    if (flag) {
                        params.put("data[keywords]", input);
                    } else {
                        params.put("data[cat_id]", ids);
                    }
                    params.put("data[order_sort]", "desc");
                    params.put("data[limit_m]", 0);
                    params.put("data[limit_n]", 100);
                    client.get(ConnectionURL.getSearchURL(), params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              JSONObject response) {
                            handlerData(response);
                        }
                    });

                }

                break;
            case R.id.search_result_order_price:
                search_result_order_default.setSelected(false);
                search_result_order_price.setSelected(true);
                findViewById(R.id.tri).setVisibility(View.VISIBLE);
                search_result_order_sales.setSelected(false);
                search_result_order_popu.setSelected(false);
                if (home_search_result_input.getText().toString().isEmpty() && flag) {
                    Toast.makeText(getApplicationContext(), "请输入搜索的商品信息", Toast.LENGTH_SHORT).show();
                } else {
                    String input = home_search_result_input.getText().toString();
                    data = new ArrayList<HashMap<String, Object>>();

                    RequestParams params = new RequestParams();
                    if (flag) {
                        params.put("data[keywords]", input);
                    } else {
                        params.put("data[cat_id]", ids);
                    }
                    params.put("data[order_field]", "shop_price");
                    if (!isPrice) {
                        isPrice = true;
                        params.put("data[order_sort]", "asc");
                        ((ImageView) findViewById(R.id.tri)).setImageDrawable(getResources().getDrawable(R.drawable.triangle));
                    } else {
                        isPrice = false;
                        params.put("data[order_sort]", "desc");
                        ((ImageView) findViewById(R.id.tri)).setImageDrawable(getResources().getDrawable(R.drawable.triangle_turn));
                    }
                    params.put("data[limit_m]", 0);
                    params.put("data[limit_n]", 100);
                    client.get(ConnectionURL.getSearchURL(), params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              JSONObject response) {
                            handlerData(response);
                        }
                    });

                }

                break;
            case R.id.search_result_order_sales:
                search_result_order_default.setSelected(false);
                search_result_order_price.setSelected(false);
                findViewById(R.id.tri).setVisibility(View.INVISIBLE);
                search_result_order_sales.setSelected(true);
                search_result_order_popu.setSelected(false);
                isPrice = false;
                if (home_search_result_input.getText().toString().isEmpty() && flag) {
                    Toast.makeText(getApplicationContext(), "请输入搜索的商品信息", Toast.LENGTH_SHORT).show();
                } else {
                    String input = home_search_result_input.getText().toString();
                    data = new ArrayList<HashMap<String, Object>>();

                    RequestParams params = new RequestParams();
                    if (flag) {
                        params.put("data[keywords]", input);
                    } else {
                        params.put("data[cat_id]", ids);
                    }
                    params.put("data[order_field]", "is_hot");
                    params.put("data[order_sort]", "desc");
                    params.put("data[limit_m]", 0);
                    params.put("data[limit_n]", 100);
                    client.get(ConnectionURL.getSearchURL(), params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              JSONObject response) {
                            handlerData(response);
                        }
                    });

                }

                break;
            case R.id.search_result_order_popu:
                search_result_order_default.setSelected(false);
                search_result_order_price.setSelected(false);
                search_result_order_sales.setSelected(false);
                findViewById(R.id.tri).setVisibility(View.INVISIBLE);
                isPrice = false;
                search_result_order_popu.setSelected(true);

                if (home_search_result_input.getText().toString().isEmpty() && flag) {
                    Toast.makeText(getApplicationContext(), "请输入搜索的商品信息", Toast.LENGTH_SHORT).show();
                } else {
                    String input = home_search_result_input.getText().toString();
                    data = new ArrayList<HashMap<String, Object>>();

                    RequestParams params = new RequestParams();
                    if (flag) {
                        params.put("data[keywords]", input);
                    } else {
                        params.put("data[cat_id]", ids);
                    }
                    params.put("data[order_field]", "click_count");
                    params.put("data[order_sort]", "desc");
                    params.put("data[limit_m]", 0);
                    params.put("data[limit_n]", 100);
                    client.get(ConnectionURL.getSearchURL(), params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              JSONObject response) {
                            handlerData(response);
                        }
                    });

                }

                break;
            default:
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            adapter = new SearchAdapter(getApplicationContext(), data, R.layout.search_result_item, from, to);
            ViewGroup.LayoutParams params =home_search_result_items.getLayoutParams();
            DisplayParams params1 = DisplayParams.getInstance(getApplicationContext());
            params.height = data.size() * DisplayUtil.dip2px(100, params1.scale);
            home_search_result_items.setLayoutParams(params);
            home_search_result_items.setAdapter(adapter);
        }

        ;
    };

    private class MyThread extends Thread {
        public void run() {
            for (int i = 0; i < data.size(); i++) {
                HashMap<String, Object> item = (HashMap<String, Object>) data.get(i);
                if (item.containsKey("goods_thumb")) {
                    item.put("goods_thumbBitMap", ConnectionURL.getHttpBitmap(item.get("goods_thumb").toString()));
                }
            }
            handler.sendEmptyMessage(200);
        }

        ;
    }

    ;

    private void handlerData(JSONObject response) {
        data.clear();
        try {
            JSONObject dataObject = response.getJSONObject("data");
            JSONArray dataArray = dataObject.getJSONArray("data");
            findViewById(R.id.noresult).setVisibility(View.GONE);
            findViewById(R.id.haveresult).setVisibility(View.VISIBLE);
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject itemObject = dataArray.getJSONObject(i);
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("goods_id", itemObject.getInt("goods_id"));
                item.put("goods_thumb", itemObject.getString("goods_thumb"));
                item.put("goods_name", itemObject.getString("goods_name"));

                BigDecimal shopPrice = new BigDecimal(itemObject.getString("shop_price"));
                BigDecimal marketPrice = new BigDecimal(itemObject.getString("market_price"));

                BigDecimal result = shopPrice.divide(shopPrice, new MathContext(2)).multiply(new BigDecimal(10));

                item.put("shop_price", shopPrice);
                item.put("market_price", marketPrice);
                item.put("discount", result + "折");
                item.put("origin", itemObject.getString("origin"));
                data.add(item);
            }
            MyThread thread = new MyThread();
            thread.start();
        } catch (JSONException e) {
            findViewById(R.id.noresult).setVisibility(View.VISIBLE);
            findViewById(R.id.haveresult).setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(position);
        int goods_id = Integer.valueOf(data.get(position).get("goods_id").toString());

        Intent intent = new Intent(SearchResultActivity.this, GoodDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("goods_id", goods_id);
        intent.putExtra("goods_id", bundle);
        startActivity(intent);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

