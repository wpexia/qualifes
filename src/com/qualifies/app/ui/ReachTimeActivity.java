package com.qualifies.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.qualifies.app.R;

public class ReachTimeActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reachtime);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        int choose = intent.getIntExtra("choose",0);
        this.setResult(choose);
        if(choose == 1){
            findViewById(R.id.timechoose1).setVisibility(View.VISIBLE);
        }
        if(choose == 2){
            findViewById(R.id.timechoose2).setVisibility(View.VISIBLE);
        }
        if(choose == 3){
            findViewById(R.id.timechoose3).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.time1).setOnClickListener(this);
        findViewById(R.id.time2).setOnClickListener(this);
        findViewById(R.id.time3).setOnClickListener(this);
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
