package com.qualifies.app.uis.personal;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.qualifies.app.R;
import com.qualifies.app.manager.PositionManager;
import com.qualifies.app.util.PlistHelper;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class AddPositionActivity extends Activity implements View.OnClickListener {


    private Dialog d;
    private SharedPreferences sp;
    private TextView position;
    private Position result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addposition);
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("user", MODE_PRIVATE);

        findViewById(R.id.choose).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.savedefault).setOnClickListener(this);
        position = (TextView) findViewById(R.id.icon_position);


        final PlistHelper plistHelper = PlistHelper.getInstance(this);
        String[] provinces = plistHelper.getProvince();
        d = new Dialog(AddPositionActivity.this);
        d.setTitle("选择地址");
        d.setContentView(R.layout.area_pick);
        final WheelView city = (WheelView) d.findViewById(R.id.city);
        final WheelView province = (WheelView) d.findViewById(R.id.province);
        final WheelView town = (WheelView) d.findViewById(R.id.town);

        ArrayWheelAdapter<String> adapterp = new ArrayWheelAdapter<String>(d.getContext(), provinces);
        adapterp.setTextSize(18);
        province.setViewAdapter(adapterp);
        province.setCurrentItem(0);
        province.setDrawShadows(false);
        province.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int provinceId = province.getCurrentItem();
                String[] citys = plistHelper.getCity(provinceId);
                ArrayWheelAdapter<String> adapterc = new ArrayWheelAdapter<String>(d.getContext(), citys);
                adapterc.setTextSize(18);
                city.setViewAdapter(adapterc);
                city.setCurrentItem(0);

                String[] towns = plistHelper.getTown(provinceId, 0);
                ArrayWheelAdapter<String> adaptert = new ArrayWheelAdapter<String>(d.getContext(), towns);
                adaptert.setTextSize(18);
                town.setViewAdapter(adaptert);
                town.setCurrentItem(0);
            }
        });


        String[] cities = plistHelper.getCity(0);
        ArrayWheelAdapter<String> adapterc = new ArrayWheelAdapter<String>(d.getContext(), cities);
        adapterc.setTextSize(18);
        city.setViewAdapter(adapterc);
        city.setCurrentItem(0);
        city.setDrawShadows(false);

        city.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int provinceId = province.getCurrentItem();
                int cityId = city.getCurrentItem();

                String[] towns = plistHelper.getTown(provinceId, cityId);
                ArrayWheelAdapter<String> adaptert = new ArrayWheelAdapter<String>(d.getContext(), towns);
                adaptert.setTextSize(18);
                town.setViewAdapter(adaptert);
                town.setCurrentItem(0);
            }
        });


        String[] towns = plistHelper.getTown(0, 0);
        ArrayWheelAdapter<String> adaptert = new ArrayWheelAdapter<String>(d.getContext(), towns);
        adaptert.setTextSize(18);
        town.setViewAdapter(adaptert);
        town.setCurrentItem(0);
        town.setDrawShadows(false);


        Button confirm = (Button) d.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String positionStr = plistHelper.getPosition(province.getCurrentItem(), city.getCurrentItem(), town.getCurrentItem());
                result = new Position(province.getCurrentItem(), city.getCurrentItem(), town.getCurrentItem(), plistHelper);
                position.setText(positionStr);
                d.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.choose: {
                d.show();
            }
            break;
            case R.id.save: {
                save(false);
            }
            break;
            case R.id.savedefault: {
                save(true);
            }
        }
    }


    private void save(boolean def) {
        String consignee = ((EditText) findViewById(R.id.consignee)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        String address = ((EditText) findViewById(R.id.position)).getText().toString();
        String tel = ((EditText) findViewById(R.id.tel)).getText().toString();
        String token = sp.getString("token", "");
        String defa = def ? "1" : "0";

        if (consignee.trim().equals("")) {
            Toast.makeText(this, "收货人姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.trim().equals("")) {
            Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (result == null) {
            Toast.makeText(this, "请选择省、市、区", Toast.LENGTH_SHORT).show();
            return;
        }
        String province = result.getProvince();
        String city = result.getCity();
        String district = result.getTown();

        if (address.trim().equals("")) {
            Toast.makeText(this, "详细地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        PositionManager positionManager = PositionManager.getInstance();
        positionManager.addPosition(token, consignee, phone, province, city, district, tel, defa, address, addPositionHandler, this);

    }


    Handler addPositionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(AddPositionActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            if (msg.what == 0) {
                finish();
            }
        }
    };

    class Position {
        int province;
        int city;
        int town;
        PlistHelper plistHelper;

        public Position(int province, int city, int town, PlistHelper plistHelper) {
            this.province = province;
            this.city = city;
            this.town = town;
            this.plistHelper = plistHelper;
        }


        public String getProvince() {
            return plistHelper.getProvinceId(province);
        }

        public String getCity() {
            return plistHelper.getCityId(province, city);
        }

        public String getTown() {
            return plistHelper.getTownId(province, city, town);
        }
    }

}
