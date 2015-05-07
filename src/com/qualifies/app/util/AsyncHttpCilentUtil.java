package com.qualifies.app.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

public class AsyncHttpCilentUtil {
    private static AsyncHttpClient client = null;

    public synchronized static AsyncHttpClient getInstence(){
        if(client ==null){
            client = new SyncHttpClient();
            client.setTimeout(1000*10);
        }
        return client;
    }
}
