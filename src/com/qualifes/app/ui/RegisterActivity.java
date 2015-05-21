package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
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
import com.qualifes.app.R;
import com.qualifes.app.manager.LoginManager;
import com.qualifes.app.manager.RegisterManager;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText username;
    private EditText code;
    private EditText password;
    private Button getCode;
    private Button viewPassword;
    private Button protocol;
    private Button agree;
    private int count;
    private Timer time;
    private TimerTask task;
    private RegisterManager registerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        registerManager = RegisterManager.getInst();
        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.username);
        code = (EditText) findViewById(R.id.codename);
        password = (EditText) findViewById(R.id.password_name);
        getCode = (Button) findViewById(R.id.get_code);
        viewPassword = (Button) findViewById(R.id.show_password);
        protocol = (Button) findViewById(R.id.protocol);
        agree = (Button) findViewById(R.id.agree);
        getCode.setOnClickListener(this);
        protocol.setOnClickListener(this);
        agree.setOnClickListener(this);
        viewPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.get_code: {
                if (username.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    registerManager.getCode(username.getText().toString(), gcHandler, getApplicationContext());
                }
            }
            break;
            case R.id.protocol: {
                Intent intent = new Intent(RegisterActivity.this, ProtocolActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.agree: {
                if (username.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                    break;
                } else if (code.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
                    break;
                } else if (password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    registerManager.register(username.getText().toString(), code.getText().toString(), password.getText().toString(), registerHandler, getApplicationContext());
                }
            }
            break;
        }
    }


    //获取验证码Handler
    Handler gcHandler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
            if (msg.what == 0) {
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
        }
    };

    //延时Handler
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

    //注册Handler
    Handler registerHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    try {
                        Thread.sleep(1000);
                        Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LoginManager loginManager = LoginManager.getInstance();
                    loginManager.login(username.getText().toString(), password.getText().toString(), loginHandle, RegisterActivity.this);
                }
                break;
                case 1: {
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    Handler loginHandle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username.getText().toString());
                    Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                break;
                case 1: {
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

}