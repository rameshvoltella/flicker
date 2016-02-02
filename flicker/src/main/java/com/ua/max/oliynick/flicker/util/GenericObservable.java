package com.ua.max.oliynick.flicker.util;

import java.util.Observable;

/**
 * Created by Максим on 01.02.2016.
 */
public abstract class GenericObservable <T> extends Observable {

    public void setValue(T value) {
        setChanged();
        notifyObservers(value);
    }

    public abstract T getValue();

}
