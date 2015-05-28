package com.qualifes.app.ui;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import com.qualifes.app.R;
import com.qualifes.app.manager.FollowManager;
import com.qualifes.app.ui.fragment.ProductListNotNullFragment;
import com.qualifes.app.ui.fragment.ProductListNullFragment;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class FollowActivity extends Activity implements View.OnClickListener {

    private SharedPreferences sp;

    private FragmentManager fragmentManager;
    private ProductListNotNullFragment productListNotNullFragment;
    private ProductListNullFragment productListNullFragment;
    private LinkedList<HashMap<String, Object>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences("user", MODE_PRIVATE);
        setContentView(R.layout.history);
        fragmentManager = getFragmentManager();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(FollowActivity.this);
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("我的收藏");
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);
        changeFragment(false);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.clear: {
                if (!productListNotNullFragment.equals(null)) {
                    productListNotNullFragment.clear();
                }
                changeFragment(true);
            }
            break;
            case R.id.back_button: {
                finish();
            }
            break;
        }
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void changeFragment(boolean isNull) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        if (!isNull) {
            if (productListNotNullFragment == null) {
                productListNotNullFragment = new ProductListNotNullFragment();
                productListNotNullFragment.setStar(true);
                productListNotNullFragment.setData(1);
                transaction.add(R.id.fragment, productListNotNullFragment);
            } else {
                transaction.show(productListNullFragment);
            }
        } else {
            if (productListNullFragment == null) {
                productListNullFragment = new ProductListNullFragment();
                productListNullFragment.setContent("暂无收藏");
                transaction.add(R.id.fragment, productListNullFragment);
            } else {
                transaction.show(productListNullFragment);
            }
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (productListNotNullFragment != null) {
            transaction.hide(productListNotNullFragment);
        }
        if (productListNullFragment != null) {
            transaction.hide(productListNullFragment);
        }
    }


    public LinkedList<HashMap<String, Object>> getData() {
        list = new LinkedList<HashMap<String, Object>>();
        String token = sp.getString("token", "");
        if (!token.equals("")) {
            FollowManager followManager = FollowManager.getInstance();
            try {
                followManager.getFollow(token, handler, this, 0);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                JSONObject resp = (JSONObject) msg.obj;
                try {
                    JSONObject data = resp.getJSONObject("data");
                    JSONArray goods = data.getJSONArray("data");
//                    Log.e("goods", goods.toString());
                    for (int i = 0; i < goods.length(); i++) {
                        JSONObject good = goods.getJSONObject(i);
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("title", good.getString("goods_name"));
                        map.put("image", good.getString("goods_thumb"));
                        map.put("place", "产地 " + good.getString("origin"));
                        double price = good.getDouble("shop_price");
                        map.put("price", "￥" + price);
                        double oldPrice = good.getDouble("market_price");
                        map.put("oldPrice", "￥" + oldPrice);
                        map.put("rec_id", good.getString("rec_id"));
                        map.put("goods_id", good.getString("goods_id"));
                        map.put("discount", (int) ((1 - (price / oldPrice)) * 100) + "% 折扣");
                        list.add(map);
                    }
                    if (goods.length() == 0) {
                        changeFragment(true);
                    }
                    if (goods.length() < 10) {
                        productListNotNullFragment.setHasMore(false);
                    }
                    productListNotNullFragment.nofity();
                } catch (JSONException e) {
                    changeFragment(true);
                }
            }
        }
    };

}
