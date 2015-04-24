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
import com.qualifies.app.adapter.SpecListViewAdapter;

import android.R.integer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GoodSpecActivity extends Activity implements OnClickListener{

	AsyncHttpClient client;
	
	ImageButton spec_back;
	
	ImageView spec_img;
	TextView spec_goods_name;
	TextView spec_shop_price;
	TextView spec_origin;
	
	ListView spec_listView;
	
	Button spec_count_sub;
	TextView spec_count_num;
	Button spec_count_add;	
	Button spec_addto_shoppingcart;
	
	int goods_id;
	int max_sale_number;
	int goods_number;
	
	JSONArray specArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		goods_id = bundle.getInt("goods_id");
		System.out.println(goods_id);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.good_spec);
		init();
	}

	private void init(){
		client = new AsyncHttpClient();
		
		spec_img = (ImageView)findViewById(R.id.spec_image);
		spec_goods_name = (TextView)findViewById(R.id.spec_goods_name);
		spec_shop_price = (TextView)findViewById(R.id.spec_shop_price);
		spec_origin = (TextView)findViewById(R.id.spec_origin);
		
		spec_back = (ImageButton)findViewById(R.id.spec_back);
		spec_back.setOnClickListener(this);
		
		
		spec_count_sub = (Button)findViewById(R.id.spec_count_sub);
		spec_count_sub.setOnClickListener(this);
		
		spec_count_add = (Button)findViewById(R.id.spec_count_add);
		spec_count_add.setOnClickListener(this);
		
		spec_count_num = (TextView)findViewById(R.id.spec_count_num);
		
		spec_listView = (ListView)findViewById(R.id.spec_listView);
		
		spec_addto_shoppingcart=(Button)findViewById(R.id.spec_addto_shoppingcart);
		spec_addto_shoppingcart.setOnClickListener(this);
		accessServer();
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			spec_img.setImageBitmap((Bitmap)msg.obj);
		};
	};
	
	private void accessServer(){
		RequestParams params = new RequestParams();
		params.put("data[goods_id]", goods_id);
		params.put("data[type]", "g");
		
		client.get(ConnectionURL.getGoodsInfoURL(), params,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					JSONObject dataObject = response.getJSONObject("data");
					spec_goods_name.setText(dataObject.getString("goods_name"));
					spec_shop_price.setText("￥"+dataObject.getString("shop_price"));
					spec_origin.setText("产地 "+dataObject.getString("origin"));
					goods_number = Integer.parseInt(dataObject.getString("goods_number"));
					max_sale_number = Integer.parseInt(dataObject.getString("max_sale_number"));
					if(max_sale_number>0){
						spec_count_num.setText(String.valueOf(max_sale_number));
					}
					new MyThread(dataObject.getString("goods_img")).start();
					
					JSONObject attribute = dataObject.getJSONObject("attribute");
					specArray = attribute.getJSONArray("spe");
					
					spec_listView.setAdapter(new SpecListViewAdapter(getApplicationContext(), specArray));
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
	class MyThread extends Thread{
		String imgURL;
		public MyThread(String _imgURL){
			imgURL = _imgURL;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Bitmap temp = ConnectionURL.getHttpBitmap(imgURL);
			Message message = handler.obtainMessage();
			message.obj = temp;
			handler.sendMessage(message);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.good_spec, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.spec_back:
			this.finish();
			break;
		case R.id.spec_count_sub:
			int numSub = Integer.parseInt(spec_count_num.getText().toString());
			if(numSub==1||numSub==max_sale_number){
				Toast.makeText(getApplicationContext(), "不能再少了哦", Toast.LENGTH_SHORT).show();
			}else {
				spec_count_num.setText(String.valueOf(--numSub));
			}
			break;
		case R.id.spec_count_add:
			int numAdd = Integer.parseInt(spec_count_num.getText().toString());
			if(numAdd<goods_number&&numAdd<100)
				numAdd++;
			spec_count_num.setText(String.valueOf(numAdd));
			break;
		case R.id.spec_addto_shoppingcart:
			if(goodsAttrIDs()!=null){
				Integer[] ids = goodsAttrIDs().toArray(new Integer[goodsAttrIDs().size()]);
				
				System.out.println(ids);
				
			}else {
				Toast.makeText(getApplicationContext(), "亲，请选择商品属性哦", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	private List<Integer> goodsAttrIDs() {
		List<Integer> goodsAttrIDs = new ArrayList<Integer>();
		for (int i = 0; i < specArray.length(); i++) {
			RelativeLayout layout = (RelativeLayout)spec_listView.getChildAt(i);
			GridView gridView = (GridView)layout.getChildAt(1);
			int select = -1;
			for (int j = 0; j < gridView.getChildCount(); j++) {
				if(gridView.getChildAt(j).isSelected())
					select = j;
			}
			if(select==-1)
				return null;
			else {
				String id=null;
				try {
					id = specArray.getJSONObject(i).getJSONArray("values").getJSONObject(select).getString("id");
				} catch (JSONException e) {
					e.printStackTrace();
				} 
				goodsAttrIDs.add(Integer.valueOf(id));
			}
		}
		System.out.println(goodsAttrIDs);
		return goodsAttrIDs;
	}
}