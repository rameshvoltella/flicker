package com.ua.max.oliynick.flicker.util;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Максим on 01.02.2016.
 */
public abstract class GenericObserver<T> {

    private T oldValue;
    private T newValue;

    private final Observer observer;

    public GenericObserver(final T initialValue) {

        observer = new Observer() {

            @Override
            public void update(Observable observable, Object data) {
                T tmp = newValue;
                newValue = (T) data;
                oldValue = tmp;
                onValueChanged(observable, oldValue, newValue);
            }
        };

        newValue = oldValue = initialValue;
    }

    public Observer getObserver() {
        return observer;
    }

    /**
     * This method will be invoked when
     * value of given observable changes
     * */
    public abstract void onValueChanged(Observable observable, T oldValue, T newValue);

}
