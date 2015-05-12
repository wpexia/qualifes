package com.qualifies.app.uis;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.qualifies.app.R;
import com.qualifies.app.uis.fragment.HomeFragment;
import com.qualifies.app.uis.fragment.SearchFragment;
import com.qualifies.app.uis.personal.PersonalFragment;
import com.qualifies.app.uis.personal.SettingActivity;
import com.qualifies.app.util.PlistHelper;

public class HomeActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    private FragmentManager manager;
    private int fragmentId;
    private LinearLayout tab;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private PersonalFragment personalFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
    }

    private void initView() {
        manager = getFragmentManager();
        fragmentId = 0;
        tab = (LinearLayout) findViewById(R.id.tab);
        changeFragment();
        findViewById(R.id.home).setOnClickListener(this);
        findViewById(R.id.personal).setOnClickListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int id = v.getId();
        switch (id) {
            case R.id.input: {
                fragmentId = 4;
                changeFragment();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.home_search_back: {
                fragmentId = 0;
                changeFragment();
            }
            break;
            case R.id.home: {
                fragmentId = 0;
                changeFragment();
            }
            break;
            case R.id.personal: {
                fragmentId = 3;
                changeFragment();
            }
            break;
            case R.id.personal_setting: {
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    private void changeFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (fragmentId) {
            case 0: {
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.main, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                tab.setVisibility(View.VISIBLE);
            }
            break;
            case 3: {
                if (personalFragment == null) {
                    personalFragment = new PersonalFragment();
                    transaction.add(R.id.main, personalFragment);
                } else {
                    transaction.show(personalFragment);
                    tab.setVisibility(View.VISIBLE);
                }
            }
            break;
            case 4: {
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    transaction.add(R.id.main, searchFragment);
                } else {
                    transaction.show(searchFragment);
                }
                tab.setVisibility(View.GONE);
            }
            break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (searchFragment != null) {
            transaction.hide(searchFragment);
        }
        if (personalFragment != null) {
            transaction.hide(personalFragment);
        }
    }

    @Override
    public void onBackPressed() {
        switch (fragmentId) {
            case 4: {
                fragmentId = 0;
                changeFragment();
            }
            break;
            default: {
                super.onBackPressed();
            }
        }
    }
}
