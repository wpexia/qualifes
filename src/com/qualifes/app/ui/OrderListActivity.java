package com.qualifes.app.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import com.qualifes.app.R;
import com.qualifes.app.manager.OrderManager;
import com.qualifes.app.ui.adapter.OrderListAdapter;
import org.json.JSONArray;

public class OrderListActivity extends Activity {
    private ListView mListView;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderlist);
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("user", MODE_PRIVATE);
        mListView = (ListView) findViewById(R.id.content);
        OrderManager manager = OrderManager.getInstance();
        manager.getOrder(sp.getString("token", ""), "", "", "", getOrderHandler);
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Handler getOrderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONArray data = (JSONArray) msg.obj;
                OrderListAdapter adapter = new OrderListAdapter(OrderListActivity.this, data);
                mListView.setAdapter(adapter);
            }
        }
    };

}
