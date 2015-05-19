package com.qualifes.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.qualifes.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderAmountAdapter extends BaseAdapter {
    LayoutInflater inflater;
    JSONArray mData;

    public OrderAmountAdapter(Context context, JSONArray data) {
        inflater = LayoutInflater.from(context);
        mData = data;
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
        convertView = inflater.inflate(R.layout.confirmitem, parent, false);
        try {
            JSONObject obj = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.name)).setText(obj.getString("name"));
            double price = obj.getDouble("data");
            String priceStr = "￥";
            if (price < 0) {
                priceStr = "-" + priceStr;
                price = -price;
            }
            priceStr += price;
            ((TextView) convertView.findViewById(R.id.price)).setText(priceStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
