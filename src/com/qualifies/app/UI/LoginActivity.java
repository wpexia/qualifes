package com.qualifies.app.ui;

import android.widget.Toast;
import com.managers.LoginManager;
import com.qualifies.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import com.qualifies.app.manager.LoginManager;

public class LoginActivity extends Activity implements OnClickListener {
    private EditText userNameText;
    private EditText passwordText;
    private Button LoginButton;
    private Button forgetPasswordButton;
    private Button registerButton;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        InitView();
    }

    private void InitView() {
        userNameText = (EditText) findViewById(R.id.LoginUsername);
        passwordText = (EditText) findViewById(R.id.LoginPassword);
        LoginButton = (Button) findViewById(R.id.Login);
        forgetPasswordButton = (Button) findViewById(R.id.ForgetPassword);
        registerButton = (Button) findViewById(R.id.RegisterInLogin);
        LoginButton.setOnClickListener(this);
        forgetPasswordButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == LoginButton) {
            if (userNameText.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (passwordText.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }

            loginManager = LoginManager.getInstance();
            loginManager.login(userNameText.getText().toString(), passwordText.getText().toString(), handler);
        }
        if (v == forgetPasswordButton) {
            //TO-DO
        }
        if (v == registerButton) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }
    //Handler
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", userNameText.getText().toString());
                    Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                }
                break;
                case 1:
                    Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };


}
