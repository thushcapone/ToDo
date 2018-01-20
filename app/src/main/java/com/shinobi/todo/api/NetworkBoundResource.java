package com.shinobi.todo.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.shinobi.todo.entity.Resource;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by rygelouv on 1/16/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

public abstract class NetworkBoundResource<LocalType, RemoteType extends BaseApiResponse>
{
    private final MediatorLiveData<Resource<LocalType>> result = new MediatorLiveData<>();

    @MainThread
    private void setValue(Resource<LocalType> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }


    @MainThread
    public NetworkBoundResource() {
        result.setValue(Resource.loading(null));
        LiveData<LocalType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> result.setValue(Resource.success(newData)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<LocalType> dbSource) {
        result.addSource(dbSource, newData -> result.setValue(Resource.loading(newData)));
        createCall()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new ApiCallbackWrapper<Response<RemoteType>, LocalType>(result){

                    @Override
                    protected void onSuccess(Response<RemoteType> response)
                    {
                        result.removeSource(dbSource);
                        saveResultAndReInit(response.body());
                    }
                });
    }

    @MainThread
    private void saveResultAndReInit(RemoteType response) {

        Observable.fromCallable(() -> {
            saveCallResult(response);
            return true;
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(t -> result.addSource(loadFromDb(), newData -> result.setValue(Resource.success(newData))));
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RemoteType item);

    @MainThread
    protected boolean shouldFetch(@Nullable LocalType data) {
        return true;
    }

    @NonNull
    @MainThread
    protected abstract LiveData<LocalType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract Observable<Response<RemoteType>> createCall();

    @MainThread
    protected void onFetchFailed() {
    }

    public final LiveData<Resource<LocalType>> getAsLiveData() {
        return result;
    }
}
