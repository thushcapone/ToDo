/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 12/09/2017.
 * Wizall,
 * thiependa.seye@wizall.com
 */

public class EndlessOnScrollListener extends RecyclerView.OnScrollListener {
    
    private static final WeakHashMap<RecyclerView, EndlessOnScrollListener> sLastInstances = new
            WeakHashMap<>();
    final LinearLayoutManager linearLayoutManager;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    
    public EndlessOnScrollListener(RecyclerView recyclerView) {
        for (Map.Entry<RecyclerView, EndlessOnScrollListener> entry : sLastInstances.entrySet()) {
            if (entry.getKey().equals(recyclerView)) {
                recyclerView.removeOnScrollListener(entry.getValue());
            }
        }
        recyclerView.addOnScrollListener(this);
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        sLastInstances.put(recyclerView, this);
    }
    
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        
        totalItemCount = linearLayoutManager.getItemCount();
        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
        if (!loading && (totalItemCount / 2 <= lastVisibleItem + visibleThreshold)) {
            // Half has been reached
            // Do something
            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMore();
            }
            loading = true;
        }
        
    }
    
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    
    public void setLoaded() {
        loading = false;
    }
    
}
