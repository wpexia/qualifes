package com.qualifies.app.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.qualifies.app.R;
import com.qualifies.app.manager.MoneyManager;
import com.qualifies.app.ui.adapter.MoneyAdapter;
import org.json.JSONArray;


public class MoneyHistoryFragment extends Fragment {
    private SharedPreferences sp;

    private ListView listView;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.money_history, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);

        listView = (ListView) mView.findViewById(R.id.listView);
        listView.setDividerHeight(0);

        MoneyManager moneyManager = MoneyManager.getInstance();
        moneyManager.getHistory(sp.getString("token", ""), historyHandler, getActivity());
    }

    Handler historyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
//                    Log.e("moneyHistory", data.toString());
                    MoneyAdapter adapter = new MoneyAdapter();
                    adapter.setContent(getActivity().getApplicationContext(), data, true);
                    listView.setAdapter(adapter);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
