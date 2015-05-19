package com.qualifes.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.qualifes.app.R;
import org.json.JSONArray;
import org.json.JSONException;

public class SpecListGridViewAdapter extends BaseAdapter {

    private Context context;
    private JSONArray dataArray;

    public SpecListGridViewAdapter(Context _context, JSONArray _dataArray) {
        context = _context;
        dataArray = _dataArray;
    }

    @Override
    public int getCount() {
        return dataArray.length();
    }

    @Override
    public Object getItem(int position) {
        Object object = null;
        try {
            object = dataArray.getJSONObject(position);
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.good_spec_grid_item, null, false);
        }
        Button button = (Button) convertView.findViewById(R.id.button);
        try {
            String label = dataArray.getJSONObject(position).getString("label");
            if (label.length() > 5)
                button.setText(label.substring(0, 4) + "..");
            else
                button.setText(label);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                GridView view = (GridView) v.getParent().getParent();
                for (int i = 0; i < view.getChildCount(); i++) {
                    ((RelativeLayout) view.getChildAt(i)).getChildAt(0).setSelected(false);
                }
                v.setSelected(true);
            }
        });
        return convertView;
    }

}
