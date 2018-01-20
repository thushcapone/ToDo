/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.cache;


import com.shinobi.todo.app.AppController;

import java.lang.reflect.Type;

/**
 * Created by thiependa on 09/09/2017.
 */

public class CacheUtils {
    
    private CacheUtils() {
    }
    
    public static <T> T getCachedValue(String url, Class<T> clazz) {
        // Je recupere la valeur du cache pour cette url
        return AppController.getInstance().getCache().get(url, clazz);
    }
    
    public static <T> T getCachedValue(String url, Type type) {
        // Je recupere la valeur du cache pour cette url
        return AppController.getInstance().getCache().get(url, type);
    }
    
    public static void save(String url, Object response) {
        AppController.getInstance().getCache().save(url, response);
    }
    
}
