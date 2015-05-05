package com.qualifies.app.uis.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import com.qualifies.app.R;
import com.qualifies.app.uis.personal.PositionActivity;

import java.util.HashMap;
import java.util.List;

public class PositionChoseAdapter extends BaseAdapter{
    private LayoutInflater mInflater = null;
    private List<HashMap<String, Object>> mData;
    private Context mContext;
    private HashMap<String,Boolean> states=new HashMap<String,Boolean>();

    public PositionChoseAdapter(List<HashMap<String, Object>> data) {
        mData = data;
    }

    public void setContent(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.position_chose_item, null);
        final RadioButton radio = (RadioButton) convertView.findViewById(R.id.positionradio);
        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String key : states.keySet()) {
                    states.put(key, false);
                }
                states.put(String.valueOf(position), radio.isChecked());
                PositionChoseAdapter.this.notifyDataSetChanged();
            }
        });
        final RadioButton head = (RadioButton) (((PositionActivity) mContext).findViewById(R.id.positionradio));
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String key : states.keySet()) {
                    states.put(key, false);
                }
                states.put("-1", head.isChecked());
                PositionChoseAdapter.this.notifyDataSetChanged();
            }
        });
        boolean res=false;
        if(states.get("-1") == null || !states.get("-1")) {
            res = false;
            states.put("-1", false);
        }
        else
            res = true;
        head.setChecked(res);
        res = false;
        if(states.get(String.valueOf(position)) == null || !states.get(String.valueOf(position))){
            res=false;
            states.put(String.valueOf(position), false);
        }
        else
            res = true;

        radio.setChecked(res);

        return convertView;
    }


}
