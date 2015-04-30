package com.qualifies.app.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.qualifies.app.R;
import com.qualifies.app.ui.adapter.MoneyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoneyNowFragment extends Fragment {
    private View mView;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.money_now, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) mView.findViewById(R.id.listView);
        MoneyAdapter adapter = new MoneyAdapter();
        adapter.setContent(getActivity(), getData());
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);
    }


    private List<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 1; i <= 3; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            list.add(map);
        }
        return list;
    }
}
