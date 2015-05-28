package com.qualifes.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualifes.app.R;
import com.qualifes.app.util.Api;
import com.qualifes.app.ui.AddPositionActivity;
import com.qualifes.app.util.JsonUtil;
import org.apache.http.Header;
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
        try {
            final JSONObject obj = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.name)).setText(obj.getString("consignee"));
            ((TextView) convertView.findViewById(R.id.phone)).setText(obj.getString("mobile"));
            ((TextView) convertView.findViewById(R.id.address)).setText(obj.getString("province_name") + "省" + obj.getString("city_name") + "市" + obj.getString("district_name") + obj.getString("address"));
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddPositionActivity.class);
                    intent.putExtra("position", obj.toString());
                    mContext.startActivity(intent);
                }
            };
            convertView.findViewById(R.id.address).setOnClickListener(listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }


    public void delete(final int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        SharedPreferences sp = mContext.getSharedPreferences("user", Activity.MODE_PRIVATE);
        params.put("token", sp.getString("token", ""));
        try {
            params.put("data[address_id]", mData.getJSONObject(position).getString("address_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Message msg = new Message();
        client.post(Api.url("del_address"), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("del_adderss", response.toString());
                Api.dealSuccessRes(response, msg);
                if (msg.what == 0) {
                    Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    mData = JsonUtil.getInstance().removeJsonArray(mData, position);
                    notifyDataSetChanged();
                }
            }
        });
    }
}
