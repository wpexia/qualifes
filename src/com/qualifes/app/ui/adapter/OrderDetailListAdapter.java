package com.qualifes.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qualifes.app.R;
import com.qualifes.app.ui.GoodDetailActivity;
import com.qualifes.app.util.AsyncImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OrderDetailListAdapter extends BaseAdapter {
    private AsyncImageLoader imageLoader = new AsyncImageLoader();
    private HashMap<String, Bitmap> bitCache = new HashMap<>();
    private JSONArray mData;
    private LayoutInflater mInflater;
    private Context context;

    public OrderDetailListAdapter(Context context, JSONArray data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.context = context;
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
        convertView = mInflater.inflate(R.layout.orderdetailnewitem, parent, false);
        try {
            JSONObject obj = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.name)).setText(obj.getString("goods_name"));
            try {
                JSONArray attrArr = obj.getJSONArray("good_attr");
                String st = "";
                for (int i = 0; i < attrArr.length(); i++) {
                    st += attrArr.getString(i) + " ";
                }
                ((TextView) convertView.findViewById(R.id.attr)).setText(st);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ImageView image = (ImageView) convertView.findViewById(R.id.image);
            final String imgStr = obj.getString("goods_thumb");
            if (!bitCache.containsKey(imgStr)) {
                Bitmap cachedImage = imageLoader.loadDrawable(imgStr, image,
                        new AsyncImageLoader.ImageCallback() {
                            public void imageLoaded(Bitmap imageDrawable,
                                                    ImageView imageView, String imageUrl) {
                                bitCache.put(imgStr, imageDrawable);
                                imageView.setImageBitmap(imageDrawable);
                            }
                        }, 1);
                if (cachedImage != null) {
                    bitCache.put(imgStr, cachedImage);
                    image.setImageBitmap(cachedImage);
                }
            } else {
                image.setImageBitmap(bitCache.get(imgStr));
            }
            final int goodId = obj.getInt("goods_id");
            convertView.findViewById(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoodDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("goods_id", goodId);
                    intent.putExtra("goods_id", bundle);
                    context.startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
