package com.qualifies.app.adapter;

import org.json.JSONArray;
import org.json.JSONException;

import com.qualifies.app.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class specListGridViewAdapter extends BaseAdapter{

	Context context;
	JSONArray dataArray;
	public specListGridViewAdapter(Context _context,JSONArray _dataArray) {
		context = _context;
		dataArray = _dataArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Object object = null;
		try {
			object = dataArray.getJSONObject(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Button button = new Button(context);
		button.setWidth(120);
		button.setHeight(56);
		button.setTextSize(12);
		try {
			String label = dataArray.getJSONObject(position).getString("label"); 
			if(label.length()>6)
				button.setText(label.substring(0, 5)+"..");
			else 
				button.setText(label);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GridView view = (GridView)v.getParent();
				for (int i = 0; i < view.getChildCount(); i++) {
					view.getChildAt(i).setSelected(false);
				}
				v.setSelected(true);
			}
		});
		button.setTextColor(context.getResources().getColorStateList(R.drawable.spec_button_color));
		button.setBackgroundResource(R.drawable.spec_button);
		return button;
	}

}
