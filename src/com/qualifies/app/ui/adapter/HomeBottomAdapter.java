package com.qualifies.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

public class HomeBottomAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private JSONArray mData;
    private AsyncImageLoader imageLoader = new AsyncImageLoader();


    public HomeBottomAdapter(Context context, JSONArray data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        //Log.e("number", String.valueOf(data.length()));
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
//        Log.e("Bottom", String.valueOf(position));
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_page_list_item, null);
        }
        try {
            JSONObject good = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.info_goods_name)).setText(good.getString("goods_name"));
            BigDecimal shopPrice = new BigDecimal(good.getString("shop_price"));
            BigDecimal marketPrice = new BigDecimal(good.getString("market_price"));
            BigDecimal result = shopPrice.divide(marketPrice, new MathContext(2)).multiply(new BigDecimal(10));
            ((TextView) convertView.findViewById(R.id.info_shop_price)).setText("￥" + shopPrice);
            ((TextView) convertView.findViewById(R.id.info_market_price)).setText("￥" + marketPrice);
            ((TextView) convertView.findViewById(R.id.info_discount)).setText(result + "折");
            ((TextView) convertView.findViewById(R.id.info_origin)).setText("产地 " + good.getString("origin"));
            ImageView image = (ImageView) convertView.findViewById(R.id.info_image);
            Bitmap cachedImage = imageLoader.loadDrawable(good.get("goods_img").toString(), image,
                    new AsyncImageLoader.ImageCallback() {
                        public void imageLoaded(Bitmap imageDrawable,
                                                ImageView imageView, String imageUrl) {
                            imageView.setImageBitmap(imageDrawable);
                        }
                    }, 5);
            if (cachedImage != null) {
                image.setImageBitmap(cachedImage);
            }
            //ImageCacheHelper.getImageCache().get(good.get("goods_img").toString(), image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
