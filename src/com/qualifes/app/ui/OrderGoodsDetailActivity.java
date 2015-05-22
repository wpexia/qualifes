package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.qualifes.app.R;
import com.qualifes.app.ui.adapter.OrderDetailAdapter;
import org.json.JSONArray;
import org.json.JSONException;

public class OrderGoodsDetailActivity extends Activity {
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
            findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ((ListView) findViewById(R.id.content)).setDividerHeight(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
