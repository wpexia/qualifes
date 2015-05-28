package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.qualifes.app.R;
import com.qualifes.app.manager.ForgetPasswordManager;
import com.qualifes.app.manager.LoginManager;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

public class ForgetPWActivity extends Activity implements View.OnClickListener {

    private EditText username;
    private EditText code;
    private EditText password;
    private Button getCode;
    private Button viewPassword;
    private Button agree;
    private int count;
    private Timer time;
    private TimerTask task;
    private ForgetPasswordManager forgetPasswordManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpw);
        forgetPasswordManager = ForgetPasswordManager.getInst();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(ForgetPWActivity.this);
    }

    private void initView() {
        ((TextView) findViewById(R.id.headers)).setText("忘记密码");
        ((EditText) findViewById(R.id.password_name)).setHint("请输入新密码");
        ((Button) findViewById(R.id.agree)).setText("确认并修改密码");
        username = (EditText) findViewById(R.id.username);
        code = (EditText) findViewById(R.id.codename);
        password = (EditText) findViewById(R.id.password_name);
        getCode = (Button) findViewById(R.id.get_code);
        viewPassword = (Button) findViewById(R.id.show_password);
        agree = (Button) findViewById(R.id.agree);
        getCode.setOnClickListener(this);
        agree.setOnClickListener(this);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    agree.callOnClick();
                    return true;
                }
                return false;
            }
        });
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

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.get_code: {
                if (username.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "手机号码不能为空！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    forgetPasswordManager.getCode(username.getText().toString(), gcHandler, getApplicationContext());
                }
            }
            break;
            case R.id.agree: {
                if (username.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "手机号码不能为空！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                } else if (code.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                } else if (password.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                } else {
                    forgetPasswordManager.register(username.getText().toString(), code.getText().toString(), password.getText().toString(), registerHandler, getApplicationContext());
                }
            }
            break;
        }
    }


    //获取验证码Handler
    Handler gcHandler = new Handler() {
        public void handleMessage(Message msg) {
            Toast toast = Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
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
                        Toast toast = Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(ForgetPWActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
                case 1: {
                    Toast toast = Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }
    };

}
