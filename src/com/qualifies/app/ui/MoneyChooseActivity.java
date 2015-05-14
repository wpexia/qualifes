package com.qualifies.app.ui;

import android.app.Activity;
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
import com.qualifies.app.R;
import com.qualifies.app.manager.MoneyManager;
import com.qualifies.app.ui.adapter.MoneyAdapter;
import com.qualifies.app.ui.adapter.MoneyChooseAdapter;
import org.json.JSONArray;

public class MoneyChooseActivity extends Activity implements View.OnClickListener {
    private ListView listView;
    private SharedPreferences sp;
    private EditText editText;
    private Button addMoney;


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

        MoneyManager moneyManager = MoneyManager.getInstance();
        moneyManager.getNow(sp.getString("token", ""), nowHandler, this);

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
        }
    }

    Handler nowHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    //Log.e("moneyNow", data.toString());
                    MoneyChooseAdapter adapter = new MoneyChooseAdapter();
                    adapter.setContent(getApplicationContext(), data);
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
