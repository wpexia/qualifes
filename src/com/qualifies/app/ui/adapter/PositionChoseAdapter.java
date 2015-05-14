package com.qualifies.app.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.ui.AddPositionActivity;
import com.qualifies.app.ui.ChoosePositionActivity;
import com.qualifies.app.ui.PositionActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PositionChoseAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private JSONArray mData;
    private Context mContext;
    private int checked = -1;

    public void setContent(JSONArray data, Context context) {
        mData = data;
        mInflater = LayoutInflater.from(context);
        mContext = context;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.position_chose_item, null);
        try {
            final JSONObject obj = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.name)).setText(obj.getString("consignee"));
            ((TextView) convertView.findViewById(R.id.phone)).setText(obj.getString("mobile"));
            ((TextView) convertView.findViewById(R.id.address)).setText(obj.getString("province_name") + "省" + obj.getString("city_name") + "市" + obj.getString("district_name") + obj.getString("address"));
            convertView.findViewById(R.id.position).setOnClickListener(new View.OnClickListener() {
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


        final RadioButton radio = (RadioButton) convertView.findViewById(R.id.positionradio);
        radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked = position;
                    notifyDataSetChanged();
                }
            }
        });
        final RadioButton head = (RadioButton) (((ChoosePositionActivity) mContext).findViewById(R.id.positionradio));
        head.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked = -1;
                    notifyDataSetChanged();
                }
            }
        });
        if (position != checked) {
            radio.setChecked(false);
        }
        if (checked != -1) {
            head.setChecked(false);
        }
        return convertView;
    }


    public int getChecked() {
        return checked;
    }

    public JSONObject getPosition() {
        try {
            JSONObject obj = mData.getJSONObject(checked);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
