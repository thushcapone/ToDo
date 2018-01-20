/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.services;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.shinobi.todo.app.AppController;

/**
 * Created by thiependa on 28/12/2017.
 */

public class MyJobService extends FrameworkJobSchedulerService {
    
    @NonNull
    @Override
    protected JobManager getJobManager() {
        return AppController.getInstance().getJobManager();
    }
    
}
