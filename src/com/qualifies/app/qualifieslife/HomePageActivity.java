package com.qualifies.app.qualifieslife;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qualifies.app.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ConnectionURL;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifies.app.adapter.HomePageGridViewAdapter;
import com.qualifies.app.adapter.HomePageListViewAdapter;
import com.qualifies.app.adapter.MyAdapter;
import com.qualifies.app.data.model.GoodInfo;
import com.qualifies.app.view.model.MyListView;
import com.qualifies.app.view.model.MyScrollView;
import com.qualifies.app.view.model.MyViewFlipper;
import com.qualifies.app.view.model.MyScrollView.OnGetBottomListener;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HomePageActivity extends Activity implements OnClickListener,OnGetBottomListener,OnGestureListener,OnItemClickListener{
	
	List<Goods> TopSlide;
	List<Goods> special_middle;
	
	List<GoodInfo> goods_red;
	List<GoodInfo> goods_blue;
	List<GoodInfo> goods_bottom;
	
	private GestureDetector gestureDetector = null;
	private Activity mActivity = null;
	
	EditText editText;
	ImageButton imageButtonSearch;
	
	MyScrollView scrollView;
	MyViewFlipper viewFlipper = null;
	ImageView imageButtonL;
	ImageView imageButtonR;
	GridView gridView1;
	GridView gridView2;
	MyListView listView;
	
	AsyncHttpClient client;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.homepage_activity);
		init();
		accessServer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
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
	
	@SuppressWarnings("deprecation")
	private void init() {
		
		mActivity = this;
		gestureDetector = new GestureDetector(this, this);
		
		client=new AsyncHttpClient();
		
		editText = (EditText)findViewById(R.id.input);
		editText.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent2 = new Intent(HomePageActivity.this, HomeSearchActivity.class);
				startActivity(intent2);
				return true;
			}
		});
		scrollView = (MyScrollView)findViewById(R.id.scrollView);
		scrollView.setBottomListener(this);
		
		viewFlipper = (MyViewFlipper)findViewById(R.id.viewFlipper);
		
		imageButtonSearch = (ImageButton)findViewById(R.id.search);
		imageButtonSearch.setOnClickListener(this);
		imageButtonL = (ImageView)findViewById(R.id.secondPic);
		imageButtonL.setOnClickListener(this);
		imageButtonR = (ImageView)findViewById(R.id.thirdPic);
		imageButtonR.setOnClickListener(this);
		
		gridView1 = (GridView)findViewById(R.id.gridView1);
		gridView1.setOnItemClickListener(this);
		gridView2 = (GridView)findViewById(R.id.gridView2);
		gridView2.setOnItemClickListener(this);
		listView = (MyListView)findViewById(R.id.bottom);
		listView.setOnItemClickListener(this);
		
		//listView����
	    LinearLayout linear = (LinearLayout)findViewById(R.id.linear);
		
	    //��ȡ��Ļ�ֱ���
		final DisplayMetrics displayMetrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	    final int height = displayMetrics.heightPixels;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height-400);
		//��listview��������ÿ��
		linear.setLayoutParams(lp);
		
	}
	
	public Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			
			if (msg.what == 1) {

					for (int i = 0; i < TopSlide.size(); i++) {
						ImageView iv = new ImageView(getApplicationContext());
						iv.setImageBitmap(TopSlide.get(i).imageBitmap);
						iv.setScaleType(ImageView.ScaleType.FIT_XY);
						viewFlipper.addView(iv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
						
						viewFlipper.setAutoStart(true);			// �����Զ����Ź��ܣ�����¼���ǰ�Զ����ţ�?
						viewFlipper.setFlipInterval(1500);
						if(viewFlipper.isAutoStart() && !viewFlipper.isFlipping()){
							viewFlipper.startFlipping(getApplicationContext());
						}
					}
					
			}else if(msg.what == 2){
					imageButtonL.setImageBitmap(special_middle.get(0).imageBitmap);
					imageButtonR.setImageBitmap(special_middle.get(1).imageBitmap);
			}else if(msg.what == 3){
				
				//���˻���ͼҾ����
				int size = goods_red.size();
				
				System.out.println(size+"jjjjjjjj");
				
				int itemWidth = 200;
				int gridviewWidth = size * itemWidth;

				//�趨gridView�ĳߴ�
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
				
				gridView1.setLayoutParams(params2); // �ص�
				gridView1.setColumnWidth(itemWidth); // �ص�
				gridView1.setStretchMode(GridView.NO_STRETCH);
				gridView1.setNumColumns(size); // �ص�
				gridView1.setAdapter(new HomePageGridViewAdapter(getApplicationContext(), goods_red, R.layout.good_group_item));
					
			}else if (msg.what==4) {
				//���˻���ͼҾ����
				int size = goods_blue.size();
				
				int itemWidth = 200;
				int gridviewWidth = size * itemWidth;

				//�趨gridView�ĳߴ�
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
				
				gridView2.setLayoutParams(params2); // �ص�
				gridView2.setColumnWidth(itemWidth); // �ص�
				gridView2.setStretchMode(GridView.NO_STRETCH);
				gridView2.setNumColumns(size); // �ص�
				gridView2.setAdapter(new HomePageGridViewAdapter(getApplicationContext(), goods_blue, R.layout.good_group_item));
				
			}else if (msg.what==5) {
				//���˻���ͼҾ����
				listView.setAdapter(new HomePageListViewAdapter(getApplicationContext(), goods_bottom, R.layout.good_info_item));
			}
		};
	};
	
	
	private void accessServer() {
		
		RequestParams requestParams=new RequestParams();
		
		
		//home_top_slide ����
		requestParams.put("type", "activity");
		requestParams.put("logo", "home_top_slide");
		client.get(ConnectionURL.getVisualURL(), requestParams,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				TopSlide = new ArrayList<Goods>();
				
				try {
					JSONArray dataArray = response.getJSONArray("data");
					
					for (int i = 0; i < dataArray.length(); i++){
						JSONObject object = dataArray.getJSONObject(i);
						
						TopSlide.add(new Goods(object.getString("name"), object.getString("img"), object.getString("url")));
						
					}
					MyThread myThread = new MyThread(1);
					myThread.start();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		
		//home_special_middle ����
		requestParams.clear();
		requestParams.put("type", "activity");
		requestParams.put("logo", "home_special_middle");
		client.get(ConnectionURL.getVisualURL(), requestParams,new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				special_middle = new ArrayList<Goods>();
				
				try {
					JSONArray dataArray = response.getJSONArray("data");
					for (int i = 0; i < dataArray.length(); i++){
						JSONObject object = dataArray.getJSONObject(i);
						special_middle.add(new Goods(object.getString("name"), object.getString("img"), object.getString("url")));
					}
					MyThread myThread = new MyThread(2);
					myThread.start();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		
		//home_goods_red ����
		requestParams.clear();
		requestParams.put("type", "goods");
		requestParams.put("logo", "home_goods_red");
		client.get(ConnectionURL.getVisualURL(), requestParams,new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				goods_red = new ArrayList<GoodInfo>();
				
				try {
					JSONObject dataObject = response.getJSONObject("data");
					JSONArray dataArray = dataObject.getJSONArray("goods");
					
					System.out.println(dataArray);
					
					for (int i = 0; i < dataArray.length(); i++){
						JSONObject object = dataArray.getJSONObject(i);
						
						goods_red.add(new GoodInfo(Integer.parseInt(object.getString("goods_id")),
								object.getString("goods_name"),
								object.getString("goods_img"),
								convertInt2Boolean(object.getInt("is_coll")),
								object.getString("origin"),
								new BigDecimal(object.getString("market_price")),
								new BigDecimal(object.getString("shop_price")),
								Boolean.getBoolean(object.getString("is_best")),
								Boolean.getBoolean(object.getString("is_new")),
								Boolean.getBoolean(object.getString("is_hot")),
								Boolean.getBoolean(object.getString("is_promote"))
								));
						
					}
					MyThread myThread = new MyThread(3);
					myThread.start();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		
		// home_goods_blue ����
		requestParams.clear();
		requestParams.put("type", "goods");
		requestParams.put("logo", "home_goods_blue");
		client.get(ConnectionURL.getVisualURL(), requestParams,new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				goods_blue = new ArrayList<GoodInfo>();
				
				try {
					JSONObject dataObject = response.getJSONObject("data");
					JSONArray dataArray = dataObject.getJSONArray("goods");
					
					System.out.println(dataArray);
					
					for (int i = 0; i < dataArray.length(); i++){
						JSONObject object = dataArray.getJSONObject(i);
						
						goods_blue.add(new GoodInfo(Integer.parseInt(object.getString("goods_id")),
								object.getString("goods_name"),
								object.getString("goods_img"),
								convertInt2Boolean(object.getInt("is_coll")),
								object.getString("origin"),
								new BigDecimal(object.getString("market_price")),
								new BigDecimal(object.getString("shop_price")),
								Boolean.getBoolean(object.getString("is_best")),
								Boolean.getBoolean(object.getString("is_new")),
								Boolean.getBoolean(object.getString("is_hot")),
								Boolean.getBoolean(object.getString("is_promote"))
								));
						
					}
					MyThread myThread = new MyThread(4);
					myThread.start();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		
		
		// home_goods_bottom ����
		requestParams.clear();
		requestParams.put("type", "goods");
		requestParams.put("logo", "home_goods_bottom");
		client.get(ConnectionURL.getVisualURL(), requestParams,new JsonHttpResponseHandler(){
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				goods_bottom = new ArrayList<GoodInfo>();
				
				try {
					JSONObject dataObject = response.getJSONObject("data");
					JSONArray dataArray = dataObject.getJSONArray("goods");

					for (int i = 0; i < dataArray.length(); i++) {
						JSONObject object = dataArray.getJSONObject(i);

						goods_bottom.add(new GoodInfo(
								Integer.parseInt(object.getString("goods_id")), 
								object.getString("goods_name"),
								object.getString("goods_img"),
								convertInt2Boolean(object.getInt("is_coll")), 
								object.getString("origin"),
								new BigDecimal(object.getString("market_price")),
								new BigDecimal(object.getString("shop_price")),
								Boolean.getBoolean(object.getString("is_best")),
								Boolean.getBoolean(object.getString("is_new")),
								Boolean.getBoolean(object.getString("is_hot")),
								Boolean.getBoolean(object.getString("is_promote"))));
					}
					MyThread myThread = new MyThread(5);
					myThread.start();

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable,
						errorResponse);
			}
		});
	}
	
	// ��������¼�?
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
			case R.id.search:
				
				break;
			case R.id.secondPic:
				
				break;
			case R.id.thirdPic:
				break;
				
			default:
				break;
		}
	}

	// ����scroll���ض�λ�õ�
	@Override
	public void onBottom() {
		// TODO Auto-generated method stub
		listView.setBottomFlag(true);
	}

	
	
	//OnGestureListener ʵ�ֵ����Ƽ���
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		System.out.println("asdsadadadaddasda");
		viewFlipper.stopFlipping();				// ����¼���ֹͣ�Զ�����?
		viewFlipper.setAutoStart(false);	
		return gestureDetector.onTouchEvent(event); 		// ע�������¼�
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e2.getX() - e1.getX() > 120) {			 // �������һ���������ҳ���?
			Animation rInAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_in); 	// ���һ���������Ľ���Ч����alpha  0.1 -> 1.0��
			Animation rOutAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_out); // ���һ����Ҳ໬���Ľ���Ч����alpha 1.0  -> 0.1��

			viewFlipper.setInAnimation(rInAnim);
			viewFlipper.setOutAnimation(rOutAnim);
			viewFlipper.showPrevious();
			return true;
		} else if (e2.getX() - e1.getX() < -120) {		 // �������󻬶����ҽ������?
			Animation lInAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_in);		// ���󻬶�������Ľ���Ч����alpha 0.1  -> 1.0��
			Animation lOutAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_out); 	// ���󻬶��Ҳ໬���Ľ���Ч����alpha 1.0  -> 0.1��

			viewFlipper.setInAnimation(lInAnim);
			viewFlipper.setOutAnimation(lOutAnim);
			viewFlipper.showNext();
			return true;
		}
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	private class Goods{
		public String name;
		public String imageStr;
		public Bitmap imageBitmap;
		public String webURL;
		public Goods(String _name,String _imageStr,String _webURL){
			name = _name;
			imageStr = _imageStr;
			webURL = _webURL;
		}
	}
	
	
	class MyThread extends Thread{
		int what;
		
		public MyThread(int what){
			Log.e("Mythread"," " + what);
			this.what = what;
		}
		@Override
		public void run() {
			super.run();
			if(what == 1){
				for (int i = 0; i < TopSlide.size(); i++) {
					Bitmap tempBitmap = ConnectionURL.getHttpBitmap(TopSlide.get(i).imageStr);
					TopSlide.get(i).imageBitmap = tempBitmap;
				}
			}else if(what==2) {
				for (int i = 0; i < special_middle.size(); i++) {
					Bitmap tempBitmap = ConnectionURL.getHttpBitmap(special_middle.get(i).imageStr);
					special_middle.get(i).imageBitmap = tempBitmap;
				}
			}else if (what==3) {
				for (int i = 0; i < goods_red.size(); i++) {
					Bitmap tempBitmap = ConnectionURL.getHttpBitmap(goods_red.get(i).getGoods_img());
					goods_red.get(i).setGoods_imgBitmap(tempBitmap);
				}
			}else if (what==4) {
				for (int i = 0; i < goods_blue.size(); i++) {
					Bitmap tempBitmap = ConnectionURL.getHttpBitmap(goods_blue.get(i).getGoods_img());
					goods_blue.get(i).setGoods_imgBitmap(tempBitmap);
				}
			}else if (what==5) {
				for (int i = 0; i < goods_bottom.size(); i++) {
					Bitmap tempBitmap = ConnectionURL.getHttpBitmap(goods_bottom.get(i).getGoods_img());
					goods_bottom.get(i).setGoods_imgBitmap(tempBitmap);
				}
			}
			
			Message message = mHandler.obtainMessage();
			message.what= this.what;
			mHandler.sendMessage(message);
		}
	}
	  /**
     * ��ȡ����ͼƬ��Դ
     * @param url
     * @return
     */
    

    private boolean convertInt2Boolean(int i){
    	if(i==0){
    		return false;
    	}else{
    		return true;
    	}
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(HomePageActivity.this, GoodDetailActivity.class);
		Bundle bundle = new Bundle();
		
		switch (parent.getId()) {
		case R.id.gridView1:
			bundle.putInt("goods_id", goods_red.get(position).getGoods_id());
			break;
		case R.id.gridView2:
			bundle.putInt("goods_id", goods_blue.get(position).getGoods_id());
			break;
		case R.id.bottom:
			bundle.putInt("goods_id", goods_bottom.get(position).getGoods_id());
			break;
		default:
			break;
		}
		
		intent.putExtra("goods_id", bundle);
		startActivity(intent);
		
	}
}
