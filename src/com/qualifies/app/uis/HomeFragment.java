package com.qualifies.app.uis;


import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.qualifies.app.R;
import com.qualifies.app.manager.HomeManger;
import com.qualifies.app.uis.adapter.HomeBottomAdapter;
import com.qualifies.app.uis.adapter.HomeGridViewAdapter;
import com.qualifies.app.util.AsyncImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    JSONArray viewFlipperData;
    JSONArray specialMiddleData;
    JSONArray goodsRedData;
    JSONArray goodsBlueData;
    JSONArray goodsBottomData;

    EditText editText;
    ScrollView scrollView;
    ViewFlipper viewFlipper;
    ImageView imageButtonL;
    ImageView imageButtonR;
    GridView gridView1;
    GridView gridView2;
    ListView listView;

    private AsyncImageLoader imageLoader = new AsyncImageLoader();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_activity, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        editText = (EditText) getActivity().findViewById(R.id.input);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        scrollView = (ScrollView) getActivity().findViewById(R.id.scrollView);

        viewFlipper = (ViewFlipper) getActivity().findViewById(R.id.viewFlipper);

        imageButtonL = (ImageView) getActivity().findViewById(R.id.secondPic);
        imageButtonL.setOnClickListener(this);
        imageButtonR = (ImageView) getActivity().findViewById(R.id.thirdPic);
        imageButtonR.setOnClickListener(this);

        gridView1 = (GridView) getActivity().findViewById(R.id.gridView1);
        gridView1.setOnItemClickListener(this);
        gridView2 = (GridView) getActivity().findViewById(R.id.gridView2);
        gridView2.setOnItemClickListener(this);
        listView = (ListView) getActivity().findViewById(R.id.bottom);
        listView.setOnItemClickListener(this);

        HomeManger homeManger = HomeManger.getInstance();
        homeManger.getLogo("home_top_slide", viewFlipperHandler, getActivity());
        homeManger.getLogo("home_special_middle", specialMiddleHandle, getActivity());
        homeManger.getGoodsInfo("home_goods_red", goodsRedHandler, getActivity());
        homeManger.getGoodsInfo("home_goods_blue", goodsBlueHandler, getActivity());
        homeManger.getGoodsInfo("home_goods_bottom", goodsBottomHandler, getActivity());
    }


    Handler viewFlipperHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    if (!data.equals(null)) {
                        viewFlipperData = data;
                    }
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
//                        Log.e("obj", obj.toString());
                        ImageView image = new ImageView(getActivity());
                        Drawable cachedImage = imageLoader.loadDrawable(obj.get("img").toString(), image,
                                new AsyncImageLoader.ImageCallback() {
                                    public void imageLoaded(Drawable imageDrawable,
                                                            ImageView imageView, String imageUrl) {
                                        imageView.setImageDrawable(imageDrawable);
                                    }
                                });
                        if (cachedImage != null) {
                            image.setImageDrawable(cachedImage);
                        }
                        image.setScaleType(ImageView.ScaleType.FIT_XY);
                        viewFlipper.addView(image, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                    viewFlipper.setAutoStart(true);
                    viewFlipper.setFlipInterval(4000);
                    if (viewFlipper.isAutoStart() && !viewFlipper.isFlipping()) {
                        Animation lInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in);
                        Animation lOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_out);

                        viewFlipper.setInAnimation(lInAnim);
                        viewFlipper.setOutAnimation(lOutAnim);
                        viewFlipper.startFlipping();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Handler specialMiddleHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    if (!data.equals(null)) {
                        specialMiddleData = data;
                    }
                    JSONObject buttonL = data.getJSONObject(0);
                    Drawable cachedImageL = imageLoader.loadDrawable(buttonL.get("img").toString(), imageButtonL,
                            new AsyncImageLoader.ImageCallback() {
                                public void imageLoaded(Drawable imageDrawable,
                                                        ImageView imageView, String imageUrl) {
                                    imageView.setImageDrawable(imageDrawable);
                                }
                            });
                    if (cachedImageL != null) {
                        imageButtonL.setImageDrawable(cachedImageL);
                    }

                    JSONObject buttonR = data.getJSONObject(1);
                    Drawable cachedImageR = imageLoader.loadDrawable(buttonR.get("img").toString(), imageButtonR,
                            new AsyncImageLoader.ImageCallback() {
                                public void imageLoaded(Drawable imageDrawable,
                                                        ImageView imageView, String imageUrl) {
                                    imageView.setImageDrawable(imageDrawable);
                                }
                            });
                    if (cachedImageR != null) {
                        imageButtonR.setImageDrawable(cachedImageR);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Handler goodsRedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    if (!data.equals(null)) {
                        goodsRedData = data;
                    }
                    int size = data.length();
                    int itemWidth = 200;
                    int gridViewWidth = size * itemWidth;

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);

                    gridView1.setLayoutParams(params);
                    gridView1.setColumnWidth(itemWidth);
                    gridView1.setStretchMode(GridView.NO_STRETCH);
                    gridView1.setNumColumns(size);
                    gridView1.setAdapter(new HomeGridViewAdapter(getActivity(), goodsRedData));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    Handler goodsBlueHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    if (!data.equals(null)) {
                        goodsBlueData = data;
                    }
                    int size = data.length();
                    int itemWidth = 200;
                    int gridViewWidth = size * itemWidth;

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);

                    gridView2.setLayoutParams(params);
                    gridView2.setColumnWidth(itemWidth);
                    gridView2.setStretchMode(GridView.NO_STRETCH);
                    gridView2.setNumColumns(size);
                    gridView2.setAdapter(new HomeGridViewAdapter(getActivity(), goodsBlueData));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Handler goodsBottomHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    if (!data.equals(null)) {
                        goodsBottomData = data;
                    }
                    HomeBottomAdapter adapter = new HomeBottomAdapter(getActivity(), goodsBottomData);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(1);
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    int totalHeight = 0;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        View listItem = adapter.getView(i, null, listView);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }
                    params.height = totalHeight + listView.getDividerHeight() * (data.length() - 1);
                    listView.setLayoutParams(params);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onClick(View v) {

    }
}
