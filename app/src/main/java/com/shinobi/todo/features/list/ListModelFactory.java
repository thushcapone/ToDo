package com.shinobi.todo.features.list;

import com.shinobi.todo.base.ModelFactory;

/**
 * Created by thiependa on 20/01/2018.
 */

class ListModelFactory implements ModelFactory<ListModel> {
    
    ListModelInteractPresenter mInteractPresenter;
    
    public ListModelFactory(ListModelInteractPresenter interactPresenter) {
        this.mInteractPresenter = interactPresenter;
    }
    
    @Override
    public ListModel create() {
        return new ListModelImpl(mInteractPresenter);
    }
}
