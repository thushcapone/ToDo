package com.shinobi.todo.features.commons;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.widget.EditText;

import com.shinobi.todo.entity.Resource;
import com.shinobi.todo.entity.ToDo;
import com.shinobi.todo.eventbus.BusProvider;
import com.shinobi.todo.eventbus.Events;
import com.shinobi.todo.features.list.TodoListRepository;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import timber.log.Timber;

/**
 * Created by rygelouv on 1/20/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

public class TodoViewModel extends ViewModel
{
    private LiveData<Resource<List<ToDo>>> mTodoList;
    private MediatorLiveData<ToDo> productAddedLiveData;
    private MediatorLiveData<ToDo> productUpdatedLiveData;


    public TodoViewModel()
    {
        BusProvider.getInstance().register(this);
        productAddedLiveData = new MediatorLiveData<>();
        productUpdatedLiveData = new MediatorLiveData<>();
    }

    public MediatorLiveData<ToDo> getProductAddedLiveData()
    {
        return productAddedLiveData;
    }

    public MediatorLiveData<ToDo> getProductUpdatedLiveData()
    {
        return productUpdatedLiveData;
    }

    public LiveData<Resource<List<ToDo>>> getTodoList()
    {
        return mTodoList = TodoListRepository.getInstance().getTodoList();
    }

    public void addTodo(String todoText)
    {
        LiveData<ToDo> todLocalSource = TodoListRepository.getInstance().addTodo(todoText);
        productAddedLiveData.addSource(todLocalSource, toDo -> {
            productAddedLiveData.removeSource(todLocalSource);
            productAddedLiveData.setValue(toDo);
        });
    }

    @Subscribe
    public void onTodoUpdatedEvent(Events.TodoUpdatedEvent event)
    {
        Timber.i("event received!");
        productUpdatedLiveData.setValue(event.toDo);
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
        BusProvider.getInstance().unregister(this);
    }

    public  static class Factory extends ViewModelProvider.NewInstanceFactory
    {
        public Factory() {}

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            return (T) new TodoViewModel();
        }
    }
}
