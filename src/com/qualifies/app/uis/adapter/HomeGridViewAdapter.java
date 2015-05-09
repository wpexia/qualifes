package com.qualifies.app.uis.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.util.AsyncImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.MathContext;

public class HomeGridViewAdapter extends BaseAdapter {
    private JSONArray data;
    private AsyncImageLoader imageLoader = new AsyncImageLoader();
    private LayoutInflater mInflater;

    public HomeGridViewAdapter(Context context, JSONArray data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return data.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_page_grid_item, null);
        }
        try {
            JSONObject good = data.getJSONObject(position);
            BigDecimal result = null;
            BigDecimal marketPrice = new BigDecimal(good.getString("market_price"));
            BigDecimal shopPrice = new BigDecimal(good.getString("shop_price"));
            if (marketPrice.compareTo(new BigDecimal(0)) != 0) {
                result = shopPrice.divide(marketPrice, new MathContext(2)).multiply(new BigDecimal(10));
            }
            ((TextView) convertView.findViewById(R.id.discount)).setText(result + "折");
            ((TextView) convertView.findViewById(R.id.price)).setText("￥" + shopPrice);
            ImageView image = (ImageView) convertView.findViewById(R.id.image);
            Drawable cachedImage = imageLoader.loadDrawable(good.get("goods_img").toString(), image,
                    new AsyncImageLoader.ImageCallback() {
                        public void imageLoaded(Drawable imageDrawable,
                                                ImageView imageView, String imageUrl) {
                            imageView.setImageDrawable(imageDrawable);
                        }
                    }, 4);
            if (cachedImage != null) {
                image.setImageDrawable(cachedImage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
