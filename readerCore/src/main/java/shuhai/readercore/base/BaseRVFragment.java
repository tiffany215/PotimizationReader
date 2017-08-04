package shuhai.readercore.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.swipe.SwipeRefreshLayout;

import java.lang.reflect.Constructor;

import javax.inject.Inject;

import butterknife.Bind;
import shuhai.readercore.R;


/**
 * @author 55345364
 * @date 2017/7/27.
 */

public abstract class BaseRVFragment<T1 extends BaseContract.BasePresenter,T2> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnItemClickListener{

    @Inject
    protected T1 mPresenter;

    @Bind(R.id.recycler_view)
    protected EasyRecyclerView recyclerView;

    protected RecyclerArrayAdapter<T2> mAdapter;


    protected void initAdapter(Class<? extends RecyclerArrayAdapter> cls,boolean refreshable,boolean loadmoreable){
        mAdapter = (RecyclerArrayAdapter<T2>) createAdapter(cls);
        initAdapter(refreshable,loadmoreable);
    }

    protected void initAdapter(boolean refreshable,boolean loadmoreable){
        if(null != recyclerView){
            recyclerView.setLayoutManager(new GridLayoutManager(getSupportActivity(),3));
            recyclerView.setAdapterWithProgress(mAdapter);
        }

        if(null != mAdapter){
            mAdapter.setOnItemClickListener(this);
        }
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
    public void onRefresh() {
        recyclerView.setRefreshing(true);
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
