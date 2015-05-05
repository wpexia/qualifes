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

public class HistoryActivity extends Activity {

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
        TextView title;
        title = (TextView) findViewById(R.id.title);
        title.setText("浏览历史");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        if (sp.contains("history")) {
            if (productListNotNullFragment == null) {
                productListNotNullFragment = new ProductListNotNullFragment();
                productListNotNullFragment.setStar(false);
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
}
