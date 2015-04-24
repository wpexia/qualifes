package com.qualifies.app.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.qualifieslife.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class SpecListViewAdapter extends BaseAdapter{

	Context context;
	JSONArray dataList;
	public SpecListViewAdapter(Context _context,JSONArray list) {
		context = _context;
		dataList = list;
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Object object = null;
		try {
			object = dataList.getJSONObject(position);
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
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		convertView = layoutInflater.inflate(R.layout.spec_list_item, null);
		
		try {
			JSONObject info = dataList.getJSONObject(position);
			TextView textView = (TextView)convertView.findViewById(R.id.spec_model_name);
			textView.setText(info.getString("name"));
			
			GridView gridView = (GridView)convertView.findViewById(R.id.spec_model_gridView);
			gridView.setAdapter(new specListGridViewAdapter(context,info.getJSONArray("values")));
		} catch (JSONException e) {
			e.printStackTrace();
		} 
        return convertView;
	}
}
