package com.qualifies.app.ui.fragment;


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
import com.qualifies.app.R;
import com.qualifies.app.manager.HomeManger;
import com.qualifies.app.ui.GoodDetailActivity;
import com.qualifies.app.ui.HomeActivity;
import com.qualifies.app.ui.adapter.HomeBottomAdapter;
import com.qualifies.app.ui.adapter.HomeGridViewAdapter;
import com.qualifies.app.util.AsyncImageLoader;
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
    public void onStart() {
        super.onStart();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
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
                        JSONObject obj = data.getJSONObject(i);
//                        Log.e("obj", obj.toString());
                        ImageView image = new ImageView(getActivity());
                        Bitmap cachedImage = imageLoader.loadDrawable(obj.get("img").toString(), image,
                                new AsyncImageLoader.ImageCallback() {
                                    public void imageLoaded(Bitmap imageDrawable,
                                                            ImageView imageView, String imageUrl) {
                                        imageView.setImageBitmap(imageDrawable);
                                    }
                                }, 1);
                        if (cachedImage != null) {
                            image.setImageBitmap(cachedImage);
                        }
//                        ImageCacheHelper.getImageSdCache().get(obj.get("img").toString(), image);
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
                    Bitmap cachedImageL = imageLoader.loadDrawable(buttonL.get("img").toString(), imageButtonL,
                            new AsyncImageLoader.ImageCallback() {
                                public void imageLoaded(Bitmap imageDrawable,
                                                        ImageView imageView, String imageUrl) {
                                    imageView.setImageBitmap(imageDrawable);
                                }
                            }, 2);
                    if (cachedImageL != null) {
                        imageButtonL.setImageBitmap(cachedImageL);
                    }
//                    ImageCacheHelper.getImageSdCache().get(buttonL.get("img").toString(), imageButtonL);
                    JSONObject buttonR = data.getJSONObject(1);
                    Bitmap cachedImageR = imageLoader.loadDrawable(buttonR.get("img").toString(), imageButtonR,
                            new AsyncImageLoader.ImageCallback() {
                                public void imageLoaded(Bitmap imageDrawable,
                                                        ImageView imageView, String imageUrl) {
                                    imageView.setImageBitmap(imageDrawable);
                                }
                            }, 2);
                    if (cachedImageR != null) {
                        imageButtonR.setImageBitmap(cachedImageR);
                    }
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
                    View listItem = adapter.getView(0, null, listView);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight() * adapter.getCount();
                    params.height = totalHeight + listView.getDividerHeight() * (data.length() - 1);
                    listView.setLayoutParams(params);
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
        if (e2.getX() - e1.getX() > 500) {             // 从左向右滑动（左进右出）
            viewFlipper.stopFlipping();                // 点击事件后，停止自动播放
            viewFlipper.setAutoStart(false);
            Animation rInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_in);    // 向右滑动左侧进入的渐变效果（alpha  0.1 -> 1.0）
            Animation rOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            viewFlipper.setInAnimation(rInAnim);
            viewFlipper.setOutAnimation(rOutAnim);
            viewFlipper.showPrevious();
            return true;
        } else if (e2.getX() - e1.getX() < -500) {         // 从右向左滑动（右进左出）
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
