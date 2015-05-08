package com.qualifies.app.uis.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.util.AsyncImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private List<HashMap<String, Object>> mData;
    private boolean hasStar = false;
    private AsyncImageLoader imageLoader = new AsyncImageLoader();
    private Context context;

    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    public ProductListAdapter(List<HashMap<String, Object>> data, boolean star) {
        mData = data;
        hasStar = star;
    }

    public void setContent(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.history_item, null);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView place = (TextView) convertView.findViewById(R.id.place);
        TextView discount = (TextView) convertView.findViewById(R.id.discount);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView oldPrice = (TextView) convertView.findViewById(R.id.oldPrice);
        ImageView star = (ImageView) convertView.findViewById(R.id.star);

        if (hasStar)
            star.setImageResource(R.drawable.star_red);
        if (mData.get(position).containsKey("title")) {
            title.setText(mData.get(position).get("title").toString());
            place.setText(mData.get(position).get("place").toString());
            discount.setText(mData.get(position).get("discount").toString());
            price.setText(mData.get(position).get("price").toString());
            oldPrice.setText(mData.get(position).get("oldPrice").toString());
            oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
            //if (context.getSharedPreferences("user", context.MODE_PRIVATE).getBoolean("loadImage", false)) {
            Drawable cachedImage = imageLoader.loadDrawable(mData.get(position).get("image").toString(), image,
                    new AsyncImageLoader.ImageCallback() {
                        public void imageLoaded(Drawable imageDrawable,
                                                ImageView imageView, String imageUrl) {
                            imageView.setImageDrawable(imageDrawable);
                        }
                    });
            if (cachedImage != null) {
                image.setImageDrawable(cachedImage);
            }//}
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mData.size();
    }


    public void delete(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    static class Holder {
        ImageView image;
        TextView title;
        TextView place;
        TextView discount;
        TextView price;
        TextView oldPrice;
        ImageView star;
    }

}
