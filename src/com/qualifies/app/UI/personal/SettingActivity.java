package com.qualifies.app.ui.personal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qualifies.app.R;

public class SettingActivity extends Activity implements View.OnClickListener {
    private SharedPreferences sp;
    private RelativeLayout openImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        sp = getSharedPreferences("User", MODE_PRIVATE);
        initView();
    }

    private void initView() {
        openImage = (RelativeLayout) findViewById(R.id.openImage);
        openImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.openImage: {
                boolean image = sp.getBoolean("loadImage", false);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("loadImage", !image);
                TextView openText = (TextView) findViewById(R.id.openText);
                RelativeLayout open = (RelativeLayout) findViewById(R.id.open);
                TextView wifi = (TextView) findViewById(R.id.wifitext);
                editor.apply();
                if (image) {
                    openText.setText("开");
                    wifi.setTextColor(getResources().getColor(R.color.settingRed));
                    openImage.setBackgroundResource(R.drawable.settingred);
                    open.setBackgroundResource(R.drawable.settingbuttonred);
                }else{
                    openText.setText("关");
                    wifi.setTextColor(getResources().getColor(R.color.settingGray));
                    openImage.setBackgroundResource(R.drawable.settingbutton);
                    open.setBackgroundResource(R.drawable.settingopen);
                }
            }
            break;
        }
    }
}
