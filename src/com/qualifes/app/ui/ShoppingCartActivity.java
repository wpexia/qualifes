package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.qualifes.app.R;
import com.qualifes.app.manager.ShoppingCartManager;
import com.qualifes.app.ui.adapter.ShoppingCartAdapter;
import com.qualifes.app.util.OfflineCartDbHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingCartActivity extends Activity implements View.OnClickListener {
    private ListView listView;
    private SharedPreferences sp;
    ShoppingCartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);
        sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.content);
        init();
    }

    private void init() {
        if (sp.contains("token")) {
            ShoppingCartManager manager = ShoppingCartManager.getInstance();
            manager.getShoppingCart(sp.getString("token", ""), getShoppingCartHandler);
        } else {
            OfflineCartDbHelper dbHelper = new OfflineCartDbHelper(ShoppingCartActivity.this);
            Cursor cursor = dbHelper.select();

            String goodsIds = "";
            while (cursor.moveToNext()) {
                goodsIds += cursor.getString(1) + ",";
            }
            ShoppingCartManager manager = ShoppingCartManager.getInstance();
            manager.getOfflineCart(goodsIds, getOfflineCartHandler);
        }
        ((TextView) ShoppingCartActivity.this.findViewById(R.id.total)).setText("0.00");
        ((CheckBox) ShoppingCartActivity.this.findViewById(R.id.totalcheckBox)).setChecked(false);
        ShoppingCartActivity.this.findViewById(R.id.creatOrder).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.creatOrder: {
                if (sp.contains("token")) {
                    try {
                        JSONArray goods = new JSONArray(adapter.getGoods());
                        if (adapter != null && goods.length() != 0) {
                            Intent intent = new Intent(ShoppingCartActivity.this, OrderConfirmActivity.class);
                            intent.putExtra("goods", adapter.getGoods());
                            startActivity(intent);
                        } else {
                            Toast.makeText(ShoppingCartActivity.this, "请选择结算商品", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.fillInStackTrace();
                    }
                } else {
                    Intent intent = new Intent(ShoppingCartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
            break;
        }
    }

    Handler getOfflineCartHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    OfflineCartDbHelper dbHelper = new OfflineCartDbHelper(ShoppingCartActivity.this);
                    Cursor cursor = dbHelper.select();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        obj.put("checked", false);
                        cursor.moveToNext();
                        obj.remove("goods_number");
                        obj.put("goods_number", cursor.getString(3));
                        obj.remove("goods_attr");
                        JSONArray newarray = new JSONArray();
                        newarray.put(cursor.getString(4));
                        obj.put("goods_attr", newarray);
                    }

                    Log.e("after", data.toString());
                    final ShoppingCartAdapter adapter = new ShoppingCartAdapter(ShoppingCartActivity.this, data);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                    CheckBox checkTotal = (CheckBox) ShoppingCartActivity.this.findViewById(R.id.totalcheckBox);
                    checkTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (adapter.flag) {
                                adapter.flag = false;
                                return;
                            }
                            if (isChecked) {
                                double total = adapter.selectAll();
                                adapter.notifyDataSetChanged();
                                ((TextView) ShoppingCartActivity.this.findViewById(R.id.total)).setText(String.valueOf(total));
                            } else {
                                adapter.selectNone();
                                adapter.notifyDataSetChanged();
                                ((TextView) ShoppingCartActivity.this.findViewById(R.id.total)).setText(String.valueOf("0"));
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                findViewById(R.id.gone).setVisibility(View.VISIBLE);
                findViewById(R.id.page).setVisibility(View.GONE);
                findViewById(R.id.guang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShoppingCartActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                });
                return;
            }
        }
    };


    Handler getShoppingCartHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        obj.put("checked", false);
                    }
                    adapter = new ShoppingCartAdapter(ShoppingCartActivity.this, data);
                    adapter.setIsactivity();
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                    CheckBox checkTotal = (CheckBox) ShoppingCartActivity.this.findViewById(R.id.totalcheckBox);
                    checkTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (adapter.flag) {
                                adapter.flag = false;
                                return;
                            }
                            if (isChecked) {
                                double total = adapter.selectAll();
                                adapter.notifyDataSetChanged();
                                ((TextView) ShoppingCartActivity.this.findViewById(R.id.total)).setText(String.valueOf(total));
                            } else {
                                adapter.selectNone();
                                adapter.notifyDataSetChanged();
                                ((TextView) ShoppingCartActivity.this.findViewById(R.id.total)).setText(String.valueOf("0"));
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                findViewById(R.id.gone).setVisibility(View.VISIBLE);
                findViewById(R.id.page).setVisibility(View.GONE);
                findViewById(R.id.guang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShoppingCartActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                });
                return;
            }
        }
    };

}
