package com.qualifies.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.qualifies.app.R;
import com.qualifies.app.ui.adapter.OrderDetailAdapter;
import org.json.JSONArray;
import org.json.JSONException;

public class OrderDetailActivity extends Activity {
    JSONArray goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetail);
        Intent intent = getIntent();
        try {
            goods = new JSONArray(intent.getStringExtra("goods"));
            Log.e("goods", goods.toString());
            OrderDetailAdapter adapter = new OrderDetailAdapter(getApplicationContext(), goods);
            ((ListView) findViewById(R.id.content)).setAdapter(adapter);
            ((ListView) findViewById(R.id.content)).setDividerHeight(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
