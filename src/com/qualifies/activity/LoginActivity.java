package com.qualifies.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import com.qualifies.app.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	 private EditText userNameText; 
	 private EditText passwordText;
	 private Button LoginButton;
	 private Button forgetPasswordButton;
	 private Button registerButton;
	 private AsyncHttpClient client=new AsyncHttpClient();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		InitView();
	}
	
	private void InitView(){
	userNameText=(EditText)findViewById(R.id.LoginUsername);
	passwordText=(EditText)findViewById(R.id.LoginPassword);
	LoginButton=(Button)findViewById(R.id.Login);
	forgetPasswordButton=(Button)findViewById(R.id.ForgetPassword);
	registerButton=(Button)findViewById(R.id.RegisterInLogin);	
	LoginButton.setOnClickListener(this);
	forgetPasswordButton.setOnClickListener(this);
	registerButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		if(v==LoginButton){
		 Thread loginThread=new Thread(new LoginThread());
		 loginThread.start();
		}
		if(v==forgetPasswordButton){
			
		}
		if(v==registerButton){
			
		}
	
		
	}
	
	public void LoginUser()
	{
		
		//......
		
		
		
	}
	class LoginThread implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String username = userNameText.getText().toString();  
            String password = passwordText.getText().toString();
            RequestParams requestParams=new RequestParams();
            final Message msg=handler.obtainMessage();
            final StringBuffer responsemessage=new StringBuffer("");
            requestParams.put("phone", username);
            requestParams.put("pwd", password);
            client.post("url", requestParams,new JsonHttpResponseHandler(){

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					responsemessage.append(response.toString());
					msg.what=0;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}
            
            });
            
            
            
		}
		
	}

	
	
	//Handler
	
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case 0:     
            	Bundle bundle = new Bundle();  
                bundle.putString("username", userNameText.getText().toString());  
                Intent intent = new Intent();  
                intent.putExtras(bundle); 
                startActivity(intent);
                break;  
            case 1:  
               
                break;  
            case 2:  
               
                break;  
            }  
  
        }   
	};
	

	

}
