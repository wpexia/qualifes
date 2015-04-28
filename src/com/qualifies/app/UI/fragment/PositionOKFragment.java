package com.qualifies.app.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.qualifies.app.R;
import com.qualifies.app.ui.adapter.PositionAdapter;
import com.qualifies.app.ui.personal.PositionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PositionOKFragment extends Fragment {
    private View mView;
    private ListView listView;
    private PositionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.position_ok, container, false);
        initView();
        return mView;
    }

    private void initView() {
        listView = (ListView) mView.findViewById(R.id.listView);
        adapter = new PositionAdapter(getData());
        listView.setAdapter(adapter);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter.setContent(getActivity());
        mView.findViewById(R.id.add).setOnClickListener((PositionActivity) getActivity());
        mView.findViewById(R.id.icon_in).setOnClickListener((PositionActivity) getActivity());
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
