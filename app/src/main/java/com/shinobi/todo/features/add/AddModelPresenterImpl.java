package com.shinobi.todo.features.add;

import com.shinobi.todo.base.BasePresenterImpl;
import com.shinobi.todo.base.ModelFactory;
import com.shinobi.todo.data.ToDoItem;

/**
 * Created by thiependa on 20/01/2018.
 */

class AddModelPresenterImpl extends BasePresenterImpl<AddView, AddModel>
        implements AddPresenter, AddModelInteractPresenter {
    
    @Override
    protected ModelFactory<AddModel> getModelFactory() {
        return new AddModelIFactory(this);
    }
    
    @Override
    public void onAddClicked(String text) {
        ToDoItem toDo = new ToDoItem();
        toDo.setText(text);
        toDo.setOnline(false);
        mModel.addToDo(toDo);
    }
    
    @Override
    public void onCancelClicked() {
        mView.close();
    }
    
    @Override
    public void onAddToDoSuccess() {
        mView.confirmAdd();
        mView.close();
    }
    
    
}
