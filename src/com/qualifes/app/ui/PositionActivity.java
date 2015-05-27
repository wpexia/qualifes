package com.qualifes.app.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.qualifes.app.R;
import com.qualifes.app.manager.PositionManager;
import com.qualifes.app.ui.fragment.PositionNullFragment;
import com.qualifes.app.ui.fragment.PositionOKFragment;
import org.json.JSONArray;

public class PositionActivity extends Activity implements View.OnClickListener {

    private SharedPreferences sp;
    private PositionNullFragment positionNullFragment;
    private PositionOKFragment positionOKFragment;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.position);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        manager = getFragmentManager();
        initView();
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        PositionManager positionManager = PositionManager.getInstance();
        positionManager.getPosition(sp.getString("token", ""), getPosition, PositionActivity.this);
    }

    Handler getPosition = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentTransaction transaction = manager.beginTransaction();
            hideFragment(transaction);
            if (msg.what == 1) {
                if (positionNullFragment == null) {
                    positionNullFragment = new PositionNullFragment();
                    transaction.add(R.id.content, positionNullFragment);
                } else {
                    transaction.show(positionNullFragment);
                }
            } else {
                if (positionOKFragment == null) {
                    positionOKFragment = new PositionOKFragment();
                    transaction.add(R.id.content, positionOKFragment);
                } else {
                    transaction.show(positionOKFragment);
                }
            }
            transaction.commit();
        }
    };

    private void hideFragment(FragmentTransaction transaction) {
        if (positionNullFragment != null) {
            transaction.hide(positionNullFragment);
        }
        if (positionOKFragment != null) {
            transaction.hide(positionOKFragment);
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.add: {
                Intent intent = new Intent(PositionActivity.this, AddPositionActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.icon_in: {

            }
            break;
        }
    }
}
