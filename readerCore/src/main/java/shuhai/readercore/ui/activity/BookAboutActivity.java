package shuhai.readercore.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import shuhai.readercore.common.Constants;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseWVActivity;
import shuhai.readercore.utils.ActivityUtils;
import shuhai.readercore.utils.UrlUtils;

/**
 * @author 55345364
 * @date 2017/8/14.
 */

public class BookAboutActivity extends BaseWVActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void addJavaScriptInterface() {

        webView.addJavascriptInterface(new Object() {

            /**
             * 1、 加载json文件件,加载固定参数
             */
            @JavascriptInterface
            public void loadData() {
                mHandler.post(new Runnable() {
                    public void run() {
                        try {
                            webView.loadUrl("javascript:waves("
                                    + UrlUtils.makeJsonText()
                                    + ")");
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
                mHandler.post(new Runnable() {
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
                mHandler.post(new Runnable() {
                    public void run() {
                        // 处理免费阅读业务
                        // 处理免费阅读业务
                        Intent intent = new Intent();
                        intent.putExtra("read.book.id",Integer.parseInt(articleid));
                        intent.setClass(mContext,ReadActivity.class);
                        startActivity(intent);
                        Toast.makeText(mContext, "阅读", Toast.LENGTH_SHORT).show();


                    }
                });
            }

            /**
             * 4、 立即阅读(章节目录阅读进入)
             */
            @JavascriptInterface
            public void readBookFromChp(final String articleid,
                                        final String chaptered, final String chapterorder) {
                mHandler.post(new Runnable() {
                    public void run() {
                        // 处理从目录等位置进入的 具体章节阅读
                        Intent intent = new Intent();
                        intent.putExtra("read.book.id",Integer.parseInt(articleid));
                        intent.putExtra("read.chapter.id",Integer.parseInt(chaptered));
                        intent.putExtra("read.chapter.order",Integer.parseInt(chapterorder));
                        intent.setClass(mContext,ReadActivity.class);
                        startActivity(intent);
                        Toast.makeText(mContext, "阅读", Toast.LENGTH_SHORT).show();
                        if (mContext instanceof Activity) {
                            Activity activity = (Activity) mContext;
                            activity.finish();
                        }
                    }
                });
            }

            /**
             * 5、 刷新网页
             */
            @JavascriptInterface
            public void refresh() {
                mHandler.post(new Runnable() {
                    public void run() {
                        webView.reload();
                    }
                });
            }

            /**
             * 6、 弹出TOAST
             */
            @JavascriptInterface
            public void alert(final String message) {
                mHandler.post(new Runnable() {
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
                mHandler.post(new Runnable() {
                    public void run() {
                        // 跳转书籍activity
                        Intent intent = new Intent(mContext,
                                BookAboutActivity.class);
                        intent.putExtra("url", vurl +"&packagename=com.shuhai.bookos");
                        mContext.startActivity(intent);
                    }
                });
            }

            /**
             * 9、 跳转个人相关
             */

            @JavascriptInterface
            public void gotoperson(final String vurl) {
                mHandler.post(new Runnable() {
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
                mHandler.post(new Runnable() {
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
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(vurl)) {
                            try {
                                Uri uri = Uri.parse(vurl);
                                Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }, "demo");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(mContext, BookAboutActivity.class);
                intent.putExtra(
                        "url",
                        UrlUtils.markSignUrl(Constants.BASE_WEB_URL
                                + "bookstore?op=search"));
                mContext.startActivity(intent);
                break;
            case R.id.action_home:
                ActivityUtils.finishAllActivities();
                break;

            case android.R.id.home:
                finish();
//                Intent upIntent = NavUtils.getParentActivityIntent(this);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    TaskStackBuilder.create(this)
//                            .addNextIntentWithParentStack(upIntent)
//                            .startActivities();
//                } else {
//                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
