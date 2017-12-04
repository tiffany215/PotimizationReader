package shuhai.readercore.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.InjectView;
import butterknife.OnClick;
import shuhai.readercore.common.Constants;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.manager.ChapterCacheManager;
import shuhai.readercore.ui.sharedp.ReaderSP;
import shuhai.readercore.utils.ActivityUtils;
import shuhai.readercore.utils.AppUtils;
import shuhai.readercore.utils.ToastUtils;
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

    @InjectView(R.id.action_settings_crc_text)
    public TextView action_settings_crc_text;


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

    @InjectView(R.id.action_settings_flip)
    public TextView action_settings_flip;

    @InjectView(R.id.action_settings_flip_text)
    public TextView action_settings_flip_text;



    public LinearLayout linearLayout;



    final String[] arrayFlip = new String[] { "无效果", "平移翻页", "覆盖翻页", "仿真翻页" };
    final int[] arrayFlipModel = new int[]{ Constants.FLIP_CONFIG.LEVEL_NO_FLIP,
            Constants.FLIP_CONFIG.LEVEL_SCROLLER_FLIP,
            Constants.FLIP_CONFIG.LEVEL_COVER_FLIP,
            Constants.FLIP_CONFIG.LEVEL_REAL_FLIP
    };



    @Override
    public int getLayoutId() {
        return R.layout.activity_app_setting;
    }

    @Override
    public void initData() {
        action_settings_rs.setOnClickListener(this);
        for (int i = 0; i < arrayFlip.length; i++) {
            if(ReaderSP.getInstance().getFlipModel() == arrayFlipModel[i]){
                action_settings_flip_text.setText(arrayFlip[i]);
            }
        }
        action_settings_crc_text.setText(ChapterCacheManager.getInstance().getCacheSize() / 1024 + "MB");
    }


    @Override
    public void initToolBar() {
        mCommonToolbar.setTitleTextColor(Color.WHITE);

        linearLayout = (LinearLayout) findViewById(R.id.action_test);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast("tttt");
            }
        });


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
                ActivityUtils.finishAllActivities();
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 设置阅读翻页方式
     */
    @OnClick(R.id.action_settings_flip)
    public void OnClickReaderFlip(){
        new AlertDialog.Builder(mContext).setItems(arrayFlip, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ReaderSP.getInstance().setFlipModel(arrayFlipModel[which]);
                action_settings_flip_text.setText(arrayFlip[which]);

            }
        }).create().show();
    }


    @OnClick(R.id.action_test)
    public void OnClickTest(){
        Toast.makeText(mContext,"66666",Toast.LENGTH_SHORT).show();
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
        new AlertDialog.Builder(mContext).setTitle("书海 V" + AppUtils.getAppVersionCode())
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
