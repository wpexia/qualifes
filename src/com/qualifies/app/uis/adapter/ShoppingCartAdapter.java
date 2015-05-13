package com.qualifies.app.uis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.qualifies.app.R;

public class ShoppingCartAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    public ShoppingCartAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
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
        convertView = inflater.inflate(R.layout.shopping_cart_item, parent , false);
        return convertView;
    }
}
