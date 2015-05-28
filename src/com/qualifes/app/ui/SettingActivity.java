package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifes.app.R;
import com.qualifes.app.util.Api;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
        findViewById(R.id.checkupdate).setOnClickListener(this);
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
            break;
            case R.id.checkupdate: {
                try {
                    PackageManager manager = this.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                    String version = String.valueOf(info.versionCode);
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("data[appver]", version);
                    params.put("data[codetype]", "android");
                    client.post(Api.url("get_app_ver"), params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                if (response.getBoolean("data")) {
                                    Uri uri = Uri.parse("http://m.qualifes.com");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SettingActivity.this, "当前是最新版本", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(SettingActivity.this, "当前是最新版本", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
