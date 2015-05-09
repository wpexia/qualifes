package com.qualifies.app.uis;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.qualifies.app.R;

public class HomeActivity extends Activity{

    private FragmentManager manager;
    private int fragmentId;
    private HomeFragment homeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        manager = getFragmentManager();
        fragmentId = 0;
        initView();
    }

    private void initView() {
        changeFragment();
    }


    private void changeFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (fragmentId) {
            case 0: {
                if(homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.main, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
            }
            break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if(homeFragment != null) {
            transaction.hide(homeFragment);
        }
    }


}
