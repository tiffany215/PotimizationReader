package shuhai.readercore.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import shuhai.readercore.R;

/**
 * @author 55345364
 * @date 2017/8/7.
 */
@SuppressLint({ "SetJavaScriptEnabled", "Instantiatable", "NewApi" })
public abstract class BaseWBActivity {


    public Context mContext;
    public View layout;
    public Handler myHandler;
    public WebView mWebView;
    private WebSettings webSettings;
    private String URL;
    private LayoutInflater mLayoutInflater;

    @SuppressLint({ "Instantiatable", "InflateParams" })
    public BaseWBActivity(Context context,String url) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        layout = mLayoutInflater.inflate(getLayoutId(),null);
        this.URL = url;
        myHandler = new Handler();
        initView();
    }


    public abstract int getLayoutId();

    private void initView() {
        mWebView = layout.findViewById(R.id.web_view);
        webSetting();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyChromeClient());
        loadWeb();
    }

    public abstract void addJavaScriptInterface();


    private void webSetting() {
        webSettings = mWebView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        mWebView.setFocusable(true);
        mWebView.setBackgroundColor(0x00ffffff);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        addJavaScriptInterface();
    }


    private class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        public void onPageFinished(WebView view, String url) {
            if (null == mWebView || null == mWebView.getTitle()) {
                return;
            }
            if (!mWebView.getTitle().equals("")
                    && mWebView.getTitle().length() > 20) {
            } else {
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mWebView.loadUrl("file:///android_asset/repair/repair.html");
            mWebView.getSettings().setLayoutAlgorithm( WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            return;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            handler.proceed();
        }

    }

    /**
     * 监听网页加载进度
     *
     * @author ck
     */
    private class MyChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture,
                    resultMsg);
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadWeb() {
        mWebView.loadUrl(URL);
    }
}
