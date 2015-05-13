package com.qualifies.app.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import cn.trinea.android.common.entity.FailedReason;
import cn.trinea.android.common.service.CacheFullRemoveType;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.service.impl.ImageMemoryCache;
import cn.trinea.android.common.service.impl.ImageSDCardCache;
import cn.trinea.android.common.service.impl.RemoveTypeUsedCountSmall;
import cn.trinea.android.common.util.CacheManager;

public class ImageCacheHelper {
    private static final ImageCache IMAGE_CACHE = new ImageCache(256, 1024);
    private static final ImageSDCardCache IMAGE_SD_CACHE = CacheManager.getImageSDCardCache();

    static {
        IMAGE_CACHE.setOpenWaitingQueue(true);
        IMAGE_CACHE.setCacheFullRemoveType(new RemoveTypeUsedCountSmall<Bitmap>());


        ImageMemoryCache.OnImageCallbackListener imageCallBack = new ImageMemoryCache.OnImageCallbackListener() {

            private static final long serialVersionUID = 1L;

            // callback function before get image, run on ui thread
            @Override
            public void onPreGet(String imageUrl, View view) {
                // Log.e(TAG_CACHE, "pre get image");
            }

            // callback function after get image successfully, run on ui thread
            @Override
            public void onGetSuccess(String imageUrl, Bitmap loadedImage, View view, boolean isInCache) {
                // can be another view child, like textView and so on
                if (view != null && loadedImage != null && view instanceof ImageView) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageBitmap(loadedImage);
                }
            }

            // callback function after get image failed, run on ui thread
            @Override
            public void onGetFailed(String imageUrl, Bitmap loadedImage, View view, FailedReason failedReason) {
                Log.e("TAG_CACHE", new StringBuilder(128).append("get image ").append(imageUrl).append(" error")
                        .toString());
            }

            @Override
            public void onGetNotInCache(String imageUrl, View view) {

            }
        };
        IMAGE_CACHE.setOnImageCallbackListener(imageCallBack);
    }

    public static ImageSDCardCache getImageSdCache() {
        return IMAGE_SD_CACHE;
    }

    public static ImageCache getImageCache() {
        return IMAGE_CACHE;
    }
}
