package shuhai.readercore.view.webview;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import shuhai.readercore.R;
import shuhai.readercore.utils.NetworkUtils;

/**
 * @author 55345364
 * @date 2017/8/7.
 */
@SuppressLint({ "SetJavaScriptEnabled", "Instantiatable", "NewApi" })
public abstract class BaseWbView {


    public Context mContext;
    public View layout;
    public Handler myHandler;
    public WebView mWebView;
    private WebSettings webSettings;
    private String URL;
    private LayoutInflater mLayoutInflater;

    public SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout errorLayout;



    @SuppressLint({ "Instantiatable", "InflateParams" })
    public BaseWbView(Context context, String url) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        layout = mLayoutInflater.inflate(getLayoutId(),null);
        this.URL = url;
        myHandler = new Handler();
        initView();
    }


    public abstract int getLayoutId();

    private void initView() {
        swipeRefreshLayout = layout.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWeb();

            }
        });
        errorLayout = layout.findViewById(R.id.view_load_fail);
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
//            errorLayout.setVisibility(View.GONE);
//            progressBar.setVisibility(View.GONE);

            if (null == mWebView || null == mWebView.getTitle()) {
                return;
            }
            if (!mWebView.getTitle().equals("")
                    && mWebView.getTitle().length() > 20) {
            } else {

            }
            swipeRefreshLayout.setRefreshing(false);
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
//            progressBar.setProgress(newProgress);
//            progressBar.postInvalidate();
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
        if (!NetworkUtils.isAvailable(mContext)) {
//            errorLayout.setVisibility(View.VISIBLE);
            return;
        }

        mWebView.loadUrl(URL);
    }

}
