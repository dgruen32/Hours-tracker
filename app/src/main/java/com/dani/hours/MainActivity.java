package com.dani.hours;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends Activity {
    private WebView web;

    /** Saves entries in the app's private storage so they survive restarts and updates. */
    public static class Store {
        private final SharedPreferences prefs;
        Store(SharedPreferences prefs) { this.prefs = prefs; }

        @JavascriptInterface
        public String load() { return prefs.getString("entries", "{}"); }

        @JavascriptInterface
        public void save(String json) { prefs.edit().putString("entries", json).apply(); }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        web.setBackgroundColor(Color.TRANSPARENT);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        web.addJavascriptInterface(new Store(getSharedPreferences("hours", MODE_PRIVATE)), "AndroidStore");
        setContentView(web);
        if (savedInstanceState != null) web.restoreState(savedInstanceState);
        else web.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        web.saveState(out);
    }
}
