package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.qualifes.app.R;
import com.qualifes.app.manager.ShoppingCartManager;
import com.qualifes.app.ui.adapter.ShoppingCartAdapter;
import com.qualifes.app.util.OfflineCartDbHelper;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingCartActivity extends Activity implements View.OnClickListener {
    private ListView listView;
    private SharedPreferences sp;
    ShoppingCartAdapter adapter;
    int[] dbIds = new int[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);
        sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.content);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        init();
    }

    private void init() {
        sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        if (sp.contains("token")) {
            ShoppingCartManager manager = ShoppingCartManager.getInstance();
            manager.getShoppingCart(sp.getString("token", ""), getShoppingCartHandler);
        } else {
            OfflineCartDbHelper dbHelper = new OfflineCartDbHelper(ShoppingCartActivity.this);
            Cursor cursor = dbHelper.select();

            String[] goodsIds = new String[100];
            String[] goodsAttrs = new String[100];
            int i = 0;
            while (cursor.moveToNext()) {
                goodsIds[i] = cursor.getString(1);
                goodsAttrs[i] = cursor.getString(2);
                dbIds[i] = cursor.getInt(0);
                i++;
            }
            ShoppingCartManager manager = ShoppingCartManager.getInstance();
            manager.getOfflineCart(goodsIds, goodsAttrs, i, getOfflineCartHandler);
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
    public void onContentChanged() {
        init();
    }

    private void createSwipeMenu() {
        listView.setOnTouchListener(new SwipeGestureListener(ShoppingCartActivity.this));
    }

    class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener implements
            View.OnTouchListener {
        Context context;
        GestureDetector gDetector;
        static final int SWIPE_MIN_DISTANCE = 120;
        static final int SWIPE_MAX_OFF_PATH = 250;
        static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public SwipeGestureListener() {
            super();
        }

        public SwipeGestureListener(Context context) {
            this(context, null);
        }

        public SwipeGestureListener(Context context, GestureDetector gDetector) {

            if (gDetector == null)
                gDetector = new GestureDetector(context, this);

            this.context = context;
            this.gDetector = gDetector;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            final int position = listView.pointToPosition(
                    Math.round(e1.getX()), Math.round(e1.getY()));
            if (Math.abs(e1.getY() - e2.getY()) < SWIPE_MAX_OFF_PATH) {
                if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                    return false;
                }
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                    JSONObject obj = (JSONObject) adapter.getItem(position);
                    obj.remove("delete");
                    try {
                        obj.put("delete", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int position = listView.pointToPosition(
                    Math.round(event.getX()), Math.round(event.getY()));
            JSONObject obj = (JSONObject) adapter.getItem(position);
            try {
                if (obj.getBoolean("delete")) {
                    obj.remove("delete");
                    obj.put("delete", false);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return gDetector.onTouchEvent(event);
        }

        public GestureDetector getDetector() {
            return gDetector;
        }

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
                    JSONArray arry = new JSONArray();
                    try {
                        JSONArray data = (JSONArray) msg.obj;
                        OfflineCartDbHelper dbHelper = new OfflineCartDbHelper(ShoppingCartActivity.this);
                        Cursor cursor = dbHelper.select();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            obj.put("checked", false);
                            obj.put("delete", false);
                            obj.put("dbid", dbIds[i]);
                            cursor.moveToNext();
                            obj.remove("goods_number");
                            obj.put("goods_number", cursor.getString(3));
                            obj.remove("goods_attr");
                            arry.put(obj);
                        }
                    } catch (ClassCastException e) {
                        JSONObject data = (JSONObject) msg.obj;
                        OfflineCartDbHelper dbHelper = new OfflineCartDbHelper(ShoppingCartActivity.this);
                        Cursor cursor = dbHelper.select();
                        for (int i = 0; i < 100; i++) {
                            if (data.has(String.valueOf(i))) {
                                JSONObject obj = data.getJSONObject(String.valueOf(i));
                                obj.put("checked", false);
                                obj.put("delete", false);
                                obj.put("dbid", dbIds[i]);
                                cursor.moveToNext();
                                obj.remove("goods_number");
                                obj.put("goods_number", cursor.getString(3));
                                obj.remove("goods_attr");
                                arry.put(obj);
                            }
                        }
                    }
                    adapter = new ShoppingCartAdapter(ShoppingCartActivity.this, arry);
                    adapter.setIsactivity();
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                    createSwipeMenu();
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
                        obj.put("delete", false);
                    }
                    adapter = new ShoppingCartAdapter(ShoppingCartActivity.this, data);
                    adapter.setIsactivity();
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                    createSwipeMenu();
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


    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
