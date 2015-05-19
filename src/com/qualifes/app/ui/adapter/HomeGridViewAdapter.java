package com.qualifes.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qualifes.app.R;
import com.qualifes.app.util.AsyncImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;

public class HomeGridViewAdapter extends BaseAdapter {
    private JSONArray data;
    private AsyncImageLoader imageLoader = new AsyncImageLoader();
    private LayoutInflater mInflater;

    HashMap<Integer, Bitmap> bitCache = new HashMap<>();

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
            JSONObject good = data.getJSONObject(position);
            return good;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        Log.e("getViewH_HomeGrid", String.valueOf(position));
        convertView = mInflater.inflate(R.layout.home_page_grid_item, parent, false);
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

            if (!bitCache.containsKey(position)) {
                Bitmap cachedImage = imageLoader.loadDrawable(good.get("goods_img").toString(), image,
                        new AsyncImageLoader.ImageCallback() {
                            public void imageLoaded(Bitmap imageDrawable,
                                                    ImageView imageView, String imageUrl) {
                                bitCache.put(position, imageDrawable);
                                imageView.setImageBitmap(imageDrawable);
                            }
                        }, 5);
                if (cachedImage != null) {
                    bitCache.put(position, cachedImage);
                    image.setImageBitmap(cachedImage);
                }
            } else {
                image.setImageBitmap(bitCache.get(position));
            }

//            ImageCacheHelper.getImageCache().get(good.get("goods_img").toString(), image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
