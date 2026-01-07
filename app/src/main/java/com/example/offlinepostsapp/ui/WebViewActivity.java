package com.example.offlinepostsapp.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinepostsapp.R;
import com.example.offlinepostsapp.data.AppPrefs;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String url = getIntent().getStringExtra("url");
        if (url == null) url = "https://jsonplaceholder.typicode.com/";

        ProgressBar pb = findViewById(R.id.progress);
        WebView wv = findViewById(R.id.webView);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                pb.setVisibility(View.VISIBLE);
            }
            @Override public void onPageFinished(WebView view, String url) {
                pb.setVisibility(View.GONE);
            }
        });

        wv.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        WebView wv = findViewById(R.id.webView);
        if (wv.canGoBack()) wv.goBack();
        else super.onBackPressed();
    }

    private void applySavedTheme() {
        String t = AppPrefs.getTheme(this);
        if (AppPrefs.THEME_DARK.equals(t)) setTheme(R.style.Theme_OfflinePosts_Dark);
        else if (AppPrefs.THEME_BLUE.equals(t)) setTheme(R.style.Theme_OfflinePosts_Blue);
        else setTheme(R.style.Theme_OfflinePosts_Light);
    }
}
