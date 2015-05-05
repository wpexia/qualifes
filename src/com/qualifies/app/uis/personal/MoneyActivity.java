package com.qualifies.app.uis.personal;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import com.qualifies.app.R;
import com.qualifies.app.uis.fragment.MoneyHistoryFragment;
import com.qualifies.app.uis.fragment.MoneyNowFragment;

public class MoneyActivity extends Activity implements View.OnClickListener {

    private MoneyNowFragment moneyNowFragment;
    private MoneyHistoryFragment moneyHistoryFragment;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money);
        manager = getFragmentManager();
        initView();
    }

    private void initView() {
        showNow();
        findViewById(R.id.history).setOnClickListener(this);
        findViewById(R.id.now).setOnClickListener(this);
    }

    private void showNow() {
        FragmentTransaction transaction = manager.beginTransaction();
        hide(transaction);
        if (moneyNowFragment == null) {
            moneyNowFragment = new MoneyNowFragment();
            transaction.add(R.id.content, moneyNowFragment);
        } else {
            transaction.show(moneyNowFragment);
        }
        transaction.commit();
    }

    private void showHistory() {
        FragmentTransaction transaction = manager.beginTransaction();
        hide(transaction);
        if (moneyHistoryFragment == null) {
            moneyHistoryFragment = new MoneyHistoryFragment();
            transaction.add(R.id.content, moneyHistoryFragment);
        } else {
            transaction.show(moneyHistoryFragment);
        }
        transaction.commit();
    }

    private void hide(FragmentTransaction transaction) {
        if (moneyHistoryFragment != null) {
            transaction.hide(moneyHistoryFragment);
        }
        if (moneyNowFragment != null) {
            transaction.hide(moneyNowFragment);
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.history: {
                showHistory();
            }
            break;
            case R.id.now: {
                showNow();
            }
            break;
        }
    }
}
