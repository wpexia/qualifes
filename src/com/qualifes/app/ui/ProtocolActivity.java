package com.qualifes.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.qualifes.app.R;

public class ProtocolActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol);
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("http://www.qualifes.com/webview/beta/ios_info/register.html");
    }
}
