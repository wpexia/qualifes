package com.qualifies.app.util;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.widget.ImageView;


public class AsyncImageLoader {



    public AsyncImageLoader() {

    }

    public Bitmap loadDrawable(final String imageUrl, final ImageView imageView, final ImageCallback imageCallback, final int size) {
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Bitmap) message.obj, imageView, imageUrl);
            }
        };
        //建立新一个新的线程下载图片
        Thread imageThread =
                new Thread() {
                    @Override
                    public void run() {
                        if (size == 1) {
                            Process.setThreadPriority(Process.THREAD_PRIORITY_DISPLAY);
                        } else {
                            Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
                        }
                        Bitmap preview_bitmap = null;
                        try {
//                            Log.e("imgUrl",imageUrl);
                            InputStream inputStream = new java.net.URL(imageUrl).openStream();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = size;
                            options.inJustDecodeBounds =false;
                            options.inPreferredConfig = Bitmap.Config.RGB_565;
                            options.inPurgeable = true;
                            options.inInputShareable = true;
                            preview_bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                            inputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message message = handler.obtainMessage(0, preview_bitmap );
                        handler.sendMessage(message);
                    }
                };
        imageThread.start();
        return null;
    }

    public Bitmap loadDrawable(final String imageUrl, final ImageView imageView, final ImageCallback imageCallback) {
        return loadDrawable(imageUrl, imageView, imageCallback, 1);
    }

    //回调接口
    public interface ImageCallback {
        public void imageLoaded(Bitmap imageDrawable, ImageView imageView, String imageUrl);
    }
}
