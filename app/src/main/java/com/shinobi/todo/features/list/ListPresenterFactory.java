package com.shinobi.todo.features.list;

import com.shinobi.todo.base.PresenterFactory;

/**
 * Created by thiependa on 20/01/2018.
 */

class ListPresenterFactory implements PresenterFactory<ListPresenter> {
    
    @Override
    public ListPresenter create() {
        return new ListPresenterImpl();
    }
    
}
