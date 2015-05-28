package com.qualifes.app.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ConnectionURL;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifes.app.R;
import com.qualifes.app.ui.HomeActivity;
import com.qualifes.app.ui.SearchResultActivity;
import com.qualifes.app.ui.adapter.SearchKindAdapter;
import com.qualifes.app.util.DisplayParams;
import com.qualifes.app.util.DisplayUtil;
import com.qualifes.app.util.SearchRecordDbHelper;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchKindFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    View mView;


    private SearchRecordDbHelper db;

    ImageButton back;
    EditText input;
    TextView search;

    ListView kind;
    ListView kindItems;
    List<Map<String, String>> cateList;
    List<Map<String, String>> childList;

    AsyncHttpClient client;

    String[] from = {"cat_name", "find"};
    int[] to = {R.id.home_search_kind_item_name, R.id.home_search_kind_item_children};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.search_kind_activity, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        db = new SearchRecordDbHelper(getActivity());

        client = new AsyncHttpClient();
        back = (ImageButton) getActivity().findViewById(R.id.home_search_kind_back);
        back.setOnClickListener(this);


        search = (TextView) getActivity().findViewById(R.id.home_search_kind_search);
        search.setOnClickListener(this);

        input = (EditText) getActivity().findViewById(R.id.home_search_kind_input);
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    search.callOnClick();
                    return true;
                }
                return false;
            }
        });

        kind = (ListView) getActivity().findViewById(R.id.homeSearchKindItems);
        kind.setOnItemClickListener(this);

        kindItems = (ListView) getActivity().findViewById(R.id.homeSearchKindDetails);
        kindItems.setOnItemClickListener(this);

        accessServer();
    }



    private void accessServer() {
        client.get(ConnectionURL.getGoodCategory(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                cateList = new ArrayList<Map<String, String>>();
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject data = dataArray.getJSONObject(i);

                        Map<String, String> item = new HashMap<String, String>();
                        item.put("cat_id", data.getString("cat_id"));
                        item.put("cat_name", data.getString("cat_name"));

                        JSONArray childArray = data.getJSONArray("find");
                        String find = "";
                        for (int j = 0; j < childArray.length(); j++) {
                            find = find + childArray.getString(j) + "/";
                        }
                        find = find.substring(0, find.length() - 1);
                        item.put("find", find);
                        cateList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                kind.setAdapter(new SearchKindAdapter(getActivity(), cateList, R.layout.search_kind_item1, from, to));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search_kind_back: {
                ((HomeActivity) getActivity()).fragmentId = 0;
                ((HomeActivity) getActivity()).changeFragment();
            }

            break;
            case R.id.home_search_kind_search:
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "请输入搜索的商品信息", Toast.LENGTH_SHORT).show();
                } else {
                    String inputData = input.getText().toString();
                    if (!db.containKey(inputData)) {
                        db.insert(inputData);
                    }
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("searchKeyWord", inputData);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.homeSearchKindItems: {
                DisplayParams params2 = DisplayParams.getInstance(getActivity());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(105, params2.scale), RelativeLayout.LayoutParams.MATCH_PARENT);
                kind.setLayoutParams(params);
                for (int i = 0; i < cateList.size(); i++) {
                    RelativeLayout layout = (RelativeLayout) parent.getChildAt(i);
                    TextView child = (TextView) layout.findViewById(R.id.home_search_kind_item_children);
                    child.setVisibility(View.GONE);
                }
                String find = cateList.get(position).get("find");
                String[] children = find.split("/");
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params1 = new RequestParams();
                params1.put("data[parent_id]", cateList.get(position).get("cat_id"));
                client.get(ConnectionURL.getGoodCategory(), params1, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            childList = new ArrayList<Map<String, String>>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject data = dataArray.getJSONObject(i);
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("child", data.getString("cat_name"));
                                map.put("id", data.getString("cat_id"));
                                childList.add(map);
                                String[] from = {"child"};
                                int[] to = {R.id.home_search_kind_child_name};
                                kindItems.setAdapter(new SearchKindAdapter(getActivity(), childList, R.layout.search_kind_item2, from, to));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            break;

            case R.id.homeSearchKindDetails: {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params2 = new RequestParams();
                params2.put("data[parent_id]", childList.get(position).get("id"));
                final StringBuffer keyWord = new StringBuffer(childList.get(position).get("id"));
                client.get(ConnectionURL.getGoodCategory(), params2, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject data = dataArray.getJSONObject(i);
                                keyWord.append("," + data.getString("cat_id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("flag", true);
                        bundle.putString("searchKeyWord", keyWord.toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            break;
            default:
                break;
        }
    }

}
