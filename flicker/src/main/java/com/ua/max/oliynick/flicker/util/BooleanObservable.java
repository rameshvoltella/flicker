package com.ua.max.oliynick.flicker.util;

/**
 * Created by Максим on 29.01.2016.
 */
public class BooleanObservable extends GenericObservable<Boolean> {

    private boolean value;

    public BooleanObservable(boolean value) {
        this.value = value;
    }

    @Override
    public synchronized void setValue(Boolean value) {
        super.setValue(value);
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

}
