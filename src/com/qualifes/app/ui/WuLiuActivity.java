package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.qualifes.app.R;
import com.qualifes.app.manager.OrderManager;

public class WuLiuActivity extends Activity {
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderfollow);
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("user", MODE_PRIVATE);
        OrderManager manager = OrderManager.getInstance();
        Intent intent = getIntent();
        manager.getOrderFollow(sp.getString("token", ""), intent.getStringExtra("orderId"), getFollowHandler);
    }

    Handler getFollowHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ((TextView) findViewById(R.id.sn)).setText((String) msg.obj);
            }
        }
    };
}
