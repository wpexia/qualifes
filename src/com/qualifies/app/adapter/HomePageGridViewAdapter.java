package com.qualifies.app.adapter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import com.qualifies.app.R;
import com.qualifies.app.data.model.GoodInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePageGridViewAdapter extends BaseAdapter{

	Context context;
	List<GoodInfo> dataList;
	String str[] = {"1","2","3","4","5"};
	String[] keys;
	int itemLayoutid;
	public HomePageGridViewAdapter(Context _context,List<GoodInfo> list,int id) {
		// TODO Auto-generated constructor stub
		context = _context;
		dataList = list;
		itemLayoutid = id;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
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
		
		GoodInfo info = dataList.get(position);
		
        TextView discountTextView = (TextView) convertView.findViewById(R.id.discount);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.price);
        BigDecimal result = info.getShop_price().divide(info.getMarket_price(),new MathContext(2)).multiply(new BigDecimal(10));
        discountTextView.setText(result+"折");
        priceTextView.setText("¥"+info.getShop_price());
        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
        imageView.setImageBitmap(info.getGoods_imgBitmap());
        return convertView;
	}

}
