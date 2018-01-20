/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.shinobi.todo.app.AppController;

import java.lang.reflect.Type;

/**
 * Created by thiependa on 09/09/2017.
 */

public class CacheHelper {
    
    // Sharedpref file name
    public static final String PREF_CACHE_NAME = "cache";
    // End Sharedpref file name
    
    // Shared pref mode
    private static final int PRIVATE_MODE = 0;
    // End Shared pref mode
    
    // Shared Preferences
    private final SharedPreferences mPrefSession;
    // Editor for Shared preferences
    private final SharedPreferences.Editor mEditorSession;
    
    public CacheHelper(Context context) {
        mPrefSession = context.getSharedPreferences(PREF_CACHE_NAME, PRIVATE_MODE);
        mEditorSession = mPrefSession.edit();
    }
    
    public void save(String url, Object response) {
        mEditorSession.putString(
                url,
                AppController.getInstance().getGson().toJson(response)
        );
        mEditorSession.apply();
    }
    
    public <T> T get(String url, Class<T> clazz) {
        String object = mPrefSession.getString(url, "");
        return AppController.getInstance().getGson().fromJson(object, clazz);
    }
    
    public <T> T get(String url, Type type) {
        String object = mPrefSession.getString(url, "");
        return AppController.getInstance().getGson().fromJson(object, type);
    }
    
}
