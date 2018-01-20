package com.shinobi.todo.features.list;

import android.support.annotation.NonNull;

import com.shinobi.todo.api.ApiErrorMessages;
import com.shinobi.todo.base.BasePresenterImpl;
import com.shinobi.todo.base.BaseView;
import com.shinobi.todo.base.ModelFactory;
import com.shinobi.todo.data.ToDoItem;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by thiependa on 20/01/2018.
 */

class ListPresenterImpl extends BasePresenterImpl<ListView, ListModel>
        implements ListPresenter, ListModelInteractPresenter {
    
    private List<ToDoItem> mToDos;
    private List<ToDoItem> mCachedToDos;
    
    public ListPresenterImpl() {
        mToDos = new ArrayList<>();
        mCachedToDos = new ArrayList<>();
    }
    
    @Override
    protected ModelFactory<ListModel> getModelFactory() {
        return new ListModelFactory(this);
    }
    
    @Override
    public void onGetCachedToDosSuccess(List<ToDoItem> toDos) {
        mCachedToDos = toDos;
        mView.stopListLoading();
        mView.setListToDos(toDos);
    }
    
    @Override
    public void onGetToDosSuccess(List<ToDoItem> toDos) {
        Timber.e("on get to do success %d", toDos.size());
        mToDos = toDos;
        mView.stopListLoading();
        mView.setListToDos(toDos);
    }
    
    @Override
    public void onAddToDoClicked() {
        mView.openAddToDo();
    }
    
    @Override
    public void onAddedToDo() {
        mModel.getToDos();
    }
    
    @Override
    public void onRequestError(String message) {
        mView.stopListLoading();
        if (ApiErrorMessages.fromValue(message) == ApiErrorMessages.UNKNOWN) {
            mView.showListError(message);
        } else {
            mView.showListError(ApiErrorMessages.fromValue(message).getResourceId());
        }
    }
    
    @Override
    public void onViewAttached(@NonNull BaseView view, boolean isNew) {
        super.onViewAttached(view, isNew);
        mView.setupRecyclerView();
        if (mToDos.isEmpty()) {
            mView.showListLoading();
            Timber.e("about to call in presenter");
            mModel.getToDos();
            Timber.e("about to call in presenter 2");
            if (!mCachedToDos.isEmpty()) {
                mView.stopListLoading();
                mView.setListToDos(mCachedToDos);
                mView.retrieveState();
            }
        } else {
            mView.setListToDos(mToDos);
            //I call retrieve state after setting the values so that I will have the scroll position
            mView.retrieveState();
        }
    }
    
}
