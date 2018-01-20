package com.shinobi.todo.eventbus;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by rygelouv on 04/12/2016.
 * <p>
 * This class is in charge of assuring that events are posted on the main thread
 * as Otta Bus cannot post on any other thread
 */

public class MainThreadBus extends Bus
{
    private final Handler mainThread = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event)
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            super.post(event);
        } else
        {
            mainThread.post(() -> post(event));
        }
    }
}
