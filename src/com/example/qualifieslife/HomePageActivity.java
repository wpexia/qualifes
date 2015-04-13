package com.example.qualifieslife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ConnectionURL;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifes.adapter.HomePageGridViewAdapter;
import com.qualifes.adapter.MyAdapter;
import com.qualifes.view.model.MyListView;
import com.qualifes.view.model.MyScrollView;
import com.qualifes.view.model.MyScrollView.OnGetBottomListener;
import com.qualifes.view.model.MyViewFlipper;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HomePageActivity extends Activity implements OnClickListener,OnGetBottomListener,OnGestureListener{

	private int[] imgs = { R.drawable.a, R.drawable.b,
			  R.drawable.c, R.drawable.d, R.drawable.e };
	
	private GestureDetector gestureDetector = null;
	private Activity mActivity = null;
	
	EditText editText;
	ImageButton imageButtonSearch;
	
	MyScrollView scrollView;
	MyViewFlipper viewFlipper = null;
	ImageButton imageButtonL;
	ImageButton imageButtonR;
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
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
	
	@SuppressWarnings("deprecation")
	private void init() {
		
		mActivity = this;
		gestureDetector = new GestureDetector(this, this);
		
		client=new AsyncHttpClient();
		
		editText = (EditText)findViewById(R.id.input);
		scrollView = (MyScrollView)findViewById(R.id.scrollView);
		scrollView.setBottomListener(this);
		
		viewFlipper = (MyViewFlipper)findViewById(R.id.viewFlipper);
		
		imageButtonSearch = (ImageButton)findViewById(R.id.search);
		imageButtonSearch.setOnClickListener(this);
		imageButtonL = (ImageButton)findViewById(R.id.secondPic);
		imageButtonL.setOnClickListener(this);
		imageButtonR = (ImageButton)findViewById(R.id.thirdPic);
		imageButtonR.setOnClickListener(this);
		
		gridView1 = (GridView)findViewById(R.id.gridView1);
		gridView2 = (GridView)findViewById(R.id.gridView2);
		
		listView = (MyListView)findViewById(R.id.bottom);
		
		//viewFlipper
		for (int i = 0; i < imgs.length; i++) { 			// ���ͼƬԴ
			ImageView iv = new ImageView(this);
			iv.setImageResource(imgs[i]);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			viewFlipper.addView(iv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		
		viewFlipper.setAutoStart(true);			// �����Զ����Ź��ܣ�����¼���ǰ�Զ����ţ�
		viewFlipper.setFlipInterval(1500);
		if(viewFlipper.isAutoStart() && !viewFlipper.isFlipping()){
			viewFlipper.startFlipping(this);
		}
		
		//���˻���ͼҾ����
		int size = 5;
		int itemWidth = 200;
		int gridviewWidth = size * itemWidth;

		//�趨gridView�ĳߴ�
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
		
		gridView1.setLayoutParams(params2); // �ص�
		gridView1.setColumnWidth(itemWidth); // �ص�
		gridView1.setStretchMode(GridView.NO_STRETCH);
		gridView1.setNumColumns(size); // �ص�
		
		gridView2.setLayoutParams(params2); // �ص�
		gridView2.setColumnWidth(itemWidth); // �ص�
		gridView2.setStretchMode(GridView.STRETCH_SPACING);
		gridView2.setNumColumns(size); // �ص�
		
		gridView1.setAdapter(new HomePageGridViewAdapter(getApplicationContext(), null, R.layout.good_group_item));
		gridView2.setAdapter(new HomePageGridViewAdapter(getApplicationContext(), null, R.layout.good_group_item));
		
		//�趨ListView������
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("infoTextView", "123");
		for (int i = 0; i < 10; i++) {
			data.add(map);
			
		}
		
		//listView����
	    LinearLayout linear = (LinearLayout)findViewById(R.id.linear);
		
	    //��ȡ��Ļ�ֱ���
		final DisplayMetrics displayMetrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	    
	    final int height = displayMetrics.heightPixels;
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height-400);
		
		//��listview��������ÿ��
		linear.setLayoutParams(lp);
		
		String[] from = {"infoTextView"};
		int[] to = {R.id.infoTextView};
		listView.setAdapter(new MyAdapter(getApplicationContext(), data, R.layout.good_info_item, from, to));
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
			case R.id.search:
				String inputSearch = editText.getText().toString();
				RequestParams requestParams=new RequestParams();
				requestParams.put("data[keywords]", inputSearch);
				client.get(ConnectionURL.getSearchURL(),requestParams, new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						super.onSuccess(statusCode, headers, response);
						System.out.println(response.toString());
					}
				});
				break;
			case R.id.secondPic:
				
				break;
			case R.id.thirdPic:
				break;
				
			default:
				break;
		}
	}

	@Override
	public void onBottom() {
		// TODO Auto-generated method stub
		listView.setBottomFlag(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		System.out.println("asdsadadadaddasda");
		viewFlipper.stopFlipping();				// ����¼���ֹͣ�Զ�����
		viewFlipper.setAutoStart(false);	
		return gestureDetector.onTouchEvent(event); 		// ע�������¼�
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e2.getX() - e1.getX() > 120) {			 // �������һ���������ҳ���
			Animation rInAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_in); 	// ���һ���������Ľ���Ч����alpha  0.1 -> 1.0��
			Animation rOutAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_out); // ���һ����Ҳ໬���Ľ���Ч����alpha 1.0  -> 0.1��

			viewFlipper.setInAnimation(rInAnim);
			viewFlipper.setOutAnimation(rOutAnim);
			viewFlipper.showPrevious();
			return true;
		} else if (e2.getX() - e1.getX() < -120) {		 // �������󻬶����ҽ������
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
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
