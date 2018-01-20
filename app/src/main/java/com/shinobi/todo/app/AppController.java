/*
 * Copyright (c) 2017. SeneVideo
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.shinobi.todo.BuildConfig;
import com.shinobi.todo.database.TodoDatabase;
import com.shinobi.todo.jobs.JobManagerInitializer;

import timber.log.Timber;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/02/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */
public class AppController extends Application
{
    private static AppController sInstance;
    private TodoDatabase mDatabase;
    
    public static synchronized AppController getInstance() {
        return sInstance;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        sInstance = this;
        initTimber();
        initRoom();
        initJobManager();
    }

    private void initRoom()
    {
        mDatabase = Room.databaseBuilder(getApplicationContext(), TodoDatabase.class, "todo_app").build();
    }

    private void initJobManager()
    {
        new JobManagerInitializer().initialize(this);
    }

    private void initTimber()
    {
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }

    public TodoDatabase getDatabase()
    {
        return mDatabase;
    }
}
