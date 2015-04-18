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
	
	public final static String getVisualURL() {
		return baseURL + visualURL;
	}
	public final static String getSearchURL() {
		return baseURL + searchURL;
	}
	
	
	/** 
     * ��ȡ����ͼƬ��Դ  
     * @param url 
     * @return Bitmap 
     */  
    public static Bitmap getHttpBitmap(String url){  
        URL myFileURL;  
        Bitmap bitmap=null;  
        try{  
            myFileURL = new URL(url);  
            //�������  
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();  
            //���ó�ʱʱ��Ϊ6000���룬conn.setConnectionTiem(0);��ʾû��ʱ������  
            conn.setConnectTimeout(6000);  
            //�������û��������  
            conn.setDoInput(true);  
            //��ʹ�û���  
            conn.setUseCaches(false);  
            //�����п��ޣ�û��Ӱ��  
            //conn.connect();  
            //�õ�������  
            InputStream is = conn.getInputStream();  
            //�����õ�ͼƬ  
            bitmap = BitmapFactory.decodeStream(is);  
            //�ر�������  
            is.close();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
        return bitmap;  
    }  
    
}
