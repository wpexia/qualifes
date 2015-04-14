package com.qualifies.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends Activity {

    private EditText username;
    private EditText code;
    private EditText password;
    private Button getCode;
    private Button viewPasword;
    private Button protocol;
    private Button agree;
    private int count;
    private Timer time;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.username);
        code = (EditText) findViewById(R.id.codename);
        password = (EditText) findViewById(R.id.password_name);
        getCode = (Button) findViewById(R.id.get_code);
        viewPasword = (Button) findViewById(R.id.show_password);
        protocol = (Button) findViewById(R.id.protocol);
        agree = (Button) findViewById(R.id.agree);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().equals("")) {
                    Thread gcThread = new Thread(new GcThread());
                    gcThread.start();
                } else {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewPasword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        viewPasword.setTextColor(Color.argb(204, 0, 170, 255));
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        viewPasword.setTextColor(Color.argb(255, 0, 170, 255));
                    }
                }

                return false;
            }
        });
        protocol.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        protocol.setTextColor(Color.argb(204, 0, 170, 255));
                        Intent intent = new Intent(RegisterActivity.this, ProtocolActivity.class);
                        startActivity(intent);
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        protocol.setTextColor(Color.argb(255, 0, 170, 255));
                    }
                }
                return false;
            }
        });
        agree.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        agree.setBackgroundColor(Color.argb(255,201,28,56));
                        if(username.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        if(code.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        if(password.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        Thread verify = new Thread(new VerifyThread());
                        verify.start();
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        agree.setBackgroundColor(Color.argb(255, 195, 34, 63));
                    }
                }
                return false;
            }
        });
    }

    class GcThread implements Runnable {
        @Override
        public void run() {
            final Message msg = gcHandler.obtainMessage();
            SyncHttpClient client = new SyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("_type", "gc");
            requestParams.put("phone", username.getText().toString());
            client.post("http://test.qualifes.com:80/app_api/v_1.03/api.php?service=reg_user", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    msg.what = 0;
                    gcHandler.sendMessage(msg);
                }

                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers, String responseString,
                                      Throwable throwable) {

                }
            });
        }
    }

    Handler gcHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    time = new Timer();
                    task = new TimerTask() {
                        public void run() {

                            Message message = new Message();
                            message.what = 1;
                            timeHandler.sendMessage(message);
                        }
                    };
                    count = 61;
                    getCode.setEnabled(false);
                    time.schedule(task, 1000, 1000);
                }
                break;
                case 1: {
                    Toast.makeText(getApplicationContext(), "获取失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            count = count - msg.what;
            if (count < 1) {
                getCode.setText("免费获取");
                getCode.setEnabled(true);
                task.cancel();
                time.cancel();
                return;
            }
            getCode.setText(String.valueOf(count));
            super.handleMessage(msg);
        }
    };


    class VerifyThread implements Runnable {
        @Override
        public void run() {
            final Message msg = verifyHandler.obtainMessage();
            SyncHttpClient client = new SyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("_type", "cod");
            requestParams.put("phone", username.getText().toString());
            requestParams.put("verify", code.getText().toString());
            client.post("http://test.qualifes.com:80/app_api/v_1.03/api.php?service=reg_user", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    msg.what = 0;
                    verifyHandler.sendMessage(msg);
                }

                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers, String responseString,
                                      Throwable throwable) {
                    msg.what = 1;
                    verifyHandler.sendMessage(msg);
                }
            });
        }
    }

    Handler verifyHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    Thread registerThread = new Thread(new RegisterThread());
                    registerThread.start();
                }
                break;
                case 1: {
                    Toast.makeText(getApplicationContext(), "验证码错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    class RegisterThread implements Runnable {
        @Override
        public void run() {
            final Message msg = registerHandler.obtainMessage();
            SyncHttpClient client = new SyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("_type", "reg");
            requestParams.put("phone", username.getText().toString());
            requestParams.put("verify", code.getText().toString());
            client.post("http://test.qualifes.com:80/app_api/v_1.03/api.php?service=reg_user", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    msg.what = 0;
                    registerHandler.sendMessage(msg);
                }

                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers, String responseString,
                                      Throwable throwable) {
                    msg.what = 1;
                    registerHandler.sendMessage(msg);
                }
            });
        }
    }

    Handler registerHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    try {
                        Thread.sleep(1000);
                        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
                case 1: {
                    Toast.makeText(getApplicationContext(), "注册失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


}
