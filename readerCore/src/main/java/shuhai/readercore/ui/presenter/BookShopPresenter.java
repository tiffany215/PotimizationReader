package shuhai.readercore.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import shuhai.readercore.Constants;
import shuhai.readercore.R;
import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.ui.activity.ComponentsActivity;
import shuhai.readercore.ui.contract.BookShopContract;
import shuhai.readercore.utils.AppUtils;

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
        ComponentsActivity recommendActivity = new ComponentsActivity(context,AppUtils.makeWebUrl(Constants.BASE_WEB_URL + "bookstore?op=tuijian"));
        ComponentsActivity giftsActivity = new ComponentsActivity(context,AppUtils.makeWebUrl(Constants.BASE_WEB_URL + "bookstore?op=serial"));
        ComponentsActivity fansActivity = new ComponentsActivity(context,AppUtils.makeWebUrl(Constants.BASE_WEB_URL + "bookstore?op=fansforuser"));
        ComponentsActivity personActivity = new ComponentsActivity(context,AppUtils.makeWebUrl(Constants.BASE_WEB_URL + "userinfo?op=showInitUser&themeid="+ 0));

        viewList.add(recommendActivity.getLayout());
        viewList.add(giftsActivity.getLayout());
        viewList.add(fansActivity.getLayout());
        viewList.add(personActivity.getLayout());

        return viewList;
    }
}
