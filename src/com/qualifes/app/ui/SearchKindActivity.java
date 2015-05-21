package com.qualifes.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loopj.android.http.RequestParams;
import com.qualifes.app.R;
import com.qualifes.app.ui.adapter.SearchKindAdapter;
import com.qualifes.app.util.SearchRecordDbHelper;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ConnectionURL;
import com.loopj.android.http.JsonHttpResponseHandler;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchKindActivity extends Activity implements OnClickListener, OnItemClickListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_kind_activity);
        init();
    }

    private void init() {
        db = new SearchRecordDbHelper(SearchKindActivity.this);

        client = new AsyncHttpClient();
        back = (ImageButton) findViewById(R.id.home_search_kind_back);
        back.setOnClickListener(this);

        input = (EditText) findViewById(R.id.home_search_kind_input);

        search = (TextView) findViewById(R.id.home_search_kind_search);
        search.setOnClickListener(this);

        kind = (ListView) findViewById(R.id.homeSearchKindItems);
        kind.setOnItemClickListener(this);

        kindItems = (ListView) findViewById(R.id.homeSearchKindDetails);
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

                kind.setAdapter(new SearchKindAdapter(getApplicationContext(), cateList, R.layout.search_kind_item1, from, to));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search_kind_back:
                this.finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.home_search_kind_search:
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请输入搜索的商品信息", Toast.LENGTH_SHORT).show();
                } else {
                    String inputData = input.getText().toString();
                    if (!db.containKey(inputData)) {
                        db.insert(inputData);
                    }
                    Intent intent = new Intent(SearchKindActivity.this, SearchResultActivity.class);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.homeSearchKindItems: {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(210, RelativeLayout.LayoutParams.MATCH_PARENT);
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
                childList = new ArrayList<Map<String, String>>();
                client.get(ConnectionURL.getGoodCategory(), params1, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject data = dataArray.getJSONObject(i);
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("child", data.getString("cat_name"));
                                map.put("id", data.getString("cat_id"));
                                childList.add(map);
                                String[] from = {"child"};
                                int[] to = {R.id.home_search_kind_child_name};
                                kindItems.setAdapter(new SearchKindAdapter(SearchKindActivity.this, childList, R.layout.search_kind_item2, from, to));
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
                final StringBuffer keyWord = new StringBuffer("");
                client.get(ConnectionURL.getGoodCategory(), params2, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject data = dataArray.getJSONObject(i);
                                if (i != 0) {
                                    keyWord.append(",");
                                }
                                keyWord.append(data.getString("cat_id"));
                            }
                            Intent intent = new Intent(SearchKindActivity.this, SearchResultActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("flag", true);
                            bundle.putString("searchKeyWord", keyWord.toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }


}