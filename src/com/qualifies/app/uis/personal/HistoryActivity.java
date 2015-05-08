package com.qualifies.app.uis.personal;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.manager.HistoryManager;
import com.qualifies.app.uis.fragment.ProductListNotNullFragment;
import com.qualifies.app.uis.fragment.ProductListNullFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class HistoryActivity extends Activity {

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

    private void initView() {
        TextView title;
        title = (TextView) findViewById(R.id.title);
        title.setText("浏览历史");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        if (!sp.contains("history")) {
            if (productListNotNullFragment == null) {
                productListNotNullFragment = new ProductListNotNullFragment();
                productListNotNullFragment.setStar(false);
                productListNotNullFragment.setData(getData(),0);
                transaction.add(R.id.fragment, productListNotNullFragment);
            } else {
                transaction.show(productListNullFragment);
            }
        } else {
            if (productListNullFragment == null) {
                productListNullFragment = new ProductListNullFragment();
                productListNullFragment.setContent("暂无浏览记录");
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

    private LinkedList<HashMap<String, Object>> getData() {
        list = new LinkedList<HashMap<String, Object>>();
        String token = sp.getString("token", "");
        if (!token.equals("")) {
            HistoryManager historyManager = HistoryManager.getInstance();
            historyManager.getHistory(token, handler, this,0);
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
                    for (int i = 0; i < goods.length(); i++) {
                        JSONObject good = goods.getJSONObject(i);
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("title", good.getString("goods_name"));
                        map.put("image", good.getString("goods_thumb"));
                        map.put("place", "产地 "+ good.getString("origin"));
                        double price = good.getDouble("shop_price");
                        map.put("price", "￥" + price);
                        double oldPrice = good.getDouble("market_price");
                        map.put("oldPrice", "￥" + oldPrice);
                        map.put("discount", (int) ((1 - (price/oldPrice)) * 100) + "% 折扣");
                        list.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
