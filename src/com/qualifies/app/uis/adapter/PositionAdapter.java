package com.qualifies.app.uis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.qualifies.app.R;
import com.qualifies.app.uis.personal.PositionActivity;

import java.util.HashMap;
import java.util.List;

public class PositionAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private List<HashMap<String, Object>> mData;
    private Context mContext;

    public PositionAdapter(List<HashMap<String, Object>> data) {
        mData = data;
    }

    public void setContent(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.position_item, null);
        convertView.findViewById(R.id.icon_in).setOnClickListener((PositionActivity) mContext);
        return convertView;
    }
}
