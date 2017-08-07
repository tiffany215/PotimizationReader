package shuhai.readercore.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 55345364
 * @date 2017/7/28.
 */

public class ViewPagerAdapter extends PagerAdapter {

    public ViewPagerAdapter(){
        super();
    }


    private List<View> mData = new ArrayList<>();

    public void setData(List<View> list){
        this.mData  = list;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mData.get(position));
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mData.get(position));
        return mData.get(position);
    }
}
