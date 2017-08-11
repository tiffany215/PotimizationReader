package shuhai.readercore.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.InjectView;
import shuhai.readercore.R;
import shuhai.readercore.utils.NetworkUtils;

/**
 * @author 55345364
 * @date 2017/8/11.
 */

public abstract  class BaseWVActivity extends BaseActivity {


    @InjectView(R.id.web_view)
    public WebView webView;

    @InjectView(R.id.swipe_refresh_layout)
    public SwipeRefreshLayout swipeRefreshLayout;


    @InjectView(R.id.web_progressbar)
    public ProgressBar progressBar;

    @InjectView(R.id.view_load_fail)
    private LinearLayout errorLayout;

    public WebSettings webSettings;


    @Override
    public void initData() {

    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void configViews() {
        webSetting();
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyChromeClient());
    }

    private void webSetting() {
        webSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webView.setFocusable(true);
        webView.setBackgroundColor(0x00ffffff);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        addJavaScriptInterface();
    }


    public abstract void addJavaScriptInterface();


    private class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        public void onPageFinished(WebView view, String url) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            if (null == webView || null == webView.getTitle()) {
                return;
            }
            if (!webView.getTitle().equals("")
                    && webView.getTitle().length() > 20) {
            } else {

            }
            swipeRefreshLayout.setRefreshing(false);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            webView.loadUrl("file:///android_asset/repair/repair.html");
            webView.getSettings().setLayoutAlgorithm( WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
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
            progressBar.setProgress(newProgress);
            progressBar.postInvalidate();
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

//    @SuppressLint("SetJavaScriptEnabled")
//    public void loadWeb() {
//        if (!NetworkUtils.isAvailable(mContext)) {
////            errorLayout.setVisibility(View.VISIBLE);
//            return;
//        }
//
//        webView.loadUrl(URL);
//    }

}
