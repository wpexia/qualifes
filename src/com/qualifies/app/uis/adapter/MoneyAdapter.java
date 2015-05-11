package com.qualifies.app.uis.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.qualifies.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MoneyAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private JSONArray mData;
    private Context mContext;
    private boolean mGray = false;

    public void setContent(Context context, JSONArray data) {
        mData = data;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setContent(Context context, JSONArray data, boolean gray) {
        setContent(context, data);
        mGray = gray;
    }

    @Override
    public int getCount() {
        return mData.length();
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
        try {
            JSONObject obj = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.where)).setText(obj.getString("where"));
            ((TextView) convertView.findViewById(R.id.money)).setText(obj.getString("type_money"));
            ((TextView) convertView.findViewById(R.id.deadline)).setText(obj.getString("use_end_date"));
            if(!obj.getString("bonus_sn").trim().equals("")){
                ((TextView) convertView.findViewById(R.id.type)).setText(obj.getString("bonus_sn").trim());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
