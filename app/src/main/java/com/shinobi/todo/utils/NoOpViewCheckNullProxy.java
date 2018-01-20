/*
 * Copyright (c) 2017.
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.utils;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/02/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Avoid the null check when using the mView on the Presenter
 * Shamelessly copied from
 * <a href="https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1">https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1</a>
 */
public class NoOpViewCheckNullProxy implements InvocationHandler {
    
    private WeakReference<Object> mView;
    
    public NoOpViewCheckNullProxy(Object view) {
        this.mView = new WeakReference<>(view);
    }
    
    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if (mView == null || mView.get() == null) {
            return null;
        }
        
        return method.invoke(mView.get(), args);
    }
    
}
