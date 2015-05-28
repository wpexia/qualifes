package com.qualifes.app.ui;

import android.app.Activity;
import android.text.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.qualifes.app.R;
import com.qualifes.app.manager.OrderManager;
import com.umeng.analytics.MobclickAgent;

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
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Handler getFollowHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ((TextView) findViewById(R.id.sn)).setText((String) msg.obj);
                ((TextView) findViewById(R.id.sn)).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(((TextView) findViewById(R.id.sn)).getText().toString());
                        Toast.makeText(getApplicationContext(), "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
