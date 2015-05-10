package com.qualifies.app.uis.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.qualifies.app.R;
import com.qualifies.app.manager.SearchManager;
import com.qualifies.app.uis.HomeActivity;
import com.qualifies.app.uis.SearchResultActivity;
import com.qualifies.app.uis.adapter.SearchHotAdapter;
import com.qualifies.app.util.SearchRecordDbHelper;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private SearchRecordDbHelper db;
    private Cursor mCursor;
    private List<String> daList;


    private ImageButton home_search_back;
    private TextView home_search_search;
    private EditText home_search_input;

    private TextView home_search_history_clear;
    private TextView home_search_history_label;
    private GridView home_search_hot_items;
    private GridView home_search_history_items;

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.search, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        db = new SearchRecordDbHelper(getActivity());
        mCursor = db.select();

        daList = new ArrayList<String>();

        while (mCursor.moveToNext()) {
            daList.add(mCursor.getString(1));
        }

        home_search_history_label = (TextView) getActivity().findViewById(R.id.home_search_history_label);
        home_search_history_items = (GridView) getActivity().findViewById(R.id.home_search_history_items);
        home_search_history_items.setAdapter(new SearchHotAdapter(getActivity(), daList, searchHandler));
        home_search_history_clear = (TextView) getActivity().findViewById(R.id.home_search_history_clear);
        if (daList.isEmpty()) {
            home_search_history_clear.setVisibility(View.GONE);
            home_search_history_label.setText("你还没有搜索记录哦");
        }


        home_search_history_clear.setOnClickListener(this);

        home_search_back = (ImageButton) getActivity().findViewById(R.id.home_search_back);
        home_search_back.setOnClickListener((HomeActivity) getActivity());

        home_search_search = (TextView) getActivity().findViewById(R.id.home_search_search);
        home_search_search.setOnClickListener(this);
        home_search_input = (EditText) getActivity().findViewById(R.id.home_search_input);

        home_search_hot_items = (GridView) getActivity().findViewById(R.id.home_search_hot_items);
        SearchManager searchManager = SearchManager.getInstance();
        searchManager.getHot(getActivity(), hotHandler);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search_search: {
                if (home_search_input.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "请输入搜索的商品信息", Toast.LENGTH_SHORT).show();
                } else {
                    String input = home_search_input.getText().toString();


                    if (!daList.contains(input)) {
                        daList.add(input);
                        db.insert(input);
                    }
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("searchKeyWord", input);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            }
            break;
            case R.id.home_search_history_clear: {
                db.clear();
                daList.clear();
            }
            break;
        }
        home_search_history_items.invalidateViews();
        if (daList.isEmpty()) {
            home_search_history_clear.setVisibility(View.GONE);
            home_search_history_label.setText("你还没有搜索记录哦");
        } else {
            home_search_history_clear.setVisibility(View.VISIBLE);
            home_search_history_label.setText("搜索历史");
        }
    }

    Handler hotHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    List<String> dataList = new ArrayList<String>();
                    for (int i = 0; i < data.length(); i++) {
                        dataList.add(data.getString(i).toString());
                    }
                    home_search_hot_items.setAdapter(new SearchHotAdapter(getActivity(), dataList, searchHandler));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Handler searchHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String input = msg.obj.toString();
            home_search_input.setText(input);
            home_search_search.callOnClick();
        }
    };
}
