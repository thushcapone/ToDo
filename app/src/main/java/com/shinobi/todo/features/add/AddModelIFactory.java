package com.shinobi.todo.features.add;

import com.shinobi.todo.base.ModelFactory;

/**
 * Created by thiependa on 20/01/2018.
 */

class AddModelIFactory implements ModelFactory<AddModel> {
    
    private AddModelInteractPresenter mInteractPresenter;
    
    AddModelIFactory(AddModelInteractPresenter interactPresenter) {
        this.mInteractPresenter = interactPresenter;
    }
    
    @Override
    public AddModel create() {
        return new AddModelImpl(mInteractPresenter);
    }
    
}
