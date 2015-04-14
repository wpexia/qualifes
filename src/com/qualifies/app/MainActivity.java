package com.qualifies.app;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity implements OnClickListener{
	TextView testTextView;
	Button jump;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		
		RequestParams requestParams=new RequestParams();
		requestParams.put("parent_id", "52");
		//requestParams.put("logo", "home_top_slide");
		
		
		AsyncHttpClient client=new AsyncHttpClient();
		//JsonHttpResponseHandler jsonHttpResponseHandler=new JsonHttpResponseHandler();
		//client.get("http://test.qualifes.com:80/app_api/v_1.02/api.php?service=get_category",jsonHttpResponseHandler );
		//string result=jsonHttpResponseHandler.getResponseString(, "UTF-8")
		client.get("http://test.qualifes.com:80/app_api/v_1.02/api.php?service=get_category",requestParams, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers, String responseString,
					Throwable throwable) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(int statusCode,
					org.apache.http.Header[] headers, JSONObject responseString) {
				// TODO Auto-generated method stub
				testTextView.setText(responseString.toString());
			}
		}
			
		 );
//		     public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//		          System.out.println(response);
//		     }
//		     public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
//		 error)
//		 {
//		          error.printStackTrace(System.out);
//		     }
//		byte[] stringBytes=new byte[128];
//			jsonHttpResponseHandler.getResponseString(stringBytes,"UTF-8" );
//			System.out.println(stringBytes);
//			
		
		
	}

	private void init() {
		testTextView=(TextView)findViewById(R.id.testtext);
		jump = (Button)findViewById(R.id.button);
		jump.setOnClickListener(this);
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
		case R.id.button:
			Intent intent = new Intent(this, HomePageActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
