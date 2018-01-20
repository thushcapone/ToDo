package com.shinobi.todo.jobs;

import android.content.Context;

public class JobManagerInitializer
{
    public JobManagerInitializer() {
    }

    public void initialize(Context context) {
        JobManagerFactory.getJobManager(context);
        // TODO you can initlize here your own eventbus system for example.
    }
}
