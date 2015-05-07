package com.qualifies.app.uis.personal;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.qualifies.app.R;
import com.qualifies.app.uis.fragment.RealNameNULLFragment;
import com.qualifies.app.uis.fragment.RealNameOKFragment;

public class RealNameActivity extends Activity {


    private FragmentManager manager;
    private SharedPreferences sp;
    private RealNameNULLFragment realNameNULLFragment;
    private RealNameOKFragment realNameOKFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realname);
        manager = getFragmentManager();
        sp = getSharedPreferences("user", MODE_PRIVATE);
        initView();
    }

    private void initView() {
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        if (sp.contains("realName")) {
            if (realNameOKFragment == null) {
                realNameOKFragment = new RealNameOKFragment();
                transaction.add(R.id.content, realNameOKFragment);
            } else
                transaction.show(realNameOKFragment);
        } else {
            if (realNameNULLFragment == null) {
                realNameNULLFragment = new RealNameNULLFragment();
                transaction.add(R.id.content, realNameNULLFragment);
            }
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (realNameNULLFragment != null) {
            transaction.hide(realNameNULLFragment);
        }
        if (realNameOKFragment != null) {
            transaction.hide(realNameOKFragment);
        }
    }
}
