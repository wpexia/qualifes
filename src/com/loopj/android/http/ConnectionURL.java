package com.loopj.android.http;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ConnectionURL {
	public final static String baseURL = "http://www.qualifes.com:80/app_api/v_1.03/api.php?service=";
	private static String visualURL = "get_visual";
	private static String searchURL = "get_so";
	private static String goodsInfo = "get_goods";
	private static String hotGoods = "get_hot";
	
	public final static String getVisualURL() {
		return baseURL + visualURL;
	}
	public final static String getSearchURL() {
		return baseURL + searchURL;
	}
	public final static String getGoodsInfoURL(){
		return baseURL + goodsInfo;
	}
	public final static String getHotGoods(){
		return baseURL + hotGoods;
	}
	/** 
     * 获取网落图片资源  
     * @param url 
     * @return Bitmap 
     */   
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(bitmap);
        return bitmap;
    }
}
