package com.ua.max.oliynick.flicker.util;

import java.util.Observable;

/**
 * Created by Максим on 29.01.2016.
 */
public class BooleanObservable extends Observable {

    private boolean value;

    public BooleanObservable(boolean value) {
        this.value = value;
    }
    public void setValue(boolean value)
    {
        this.value = value;
        setChanged();
        notifyObservers();
    }
    public boolean getValue()
    {
        return value;
    }

}
