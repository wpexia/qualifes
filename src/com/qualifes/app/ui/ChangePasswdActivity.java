package com.qualifes.app.ui;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifes.app.R;
import com.qualifes.app.config.Api;
import org.apache.http.Header;
import org.json.JSONObject;

public class ChangePasswdActivity extends Activity implements View.OnClickListener {

    EditText oldpw;
    EditText newpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepwd);
        initView();
    }

    private void initView() {
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.Login).setOnClickListener(this);
        oldpw = (EditText) findViewById(R.id.old_pwd);
        newpw = (EditText) findViewById(R.id.new_pwd);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_button: {
                finish();
            }
            break;
            case R.id.Login: {
                if (oldpw.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请输入当前密码", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (newpw.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    break;
                }
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                params.put("token", sp.getString("token", ""));
                params.put("data[pwd]", oldpw.getText().toString());
                params.put("data[new_pwd]", newpw.getText().toString());
                final Message msg = new Message();
                client.post(Api.url("upd_user"), params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Api.dealSuccessRes(response, msg);
                        Toast.makeText(ChangePasswdActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        if (msg.what == 0) {
                            finish();
                        }
                    }
                });
            }
        }
    }
}
