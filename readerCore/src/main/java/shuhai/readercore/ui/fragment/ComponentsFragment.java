package shuhai.readercore.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shuhai.readercore.R;

/**
 * @author 55345364
 * @date 2017/7/31.
 */

public class ComponentsFragment extends Fragment {

    public static ComponentsFragment newInstance(){
        ComponentsFragment fragment = new ComponentsFragment();
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_landing_page,null);
        return view;
    }
}
