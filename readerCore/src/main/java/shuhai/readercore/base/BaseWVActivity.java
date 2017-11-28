package shuhai.readercore.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.dyhdyh.widget.loading.dialog.LoadingDialog;

import butterknife.InjectView;
import shuhai.readercore.R;
import shuhai.readercore.ui.dialog.LoadingCallback;
import shuhai.readercore.utils.NetworkUtils;
import shuhai.readercore.utils.UrlUtils;

/**
 * @author 55345364
 * @date 2017/8/11.
 */
@SuppressLint({ "SetJavaScriptEnabled", "Instantiatable", "NewApi" })
public abstract  class BaseWVActivity extends BaseActivity {


    @InjectView(R.id.web_view)
    public WebView webView;

    @InjectView(R.id.swipe_refresh_layout)
    public SwipeRefreshLayout swipeRefreshLayout;



//    @InjectView(R.id.web_progressbar)
//    public ProgressBar progressBar;
//
//    @InjectView(R.id.view_load_fail)
//    public LinearLayout errorLayout;

    public WebSettings webSettings;

    public Handler mHandler;

    public String mURl;

    @Override
    public void initData() {
        mURl = getIntent().getStringExtra("url");
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public void configViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWeb();
            }
        });
        mHandler = new Handler();
        webSetting();
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyChromeClient());
        loadWeb();
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

            loadingDialog.show();

        }

        public void onPageFinished(WebView view, String url) {
//            errorLayout.setVisibility(View.GONE);
//            progressBar.setVisibility(View.GONE);
            if (null == webView || null == webView.getTitle()) {
                return;
            }
            if (!webView.getTitle().equals("") && webView.getTitle().length() > 20) {
                mCommonToolbar.setTitle(webView.getTitle());
            } else {

            }
            swipeRefreshLayout.setRefreshing(false);
            LoadingDialog.cancel();
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            LoadingDialog.cancel();
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

        webView.loadUrl(UrlUtils.markSignUrl(mURl));
    }

}
