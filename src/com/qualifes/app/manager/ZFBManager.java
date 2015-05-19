package com.qualifes.app.manager;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import com.alipay.sdk.app.PayTask;

public class ZFBManager {

    public static void pay(String params, Activity activity, Handler handler) {
        Thread payThread = new Thread(new PayThread(params, activity, handler));
        payThread.start();
    }

    private static class PayThread implements Runnable {
        String params;
        Activity activity;
        Handler handler;

        public PayThread(String params, Activity activity, Handler handler) {
            this.params = params;
            this.activity = activity;
            this.handler = handler;
        }

        @Override
        public void run() {
            PayTask task = new PayTask(activity);
            String result = task.pay(params);
            Message msg = handler.obtainMessage();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    }
}
