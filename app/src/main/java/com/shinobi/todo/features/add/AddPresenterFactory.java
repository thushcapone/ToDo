package com.shinobi.todo.features.add;

import com.shinobi.todo.base.PresenterFactory;

/**
 * Created by thiependa on 20/01/2018.
 */

class AddPresenterFactory implements PresenterFactory<AddPresenter> {
    
    @Override
    public AddPresenter create() {
        return new AddModelPresenterImpl();
    }
    
}
