package shuhai.readercore.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import shuhai.readercore.R;

/**
 * @author 55345364
 * @date 2017/8/7.
 */
@SuppressLint({ "SetJavaScriptEnabled", "Instantiatable", "NewApi" })
public abstract class BaseWBActivity {

    private LayoutInflater mLayoutInflater;

    public WebView mWebView;

    private WebSettings mWebSettings;

    public View layout;

    private Handler mHandler;

    private Context mContext;

    public BaseWBActivity(Context context){
        this.mContext = context;
    }


    public abstract int getLayoutId();

    public void configViews() {
        mLayoutInflater = LayoutInflater.from(mContext);
        layout = mLayoutInflater.inflate(getLayoutId(),null);
        mWebView = layout.findViewById(R.id.web_view);
        webSetting();
    }


    private void webSetting() {
        mHandler = new Handler();
        mWebSettings = mWebView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.setFocusable(true);
        mWebView.setBackgroundColor(0x00ffffff);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.addJavascriptInterface(new Object(){
            /**
             * 1、 加载json文件件,加载固定参数
             */
            @JavascriptInterface
            public void loadData() {
                mHandler.post(new Runnable() {
                    public void run() {
                        try {
                            mWebView.loadUrl("javascript:waves()");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        },"demo");
    }






}
