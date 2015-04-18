package com.qualifes.adapter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.qualifieslife.R;
import com.qualifes.data.model.GoodInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePageListViewAdapter extends BaseAdapter {

	Context context;
	List<GoodInfo> dataList;
	int itemLayoutid;
	public HomePageListViewAdapter(Context _context,List<GoodInfo> list,int id) {
		// TODO Auto-generated constructor stub
		
		System.out.println("------------------------------------------------------");
		
		context = _context;
		dataList = list;
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
		convertView = layoutInflater.inflate(R.layout.good_info_item, null);
		
		GoodInfo info = dataList.get(position);
		
        TextView goods_name = (TextView)convertView.findViewById(R.id.info_goods_name);
        goods_name.setText(info.getGoods_name());
        
        TextView shop_price = (TextView) convertView.findViewById(R.id.info_shop_price);
        shop_price.setText("￥"+info.getShop_price().toString());
        
        TextView market_price = (TextView) convertView.findViewById(R.id.info_market_price);
        market_price.setText("￥"+info.getMarket_price().toString());
        
        TextView discount = (TextView) convertView.findViewById(R.id.info_discount);
        BigDecimal result = info.getShop_price().divide(info.getMarket_price(),new MathContext(2)).multiply(new BigDecimal(10));
        discount.setText(result+"折");
        
        TextView origin = (TextView) convertView.findViewById(R.id.info_origin);
        origin.setText("产地 "+info.getOrigin());
        
        ImageView imageView = (ImageView) convertView.findViewById(R.id.info_image);
        imageView.setImageBitmap(info.getGoods_imgBitmap());
        
        return convertView;
	}

}
