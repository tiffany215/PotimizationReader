package shuhai.readercore.ui.activity;

import android.content.Context;
import android.view.View;

import shuhai.readercore.R;
import shuhai.readercore.base.BaseWBActivity;

/**
 * @author 55345364
 * @date 2017/8/7.
 */

public class ComponentsActivity extends BaseWBActivity{

    private String mUrl;

    public ComponentsActivity(Context context,String url){
        super(context);
        this.mUrl = url;
        configViews();
        loadWeb();

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }


    public View getLayout(){
        return layout;
    }


    private void loadWeb(){
        mWebView.loadUrl(mUrl);
    }

}
