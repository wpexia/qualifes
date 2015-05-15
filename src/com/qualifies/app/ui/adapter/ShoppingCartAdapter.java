package com.qualifies.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.qualifies.app.R;
import com.qualifies.app.ui.HomeActivity;
import com.qualifies.app.util.AsyncImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private JSONArray mData;
    private AsyncImageLoader imageLoader = new AsyncImageLoader();
    public boolean flag = false;

    Map<String, Bitmap> bitCache = new HashMap<>();

    public ShoppingCartAdapter(Context context, JSONArray data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mData = data;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        convertView = inflater.inflate(R.layout.shopping_cart_item, parent, false);
        try {
            final JSONObject obj = mData.getJSONObject(position);
            ((TextView) convertView.findViewById(R.id.name)).setText(obj.getString("goods_name"));

            final EditText number = ((EditText) convertView.findViewById(R.id.number));
            if (obj.getInt("goods_number") > obj.getInt("goods_inventory")) {
                obj.remove("goods_number");
                obj.put("goods_number", obj.getString("goods_inventory"));
            }
            flag = false;
            number.setText(obj.getString("goods_number"));
            number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (number.getText().toString().trim().equals("")) {
                            number.setText("1");
                            try {
                                obj.remove("goods_number");
                                obj.put("goods_number", "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        int num = Integer.parseInt(number.getText().toString());
                        if (num < 1) {
                            number.setText("1");
                            try {
                                obj.remove("goods_number");
                                obj.put("goods_number", "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        try {
                            if (num > obj.getInt("goods_inventory")) {
                                try {
                                    obj.remove("goods_number");
                                    obj.put("goods_number", obj.getString("goods_inventory"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                number.setText(obj.getString("goods_inventory"));
                                Toast.makeText(context, "亲，库存只有这么多哦", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            ImageView image = (ImageView) convertView.findViewById(R.id.image);
//            ImageCacheHelper.getImageCache().get(obj.getString("goods_thumb"), image);
            if (!bitCache.containsKey(obj.getString("goods_thumb"))) {
                Bitmap imgaeCache = imageLoader.loadDrawable(obj.getString("goods_thumb"), image,
                        new AsyncImageLoader.ImageCallback() {
                            public void imageLoaded(Bitmap imageDrawable,
                                                    ImageView imageView, String imageUrl) {
                                bitCache.put(imageUrl, imageDrawable);
                                imageView.setImageBitmap(imageDrawable);
                            }
                        }, 1);
                if (imgaeCache != null) {
                    image.setImageBitmap(imgaeCache);
                }
            } else {
                image.setImageBitmap(bitCache.get(obj.getString("goods_thumb")));
            }

            final CheckBox check = (CheckBox) convertView.findViewById(R.id.checkBox);
            check.setChecked(obj.getBoolean("checked"));
            convertView.findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (number.getText().toString().trim().equals("")) {
                        return;
                    }
                    int num = Integer.parseInt(number.getText().toString());
                    if (num > 1) {
                        if (check.isChecked()) {
                            try {
                                TextView total = (TextView) ((HomeActivity) parent.getContext()).findViewById(R.id.total);
                                double money = obj.getDouble("goods_price");
                                double totalMoney = Double.parseDouble(total.getText().toString());
                                totalMoney -= money;
                                total.setText(String.valueOf(totalMoney));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        num -= 1;
                        number.setText(String.valueOf(num));
                        try {
                            obj.remove("goods_number");
                            obj.put("goods_number", String.valueOf(num));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, "亲，不能再少了哦", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            convertView.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(number.getText().toString());
                    try {
                        if (num < obj.getInt("goods_inventory")) {
                            if (check.isChecked()) {
                                try {
                                    TextView total = (TextView) ((HomeActivity) parent.getContext()).findViewById(R.id.total);
                                    double money = obj.getDouble("goods_price");
                                    double totalMoney = Double.parseDouble(total.getText().toString());
                                    totalMoney += money;
                                    total.setText(String.valueOf(totalMoney));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            num += 1;
                            number.setText(String.valueOf(num));
                            try {
                                obj.remove("goods_number");
                                obj.put("goods_number", String.valueOf(num));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, "亲，库存只有这么多哦", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        flag = true;
                        obj.remove("checked");
                        obj.put("checked", isChecked);
                        double money = obj.getDouble("goods_price") * Integer.parseInt(number.getText().toString());
                        TextView total = (TextView) ((HomeActivity) parent.getContext()).findViewById(R.id.total);
                        if (!isChecked) {
                            CheckBox totalCheck = (CheckBox) ((HomeActivity) parent.getContext()).findViewById(R.id.totalcheckBox);
                            if (totalCheck.isChecked()) {
                                totalCheck.setChecked(false);
                            }
                            double totalMoney = Double.parseDouble(total.getText().toString());
                            totalMoney -= money;
                            total.setText(String.valueOf(totalMoney));
                        } else {
                            CheckBox totalCheck = (CheckBox) ((HomeActivity) parent.getContext()).findViewById(R.id.totalcheckBox);
                            double totalMoney = Double.parseDouble(total.getText().toString());
                            totalMoney += money;
                            if (allChecked()) {
                                totalCheck.setChecked(true);
                            }
                            total.setText(String.valueOf(totalMoney));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            JSONArray attr = obj.getJSONArray("goods_attr");
            String attrStr = "";
            for (int i = 0; i < attr.length(); i++) {
                attrStr += attr.getString(i);
            }
            if (attrStr.length() > 19) {
                attrStr = attrStr.substring(0, 18) + "…";
            }
            ((TextView) convertView.findViewById(R.id.attr)).setText(attrStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public double selectAll() {
        try {
            double total = 0;
            for (int i = 0; i < mData.length(); i++) {
                JSONObject obj = mData.getJSONObject(i);
                total += obj.getDouble("goods_price") * obj.getInt("goods_number");
                obj.remove("checked");
                obj.put("checked", true);
            }
            return total;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean allChecked() {
        try {
            for (int i = 0; i < mData.length(); i++) {
                JSONObject obj = mData.getJSONObject(i);
                if (!obj.getBoolean("checked")) return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void selectNone() {
        try {
            for (int i = 0; i < mData.length(); i++) {
                JSONObject obj = mData.getJSONObject(i);
                obj.remove("checked");
                obj.put("checked", false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getGoods() {
        JSONArray result = new JSONArray();
        try {
            for (int i = 0; i < mData.length(); i++) {
                JSONObject obj = mData.getJSONObject(i);
                if (obj.getBoolean("checked")) {
                    result.put(obj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}