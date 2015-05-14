package com.qualifies.app.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import com.qualifies.app.R;


import java.util.List;

public class SearchHotAdapter extends BaseAdapter{

	Context context;
	List<String> dataArray;
	Handler handler;
	public SearchHotAdapter(Context _context, List<String> _dataArray, Handler _handler) {
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
		button.setTextColor(0xff6e6e6e);
//		button.setTextColor(context.getResources().getColorStateList(R.drawable.spec_button_color));
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
