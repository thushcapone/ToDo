/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.utils;


import com.shinobi.todo.base.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 11/09/2017.
 * Wizall,
 * thiependa.seye@wizall.com
 */

public class RecyclerViewUtils {
    
    private RecyclerViewUtils() {
    }
    
    public static <T extends BaseRecyclerViewAdapter> void addLoading(final List dataSet,
                                                                      final T adapter) {
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i) == null) {
                return;
            }
        }
        dataSet.add(null);
        adapter.addItem(null);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
    }
    
    public static <T extends BaseRecyclerViewAdapter> void removeLoading(final List dataSet,
                                                                         final T adapter) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i) == null) {
                positions.add(i);
            }
        }
        for (int i = positions.size() - 1; i >= 0; i--) {
            dataSet.remove(positions.get(i).intValue());
            adapter.removeItem(positions.get(i));
        }
        adapter.setLoaded();
    }
    
    public static <T extends BaseRecyclerViewAdapter> void addObjects(final List dataSet,
                                                                      final T adapter,
                                                                      final List objects) {
        for (Object object : objects) {
            dataSet.add(object);
            adapter.addItem(object);
        }
        adapter.notifyDataSetChanged();
    }
    
    public static <T extends BaseRecyclerViewAdapter> void clearObjects(final T adapter) {
        adapter.clear();
    }
    
}
