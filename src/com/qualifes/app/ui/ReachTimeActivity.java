package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.qualifes.app.R;
import com.umeng.analytics.MobclickAgent;

public class ReachTimeActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reachtime);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    private void initView() {
        Intent intent = getIntent();
        int choose = intent.getIntExtra("choose",0);
        this.setResult(choose);
        if(choose == 0){
            findViewById(R.id.timechoose1).setVisibility(View.VISIBLE);
        }
        if(choose == 1){
            findViewById(R.id.timechoose2).setVisibility(View.VISIBLE);
        }
        if(choose == 2){
            findViewById(R.id.timechoose3).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.time1).setOnClickListener(this);
        findViewById(R.id.time2).setOnClickListener(this);
        findViewById(R.id.time3).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.time1:{
                this.setResult(1);
                finish();
            }
            break;
            case R.id.time2:{
                this.setResult(2);
                finish();
            }
            break;
            case R.id.time3:{
                this.setResult(3);
                finish();
            }
            break;
        }
    }
}
