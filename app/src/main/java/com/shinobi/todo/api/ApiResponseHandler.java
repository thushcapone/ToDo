/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.api;


import com.shinobi.todo.app.AppController;
import com.shinobi.todo.base.BaseModelInteractPresenter;
import com.shinobi.todo.data.ApiError;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by thiependa on 22/10/2017.
 */

public abstract class ApiResponseHandler<T> implements Callback<T> {
    
    public BaseModelInteractPresenter interactPresenter;
    
    private ApiResponseHandler() {
    }
    
    protected ApiResponseHandler(final BaseModelInteractPresenter interactPresenter) {
        this.interactPresenter = interactPresenter;
    }
    
    public abstract void onSuccess(T response);
    
    public void onError(String error) {
        interactPresenter.onRequestError(error);
    }
    
    public void onFailure(String error) {
        interactPresenter.onRequestError(error);
    }
    
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Timber.e("received response %s", response.isSuccessful());
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            ApiError error = ApiRetrofitGenerator.ErrorUtils.parseError(response);
            onError(error.getError());
        }
    }
    
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        // something went completely south (like no internet connection)
        Timber.e("on failure of call %s, error is %s", call.request().url().url(), t);
        if (t instanceof IOException && t.getLocalizedMessage().equalsIgnoreCase("Canceled")) {
            onFailure(ApiErrorMessages.CALL_CANCELLED.getValue());
        } else {
            onFailure(ApiErrorMessages.NO_NETWORK.getValue());
            AppController.getInstance().getJobManager().addJobInBackground(
                    ApiRetryCall.builder()
                    .call(call)
                    .callback(this)
                    .build()
            );
        }
    }
    
}
