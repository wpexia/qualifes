package com.qualifies.app.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.qualifies.app.R;
import com.qualifies.app.manager.MoneyManager;
import com.qualifies.app.ui.adapter.MoneyAdapter;
import org.json.JSONArray;

public class MoneyNowFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private ListView listView;
    private SharedPreferences sp;
    private EditText editText;
    private Button addMoney;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.money_now, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);

        listView = (ListView) mView.findViewById(R.id.listView);
        listView.setDividerHeight(0);
        addMoney = (Button) mView.findViewById(R.id.addMoney);
        addMoney.setClickable(false);
        addMoney.setOnClickListener(this);

        editText = (EditText) mView.findViewById(R.id.editText);
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
        moneyManager.getNow(sp.getString("token", ""), nowHandler, getActivity());


    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.addMoney: {
                MoneyManager moneyManager = MoneyManager.getInstance();
                moneyManager.addMoney(editText.getText().toString(), sp.getString("token", ""), addMoneyHandler, getActivity());
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
                    MoneyAdapter adapter = new MoneyAdapter();
                    adapter.setContent(getActivity(), data);
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
                Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
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
