package com.qualifies.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.ui.AddPositionActivity;
import com.qualifies.app.ui.PositionActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PositionAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private JSONArray mData;
    private Context mContext;

    public void setContent(JSONArray data, Context context) {
        mData = data;
        mContext = context;
        mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.position_item, null);
        convertView.findViewById(R.id.icon_in).setOnClickListener((PositionActivity) mContext);
        try {
            final JSONObject obj = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.name)).setText(obj.getString("consignee"));
            ((TextView) convertView.findViewById(R.id.phone)).setText(obj.getString("mobile"));
            ((TextView) convertView.findViewById(R.id.address)).setText(obj.getString("province_name") + "省" + obj.getString("city_name") + "市" + obj.getString("district_name") + obj.getString("address"));
            convertView.findViewById(R.id.notdefaultposition).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddPositionActivity.class);
                    intent.putExtra("position", obj.toString());
                    mContext.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
