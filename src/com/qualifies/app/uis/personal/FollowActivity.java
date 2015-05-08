package com.qualifies.app.uis.personal;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.uis.fragment.ProductListNotNullFragment;
import com.qualifies.app.uis.fragment.ProductListNullFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FollowActivity extends Activity {

    private SharedPreferences sp;

    private FragmentManager fragmentManager;
    private ProductListNotNullFragment productListNotNullFragment;
    private ProductListNullFragment productListNullFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences("user", MODE_PRIVATE);
        setContentView(R.layout.history);
        fragmentManager = getFragmentManager();
        initView();
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("我的收藏");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        if (sp.contains("follow")) {
            if (productListNotNullFragment == null) {
                productListNotNullFragment = new ProductListNotNullFragment();
                productListNotNullFragment.setStar(true);
                productListNotNullFragment.setData(getData(),1);
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

    private LinkedList<HashMap<String, Object>> getData() {
        LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String, Object>>();
        for (int i = 1; i <= 40; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", "婴儿神仙水 Mommy bilss gripe 缓解 胀气 吐奶等");
            map.put("image", "http://test.qualifes.com/images/201410/thumb_img/90_thumb_G_1414501935478.jpg");
            map.put("place", "产地 美国" + String.valueOf(i));
            map.put("discount", "55% 折扣");
            map.put("price", "￥298.00");
            map.put("oldPrice", "￥589.00");
            list.add(map);
        }
        return list;
    }

}
