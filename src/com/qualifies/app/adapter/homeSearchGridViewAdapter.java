package com.qualifies.app.adapter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.qualifieslife.R;
import com.example.qualifieslife.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class homeSearchGridViewAdapter extends BaseAdapter{

	Context context;
	List<String> dataArray;
	Handler handler;
	public homeSearchGridViewAdapter(Context _context,List<String> _dataArray,Handler _handler) {
		context = _context;
		dataArray = _dataArray;
		handler = _handler;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Button button = new Button(context);
		button.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT,60));
		button.setBackgroundResource(R.drawable.home_search_item_button);
//		button.setTextColor(0xff6e6e6e);
		button.setTextColor(context.getResources().getColorStateList(R.drawable.spec_button_color));
		button.setTextSize(12);
		String label = null;
		
		label = dataArray.get(position); 
		button.setText(label);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Message message = handler.obtainMessage();
				message.what = 1;
				message.obj = ((Button)v).getText().toString();
				handler.sendMessage(message);
				
			}
		});
		
		return button;
	}
	
	
}
