package com.qualifes.app.ui;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.Toast;
import com.qualifes.app.manager.LoginManager;
import com.qualifes.app.R;

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
import com.qualifes.app.manager.ShoppingCartManager;
import com.qualifes.app.util.OfflineCartDbHelper;

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
        findViewById(R.id.ForgetPassword).setOnClickListener(this);
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
            loginManager.login(userNameText.getText().toString(), passwordText.getText().toString(), handler, this);
        }
        if (v == forgetPasswordButton) {
            //TO-DO
        }
        if (v == registerButton) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.ForgetPassword) {
            Intent intent = new Intent(LoginActivity.this, ForgetPWActivity.class);
            startActivity(intent);
        }
    }

    //Handler
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                    String token = sp.getString("token", "  ");
                    OfflineCartDbHelper helper = new OfflineCartDbHelper(getApplicationContext());
                    Cursor cursor = helper.select();
                    String[] goodIds = new String[100];
                    String[] goodAttr = new String[100];
                    String[] goodNum = new String[100];
                    int i = 0;
                    while (cursor.moveToNext()) {
                        goodIds[i] = cursor.getString(1);
                        goodAttr[i] = cursor.getString(2);
                        goodNum[i] = cursor.getString(3);
                        i++;
                    }
                    ShoppingCartManager manager = ShoppingCartManager.getInstance();
                    manager.addShoppingCart(token, goodIds, goodAttr, goodNum, addOfflineHandler, getApplicationContext());

                    finish();
                }
                break;
                case 1:
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    Handler addOfflineHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                OfflineCartDbHelper helper = new OfflineCartDbHelper(getApplicationContext());
                helper.clear();
            }
        }
    };


}
