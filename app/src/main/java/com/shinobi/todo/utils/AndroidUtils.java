/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.utils;

import android.os.Bundle;

import timber.log.Timber;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 02/08/2017.
 * Wizall,
 * thiependa.seye@wizall.com
 */

public class AndroidUtils {
    
    private AndroidUtils() {
    }
    
    public static void debugBundle(Bundle bundle) {
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            String temp = String.format("%s %s (%s)", key, value.toString(),
                                        value.getClass().getName()
            );
            Timber.e("key %s", temp);
        }
    }
    
}
