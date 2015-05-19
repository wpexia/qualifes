package com.qualifes.app.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import com.qualifes.app.R;
import com.qualifes.app.manager.OrderManager;
import com.qualifes.app.ui.adapter.OrderListAdapter;
import org.json.JSONArray;

public class OrderNoShipActivity extends Activity{
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
        manager.getOrder(sp.getString("token", ""), "2", "0,3,5", "1,5", getOrderHandler);
    }

    Handler getOrderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONArray data = (JSONArray) msg.obj;
                OrderListAdapter adapter = new OrderListAdapter(OrderNoShipActivity.this, data);
                mListView.setAdapter(adapter);
            }
        }
    };
}
