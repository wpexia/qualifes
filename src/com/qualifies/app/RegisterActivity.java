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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends Activity
{

    private EditText username;
    private EditText code;
    private EditText password;
    private Button getCode;
    private Button viewPasword;
    private Button protocol;
    private Button agree;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
    }

    private void initView()
    {
        username = (EditText) findViewById(R.id.username);
        code = (EditText) findViewById(R.id.codename);
        password = (EditText) findViewById(R.id.password_name);
        getCode = (Button) findViewById(R.id.get_code);
        viewPasword = (Button) findViewById(R.id.show_password);
        protocol = (Button) findViewById(R.id.protocol);
        agree = (Button) findViewById(R.id.agree);
        getCode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getCode();
            }
        });
        viewPasword.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:{
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        viewPasword.setTextColor(Color.argb(204, 0, 170, 255));

                    }
                        break;
                    case MotionEvent.ACTION_UP:
                    {
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        viewPasword.setTextColor(Color.argb(255, 0, 170, 255));
                    }
                }

                return false;
            }
        });
        protocol.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        protocol.setTextColor(Color.argb(204, 0, 170, 255));
                        Intent intent = new Intent(RegisterActivity.this, ProtocolActivity.class);
                        startActivity(intent);
                    }
                    break;
                    case MotionEvent.ACTION_UP:{
                        protocol.setTextColor(Color.argb(255, 0, 170, 255));
                    }
                }
                return false;
            }
        });
        agree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                verify();
            }
        });
    }

    private void getCode()
    {
        count = 61;
        getCode.setEnabled(false);
        time.schedule(task, 1000, 1000);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        client.get("http://test.qualifes.com:80/app_api/v_1.02/api.php?service=reg_user&data%5B_type%5D=gc&data%5Bphone%5D=" + username.getText(), requestParams, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {

            }

            @Override
            public void onFailure(int statusCode,
                                  org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable)
            {

            }
        });
    }


    private void verify()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        client.get("http://test.qualifes.com:80/app_api/v_1.02/api.php?service=reg_user&data%5B_type%5D=cod&data%5Bphone%5D=" + username.getText() + "&data%5Bverify%5D=" + code.getText(), requestParams, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                register();
            }

            @Override
            public void onFailure(int statusCode,
                                  org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable)
            {

            }
        });
    }


    private void register()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        client.get("http://test.qualifes.com:80/app_api/v_1.02/api.php?service=reg_user&data%5B_type%5D=reg&data%5Bphone%5D=" + username.getText() + "&data%5Bpwd%5D=" + password.getText() + "&data%5Bverify%5D=" + code.getText(), requestParams, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode,
                                  org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable)
            {

            }
        });
    }

    Timer time = new Timer();
    Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            count = count - msg.what;
            if (count < 1)
            {
                getCode.setText("免费获取");
                getCode.setEnabled(true);
                return;
            }
            getCode.setText(String.valueOf(count));
            super.handleMessage(msg);
        }
    };
    TimerTask task = new TimerTask()
    {
        public void run()
        {

            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };


}
