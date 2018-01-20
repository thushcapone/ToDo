/*
 * Copyright (c) 2017. SeneVideo
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.base;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/02/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */

import android.support.annotation.NonNull;

import com.tc.senevideos.api.ApiErrorMessages;
import com.tc.senevideos.utils.NoOpCheckNullProxy;
import com.tc.senevideos.utils.NoOpViewCheckNullProxy;
import com.tc.senevideos.utils.ReflectionUtils;

import java.lang.reflect.Proxy;

/**
 * Mother Presenter Implementation that all the Base Presenter Implementation inherits from.
 * Implements the function that are common to all the Base Presenter Implementation
 */
public abstract class BasePresenterImpl<V extends BaseView, M extends BaseModel>
        implements BasePresenter, BaseModelInteractPresenter {
    
    protected V mView;
    protected M mModel;
    
    @SuppressWarnings("unchecked")
    public BasePresenterImpl() {
        ModelFactory factory = getModelFactory();
        if (factory != null) {
            Class classOfM = ReflectionUtils.getTypeArguments(BasePresenterImpl.class, getClass()
            ).get(1);
            this.mModel = (M) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{
                                                             classOfM
                                                     },
                                                     new NoOpCheckNullProxy(factory.create())
            );
        } else {
            this.mModel = (M) new BaseModelImpl<BaseModelInteractPresenter>(this);
        }
    }
    
    @Override
    public void onRequestError(String message) {
        mView.stopLoading();
        if (ApiErrorMessages.fromValue(message) == ApiErrorMessages.UNKNOWN) {
            mView.showError(message);
        } else if(ApiErrorMessages.fromValue(message) != ApiErrorMessages.NO_NETWORK){
            mView.showError(ApiErrorMessages.fromValue(message).getResourceId());
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttached(@NonNull BaseView view, boolean isNew) {
        Class classOfV = ReflectionUtils.getTypeArguments(BasePresenterImpl.class, getClass())
                                        .get(0);
        this.mView = (V) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{classOfV},
                                                new NoOpViewCheckNullProxy(view)
        );
        if (setupView()) {
            this.mView.setupView();
            if (!isNew) {
                this.mView.retrieveState();
            }
        }
        if (closeKeyboardOnStartup()) {
            this.mView.hideKeyboard();
        }
    }
    
    @Override
    public void onHomeClicked() {
        this.mView.close();
    }
    
    @Override
    public void onViewDetached() {
        this.mView.saveState();
        // TODO: 17/09/2017 Notify the InvocationHandler that the view has been detached and cancel
        // all methods calls after this. This will help avoid the IllegalStateException gracefully.
    }
    
    @Override
    public void onDestroyed() {
        //Nothing to do yet
    }
    
    /**
     * Instance of {@link ModelFactory} use to create a Model when needed.
     */
    @SuppressWarnings("unchecked")
    protected abstract ModelFactory<M> getModelFactory();
    
    /**
     * If the view shouldn't be setting up the view, this method should be overriden and given false
     * as a return value
     *
     * @return
     */
    protected boolean setupView() {
        return true;
    }
    
    /**
     * If the view shouldn't hide the keyboard on startup, this method should be overriden and given
     * false as a return value so the keyboard will be shown
     *
     * @return
     */
    protected boolean closeKeyboardOnStartup() {
        return true;
    }
    
}
