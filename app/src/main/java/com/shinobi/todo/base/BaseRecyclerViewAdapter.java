/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.tc.senevideos.utils.EndlessOnScrollListener;
import com.tc.senevideos.utils.ItemClickListener;
import com.tc.senevideos.utils.OnLoadMoreListener;
import com.tc.senevideos.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by thiependa on 01/10/2017.
 */

public abstract class BaseRecyclerViewAdapter<T>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    protected List<T> mDataset = new ArrayList<>();
    protected Context mContext;
    protected ItemClickListener mClickListener;
    private EndlessOnScrollListener mScrollListener;
    
    public BaseRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }
    
    public BaseRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this(context);
        mScrollListener = new EndlessOnScrollListener(recyclerView);
    }
    
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    
    public T getItem(int id) {
        try {
            return mDataset.get(id);
        } catch (IndexOutOfBoundsException e) {
            Timber.e("index of out of bonds caught for %s", this.getClass().getCanonicalName());
            Class classOfT = ReflectionUtils.getTypeArguments(BaseRecyclerViewAdapter.class,
                                                              getClass()).get(0);
            try {
                return (T) classOfT.newInstance();
            } catch (Exception e1) {
                Timber.e("exception caught during new instance of %s for %s", classOfT
                                 .getCanonicalName(),
                         this.getClass().getCanonicalName()
                );
                throw new RuntimeException(e);
            }
        }
    }
    
    public void clear() {
        int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);
    }
    
    public void removeItem(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
    
    public void addItem(T object) {
        mDataset.add(object);
    }
    
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mScrollListener.setOnLoadMoreListener(onLoadMoreListener);
    }
    
    public void setLoaded() {
        mScrollListener.setLoaded();
    }
    
}
