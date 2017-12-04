package shuhai.readercore.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import shuhai.readercore.R;
import shuhai.readercore.ui.sharedp.ReaderSP;
import shuhai.readercore.ui.widget.ReaderThemeButton;
import shuhai.readercore.view.readview.factory.PageFactory;

/**
 * @author 55345364
 * @date 2017/11/21.
 */

public class BookReadThemeSettingDialog extends Dialog{


    private Context mContext;
    private PageFactory mPageFactory;

    private ReaderThemeButton[] themeStyleViews;
    private int[] themeStyleIds;

    public BookReadThemeSettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }


    protected BookReadThemeSettingDialog(Context context,PageFactory pageFactory) {
        this(context, R.style.dialog_from_bottom);
        this.mPageFactory = pageFactory;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_read_theme_setting);
        initValues();
        initView();
    }

    private void initView() {

        themeStyleIds = new int[]{R.id.read_style_theme_00,R.id.read_style_theme_01,R.id.read_style_theme_02,
                                  R.id.read_style_theme_03,R.id.read_style_theme_04,R.id.read_style_theme_05,
                                  R.id.read_style_theme_06,R.id.read_style_theme_07,R.id.read_style_theme_08,
                                  R.id.read_style_theme_09,R.id.read_style_theme_10,R.id.read_style_theme_11,
                                  R.id.read_style_theme_12,R.id.read_style_theme_13,R.id.read_style_theme_14,
                                  R.id.read_style_theme_15,R.id.read_style_theme_16,R.id.read_style_theme_17,
        };

        themeStyleViews = new ReaderThemeButton[themeStyleIds.length];
        for (int i = 0; i < themeStyleIds.length; i++) {
            themeStyleViews[i] = findViewById(themeStyleIds[i]);
            themeStyleViews[i].setOnClickListener(new ReadThemeStyleOnClickListener());
        }
    }


    private void initValues() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dms = mContext.getResources().getDisplayMetrics();
        lp.width = dms.widthPixels;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }


    /**
     * 主题设置按钮点击监听事件
     * @author Wangxu
     *
     */
    private class ReadThemeStyleOnClickListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < themeStyleIds.length; i++) {
                if (v == themeStyleViews[i]) {
                    themeStyleViews[i].setSelected(true);
                    ReaderSP.getInstance().setReaderTheme(i);
                } else {
                    themeStyleViews[i].setSelected(false);
                }

                if(themeStyleIds[i] == R.id.read_style_theme_05 || themeStyleIds[i] == R.id.read_style_theme_10){
                    mPageFactory.setTextColor(R.color.primary_text);
                }else {
                    mPageFactory.setTextColor(R.color.title_text);
                }
            }
            mPageFactory.setTheme(ReaderSP.getInstance().getReaderTheme());
        }
    }
}
