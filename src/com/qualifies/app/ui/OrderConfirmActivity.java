package com.qualifies.app.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.ui.fragment.OrderConfirmNullFragment;
import com.qualifies.app.ui.fragment.OrderConfirmOkFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderConfirmActivity extends Activity implements View.OnClickListener {

    private FragmentManager manager;

    private OrderConfirmNullFragment nullFragment;
    private OrderConfirmOkFragment okFragment;
    private int fragmentId = 0;


    private JSONObject position;
    private JSONArray goods;
    private int receiveTime = 0;
    private int payFunction = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirm);
        manager = getFragmentManager();
        Intent intent = getIntent();
        try {
            goods = new JSONArray(intent.getStringExtra("goods"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        findViewById(R.id.goods_detail).setOnClickListener(this);
        findViewById(R.id.receive_time).setOnClickListener(this);
        findViewById(R.id.send_function).setOnClickListener(this);
        findViewById(R.id.pay_function).setOnClickListener(this);
        findViewById(R.id.redmoney).setOnClickListener(this);
        changeFragment();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.chooseposition: {
                Intent intent = new Intent(this, ChoosePositionActivity.class);
                startActivityForResult(intent, 1);
            }
            break;
            case R.id.goods_detail: {
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra("goods", goods.toString());
                startActivity(intent);
            }
            break;
            case R.id.receive_time: {
                Intent intent = new Intent(this, ReachTimeActivity.class);
                intent.putExtra("choose", receiveTime);
                startActivityForResult(intent, 2);
            }
            break;
            case R.id.send_function: {
                Intent intent = new Intent(this, SendFunctionPriceActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.pay_function: {
                Intent intent = new Intent(this, PayFunctionActivity.class);
                startActivityForResult(intent, 3);
            }
            break;
            case R.id.redmoney: {
                Intent intent = new Intent(this, MoneyChooseActivity.class);
                startActivityForResult(intent, 4);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            return;
        }
        switch (requestCode) {
            case 1: {
                try {
                    position = new JSONObject(data.getStringExtra("position"));
                    fragmentId = 1;
                    changeFragment();
//                    Log.e("position", position.toString());
                    okFragment.changeView(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            case 2: {
                String[] strs = {"节假日工作日均可", "仅工作日", "仅节假日"};
                ((TextView) findViewById(R.id.timeContent)).setText(strs[resultCode - 1]);
                receiveTime = resultCode;
            }
            break;
            case 3: {
                if (resultCode == 1) {
                    ((ImageView) findViewById(R.id.paylogo)).setImageDrawable(getResources().getDrawable(R.drawable.pay_zfb));
                    ((TextView) findViewById(R.id.paycontent)).setText("支付宝");
                }
                if (resultCode == 2) {
                    ((ImageView) findViewById(R.id.paylogo)).setImageDrawable(getResources().getDrawable(R.drawable.pay_wechat));
                    ((TextView) findViewById(R.id.paycontent)).setText("微信支付");
                }
                payFunction = resultCode;
            }
        }
    }

    private void changeFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        if (fragmentId == 0) {
            if (nullFragment == null) {
                nullFragment = new OrderConfirmNullFragment();
                transaction.add(R.id.head, nullFragment);
            } else {
                transaction.show(nullFragment);
            }
        } else {
            if (okFragment == null) {
                okFragment = new OrderConfirmOkFragment();
                transaction.add(R.id.head, okFragment);
            } else {
                transaction.show(okFragment);
            }
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (nullFragment != null) {
            transaction.hide(nullFragment);
        }
        if (okFragment != null) {
            transaction.hide(okFragment);
        }
    }
}
