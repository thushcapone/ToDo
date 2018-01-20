package com.shinobi.todo.api;

import android.arch.lifecycle.MediatorLiveData;

import com.shinobi.todo.entity.Resource;
import com.shinobi.todo.errors.APIErrorsStatus;
import com.shinobi.todo.errors.AppErrors;
import com.shinobi.todo.errors.ErrorsHandler;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by rygelouv on 9/22/17.
 * <p>
 * Todo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

public abstract class ApiCallbackWrapper<T extends Response<?>,
        LD> extends DisposableObserver<T>
{
    private MediatorLiveData<Resource<LD>> data;

    public ApiCallbackWrapper(MediatorLiveData<Resource<LD>> data)
    {
        this.data = data;
    }

    protected abstract void onSuccess(T t);

    /**
     * We can return StatusCodes of different cases from your API and handle it here.
     * These cases are in BaseApiResponse which is inherit from all responses
     */
    @Override
    public void onNext(T t)
    {
        if (t.isSuccessful())
            if (t.body() != null)
                onSuccess(t);
            else
                this.data.setValue(Resource.success(null));
        else
            onError(t);
    }

    @Override
    public void onError(Throwable e)
    {
        Timber.e(e);
        this.data.setValue(
                Resource.error(AppErrors.GENERIC_ERROR)
        );
    }

    protected void onError(T t)
    {
        this.data.setValue(
                Resource.error(ErrorsHandler.handleApiError(APIErrorsStatus.EXECUTION_ERROR)) // for simulation
        );
    }

    protected void onHttpError(T t)
    {
        this.data.setValue(Resource.error(ErrorsHandler.handleApiError(APIErrorsStatus.TODO_ALREADY_EXIST))); // We suppose that each time the request is no success it's because the entity already exist
    }

    @Override
    public void onComplete() {

    }
}
