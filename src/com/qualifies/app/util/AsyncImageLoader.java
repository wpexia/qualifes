package com.qualifies.app.util;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;


public class AsyncImageLoader {

    //SoftReference是软引用，是为了更好的为了系统回收变量
    private static HashMap<String, SoftReference<Drawable>> imageCache;

    static {
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }


    public AsyncImageLoader() {

    }

    public Drawable loadDrawable(final String imageUrl,final ImageView imageView, final ImageCallback imageCallback,final int size){
        if (imageCache.containsKey(imageUrl)) {
            //从缓存中获取
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            Drawable drawable = softReference.get();
            if (drawable != null) {
                return drawable;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, imageView,imageUrl);
            }
        };
        //建立新一个新的线程下载图片
        new Thread() {
            @Override
            public void run() {
                Drawable drawable = null;
                try {
                    InputStream inputStream = new java.net.URL(imageUrl).openStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = size;
                    Bitmap preview_bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    drawable = new BitmapDrawable(preview_bitmap);
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                Message message = handler.obtainMessage(0, drawable);
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }

    public Drawable loadDrawable(final String imageUrl,final ImageView imageView, final ImageCallback imageCallback){
        return loadDrawable(imageUrl,imageView,imageCallback,1);
    }
    //回调接口
    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl);
    }
}
