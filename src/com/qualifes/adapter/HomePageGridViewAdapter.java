package com.qualifes.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.qualifieslife.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HomePageGridViewAdapter extends BaseAdapter{

	Context context;
	ArrayList<HashMap<String, Object>> dataList;
	String str[] = {"1","2","3","4","5"};
	String[] keys;
	int itemLayoutid;
	public HomePageGridViewAdapter(Context _context,ArrayList<HashMap<String, Object>> list,int id) {
		// TODO Auto-generated constructor stub
		context = _context;
		dataList = list;
		itemLayoutid = id;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return str[position];
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
		convertView = layoutInflater.inflate(R.layout.good_group_item, null);
        TextView discountTextView = (TextView) convertView.findViewById(R.id.discount);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.price);
        discountTextView.setText(str[position]+"折");
        priceTextView.setText("¥"+str[position]);
        
        return convertView;
	}

}
