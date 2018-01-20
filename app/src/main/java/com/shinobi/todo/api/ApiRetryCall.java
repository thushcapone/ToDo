/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.shinobi.todo.app.AppController;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;

/**
 * Created by thiependa on 28/12/2017.
 */

public class ApiRetryCall<T> extends Job {
    
    private static final int PRIORITY = 1;
    private transient Call<T> call;
    private transient Callback callback;
    private String request;
    
    public static ApiRetryCallBuilder builder(){
        return new ApiRetryCallBuilder();
    }
    
    private ApiRetryCall(Params params, Call<T> call, Callback callback) {
        super(params);
        this.call = call;
        this.callback = callback;
        this.request = AppController.getInstance().getGson().toJson(call.request());
        Timber.e("on created the job to the queue");
    }
    
    @Override
    public void onAdded() {
        //Nothing to do
        Timber.e("on added the job to the queue");
    }
    
    @Override
    public void onRun() throws Throwable {
        Timber.e("on running again the job");
        if(call != null) {
            Timber.e("on cloning request");
            call.clone().enqueue(callback);
        }else{
            Request call = AppController.getInstance().getGson().fromJson(request, Request.class);
            OkHttpClient client = new OkHttpClient();
            try {
                Timber.e("on executing request");
                client.newCall(call).execute();
                Timber.e("on done executing request");
            }catch (IOException e){
                Timber.e("exception on running request %s", e);
            }
        }
    }
    
    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        //Nothing to do
        Timber.e("retry cancelled %s", throwable);
    }
    
    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount,
                                                     int maxRunCount) {
        Timber.e("retry exception %s", throwable);
        return RetryConstraint.RETRY;
    }
    
    static class ApiRetryCallBuilder {
        
        private Params params;
        private Call call;
        private Callback callback;
        
        public ApiRetryCallBuilder call(Call call){
            this.call = call;
            return this;
        }
    
        public ApiRetryCallBuilder callback(Callback callback){
            this.callback = callback;
            return this;
        }
        
        public ApiRetryCall build(){
            //FYI 29/12/2017 : For now I cannot persist the job because after adding the
            // RECEIVE_BOOT_COMPLETED permission, the device must be restarted or the app re-installed
            // to refresh the cache permission of the receive boot completed apps, something
            // I can't afford so for now the job won't be persisted meaning if the user did the
            // action while not having the connection, when the devices shutdown or crashes I will
            // lose this queue. I will have this for now and in five months, I will persist it
//            params = new Params(PRIORITY).requireNetwork().persist();
            params = new Params(PRIORITY).requireNetwork();
            return new ApiRetryCall(params, call, callback);
        }
        
    }
    
}
