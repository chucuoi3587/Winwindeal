package vn.winwindeal.android.app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.FontUtil;

/**
 * Created by nhannguyen on 4/12/2018.
 */

public class AboutUsActivity extends BaseActivity {

    Toolbar toolbar;
    private WebView mWebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_activity);
        initComponents();
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SpannableString ss = new SpannableString(getResources().getString(R.string.about_us));
        ss.setSpan(new RelativeSizeSpan(0.85f), 0, ss.length(), 0);
        ss.setSpan(FontUtil.getFontAssets(this, FontUtil.ROBOTO_MEDIUM), 0, ss.length(), 0);
        setTitle(ss);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWebview = (WebView) findViewById(R.id.webview);
        mWebview.setPadding(0, 0, 0, 0);
        mWebview.getSettings().setJavaScriptEnabled(true);
//        mWebview.getSettings().setSupportZoom(true);
//        mWebview.getSettings().setBuiltInZoomControls(true);
//        mWebview.getSettings().setDisplayZoomControls(false);
        mWebview.setInitialScale(30);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);

        mWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 19) {
            mWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebview.loadUrl("file:///android_asset/about_us.html");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
