package com.qualifes.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private List<HashMap<String, Object>> mData;
    private boolean hasStar = false;
    private AsyncImageLoader imageLoader = new AsyncImageLoader();
    final private Context context;

    private Map<String, Bitmap> bitCache = new HashMap<>();

    public ProductListAdapter(List<HashMap<String, Object>> data, boolean star, Context context) {
        this.context = context;
        mData = data;
        hasStar = star;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
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
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoodDetailActivity.class);
                    Bundle bundle = new Bundle();
//                    Log.e("goodsId", mData.get(position).get("goods_id").toString());
                    bundle.putInt("goods_id", Integer.parseInt(mData.get(position).get("goods_id").toString()));
                    intent.putExtra("goods_id", bundle);
                    parent.getContext().startActivity(intent);
                }
            });
            //if (context.getSharedPreferences("user", context.MODE_PRIVATE).getBoolean("loadImage", false)) {
            final String imageUrl = mData.get(position).get("image").toString();
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

            //}
//            ImageCacheHelper.getImageCache().get(mData.get(position).get("image").toString(), image);
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
}
