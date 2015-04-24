package com.qualifies.app.qualifieslife;

import java.math.BigDecimal;
import java.math.MathContext;
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
import com.qualifies.app.adapter.HomePageGridViewAdapter;
import com.qualifies.app.adapter.HomePageListViewAdapter;
import com.qualifies.app.view.model.MyViewFlipper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GoodDetailActivity extends Activity implements OnClickListener{

	AsyncHttpClient client;
	ImageButton detail_back;
	ImageButton detail_back_home;
	MyViewFlipper detail_imgs;
	ImageButton detail_star;
	ImageButton detail_share;
	TextView detail_good_name;
	TextView detail_goods_name;
	TextView detail_shop_price;
	TextView detail_market_price;
	TextView detail_discount;
	TextView detail_origin;
	ImageButton detail_good_info_press;
	ImageButton detail_spec_press;
	ImageButton detail_gooddata_press;
	
	Button detail_addto_shoppingcart;
	Button detail_closing_cost;
	int goods_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.good_details);
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getBundleExtra("goods_id");
		goods_id = bundle.getInt("goods_id");
		init();
	}
	private void init() {
		client = new AsyncHttpClient();
		detail_back = (ImageButton)findViewById(R.id.detail_back);
		detail_back.setOnClickListener(this);
		
		detail_back_home = (ImageButton)findViewById(R.id.detail_back_home);
		detail_back_home.setOnClickListener(this);
		
		detail_imgs = (MyViewFlipper)findViewById(R.id.detail_imgs);
		
		detail_star = (ImageButton)findViewById(R.id.detail_star);
		detail_star.setOnClickListener(this);
		
		detail_share = (ImageButton)findViewById(R.id.detail_share);
		detail_share.setOnClickListener(this);
		
		detail_good_name = (TextView)findViewById(R.id.detail_good_name);
		detail_goods_name = (TextView)findViewById(R.id.detail_goods_name);
		
		detail_shop_price= (TextView)findViewById(R.id.detail_shop_price);
		detail_market_price= (TextView)findViewById(R.id.detail_market_price);
		detail_discount= (TextView)findViewById(R.id.detail_discount);
		detail_origin= (TextView)findViewById(R.id.detail_origin);
		
		detail_good_info_press = (ImageButton)findViewById(R.id.detail_good_info_press);
		detail_good_info_press.setOnClickListener(this);
		
		detail_spec_press = (ImageButton)findViewById(R.id.detail_spec_press);
		detail_spec_press.setOnClickListener(this);
		
		detail_gooddata_press = (ImageButton)findViewById(R.id.detail_gooddata_press);
		detail_gooddata_press.setOnClickListener(this);
		
		detail_closing_cost = (Button)findViewById(R.id.detail_closing_cost);
		detail_closing_cost.setOnClickListener(this);
		
		detail_addto_shoppingcart = (Button)findViewById(R.id.detail_addto_shoppingcart);
		detail_addto_shoppingcart.setOnClickListener(this);
		accessServer();
	}
	
	private void accessServer(){
		RequestParams params = new RequestParams();
		params.put("data[goods_id]", goods_id );
		params.put("data[type]", "j");
		client.get(ConnectionURL.getGoodsInfoURL(), params,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				List<String> imgStrs = new ArrayList<String>();
				try {
					JSONObject dataObject = response.getJSONObject("data");
					detail_good_name.setText(dataObject.getString("goods_name"));
					JSONArray imgsArray = dataObject.getJSONArray("goods_img");
					
					for (int i = 0; i < imgsArray.length(); i++) {
						imgStrs.add(imgsArray.getString(i));
					}
					if(dataObject.getInt("is_coll")!=0){
						detail_star.setBackgroundResource(R.drawable.star_red);
					}
					detail_goods_name.setText(dataObject.getString("goods_name"));
					BigDecimal shopPrice = new BigDecimal(dataObject.getString("shop_price"));
					BigDecimal marketPrice = new BigDecimal(dataObject.getString("market_price"));
					detail_shop_price.setText("￥"+shopPrice);
					detail_market_price.setText("￥"+marketPrice);
					detail_market_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); 
					BigDecimal result = shopPrice.divide(marketPrice,new MathContext(2)).multiply(new BigDecimal(10));
					detail_discount.setText(result+"折");
					
					detail_origin.setText("产地  "+dataObject.getString("origin"));
			        
					MyThread thread = new MyThread(1, imgStrs);
					thread.start();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
	@Override
	public void onClick(View v) {
		
		Intent intent2 = new Intent(GoodDetailActivity.this,GoodSpecActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("goods_id", 174);
		intent2.putExtras(bundle);
		
		switch (v.getId()) {
		case R.id.detail_back:
			GoodDetailActivity.this.finish();
			break;
		case R.id.detail_back_home:
			Intent intent = new Intent(GoodDetailActivity.this, HomePageActivity.class);
			startActivity(intent);
			break;
		case R.id.detail_star:
			break;
		case R.id.detail_share:
			break;
		case R.id.detail_good_info_press:
			break;
		case R.id.detail_spec_press:
			startActivity(intent2);
			break;
		case R.id.detail_gooddata_press:
			break;
		case R.id.detail_closing_cost:
			break;
		case R.id.detail_addto_shoppingcart:
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
	
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			
			if(msg.what==1){
				List<Bitmap> imgBitmaps = (List<Bitmap>)msg.obj;
				for (int i = 0; i < imgBitmaps.size(); i++) {
					ImageView iv = new ImageView(getApplicationContext());
					iv.setImageBitmap(imgBitmaps.get(i));
					iv.setScaleType(ImageView.ScaleType.FIT_XY);
					detail_imgs.addView(iv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
					
					detail_imgs.setAutoStart(true);			// 设置自动播放功能（点击事件，前自动播放）
					detail_imgs.setFlipInterval(1500);
					if(detail_imgs.isAutoStart() &&imgBitmaps.size()>1&& !detail_imgs.isFlipping()){
						detail_imgs.startFlipping(getApplicationContext());
					}
				}
				
				
			}
			
		};
	};
	
	class MyThread extends Thread{
		int what;
		List<String> dataList;
		List<Bitmap> dataBitmaps;
		public MyThread(int what,List<String> list){
			this.what = what;
			dataList = list;
		}
		@Override
		public void run() {
			super.run();
			dataBitmaps = new ArrayList<Bitmap>();
			for (int i = 0; i < dataList.size(); i++) {
				dataBitmaps.add(ConnectionURL.getHttpBitmap(dataList.get(i)));
			}
			
			Message message = mHandler.obtainMessage();
			message.what= this.what;
			message.obj = dataBitmaps;
			mHandler.sendMessage(message);
		}
	}
}
