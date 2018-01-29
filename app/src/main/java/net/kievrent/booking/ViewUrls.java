package net.kievrent.booking;
/*Developed by Sheriff © */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;


public class ViewUrls extends Activity {
    private MediaPlayer WarningSoundButton;
    private WebView KievRentWebView;
    private FrameLayout KievRentWebViewPlaceholder;

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewurls);
        WarningSoundButton = MediaPlayer.create(ViewUrls.this, R.raw.button);
        initUI();
    }

    private void initUI() {
        KievRentWebViewPlaceholder = ((FrameLayout) findViewById(R.id.KievRentWebViewPlaceholder));
        if (KievRentWebView == null) {
            KievRentWebView = new WebView(this);
            KievRentWebView.setWebViewClient(new KievRentWebViewClient());
            KievRentWebView.setWebViewClient(new Callback());
            KievRentWebView.getSettings().setUseWideViewPort(true);
            KievRentWebView.getSettings().setLoadWithOverviewMode(true);
            KievRentWebView.getSettings().setBuiltInZoomControls(true);
            KievRentWebView.getSettings().setSupportZoom(true);
            KievRentWebView.setPadding(0, 0, 0, 0);
            KievRentWebView.getSettings().setDomStorageEnabled(true);
            Uri urlMain = getIntent().getData();
            KievRentWebView.loadUrl(urlMain.toString());
            String urlCheck = KievRentWebView.getUrl();
            MediaPlayer warningSoundGood = MediaPlayer.create(ViewUrls.this, R.raw.good);
            warningSoundGood.start();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.url) + "\n" + urlCheck, Toast.LENGTH_LONG).show();
        }
        KievRentWebViewPlaceholder.addView(KievRentWebView);
    }

    @Override
    public void onBackPressed() {
        WarningSoundButton.start();
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (KievRentWebView != null) {
            KievRentWebViewPlaceholder.removeView(KievRentWebView);
        }
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_viewurls);
        initUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        KievRentWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        KievRentWebView.restoreState(savedInstanceState);
    }

    private class Callback extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading
                (WebView view, String url) {
            return (false);
        }
    }

    private class KievRentWebViewClient extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView viewBrowser, String url) {
            if (Uri.parse(url).getHost().endsWith("apartment.kiev.ua")) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            viewBrowser.getContext().startActivity(intent);
            return true;
        }
    }
}




