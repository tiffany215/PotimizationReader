package shuhai.readercore.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import shuhai.readercore.R;
import shuhai.readercore.base.BaseWBActivity;

/**
 * @author 55345364
 * @date 2017/8/7.
 */

public class ComponentsActivity extends BaseWBActivity{

    private String mUrl;

    public ComponentsActivity(Context context,String url){
        super(context,url);
        this.mUrl = url;
        loadWeb();

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void addJavaScriptInterface() {
        mWebView.addJavascriptInterface(new Object() {

            /**
             * 1、 加载json文件件,加载固定参数
             */
            @JavascriptInterface
            public void loadData() {
                myHandler.post(new Runnable() {
                    public void run() {
                        try {
//                            mWebView.loadUrl("javascript:waves("
//                                    + AppUtil
//                                    .makeJsonText(appContext, mContext)
//                                    + ")");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            /**
             * 2、 添加书架
             */
            @JavascriptInterface
            public void addStore(final String articleid) {
                myHandler.post(new Runnable() {
                    public void run() {
//                        try {
//                            if (!bkbaseInfo.checkBook(articleid)) {
//                                new GetBookBaseInfoTask(mContext, true)
//                                        .execute(articleid);
//                            } else {
//                                UIHelper.ToastMessage(mContext, "已添加书架");
//                            }
//                        } catch (NumberFormatException e) {
//                            e.printStackTrace();
//                        } catch (AppException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
            }

            /**
             * 3、 立即阅读(免费阅读)readBook(final String articleid)
             */
            @JavascriptInterface
            public void readBook(final String articleid) {
                myHandler.post(new Runnable() {
                    public void run() {
                        // 处理免费阅读业务
                        // 处理免费阅读业务
//                        OpenReadBookTask.getInstance(appContext, mContext)
//                                .readBook(
//                                        BkConfig.SHOP_BOOKS,
//                                        articleid,
//                                        ReadSetting.getIntance(mContext)
//                                                .getLastReadChp(articleid),
//                                        ReadSetting.getIntance(mContext)
//                                                .getLastReadOrd(articleid),
//                                        null, null);

                        Toast.makeText(mContext, "阅读", Toast.LENGTH_SHORT).show();


                    }
                });
            }

            /**
             * 4、 立即阅读(章节目录阅读进入)
             */
            @JavascriptInterface
            public void readBookFromChp(final String articleid,
                                        final String chaptered, final String chapterorder,
                                        final String isvip) {
                myHandler.post(new Runnable() {
                    public void run() {
                        // 处理从目录等位置进入的 具体章节阅读
                        Toast.makeText(mContext, "阅读", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /**
             * 5、 刷新网页
             */
            @JavascriptInterface
            public void refresh() {
                myHandler.post(new Runnable() {
                    public void run() {
                        mWebView.reload();
                    }
                });
            }

            /**
             * 6、 弹出TOAST
             */
            @JavascriptInterface
            public void alert(final String message) {
                myHandler.post(new Runnable() {
                    public void run() {
//                        UIHelper.ToastMessage(mContext, message);
                    }
                });
            }

            /**
             * 8、 跳转书籍相关
             */

            @JavascriptInterface
            public void gotobookdetail(final String vurl) {
                myHandler.post(new Runnable() {
                    public void run() {
                        // 跳转书籍activity
                        Intent intent = new Intent(mContext,
                                ReadActivity.class);
                        intent.putExtra("url", vurl);
                        mContext.startActivity(intent);
                    }
                });
            }

            /**
             * 9、 跳转个人相关
             */

            @JavascriptInterface
            public void gotoperson(final String vurl) {
                myHandler.post(new Runnable() {
                    public void run() {

                        // 跳转个人activity
                        Intent intent = new Intent(mContext,
                                ReadActivity.class);
                        intent.putExtra("url", vurl);
                        mContext.startActivity(intent);

                    }
                });
            }

            /**
             * 9、 跳转支付相关
             */

            @JavascriptInterface
            public void gotopay(final String vurl) {
                myHandler.post(new Runnable() {
                    public void run() {

                        // 跳转支付activity
                        Intent intent = new Intent(mContext,
                                ReadActivity.class);
                        intent.putExtra("url", vurl);
                        mContext.startActivity(intent);
                    }
                });
            }

            /**
             * 10、跳转至外部浏览器
             *
             * @param vurl
             */
            @JavascriptInterface
            public void gotoBrowserOut(final String vurl) {
                myHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(vurl)) {
                            try {
                                Uri uri = Uri.parse(vurl);
                                Intent viewIntent = new Intent(Intent.ACTION_VIEW,uri);
                                mContext.startActivity(viewIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }

            /**
             * 微社区登录
             */
            @JavascriptInterface
            public void gotoCommunity() {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }


        }, "demo");
    }


    public View getLayout(){
        return layout;
    }


    public void loadWeb(){
        mWebView.loadUrl(mUrl);
    }

}
