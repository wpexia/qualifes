package com.qualifies.app.ui.personal;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.ui.fragment.HistoryNotNullFragment;
import com.qualifies.app.ui.fragment.HistoryNullFragment;

public class HistoryActivity extends Activity {

    private SharedPreferences sp;

    private FragmentManager fragmentManager;
    private HistoryNotNullFragment historyNotNullFragment;
    private HistoryNullFragment historyNullFragment;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences("user", MODE_PRIVATE);
        setContentView(R.layout.history);
        fragmentManager = getFragmentManager();
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        title.setText("浏览历史");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        if (!sp.contains("history")) {
            if (historyNotNullFragment == null) {
                historyNotNullFragment = new HistoryNotNullFragment();
                transaction.add(R.id.fragment, historyNotNullFragment);
            } else {
                transaction.show(historyNullFragment);
            }
        } else {
            if (historyNullFragment == null) {
                historyNullFragment = new HistoryNullFragment();
                transaction.add(R.id.fragment, historyNullFragment);
            } else {
                transaction.show(historyNullFragment);
            }
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (historyNotNullFragment != null) {
            transaction.hide(historyNotNullFragment);
        }
        if (historyNullFragment != null) {
            transaction.hide(historyNullFragment);
        }
    }
}
