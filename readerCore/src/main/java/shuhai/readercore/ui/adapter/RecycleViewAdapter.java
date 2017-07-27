package shuhai.readercore.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import shuhai.readercore.R;

/**
 * @author 55345364
 * @date 2017/7/27.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<NormalViewHolder>{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int[] mCovers;
    private int[] mNames;
    private int[] mProgress;

    public RecycleViewAdapter(Context context){
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(mLayoutInflater.inflate(R.layout.item_book_store_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(NormalViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mNames == null ? 0 : mNames.length;
    }
}




class NormalViewHolder extends RecyclerView.ViewHolder{

    CardView mCardView;
    ImageView mBookCover;
    TextView mBookName;
    TextView mReadProgress;


    public NormalViewHolder(View itemView) {
        super(itemView);
        mCardView = (CardView) itemView.findViewById(R.id.cv_item);
        mBookCover = (ImageView) itemView.findViewById(R.id.book_cover);
        mBookName = (TextView) itemView.findViewById(R.id.book_cover);
        mReadProgress = (TextView) itemView.findViewById(R.id.book_progress);
    }
}
