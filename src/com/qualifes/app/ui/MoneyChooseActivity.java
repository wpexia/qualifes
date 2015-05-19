package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.qualifes.app.R;
import com.qualifes.app.manager.MoneyManager;
import com.qualifes.app.ui.adapter.MoneyChooseAdapter;
import org.json.JSONArray;
import org.json.JSONException;

public class MoneyChooseActivity extends Activity implements View.OnClickListener {
    private ListView listView;
    private SharedPreferences sp;
    private EditText editText;
    private Button addMoney;
    private MoneyChooseAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_choose);
        sp = getSharedPreferences("user", Activity.MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.listView);
        listView.setDividerHeight(0);
        addMoney = (Button) findViewById(R.id.addMoney);
        addMoney.setClickable(false);
        addMoney.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    addMoney.setTextColor(getResources().getColor(R.color.af));
                    addMoney.setClickable(true);
                } else {
                    addMoney.setTextColor(getResources().getColor(R.color.be));
                    addMoney.setClickable(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        findViewById(R.id.back_button).setOnClickListener(this);

        Intent intent = getIntent();
        try {
            JSONArray goods = new JSONArray(intent.getStringExtra("goods"));
            MoneyManager moneyManager = MoneyManager.getInstance();
            moneyManager.getCanUse(sp.getString("token", ""), goods, canUsehandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.addMoney: {
                MoneyManager moneyManager = MoneyManager.getInstance();
                moneyManager.addMoney(editText.getText().toString(), sp.getString("token", ""), addMoneyHandler, this);
            }
            break;
            case R.id.back_button: {
                if (adapter != null) {
                    Intent result = new Intent();
                    result.putExtra("moneyId", adapter.getCheck());
                    result.putExtra("moneyName", adapter.getCheckName());
                    setResult(1, result);
                }
                finish();
            }
            break;
        }
    }

    Handler canUsehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
//                    Log.e("MoneyChoose", data.toString());
                    adapter = new MoneyChooseAdapter();
                    Intent intent = getIntent();
                    String moneyId = intent.getStringExtra("money");
                    adapter.setContent(getApplicationContext(), data);
                    adapter.setCheck(moneyId);
                    listView.setAdapter(adapter);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Handler addMoneyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (msg.what == 0) {
                editText.setText("");
                addMoney.setTextColor(getResources().getColor(R.color.be));
                addMoney.setClickable(false);
            }
        }
    };
}
