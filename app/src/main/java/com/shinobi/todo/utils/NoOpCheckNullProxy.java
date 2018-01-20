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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Avoid the null check of any object.
 * For an android component use {@link NoOpViewCheckNullProxy}
 */
public class NoOpCheckNullProxy implements InvocationHandler {
    
    private Object mObject;
    
    public NoOpCheckNullProxy(Object object) {
        this.mObject = object;
    }
    
    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if (mObject == null) {
            return null;
        }
        
        return method.invoke(mObject, args);
    }
    
}
