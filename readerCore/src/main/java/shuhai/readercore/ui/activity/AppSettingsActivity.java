package shuhai.readercore.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import shuhai.readercore.Constants;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.utils.AppUtils;
import shuhai.readercore.utils.UrlUtils;

public class AppSettingsActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.action_settings_uf)
    public TextView action_settings_uf;

    @InjectView(R.id.action_settings_as)
    public TextView action_settings_as;

    @InjectView(R.id.action_settings_sp)
    public TextView action_settings_sp;

    @InjectView(R.id.action_settings_hc)
    public TextView action_settings_hc;

    @InjectView(R.id.action_settings_ctc)
    public  TextView action_settings_ctc;

    @InjectView(R.id.action_settings_rs)
    public TextView action_settings_rs;


    @InjectView(R.id.action_settings_sm)
    public TextView action_settings_sm;

    @InjectView(R.id.action_settings_vu)
    public TextView action_settings_vu;

    @InjectView(R.id.action_settings_dn)
    public  TextView action_settings_dn;

    @InjectView(R.id.action_settings_ts)
    public TextView action_settings_ts;


    @Override
    public int getLayoutId() {
        return R.layout.activity_app_setting;
    }

    @Override
    public void initData() {
        action_settings_rs.setOnClickListener(this);
    }


    @Override
    public void initToolBar() {
        mCommonToolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public void configViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

                break;

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.action_settings_crc)
    public void OnClickCleanReaderCache(){
        new AlertDialog.Builder(mContext).setTitle("清除阅读缓存")
                .setMessage("您确定清除所有书籍的缓存记录吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //开启线程清理缓存数据


                                //切换至UI线程
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }).start();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();

    }


    @OnClick(R.id.action_settings_ctc)
    public void OnClickClearThemeCache(){
        new AlertDialog.Builder(mContext).setTitle("清除主题缓存")
                .setMessage("您确定要清理所有主题缓存吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }).start();


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }


    @OnClick(R.id.action_settings_as)
    public void OnClickAboutShuhai(){
        new AlertDialog.Builder(mContext).setTitle("书海V" + AppUtils.getPackageName())
                .setMessage(R.string.about_shuhai_prompt)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }).start();


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_settings_rs:
                startActivity(new Intent(mContext,ReadSettingActivity.class));
                break;
        }
    }
}
