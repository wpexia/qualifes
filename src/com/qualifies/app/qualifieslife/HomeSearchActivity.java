package com.qualifies.app.qualifieslife;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.qualifieslife.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ConnectionURL;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifies.app.adapter.homeSearchGridViewAdapter;
import com.qualifies.app.dbhelper.dbHelper;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HomeSearchActivity extends Activity implements OnClickListener{

	private dbHelper db;
	private Cursor myCursor;
	
	ImageButton home_search_back;
	TextView home_search_search;
	EditText home_search_input;
	
	TextView home_search_history_clear;
	
	GridView home_search_hot_items;
	GridView home_search_history_items;
	
	AsyncHttpClient client;
	List<String> daList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_search);
		init();
	}

	private void init(){
		
		db=new dbHelper(HomeSearchActivity.this);
	    myCursor=db.select();
	    daList = new ArrayList<String>();
	    while (myCursor.moveToNext()) {
			daList.add(myCursor.getString(1));
		}
	    home_search_history_items = (GridView)findViewById(R.id.home_search_history_items);
	    home_search_history_items.setAdapter(new homeSearchGridViewAdapter(getApplicationContext(),daList,handler));
	    home_search_history_clear = (TextView)findViewById(R.id.home_search_history_clear);
	    home_search_history_clear.setOnClickListener(this);
	    
		client = new AsyncHttpClient();
		home_search_back = (ImageButton) findViewById(R.id.home_search_back);
		home_search_back.setOnClickListener(this);
		home_search_search = (TextView) findViewById(R.id.home_search_search);
		home_search_search.setOnClickListener(this);
		home_search_input = (EditText)findViewById(R.id.home_search_input);
		
		home_search_hot_items = (GridView)findViewById(R.id.home_search_hot_items);
		client.get(ConnectionURL.getHotGoods(), new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					JSONArray dataArray = response.getJSONArray("data");
					List<String> dataList = new ArrayList<String>();
					for (int i = 0; i < dataArray.length(); i++) {
						dataList.add(dataArray.getString(i).toString());
					}
					home_search_hot_items.setAdapter(new homeSearchGridViewAdapter(getApplicationContext(), dataList,handler));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String input = msg.obj.toString();
			System.out.println(input);
			home_search_input.setText(input);
			home_search_search.callOnClick();
		};
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_search_back:
			HomeSearchActivity.this.finish();
			break;
		case R.id.home_search_search:
			if(home_search_input.getText().toString().isEmpty()){
				Toast.makeText(getApplicationContext(), "请输入搜索的商品信息", Toast.LENGTH_SHORT).show();
			}else {
				String input = home_search_input.getText().toString();
				
				db.insert(input);
				if(!daList.contains(input))
					daList.add(input);
				
				
				RequestParams params = new RequestParams();
				params.put("data[keywords]", input);
				params.put("data[order_sort]", "desc");
				params.put("data[limit_m]", 0);
				params.put("data[limit_n]", 10);
				client.get(ConnectionURL.getSearchURL(),params,new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						System.out.println(response);
					}
				});
			}
			break;
		case R.id.home_search_history_clear:
			db.clear();
			daList.clear();
			break;
		default:
			break;
		}
		
		home_search_history_items.invalidateViews();
	}
}
