package com.qualifes.app.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.qualifes.app.R;
import com.qualifes.app.manager.HomeManger;
import com.qualifes.app.ui.GoodDetailActivity;
import com.qualifes.app.ui.HomeActivity;
import com.qualifes.app.ui.WebActivity;
import com.qualifes.app.ui.adapter.HomeBottomAdapter;
import com.qualifes.app.ui.adapter.HomeGridViewAdapter;
import com.qualifes.app.util.AsyncImageLoader;
import com.qualifes.app.util.DisplayParams;
import com.qualifes.app.util.DisplayUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, GestureDetector.OnGestureListener, View.OnTouchListener {

    private JSONArray viewFlipperData;
    private JSONArray specialMiddleData;
    private JSONArray goodsRedData;
    private JSONArray goodsBlueData;
    private JSONArray goodsBottomData;

    private EditText editText;
    private ScrollView scrollView;
    private ViewFlipper viewFlipper;
    private ImageView imageButtonL;
    private ImageView imageButtonR;
    private GridView gridView1;
    private GridView gridView2;
    private ListView listView;

    private GestureDetector gestureDetector = null;


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

    @Override
    public void onResume() {
        super.onResume();
        HomeManger homeManger = HomeManger.getInstance();
        homeManger.getGoodsInfo("home_goods_bottom", goodsBottomHandler, getActivity());
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
        ViewGroup.LayoutParams params = viewFlipper.getLayoutParams();
        DisplayParams params1 = DisplayParams.getInstance(getActivity());
        params.height = params1.screenWidth / 2;
        viewFlipper.setLayoutParams(params);

        imageButtonL = (ImageView) getActivity().findViewById(R.id.secondPic);
        imageButtonL.setOnClickListener(this);
        imageButtonR = (ImageView) getActivity().findViewById(R.id.thirdPic);
        imageButtonR.setOnClickListener(this);
        params = imageButtonL.getLayoutParams();
        params.height = ((params1.screenWidth /2) / 16) * 9;
        imageButtonL.setLayoutParams(params);
        imageButtonR.setLayoutParams(params);


        gridView1 = (GridView) getActivity().findViewById(R.id.gridView1);
        gridView1.setOnItemClickListener(this);
        gridView2 = (GridView) getActivity().findViewById(R.id.gridView2);
        gridView2.setOnItemClickListener(this);
        listView = (ListView) getActivity().findViewById(R.id.bottom);
        listView.setOnItemClickListener(this);


        editText = (EditText) getActivity().findViewById(R.id.input);
        editText.setOnTouchListener((HomeActivity) getActivity());

        gestureDetector = new GestureDetector(getActivity(), this);
        viewFlipper.setOnTouchListener(this);

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
                        final JSONObject obj = data.getJSONObject(i);
//                        Log.e("obj", obj.toString());
                        ImageView image = new ImageView(getActivity());
                        image.setClickable(true);
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), WebActivity.class);
                                try {
                                    intent.putExtra("url", obj.getString("url"));
                                    intent.putExtra("title", obj.getString("name"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Bitmap cachedImage = imageLoader.loadDrawable(obj.get("img").toString(), image,
                                new AsyncImageLoader.ImageCallback() {
                                    public void imageLoaded(Bitmap imageDrawable,
                                                            ImageView imageView, String imageUrl) {
                                        imageView.setImageBitmap(imageDrawable);
                                    }
                                }, 1, -19);
                        if (cachedImage != null) {
                            image.setImageBitmap(cachedImage);
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
                    final JSONObject buttonL = data.getJSONObject(0);
                    Bitmap cachedImageL = imageLoader.loadDrawable(buttonL.get("img").toString(), imageButtonL,
                            new AsyncImageLoader.ImageCallback() {
                                public void imageLoaded(Bitmap imageDrawable,
                                                        ImageView imageView, String imageUrl) {
                                    imageView.setImageBitmap(imageDrawable);
                                }
                            }, 1, -16);
                    if (cachedImageL != null) {
                        imageButtonL.setImageBitmap(cachedImageL);
                    }
                    imageButtonL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            try {
                                intent.putExtra("url", buttonL.getString("url"));
                                intent.putExtra("title", buttonL.getString("name"));
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
//                    ImageCacheHelper.getImageSdCache().get(buttonL.get("img").toString(), imageButtonL);
                    final JSONObject buttonR = data.getJSONObject(1);
                    Bitmap cachedImageR = imageLoader.loadDrawable(buttonR.get("img").toString(), imageButtonR,
                            new AsyncImageLoader.ImageCallback() {
                                public void imageLoaded(Bitmap imageDrawable,
                                                        ImageView imageView, String imageUrl) {
                                    imageView.setImageBitmap(imageDrawable);
                                }
                            }, 1, -16);
                    if (cachedImageR != null) {
                        imageButtonR.setImageBitmap(cachedImageR);
                    }
                    imageButtonR.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            try {
                                intent.putExtra("url", buttonR.getString("url"));
                                intent.putExtra("title", buttonR.getString("name"));
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
//                    ImageCacheHelper.getImageSdCache().get(buttonR.get("img").toString(), imageButtonR);
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
                    DisplayParams params1 = DisplayParams.getInstance(getActivity());
                    int itemWidth = DisplayUtil.dip2px(100, params1.scale);
                    int gridViewWidth = size * itemWidth;

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);

                    gridView1.setLayoutParams(params);
                    gridView1.setColumnWidth(itemWidth);
                    gridView1.setStretchMode(GridView.NO_STRETCH);
                    gridView1.setNumColumns(size);

                    gridView1.setHorizontalScrollBarEnabled(false);
                    gridView2.setVerticalScrollBarEnabled(false);
                    gridView1.setAdapter(new HomeGridViewAdapter(getActivity().getApplicationContext(), goodsRedData));
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
                    DisplayParams params1 = DisplayParams.getInstance(getActivity());
                    int itemWidth = DisplayUtil.dip2px(100, params1.scale);
                    int gridViewWidth = size * itemWidth;

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);

                    gridView2.setLayoutParams(params);
                    gridView2.setColumnWidth(itemWidth);
                    gridView2.setStretchMode(GridView.NO_STRETCH);
                    gridView2.setNumColumns(size);
                    gridView2.setHorizontalScrollBarEnabled(false);
                    gridView2.setVerticalScrollBarEnabled(false);
                    gridView2.setAdapter(new HomeGridViewAdapter(getActivity().getApplicationContext(), goodsBlueData));
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
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    int count = adapter.getCount();
                    listView.setDividerHeight(0);
                    DisplayParams params1 = DisplayParams.getInstance(getActivity());
                    params.height = count * DisplayUtil.dip2px(100, params1.scale);
                    listView.setLayoutParams(params);
                    listView.setOnItemClickListener(HomeFragment.this);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int id = v.getId();
        switch (id) {
            case R.id.viewFlipper: {
                viewFlipper.stopFlipping();                // 点击事件后，停止自动播放
                viewFlipper.setAutoStart(false);
                return gestureDetector.onTouchEvent(event);
            }
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), GoodDetailActivity.class);
        Bundle bundle = new Bundle();

        switch (parent.getId()) {
            case R.id.gridView1: {
                try {
                    JSONObject obj = goodsRedData.getJSONObject(position);
                    bundle.putInt("goods_id", obj.getInt("goods_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            case R.id.gridView2: {
                try {
                    JSONObject obj = goodsBlueData.getJSONObject(position);
                    bundle.putInt("goods_id", obj.getInt("goods_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            case R.id.bottom: {
                try {
                    JSONObject obj = goodsBottomData.getJSONObject(position);
                    bundle.putInt("goods_id", obj.getInt("goods_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            default:
                break;
        }

        intent.putExtra("goods_id", bundle);
        startActivity(intent);
    }

    public boolean onTouchEvent(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        DisplayParams params = DisplayParams.getInstance(getActivity().getApplicationContext());
        if (e1.getY() > DisplayUtil.dip2px(210, params.scale)) {
//            Log.e("y", String.valueOf(e1.getY()));
//            Log.e("px", String.valueOf(DisplayUtil.px2dip(210, params.scale)));
            return false;
        }
        if (e2.getX() - e1.getX() > DisplayUtil.dip2px(100, params.scale)) {             // 从左向右滑动（左进右出）
            viewFlipper.stopFlipping();                // 点击事件后，停止自动播放
            viewFlipper.setAutoStart(false);
            Animation rInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_in);    // 向右滑动左侧进入的渐变效果（alpha  0.1 -> 1.0）
            Animation rOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            viewFlipper.setInAnimation(rInAnim);
            viewFlipper.setOutAnimation(rOutAnim);
            viewFlipper.showPrevious();
            return true;
        } else if (e2.getX() - e1.getX() < -DisplayUtil.dip2px(100, params.scale)) {         // 从右向左滑动（右进左出）
            viewFlipper.stopFlipping();                // 点击事件后，停止自动播放
            viewFlipper.setAutoStart(false);
            Animation lInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in);        // 向左滑动左侧进入的渐变效果（alpha 0.1  -> 1.0）
            Animation lOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_out);    // 向左滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            viewFlipper.setInAnimation(lInAnim);
            viewFlipper.setOutAnimation(lOutAnim);
            viewFlipper.showNext();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
