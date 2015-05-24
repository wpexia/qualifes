package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.qualifes.app.R;
import com.qualifes.app.manager.PositionManager;
import com.qualifes.app.ui.adapter.PositionChoseAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ChoosePositionActivity extends Activity implements View.OnClickListener {


    private SharedPreferences sp;
    private ListView listView;
    private JSONObject defaultPo;
    PositionChoseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.position_chose);
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("user", MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.listView);

        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
        getPos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPos();
    }

    private void getPos() {
        PositionManager positionManager = PositionManager.getInstance();
        positionManager.getPosition(sp.getString("token", ""), getPosition, ChoosePositionActivity.this);
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.back_button: {
                finish();
            }
            break;
            case R.id.finish: {
                Intent intent = new Intent();
                if (adapter.getChecked() != -1) {
                    intent.putExtra("position", (adapter.getPosition().toString()));
                } else if (defaultPo != null) {
                    intent.putExtra("position", (defaultPo.toString()));
                } else {
                    Toast toast = Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                this.setResult(1, intent);
                finish();
            }
            break;
            case R.id.add: {
                Intent intent = new Intent(ChoosePositionActivity.this, AddPositionActivity.class);
                startActivity(intent);
            }
        }
    }

    Handler getPosition = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    JSONArray noDefault = new JSONArray();
                    for (int i = 0; i < data.length(); i++) {
                        final JSONObject obj = data.getJSONObject(i);
                        if (obj.getInt("is_default") == 0) {
                            noDefault.put(obj);
                        } else {
                            defaultPo = obj;
                            ((RadioButton) findViewById(R.id.positionradio)).setChecked(true);
                            ((TextView) findViewById(R.id.defaultname)).setText(obj.getString("consignee"));
                            ((TextView) findViewById(R.id.defaultphone)).setText(obj.getString("mobile"));
                            ((TextView) findViewById(R.id.defaultaddress)).setText(obj.getString("province_name") + "省" + obj.getString("city_name") + "市" + obj.getString("district_name") + obj.getString("address"));
                            findViewById(R.id.defaultposition).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChoosePositionActivity.this, AddPositionActivity.class);
                                    intent.putExtra("position", obj.toString());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                    adapter = new PositionChoseAdapter();
                    adapter.setContent(noDefault, ChoosePositionActivity.this);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                findViewById(R.id.okposition).setVisibility(View.GONE);
                findViewById(R.id.noposition).setVisibility(View.VISIBLE);
            }
        }
    };

}
