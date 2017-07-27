package shuhai.readercore.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Constructor;

import shuhai.readercore.ui.adapter.RecycleViewAdapter;


/**
 * @author 55345364
 * @date 2017/7/27.
 */

public abstract class BaseRVFragment<T1 extends BaseContract.BasePresenter,T2> extends BaseFragment {

    protected T1 mPresenter;

    protected RecyclerView recyclerView;

    protected RecycleViewAdapter mAdapter;


    private void initAdapter(boolean refreshable,boolean loadmoreable){

        if(null != recyclerView){
            recyclerView.setLayoutManager(new LinearLayoutManager(getSupportActivity()));
            recyclerView.setAdapter(mAdapter);
        }


        if(null != mAdapter){

        }

        if(loadmoreable){

        }


        //是否刷新了adapter
        if(refreshable && null != mAdapter){



        }

    }


    protected void initAdapter(Class<? extends RecycleViewAdapter> cls,boolean refreshable,boolean loadmoreable){
        mAdapter = (RecycleViewAdapter) createAdapter(cls);
        initAdapter(refreshable,loadmoreable);
    }


    private Object createAdapter(Class<?> cls){
        Object obj;
        try {
            Constructor constructor = cls.getDeclaredConstructor(Context.class);
            constructor.setAccessible(true);
            obj = constructor.newInstance(mContext);
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }


    @Override
    public void attachView() {
        if(null != mPresenter){
            mPresenter.attachView(this);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != mPresenter){
            mPresenter.detachView();
        }
    }


}
