package com.qualifies.app.uis.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.qualifies.app.R;
import com.qualifies.app.uis.adapter.PositionChoseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PositionChoseFragment extends Fragment {
    View mView;
    private ListView listView;
    private PositionChoseAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.position_chose, container, false);
        initView();
        return mView;
    }

    private void initView() {
        listView = (ListView) mView.findViewById(R.id.listView);
        adapter = new PositionChoseAdapter(getData());
        listView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter.setContent(getActivity());
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
