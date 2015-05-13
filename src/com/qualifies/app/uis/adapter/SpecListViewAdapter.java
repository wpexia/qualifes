package com.qualifies.app.uis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.uis.view.GoodSpecGridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.ViewGroup.LayoutParams;

public class SpecListViewAdapter extends BaseAdapter {

    Context context;
    JSONArray dataList;

    public SpecListViewAdapter(Context _context, JSONArray list) {
        context = _context;
        dataList = list;
    }


    @Override
    public int getCount() {
        return dataList.length();
    }

    @Override
    public Object getItem(int position) {
        Object object = null;
        try {
            object = dataList.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.good_spec_list_item, null);

        try {
            JSONObject info = dataList.getJSONObject(position);
            TextView textView = (TextView) convertView.findViewById(R.id.spec_model_name);
            textView.setText(info.getString("name"));

            GoodSpecGridView gridView = (GoodSpecGridView) convertView.findViewById(R.id.spec_model_gridView);
            JSONArray data = info.getJSONArray("values");
            gridView.setAdapter(new SpecListGridViewAdapter(context, data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
