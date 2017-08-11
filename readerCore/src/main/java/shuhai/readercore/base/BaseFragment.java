package shuhai.readercore.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * @author 55345364
 * @date 2017/7/27.
 */

public  abstract  class BaseFragment extends Fragment {

    public View parentView;

    public FragmentActivity fragmentActivity;

    public Context mContext;

    protected abstract void setupActivityComponent();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(getLayoutResId(),container,false);
        ButterKnife.inject(this,parentView);
        fragmentActivity = getSupportActivity();
        mContext = fragmentActivity;
        return parentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActivityComponent();
        attachView();
        initData();
        configView();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public FragmentActivity getSupportActivity(){
        return super.getActivity();
    }


    public abstract int getLayoutResId();

    public abstract void attachView();

    public abstract void initData();

    public abstract void configView();

}
