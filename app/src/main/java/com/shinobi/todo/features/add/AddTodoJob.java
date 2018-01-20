package com.shinobi.todo.features.add;

import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.shinobi.todo.api.ApiCallbackWrapper;
import com.shinobi.todo.api.ApiRetrofitGenerator;
import com.shinobi.todo.app.AppController;
import com.shinobi.todo.entity.Resource;
import com.shinobi.todo.entity.ToDo;
import com.shinobi.todo.eventbus.BusProvider;
import com.shinobi.todo.eventbus.Events;
import com.shinobi.todo.features.commons.AddTodoPayload;
import com.shinobi.todo.features.commons.TodoAPI;
import com.shinobi.todo.jobs.JobPriority;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by rygelouv on 1/20/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

public class AddTodoJob extends Job
{
    public static final String TAG = AddTodoJob.class.getSimpleName();
    private ToDo mTodo;

    public AddTodoJob(ToDo toDo)
    {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .groupBy(TAG));
                //.persist());
        this.mTodo = toDo;
    }

    @Override
    public void onAdded()
    {
        Timber.d("onAdded executed.....");
    }

    @Override
    public void onRun() throws Throwable
    {
        Timber.d("onRun executed.....");
        MediatorLiveData<Resource<ToDo>> toDoMediatorLiveData = new MediatorLiveData<>(); // Just because ApiCallbackWrapper needs it
        TodoAPI.TodoEdnPoints endpoint = ApiRetrofitGenerator.createService(TodoAPI.TodoEdnPoints.class);
        AddTodoPayload payload = new AddTodoPayload();
        payload.uuid = mTodo.uuid;
        payload.text = mTodo.text;
        endpoint.sendTodoList(payload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new ApiCallbackWrapper<Response<ToDo>, ToDo>(toDoMediatorLiveData)
                {
                    @Override
                    protected void onSuccess(Response<ToDo> toDoResponse)
                    {
                        ToDo toDo = toDoResponse.body();
                        toDo.status = ToDo.Status.SENT;
                        Observable.fromCallable(() ->
                        {
                            AppController.getInstance().getDatabase().todoDAO().insertToDo(toDo);
                            Timber.d("Update done!");
                            return true;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(aBoolean -> {
                            BusProvider.getInstance().post(new Events.TodoUpdatedEvent(toDo));
                        });
                    }
                });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable)
    {
        Timber.d("onCancel executed.....");

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount)
    {
        Timber.d("shouldReRunOnThrowable executed.....");

        return RetryConstraint.RETRY;
    }
}
