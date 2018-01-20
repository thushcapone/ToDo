/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.base;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 18/03/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tc.senevideos.R;
import com.tc.senevideos.app.Tags;
import com.tc.senevideos.utils.KeyboardUtils;
import com.tc.senevideos.widgets.message_dialog.MessageCallback;
import com.tc.senevideos.widgets.message_dialog.error.ErrorMessageDialog;
import com.tc.senevideos.widgets.progress_dialog.ProgressDialog;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Mother Fragment that all the fragments using the MVP inherit from.
 * Implements the functions that are common to all the fragments.
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment
        implements BaseView, MessageCallback {
    
    private static final int LOADER_ID = 103;
    
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    
    protected P mPresenter;
    protected Bundle mSavedInstanceState;
    protected View mCustomView;
    protected ErrorMessageDialog mErrorMessageDialog;
    private boolean isNewFragment;
    private ProgressDialog mLoadingDialog;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        
        isNewFragment = savedInstanceState == null;
        
        if (savedInstanceState != null) {
            this.mSavedInstanceState = savedInstanceState.getBundle(Tags.BUNDLE_STATE);
        } else {
            this.mSavedInstanceState = new Bundle();
        }
        
        // LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                return new PresenterLoader<>(BaseFragment.this.getContext(), getPresenterFactory());
            }
            
            @Override
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                BaseFragment.this.mPresenter = presenter;
            }
            
            @Override
            public final void onLoaderReset(Loader<P> loader) {
                BaseFragment.this.mPresenter = null;
            }
        });
        
        return mCustomView;
    }
    
    @Override
    public void setupView() {
        ButterKnife.bind(this, mCustomView);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onViewAttached(this, isNewFragment);
        isNewFragment = false;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onViewDetached();
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBundle(Tags.BUNDLE_STATE, this.mSavedInstanceState);
        // Always call the superclass so it can save the mCustomView hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    public void showLoading() {
        mLoadingDialog = ProgressDialog.newInstance(getResources().getString(R.string.loading));
        mLoadingDialog.show(getChildFragmentManager(), Tags.DIALOG_LOADING);
    }
    
    @Override
    public void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
    
    @Override
    public void showError(Object msg) {
        String message = msg instanceof Integer ? getResources().getString((Integer) msg) :
                (String) msg;
        mErrorMessageDialog = (ErrorMessageDialog) ErrorMessageDialog
                .builder()
                .withMessage(message)
                .build();
        mErrorMessageDialog.show(getChildFragmentManager(), Tags.DIALOG_ERROR);
    }
    
    @Override
    public void retrieveState() {
        if (isAdded()) {
            mLoadingDialog =
                    (ProgressDialog) getChildFragmentManager().findFragmentByTag(Tags.DIALOG_LOADING);
            
            mErrorMessageDialog =
                    (ErrorMessageDialog) getChildFragmentManager().findFragmentByTag(Tags.DIALOG_ERROR);
        }
    }
    
    @Override
    public void saveState() {
        //Nothing to do yet
    }
    
    @Override
    public void noConnection() {
        Toast.makeText(this.getContext(), R.string.error_no_connection, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void hideKeyboard() {
        KeyboardUtils.hide(this.getContext(), mCustomView);
    }
    
    @Override
    public void close() {
        this.getActivity().finish();
    }
    
    @Override
    public void onMessageOk(String tag) {
        List<Fragment> fragmentChilds = getChildFragmentManager().getFragments();
        if (fragmentChilds != null) {
            for (Fragment f : fragmentChilds) {
                if (f instanceof BaseFragment) {
                    ((BaseFragment) f).onMessageOk(tag);
                }
            }
        }
    }
    
    @Override
    public void onMessageCancel(String tag) {
        List<Fragment> fragmentChilds = getChildFragmentManager().getFragments();
        if (fragmentChilds != null) {
            for (Fragment f : fragmentChilds) {
                if (f instanceof BaseFragment) {
                    ((BaseFragment) f).onMessageCancel(tag);
                }
            }
        }
    }
    
    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance
     * should not contain {@link BaseFragment} context reference since it will be keep on
     * rotations.
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();
    
    /**
     * Could handle back press.
     *
     * @return true if back press was handled
     */
    public boolean onBackPressed() {
        return false;
    }
}
