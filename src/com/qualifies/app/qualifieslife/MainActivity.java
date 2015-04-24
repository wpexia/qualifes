package com.qualifies.app.qualifieslife;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qualifies.app.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ConnectionURL;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity implements OnClickListener{
	TextView testTextView;
	Button main_homepage;
	Button main_details;
	ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		MyThread myThread = new MyThread();
		myThread.start();
		
		RequestParams requestParams=new RequestParams();
		requestParams.put("type", "activity");
		requestParams.put("logo", "home_top_slide");
		//requestParams.put("logo", "home_top_slide");
		
		
		AsyncHttpClient client=new AsyncHttpClient();
		client.get(ConnectionURL.getVisualURL(),requestParams, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers, String responseString,
					Throwable throwable) {
			}

			@Override
			public void onSuccess(int statusCode,
					org.apache.http.Header[] headers, JSONObject responseString) {
				testTextView.setText("success");
			}
		}
			
		 );			
		
	}

	private void init() {
		testTextView=(TextView)findViewById(R.id.testtext);
		main_homepage = (Button)findViewById(R.id.main_homepage);
		main_details = (Button)findViewById(R.id.main_details);
		main_homepage.setOnClickListener(this);
		main_details.setOnClickListener(this);
		imageView = (ImageView)findViewById(R.id.image);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_homepage:
			Intent homepageIntent = new Intent(this, HomePageActivity.class);
			startActivity(homepageIntent);
			break;
		case R.id.main_details:
			Intent gooddetailIntent = new Intent(this, GoodDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("goods_id", 123);
			gooddetailIntent.putExtra("goods_id", bundle);
			startActivity(gooddetailIntent);
			break;
		default:
			break;
		}
	}
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 200 ){
				imageView.setImageBitmap((Bitmap)msg.obj);
			}else {
				System.out.println("error");
			}
		};
	};
	class MyThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			String imgString = "http://h.hiphotos.baidu.com/zhidao/pic/item/5bafa40f4bfbfbed0470471b78f0f736afc31fac.jpg";
			Bitmap teBitmap = getHttpBitmap(imgString);

			Message message = mHandler.obtainMessage();
			message.what = 200;
			message.obj = teBitmap;
			mHandler.sendMessage(message);
		}
	}
	
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //�������
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //���ó�ʱʱ��Ϊ6000���룬conn.setConnectionTiem(0);��ʾû��ʱ������
            conn.setConnectTimeout(6000);
            //�������û��������
            conn.setDoInput(true);
            //��ʹ�û���
            conn.setUseCaches(false);
            //�����п��ޣ�û��Ӱ��
            //conn.connect();
            //�õ�������
            InputStream is = conn.getInputStream();
            //�����õ�ͼƬ
            bitmap = BitmapFactory.decodeStream(is);
            //�ر�������
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(bitmap);
        return bitmap;
         
    }
}
