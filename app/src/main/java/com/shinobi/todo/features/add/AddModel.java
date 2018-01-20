package com.shinobi.todo.features.add;

import com.shinobi.todo.base.BaseModel;
import com.shinobi.todo.data.ToDoItem;

/**
 * Created by thiependa on 20/01/2018.
 */

interface AddModel extends BaseModel {
    
    void addToDo(ToDoItem toDo);
    
}
