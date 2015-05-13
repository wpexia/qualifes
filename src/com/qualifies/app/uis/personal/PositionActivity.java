package com.qualifies.app.uis.personal;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.qualifies.app.R;
import com.qualifies.app.uis.fragment.PositionNullFragment;
import com.qualifies.app.uis.fragment.PositionOKFragment;

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
    }

    private void initView() {
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        if (sp.contains("position")) {
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
