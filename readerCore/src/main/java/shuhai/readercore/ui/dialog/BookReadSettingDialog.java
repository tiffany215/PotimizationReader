package shuhai.readercore.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import shuhai.readercore.Constants;
import shuhai.readercore.R;
import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.activity.BookAboutActivity;
import shuhai.readercore.ui.sharedp.ReaderSP;
import shuhai.readercore.utils.UrlUtils;
import shuhai.readercore.view.readview.factory.PageFactory;

/**
 * Created by Administrator on 2017/11/19.
 */

public class BookReadSettingDialog extends Dialog implements View.OnClickListener{

    private Context mContext;

    public RelativeLayout TopMenuLayout;
    public LinearLayout BottomMenuLayout;
    public View NullView;
    public ImageButton SystemBack;
    private PageFactory mPageFactory;



    public BookReadSettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }


    private void init() {
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }

    public BookReadSettingDialog(Context context,PageFactory pageFactory) {
        this(context, R.style.dialog_no_title);
        this.mPageFactory = pageFactory;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_read_book_setting);
        initView();
        initValues();
//        initSetting();
        initAnimationIn();
    }

    private void initValues() {

        TopMenuLayout = findViewById(R.id.top_menu_layout);
        TopMenuLayout.setOnClickListener(this);

        BottomMenuLayout = findViewById(R.id.bottom_menu_layout);
        BottomMenuLayout.setOnClickListener(this);

        findViewById(R.id.system_back).setOnClickListener(this);
        findViewById(R.id.null_view).setOnClickListener(this);
        findViewById(R.id.read_top_mark).setOnClickListener(this);
        findViewById(R.id.read_catalog).setOnClickListener(this);
        findViewById(R.id.read_setting).setOnClickListener(this);
        findViewById(R.id.speech_book).setOnClickListener(this);
        findViewById(R.id.read_night).setOnClickListener(this);
        findViewById(R.id.read_download_chapter).setOnClickListener(this);
        findViewById(R.id.read_more_book_detail).setOnClickListener(this);
        findViewById(R.id.bookstore_menu_night_model).setOnClickListener(this);



    }


    /**
     * 动画退出
     */
    private void initAnimationOut() {
        TopMenuLayout.clearAnimation();
        Animation animTopOut = AnimationUtils.loadAnimation(mContext,
                R.anim.top_menu_out);
        TopMenuLayout.setAnimation(animTopOut);
        TopMenuLayout.startAnimation(animTopOut);

        BottomMenuLayout.clearAnimation();
        Animation animBottomOut = AnimationUtils.loadAnimation(mContext,R.anim.bottom_menu_out);
        BottomMenuLayout.setAnimation(animBottomOut);
        BottomMenuLayout.startAnimation(animBottomOut);

        animBottomOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }
        });

    }

    /**
     *
     */
    private void initAnimationIn() {
        Animation animIn = AnimationUtils.loadAnimation(mContext,
                R.anim.top_menu_in);
        TopMenuLayout.setAnimation(animIn);
        Animation animInBottom = AnimationUtils.loadAnimation(mContext,
                R.anim.bottom_menu_in);
        BottomMenuLayout.setAnimation(animInBottom);
    }


    private void initView() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dms = mContext.getResources().getDisplayMetrics();
        lp.width = dms.widthPixels;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }


//
//    @OnClick(R.id.null_view)
//    public void OnClickCancelDialog(){
//        initAnimationOut();
//    }
//
//    @OnClick(R.id.system_back)
//    public void OnClickDialogDismiss(){
//        dismiss();
//    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Class<?> cls = null;
        switch (v.getId()) {
            case R.id.read_more_book_detail:
                cls = BookAboutActivity.class;
                intent.putExtra("url", UrlUtils.markSignUrl(Constants.BASE_WEB_URL + "bookinfo?op=showbookinfo&articleid=" +mPageFactory.getArticleId()));
                break;

            case R.id.read_setting:
                new BookReadMoreSettingDialog(mContext,mPageFactory).show();
                break;

            case R.id.read_night:
                if(ReaderSP.getInstance().getReaderDayOrNight()){
                    mPageFactory.setTheme(ThemeManager.READ_STYLE_THEME_00);
                }else{
                    mPageFactory.setTheme(ThemeManager.READ_STYLE_THEME_05);
                }
                    ReaderSP.getInstance().setReaderDayOrNight(!ReaderSP.getInstance().getReaderDayOrNight());
                break;
        }

        if(null != cls){
            intent.setClass(mContext, cls);
            mContext.startActivity(intent);
        }
        initAnimationOut();

    }
}
