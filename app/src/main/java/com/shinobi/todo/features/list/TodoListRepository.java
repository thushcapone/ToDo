package com.shinobi.todo.features.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.shinobi.todo.api.ApiRetrofitGenerator;
import com.shinobi.todo.api.NetworkBoundResource;
import com.shinobi.todo.app.AppController;
import com.shinobi.todo.entity.Resource;
import com.shinobi.todo.entity.ToDo;
import com.shinobi.todo.features.add.AddTodoJob;
import com.shinobi.todo.features.commons.ToDoDAO;
import com.shinobi.todo.features.commons.TodoAPI;
import com.shinobi.todo.jobs.JobManagerFactory;

import java.util.List;
import java.util.UUID;

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

public class TodoListRepository
{
    public static TodoListRepository mInstance;
    private ToDoDAO mTodoDAO;
    private TodoAPI.TodoEdnPoints mTodoEndpoint;
    
    public synchronized static TodoListRepository getInstance()
    {
        if (mInstance == null)
            mInstance = new TodoListRepository();
        return mInstance;
    }

    public TodoListRepository()
    {
        this.mTodoDAO = AppController.getInstance().getDatabase().todoDAO();
        this.mTodoEndpoint = ApiRetrofitGenerator.createService(TodoAPI.TodoEdnPoints.class);
    }

    public LiveData<Resource<List<ToDo>>> getTodoList()
    {
        return new NetworkBoundResource<List<ToDo>, TodoListResponse>()
        {
            @Override
            protected void saveCallResult(@NonNull TodoListResponse response)
            {
                mTodoDAO.insertToDoList(response.results);
            }

            @NonNull
            @Override
            protected LiveData<List<ToDo>> loadFromDb()
            {
                return mTodoDAO.getToDoList();
            }

            @NonNull
            @Override
            protected Observable<Response<TodoListResponse>> createCall()
            {
                return mTodoEndpoint.getTodoList();
            }
        }.getAsLiveData();
    }

    public LiveData<ToDo> addTodo(String todoText)
    {
        MediatorLiveData<ToDo> toDoMediatorLiveData = new MediatorLiveData<>();
        toDoMediatorLiveData.addSource(saveTodo(UUID.randomUUID().toString(), todoText), todo -> {
            toDoMediatorLiveData.setValue(todo);
            sync(todo);
        });
        return toDoMediatorLiveData;
    }


    public LiveData<ToDo> saveTodo(String uuid, String text)
    {
        MutableLiveData<ToDo> toDoMutableLiveData = new MutableLiveData<>();
        ToDo toDo = new ToDo();
        toDo.uuid = uuid;
        toDo.text = text;
        toDo.status = ToDo.Status.STORED;

        Observable.fromCallable(() ->
        {
            mTodoDAO.insertToDo(toDo);
            Timber.i("Message Insert Done!");
            return true;
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(aBoolean -> toDoMutableLiveData.setValue(toDo));

        return toDoMutableLiveData;
    }

    private void sync(ToDo todo)
    {
        JobManagerFactory.getJobManager().addJobInBackground(new AddTodoJob(todo));
    }
}
