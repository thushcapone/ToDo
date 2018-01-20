package com.shinobi.todo.features.list;

import com.shinobi.todo.base.BaseView;
import com.shinobi.todo.data.ToDoItem;

import java.util.List;

/**
 * Created by thiependa on 20/01/2018.
 */

interface ListView extends BaseView {
    
    void setupRecyclerView();
    
    void showListLoading();
    
    void stopListLoading();
    
    void setListToDos(List<ToDoItem> toDos);
    
    void showListError(Object msg);
    
    void openAddToDo();
    
}
