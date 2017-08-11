package shuhai.readercore.ui.presenter;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.view.webview.ComponentsWebView;
import shuhai.readercore.ui.contract.BookShopContract;

/**
 * @author 55345364
 * @date 2017/8/4.
 */

public class BookShopPresenter extends RxPresenter<View> implements BookShopContract.Presents<View> {


    @Override
    public void attachView(View view) {

    }

    @Override
    public void detachView() {

    }


    @Override
    public List<View> obtainView(Context context) {

        List<View> viewList = new ArrayList<>();
//        ComponentsWebView recommendActivity = new ComponentsWebView(context,AppUtils.makeWebUrl(Constants.BASE_WEB_URL + "bookstore?op=tuijian"));
//        ComponentsWebView giftsActivity = new ComponentsWebView(context,AppUtils.makeWebUrl(Constants.BASE_WEB_URL + "bookstore?op=serial"));
//        ComponentsWebView fansActivity = new ComponentsWebView(context,AppUtils.makeWebUrl(Constants.BASE_WEB_URL + "bookstore?op=fansforuser"));
//        ComponentsWebView personActivity = new ComponentsWebView(context,AppUtils.makeWebUrl(Constants.BASE_WEB_URL + "userinfo?op=showInitUser&themeid="+ 0));
        ComponentsWebView recommendActivity = new ComponentsWebView(context,"http://appdata.shuhai.com/ishuhai/app/bookstore?op=tuijian&version=61&source=shuhai&packagename=com.shuhai.bookos");
        ComponentsWebView serialActivity = new ComponentsWebView(context,"http://appdata.shuhai.com/ishuhai/app/bookstore?op=serial&version=61&source=shuhai&packagename=com.shuhai.bookos");
        ComponentsWebView giftsActivity = new ComponentsWebView(context,"http://appdata.shuhai.com/ishuhai/app/bookstore?op=fansforuser&version=61&source=shuhai&packagename=com.shuhai.bookos");
        ComponentsWebView personActivity = new ComponentsWebView(context,"http://appdata.shuhai.com/ishuhai/app/userinfo?op=showInitUser&themeid=0&uid=0&pass=&username=&version=61&source=shuhai&packagename=com.shuhai.bookos");



        viewList.add(recommendActivity.getLayout());
        viewList.add(serialActivity.getLayout());
        viewList.add(giftsActivity.getLayout());
        viewList.add(personActivity.getLayout());

        return viewList;
    }
}
