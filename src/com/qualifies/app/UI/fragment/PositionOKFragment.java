package com.qualifies.app.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.qualifies.app.R;
import com.qualifies.app.ui.adapter.PositionAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PositionOKFragment extends Fragment {
    private View view;
    private ListView listView;
    private PositionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.position_ok, container, false);
        initView();
        return view;
    }

    private void initView() {
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new PositionAdapter(getActivity(), getData());
        listView.setAdapter(adapter);
    }


    private List<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 1; i <= 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            list.add(map);
        }
        return list;
    }
}
