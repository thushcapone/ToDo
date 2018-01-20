/*
 * Copyright (c) 2017. SeneVideo
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;
import com.shinobi.todo.data.ToDo;
import com.shinobi.todo.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/02/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */
public class PreferencesHelper {
    
    // Sharedpref file name
    public static final String PREF_SESSION_NAME = "session";
    // End Sharedpref file name
    
    // Shared pref mode
    private static final int PRIVATE_MODE = 0;
    // End Shared pref mode
    
    //Session
    private static final String KEY_TO_DO = "to do";
    //End Session
    
    // Shared Preferences
    private final SharedPreferences mPrefSession;
    // Editor for Shared preferences
    private final SharedPreferences.Editor mEditorSession;
    
    PreferencesHelper(Context context) {
        mPrefSession = context.getSharedPreferences(PREF_SESSION_NAME, PRIVATE_MODE);
        mEditorSession = mPrefSession.edit();
    }
    
    public List<ToDo> getToDos() {
        String toDos = mPrefSession.getString(KEY_TO_DO, "");
        List<ToDo> list = StringUtils.stringToList(toDos, new
                TypeToken<ArrayList<ToDo>>() {
        }.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
    
    public void addToDo(ToDo toDo) {
        List<ToDo> list = getToDos();
        list.add(toDo);
        saveToDos(list);
    }
    
    private void saveToDos(List<ToDo> videos) {
        mEditorSession.putString(KEY_TO_DO, StringUtils.listToString(videos));
        mEditorSession.apply();
    }
    
}
