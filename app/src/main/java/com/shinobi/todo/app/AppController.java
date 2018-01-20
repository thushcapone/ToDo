/*
 * Copyright (c) 2017. SeneVideo
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.app;

import android.app.Application;
import android.os.Build;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shinobi.todo.cache.CacheHelper;
import com.shinobi.todo.services.MyGcmJobService;
import com.shinobi.todo.services.MyJobService;

import timber.log.Timber;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/02/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */
public class AppController extends Application {
    
    private static AppController sInstance;
    private PreferencesHelper mPreferences;
    private CacheHelper mCache;
    private Gson mGson;
    private JobManager mJobManager;
    
    public static synchronized AppController getInstance() {
        return sInstance;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initPreferencesHelper();
        initCache();
        initGson();
        initJobManager();
    }
    
    public PreferencesHelper getPreferences() {
        return mPreferences;
    }
    
    public CacheHelper getCache() {
        return mCache;
    }
    
    public Gson getGson() {
        return mGson;
    }
    
    public JobManager getJobManager() {
        return mJobManager;
    }
    
    private void initPreferencesHelper() {
        mPreferences = new PreferencesHelper(this.getApplicationContext());
    }
    
    private void initCache() {
        mCache = new CacheHelper(this.getApplicationContext());
    }
    
    private void initGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        mGson = gsonBuilder.create();
    }
    
    private void initJobManager() {
        Configuration.Builder builder = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }
                    
                    @Override
                    public void d(String text, Object... args) {
                        Timber.d(String.format(text, args));
                    }
                    
                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Timber.e(String.format(text, args), t);
                    }
                    
                    @Override
                    public void e(String text, Object... args) {
                        Timber.e(String.format(text, args));
                    }
                    
                    @Override
                    public void v(String text, Object... args) {
                    
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120);//wait 2 minute
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(
                    this,
                    MyJobService.class
            ), true);
        } else {
            int enableGcm = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            if (enableGcm == ConnectionResult.SUCCESS) {
                builder.scheduler(GcmJobSchedulerService.createSchedulerFor(
                        this,
                        MyGcmJobService.class
                ), true);
            }
        }
        mJobManager = new JobManager(builder.build());
    }
    
}
