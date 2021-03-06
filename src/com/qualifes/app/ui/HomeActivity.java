package com.qualifes.app.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifes.app.R;
import com.qualifes.app.util.Api;
import com.qualifes.app.ui.fragment.*;
import com.qualifes.app.util.PlistHelper;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    private FragmentManager manager;
    public int fragmentId;
    private LinearLayout tab;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private ShoppingCartFragment shoppingCartFragment;
    private PersonalFragment personalFragment;
    private SearchKindFragment searchKindFragment;
    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        manager = getFragmentManager();
        fragmentId = 0;
        MobclickAgent.setDebugMode(true);
//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
//		MobclickAgent.setAutoLocation(true);
//		MobclickAgent.setSessionContinueMillis(1000);

        MobclickAgent.updateOnlineConfig(this);

        Api.setContext(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final PlistHelper plistHelper = PlistHelper.getInstance(getApplicationContext());
            }
        }).start();

        manager = getFragmentManager();
        changeFragment();
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 3000);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        if (!sp.contains("first")) {
            Handler xx = new Handler();
            xx.postDelayed(new GuideHandler(), 1000);
        }
    }


    class GuideHandler implements Runnable {
        @Override
        public void run() {
            flag = true;
            Intent intent = new Intent(HomeActivity.this, GuideActivity.class);
            startActivity(intent);
        }
    }


    class splashhandler implements Runnable {

        public void run() {
            flag = true;
            try {
                initView();
                Log.e("init", "1");
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

    }

    private void initView() {
        findViewById(R.id.tab).setVisibility(View.VISIBLE);
        findViewById(R.id.main).setVisibility(View.VISIBLE);
        findViewById(R.id.start).setVisibility(View.GONE);
        tab = (LinearLayout) findViewById(R.id.tab);
        changeFragment();
        findViewById(R.id.home).setOnClickListener(this);
        findViewById(R.id.personal).setOnClickListener(this);
        findViewById(R.id.shoppingcart).setOnClickListener(this);
        findViewById(R.id.list).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(HomeActivity.this);
        if (flag) {
            Log.e("init", "2");
            initView();
        } else {
            findViewById(R.id.tab).setVisibility(View.GONE);
            findViewById(R.id.main).setVisibility(View.GONE);
            findViewById(R.id.start).setVisibility(View.VISIBLE);
            Handler x = new Handler();
            x.postDelayed(new splashhandler(), 2000);
        }
        onRefresh();
    }



    @Override
    public void onContentChanged() {
        onRefresh();
    }

    public void onRefresh() {
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        if (sp.contains("token")) {
            AsyncHttpClient client = new AsyncHttpClient();
            final Message msg = new Message();
            RequestParams params = new RequestParams();
            params.put("token", sp.getString("token", ""));
            params.put("data[limit][m]", "0");
            params.put("data[limit][n]", "100");
            client.get(Api.url("get_cart"), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Api.dealSuccessRes(response, msg);
//                    Log.e("get_cart", response.toString());
                    try {
                        msg.obj = response.getJSONObject("data").getJSONArray("data");
                        int number = response.getJSONObject("data").getJSONArray("data").length();
                        if (number > 0) {
                            ((TextView) findViewById(R.id.badge)).setText(String.valueOf(number));
                            findViewById(R.id.badge).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.badge).setVisibility(View.INVISIBLE);
                        }
                    } catch (JSONException e) {
                        findViewById(R.id.badge).setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }
                }
            });
        } else {
            findViewById(R.id.badge).setVisibility(View.INVISIBLE);
        }
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
            case R.id.shoppingcart: {
                fragmentId = 2;
                changeFragment();
            }
            break;
            case R.id.list: {
//                Intent intent = new Intent(this, SearchKindActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
                fragmentId = 1;
                changeFragment();
            }
        }
    }

    public void changeFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (fragmentId) {
            case 0: {
                findViewById(R.id.home).setSelected(true);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.main, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                if (flag) {
                    tab.setVisibility(View.VISIBLE);
                }
            }
            break;
            case 1: {
                findViewById(R.id.list).setSelected(true);
                if (searchKindFragment == null) {
                    searchKindFragment = new SearchKindFragment();
                    transaction.add(R.id.main, searchKindFragment);
                } else {
                    transaction.show(searchKindFragment);
                    tab.setVisibility(View.VISIBLE);
                }
            }
            break;
            case 2: {
                findViewById(R.id.shoppingcart).setSelected(true);
                if (shoppingCartFragment == null) {
                    shoppingCartFragment = new ShoppingCartFragment();
                    transaction.add(R.id.main, shoppingCartFragment);
                } else {
                    transaction.show(shoppingCartFragment);

                    tab.setVisibility(View.VISIBLE);
                }
            }
            break;
            case 3: {
                findViewById(R.id.personal).setSelected(true);
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
        if (shoppingCartFragment != null) {
            transaction.hide(shoppingCartFragment);
        }
        if (searchKindFragment != null) {
            transaction.hide(searchKindFragment);
        }
        findViewById(R.id.home).setSelected(false);
        findViewById(R.id.personal).setSelected(false);
        findViewById(R.id.shoppingcart).setSelected(false);
        findViewById(R.id.list).setSelected(false);
        try {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(HomeActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragmentId != 0) {
                fragmentId = 0;
                changeFragment();
                return false;
            } else {
                flag = false;
                moveTaskToBack(false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (fragmentId == 0) {
            if (homeFragment.onTouchEvent(ev)) return true;
        }
        return super.dispatchTouchEvent(ev);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
