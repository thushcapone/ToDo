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

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.Toast;

import com.tc.senevideos.R;
import com.tc.senevideos.app.AppController;
import com.tc.senevideos.app.Tags;
import com.tc.senevideos.utils.KeyboardUtils;
import com.tc.senevideos.utils.LifecycleCallback;
import com.tc.senevideos.widgets.message_dialog.MessageCallback;
import com.tc.senevideos.widgets.message_dialog.error.ErrorMessageDialog;
import com.tc.senevideos.widgets.progress_dialog.ProgressDialog;

import java.util.List;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Mother Activity that all the activities using the MVP inherit from.
 * Implements the functions that are common to all the activities.
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity
        implements BaseView, MessageCallback, LifecycleCallback.ForegroundListener {
    
    private static final int LOADER_ID = 101;
    
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    
    protected P mPresenter;
    protected Bundle mSavedInstanceState;
    protected ErrorMessageDialog mErrorMessageDialog;
    private boolean isNewActivity;
    private ProgressDialog mLoadingDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        isNewActivity = savedInstanceState == null;
        
        if (savedInstanceState != null) {
            this.mSavedInstanceState = savedInstanceState.getBundle(Tags.BUNDLE_STATE);
        } else {
            this.mSavedInstanceState = new Bundle();
        }
        // LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
        getSupportLoaderManager().initLoader(LOADER_ID, null, new LoaderManager
                .LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                return new PresenterLoader<>(BaseActivity.this, getPresenterFactory());
            }
            
            @Override
            @SuppressWarnings("unchecked")
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                BaseActivity.this.mPresenter = presenter;
                // TODO: 04/08/2017 Have to check if my null proxy is working
                // FYI: 04/12/2017 Have a NPE on the presenter so I guess it's not really working
                //                Class classOfP = ReflectionUtils.getTypeArguments(BaseActivity
                // .class, BaseActivity.this.getClass()).get(0);
                //                BaseActivity.this.mPresenter = (P) Proxy.newProxyInstance
                // (getClass().getClassLoader(), new Class[]{classOfP},
                //                                                                          new
                // NoOpCheckNullProxy(presenter));
            }
            
            @Override
            public final void onLoaderReset(Loader<P> loader) {
                BaseActivity.this.mPresenter = null;
                // TODO: 04/08/2017 If my null proxy is working, I can delete the following line
                // FYI: 04/12/2017 Have a NPE on the presenter so I guess it's not really working
                //BaseActivity.this.mPresenter = null;
            }
        });
        
        //Add foreground listener
        AppController.getInstance().getLifecycleCallback().addListener(this);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            mPresenter.onHomeClicked();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    
    @Override
    public void setupView() {
        ButterKnife.bind(this);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onViewAttached(this, isNewActivity);
        isNewActivity = false;
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onViewDetached();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Remove foreground listener
        AppController.getInstance().getLifecycleCallback().removeListener(this);
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
        mLoadingDialog.show(getSupportFragmentManager(), Tags.DIALOG_LOADING);
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
        mErrorMessageDialog.show(getSupportFragmentManager(), Tags.DIALOG_ERROR);
    }
    
    @Override
    public void retrieveState() {
        mLoadingDialog =
                (ProgressDialog) getSupportFragmentManager().findFragmentByTag(Tags.DIALOG_LOADING);
        mErrorMessageDialog =
                (ErrorMessageDialog) getSupportFragmentManager().findFragmentByTag(Tags.DIALOG_ERROR);
        
    }
    
    @Override
    public void saveState() {
        //Nothing to do yet
    }
    
    @Override
    public void noConnection() {
        Toast.makeText(this, R.string.error_no_connection, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void hideKeyboard() {
        KeyboardUtils.hide(this);
    }
    
    @Override
    public void close() {
        finish();
    }
    
    @Override
    public void onMessageOk(String tag) {
        //Nothing to do yet
    }
    
    @Override
    public void onMessageCancel(String tag) {
        //Nothing to do yet
    }
    
    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        
        boolean handled = false;
        if (fragmentList != null) {
            for (Fragment f : fragmentList) {
                if (handleOnBackPressed(f)) {
                    handled = true;
                }
            }
        }
        
        if (!handled) {
            super.onBackPressed();
        }
    }
    
    protected Boolean handleOnBackPressed(Fragment fragment) {
        Boolean handled = false;
        if (fragment != null) {
            List<Fragment> fragmentChilds = fragment.getChildFragmentManager().getFragments();
            if (fragmentChilds != null) {
                for (Fragment f : fragmentChilds) {
                    if (f instanceof BaseFragment) {
                        if (((BaseFragment) f).onBackPressed()) {
                            handled = true;
                        }
                        handleOnBackPressed(f);
                    }
                }
            }
            if (fragment instanceof BaseFragment) {
                if (((BaseFragment) fragment).onBackPressed()) {
                    handled = true;
                }
            }
        }
        return handled;
    }
    
    @Override
    public void onBecameBackground() {
        //Nothing to do
    }
    
    @Override
    public void onBecameForeground(String activity) {
        //Nothing to do yet
    }
    
    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance
     * should not contain {@link BaseActivity} context reference since it will be keep on
     * rotations.
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();
    
}