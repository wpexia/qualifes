package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.qualifes.app.R;
import com.umeng.analytics.MobclickAgent;

public class SettingActivity extends Activity implements View.OnClickListener {
    private SharedPreferences sp;
    private RelativeLayout openImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    private void initView() {
        openImage = (RelativeLayout) findViewById(R.id.openImage);
        openImage.setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.call).setOnClickListener(this);
        findViewById(R.id.cleancache).setOnClickListener(this);
        findViewById(R.id.aboutus).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
        findViewById(R.id.changepassword).setOnClickListener(this);
        if (!sp.contains("token")) {
            findViewById(R.id.logout).setVisibility(View.INVISIBLE);
        }
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.openImage: {
                boolean image = sp.getBoolean("loadImage", false);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("loadImage", !image);
                editor.apply();
                TextView openText = (TextView) findViewById(R.id.openText);
                RelativeLayout open = (RelativeLayout) findViewById(R.id.open);
                TextView wifi = (TextView) findViewById(R.id.wifitext);
                if (image) {
                    openText.setText("开");
                    wifi.setTextColor(getResources().getColor(R.color.settingRed));
                    openImage.setBackgroundResource(R.drawable.settingred);
                    open.setBackgroundResource(R.drawable.settingbuttonred);
                } else {
                    openText.setText("关");
                    wifi.setTextColor(getResources().getColor(R.color.settingGray));
                    openImage.setBackgroundResource(R.drawable.settingbutton);
                    open.setBackgroundResource(R.drawable.settingopen);
                }
            }
            break;
            case R.id.logout: {
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("token");
                editor.apply();
                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.call: {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4008946066"));
                startActivity(intent);
            }
            break;
            case R.id.cleancache: {
                Toast.makeText(this, "清除成功", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.aboutus: {
                Intent intent = new Intent(SettingActivity.this, WebActivity.class);
                intent.putExtra("title", "关于我们");
                intent.putExtra("url", "http://www.qualifes.com/webview/release/ios_info/about.html");
                startActivity(intent);
            }
            break;
            case R.id.help: {
                Intent intent = new Intent(SettingActivity.this, WebActivity.class);
                intent.putExtra("title", "使用帮助");
                intent.putExtra("url", "http://www.qualifes.com/webview/release/ios_info/help.html");
                startActivity(intent);
            }
            break;
            case R.id.changepassword: {
                if (sp.contains("token")) {
                    Intent intent = new Intent(SettingActivity.this, ChangePasswdActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }
    }


    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
