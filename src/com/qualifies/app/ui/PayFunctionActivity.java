package com.qualifies.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.qualifies.app.R;

public class PayFunctionActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payfunction);
        setResult(0);
        initView();
    }


    private void initView() {
        findViewById(R.id.pay1).setOnClickListener(this);
        findViewById(R.id.pay2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.pay1: {
                setResult(1);
                finish();
            }
            break;
            case R.id.pay2: {
                setResult(2);
                finish();
            }
            break;
        }
    }
}
