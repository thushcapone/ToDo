package com.shinobi.todo.features.list;

import com.shinobi.todo.base.BaseModelInteractPresenter;
import com.shinobi.todo.data.ToDoItem;

import java.util.List;

/**
 * Created by thiependa on 20/01/2018.
 */

interface ListModelInteractPresenter extends BaseModelInteractPresenter {
    
    void onGetCachedToDosSuccess(List<ToDoItem> toDos);
    
    void onGetToDosSuccess(List<ToDoItem> toDos);
    
}
