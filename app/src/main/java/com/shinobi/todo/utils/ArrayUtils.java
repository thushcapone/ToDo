/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.utils;

import java.util.List;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 12/09/2017.
 * Wizall,
 * thiependa.seye@wizall.com
 */

public class ArrayUtils {
    
    private ArrayUtils() {
    }
    
    public static <T> List<T> addAll(final List<T> baseList, final List<T> addedList) {
        for (T added : addedList) {
            baseList.add(added);
        }
        return baseList;
    }
    
}
