package com.qualifies.app.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import com.qualifies.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MoneyChooseAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private JSONArray mData;
    private String check = "";


    public void setContent(Context context, JSONArray data) {
        mData = data;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.money_choose_item, null);
        try {
            final JSONObject obj = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.where)).setText(obj.getString("type_name"));
            ((TextView) convertView.findViewById(R.id.money)).setText(obj.getString("type_money"));
            ((TextView) convertView.findViewById(R.id.deadline)).setText(obj.getString("use_end_date"));
            RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.radio);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        try {
                            check = obj.getString("bonus_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                    }
                }
            });
            if (!check.equals(obj.getString("bonus_id"))) {
                radioButton.setChecked(false);
            } else {
                radioButton.setChecked(true);
            }
            if (!obj.getString("bonus_sn").trim().equals("")) {
                ((TextView) convertView.findViewById(R.id.type)).setText(obj.getString("bonus_sn").trim());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getCheckName() {
        for(int i = 0;i<mData.length();i++){
            try {
                JSONObject obj = mData.getJSONObject(i);
                if(check.equals(obj.getString("bonus_id"))) {
                    return obj.getString("type_name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
