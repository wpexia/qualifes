package com.qualifes.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.qualifes.app.R;
import com.qualifes.app.util.AsyncImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderListGridAdapter extends BaseAdapter {
    private AsyncImageLoader imageLoader = new AsyncImageLoader();
    private JSONArray mData;
    private LayoutInflater mInflater;
    private Map<String, Bitmap> bitCache = new HashMap<>();

    public OrderListGridAdapter(Context context, JSONArray data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.orderlistgriditem, null, false);
        try {
            JSONObject obj = mData.getJSONObject(position);
            ImageView image = (ImageView) convertView.findViewById(R.id.image);
            String imageUrl = obj.getString("goods_thumb");
            if (!bitCache.containsKey(imageUrl)) {
                Bitmap cachedImage = imageLoader.loadDrawable(imageUrl, image,
                        new AsyncImageLoader.ImageCallback() {
                            public void imageLoaded(Bitmap imageDrawable,
                                                    ImageView imageView, String imageUrl) {
                                bitCache.put(imageUrl, imageDrawable);
                                imageView.setImageBitmap(imageDrawable);
                            }
                        }, 1);
                if (cachedImage != null) {
                    bitCache.put(imageUrl, cachedImage);
                    image.setImageBitmap(cachedImage);
                }
            } else {
                image.setImageBitmap(bitCache.get(imageUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
