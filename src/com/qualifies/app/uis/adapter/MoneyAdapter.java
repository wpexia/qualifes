package com.qualifies.app.uis.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.qualifies.app.R;

import java.util.HashMap;
import java.util.List;

public class MoneyAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private List<HashMap<String, Object>> mData;
    private Context mContext;
    private boolean mGray = false;

    public void setContent(Context context, List<HashMap<String, Object>> data) {
        mData = data;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setContent(Context context, List<HashMap<String, Object>> data, boolean gray) {
        setContent(context, data);
        mGray = gray;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.money_now_item, null);
        if (mGray) {
            convertView.findViewById(R.id.left).setBackgroundColor(mContext.getResources().getColor(R.color.settingGray));
            ((TextView) convertView.findViewById(R.id.rmb)).setTextColor(mContext.getResources().getColor(R.color.settingGray));
            ((TextView) convertView.findViewById(R.id.money)).setTextColor(mContext.getResources().getColor(R.color.settingGray));
            ((TextView) convertView.findViewById(R.id.deadline)).setTextColor(mContext.getResources().getColor(R.color.settingRed));
            ((TextView) convertView.findViewById(R.id.deadline)).setText("已过期");
        }
        return convertView;
    }
}
