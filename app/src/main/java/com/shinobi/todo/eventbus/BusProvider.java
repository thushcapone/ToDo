package com.shinobi.todo.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by rygelouv on 13/08/16.
 * <p>
 * Will provide single access to the otto BUS
 */
public class BusProvider
{
    private static final Bus BUS = new MainThreadBus();

    private BusProvider()
    {
        // Don't allow public instantiation
    }

    public synchronized static Bus getInstance()
    {
        return BUS;
    }
}