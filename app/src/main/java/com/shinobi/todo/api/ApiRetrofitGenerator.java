/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.api;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.shinobi.todo.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by thiependa on 05/06/2017.
 */

public class ApiRetrofitGenerator {
    
    private static final String API_BASE_URL = BuildConfig.URL_API;
    
    private static Retrofit.Builder sBuilder = new Retrofit.Builder().baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    
    public ApiRetrofitGenerator() {
    }
    
    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        
        httpClient.addInterceptor(new LoggingInterceptor.Builder()
                                          .loggable(BuildConfig.DEBUG)
                                          .setLevel(Level.BASIC)
                                          .log(Platform.INFO)
                                          .request("Request")
                                          .response("Response")
                                          .addHeader("version", BuildConfig.VERSION_NAME)
                                          .build());
        
        OkHttpClient client = httpClient
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = sBuilder.client(client).build();
        return retrofit.create(serviceClass);
    }
    
    // Header inserted in retrofit request builder
    private static Request.Builder requestBuild(Request request, String auth) {
        if (auth != null) {
            return request.newBuilder()
                          .header("Accept", "application/json")
                          .header("Authorization", auth)
                          .method(request.method(), request.body());
        } else {
            return request.newBuilder()
                          .header("Accept", "application/json")
                          .method(request.method(), request.body());
        }
    }
    
    /*public static class ErrorUtils {
        
        private ErrorUtils() {
        }
        
        public static ApiError parseError(retrofit2.Response<?> response) {
            ApiError error;
            String errorBody = "";
            try {
                errorBody = response.errorBody().string();
                error = AppController.getInstance().getGson().fromJson(errorBody, ApiError.class);
                if (error.getError().isEmpty()) {
                    error.setError(errorBody);
                }
            } catch (Exception e) {
                Timber.e("exception unformatted : %s", e.getMessage());
                error = new ApiError();
                error.setError(errorBody);
            }
            
            return error;
        }
        
    }*/
    
}
