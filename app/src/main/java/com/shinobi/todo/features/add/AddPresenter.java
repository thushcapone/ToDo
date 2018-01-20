package com.shinobi.todo.features.add;

import com.shinobi.todo.base.BasePresenter;

/**
 * Created by thiependa on 20/01/2018.
 */

interface AddPresenter extends BasePresenter {
    
    void onAddClicked(String text);
    
    void onCancelClicked();
    
}
