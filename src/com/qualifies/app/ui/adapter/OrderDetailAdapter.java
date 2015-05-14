package com.qualifies.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.ui.GoodDetailActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailAdapter extends BaseAdapter {

    JSONArray mData;
    Context context;

    public OrderDetailAdapter(Context context, JSONArray data) {
        this.context = context;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.orderdetailitem, parent, false);
        }
        try {
            final JSONObject obj = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.name)).setText(obj.getString("goods_name"));
            ((TextView) convertView.findViewById(R.id.num)).setText("数量 ×" + obj.getString("goods_number"));
            ImageView image = (ImageView) convertView.findViewById(R.id.image);
//            ImageCacheHelper.getImageSdCache().get(obj.getString("goods_thumb"), image);
            convertView.findViewById(R.id.good).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, GoodDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("goods_id", obj.getInt("goods_id"));
                        intent.putExtra("goods_id", bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            ((TextView) convertView.findViewById(R.id.money)).setText("￥" + obj.getString("goods_price"));
            String attrStr = "规格：";
            try {
                JSONArray attr = obj.getJSONArray("goods_attr");
                for (int i = 0; i < attr.length(); i++) {
                    attrStr += attr.getString(i);
                }
            } catch (JSONException e) {
                attrStr = "";
                e.printStackTrace();
            }
            ((TextView) convertView.findViewById(R.id.attr)).setText(attrStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
